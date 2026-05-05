package usco.edu.co.parcial2.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/medico/**").hasAuthority("MEDICO")
                        .requestMatchers("/paciente/**").hasAuthority("PACIENTE")
                        .requestMatchers("/api/medicos/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/api/consultas/**").hasAnyAuthority("ADMINISTRADOR", "MEDICO", "PACIENTE")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/sin-permiso"))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"))) {
                    response.sendRedirect("/admin");
                    return;
                }
                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MEDICO"))) {
                    response.sendRedirect("/medico");
                    return;
                }
                response.sendRedirect("/paciente");
            }
        };
    }
}
