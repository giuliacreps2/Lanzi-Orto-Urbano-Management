package giuliacrepaldi.Lanzi_Orto_Urbano_Management.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Not authenticated\"}");
                })
        );
        
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/municipalities/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/catalog").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/product-categories/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/product-category-attributes/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/packaging/**").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET, "/orders/my").authenticated()
                .requestMatchers(HttpMethod.GET, "/b2b/me").authenticated()

                .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/packaging/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/packaging/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/packaging/**").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET, "/labels/*/label").permitAll()
                .requestMatchers("/api/import/**").hasAuthority("ADMIN")
                .requestMatchers("/roles/**").hasAuthority("ADMIN")
                .requestMatchers("/auth/b2b/*/approve", "/auth/b2b/*/reject").hasAuthority("ADMIN")
                .requestMatchers("/register/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}


