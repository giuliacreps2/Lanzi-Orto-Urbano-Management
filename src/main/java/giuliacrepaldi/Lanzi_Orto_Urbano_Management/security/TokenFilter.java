package giuliacrepaldi.Lanzi_Orto_Urbano_Management.security;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.UserRole;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersRolesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "accessToken";

    private final TokenTools tokenTools;
    private final UsersService usersService;
    private final UsersRolesService usersRolesService;

    public TokenFilter(TokenTools tokenTools, UsersService usersService, UsersRolesService usersRolesService) {
        this.tokenTools = tokenTools;
        this.usersService = usersService;
        this.usersRolesService = usersRolesService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractTokenFromCookies(request);

        if (accessToken == null || accessToken.isEmpty()
                || accessToken.equalsIgnoreCase("null") || accessToken.equalsIgnoreCase("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID userId = tokenTools.extractUserId(accessToken);
            User user = usersService.findById(userId);

            List<UserRole> listRoles = usersRolesService.findByUserId(userId);
            List<SimpleGrantedAuthority> authorities = listRoles
                    .stream()
                    .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
                    .toList();

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            // Token non valido/scaduto/utente non trovato: procedi come richiesta anonima.
            // Sarà Spring Security (o il controller) a rispondere 401/403 se la rotta è protetta.
            log.debug("Token non valido per {}: {}", request.getServletPath(), e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return antPathMatcher.match("/auth/login", servletPath)
                || antPathMatcher.match("/auth/logout", servletPath)
                || antPathMatcher.match("/register/**", servletPath);
    }
}