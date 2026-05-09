package giuliacrepaldi.Lanzi_Orto_Urbano_Management.security;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.UserRole;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.UsersRolesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
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

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final UsersService usersService;
    private final UsersRolesService usersRolesService;

    public TokenFilter(TokenTools tokenTools, @Lazy UsersService usersService, @Lazy UsersRolesService usersRolesService) {
        this.tokenTools = tokenTools;
        this.usersService = usersService;
        this.usersRolesService = usersRolesService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Invalid Token. Please provide a valid Token");

        String accessToken = authHeader.replace("Bearer ", "");

        UUID userDb = tokenTools.extractUserId(accessToken);
        User user = usersService.findById(userDb);

        List<UserRole> listRoles = usersRolesService.findByUserId(userDb);
        List<SimpleGrantedAuthority> authorities = listRoles
                .stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName())).toList();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
