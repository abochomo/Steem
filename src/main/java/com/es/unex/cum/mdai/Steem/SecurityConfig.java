package com.es.unex.cum.mdai.Steem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // -------------------------------------------------------------
                // 1. NUEVO: Ignorar protección CSRF solo para la API de la IA
                // Esto soluciona el error 403 Forbidden en el botón "Magic"
                // -------------------------------------------------------------
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                .authorizeHttpRequests((requests) -> requests
                        // -----------------------------------------------------
                        // 2. NUEVO: Permitir acceso público a la API
                        // -----------------------------------------------------
                        .requestMatchers("/api/**").permitAll()

                        // --- TU CONFIGURACIÓN ORIGINAL ABAJO ---
                        .requestMatchers("/", "/login", "/registro/**", "/registroDesarrollador",
                                "/images/**", "/css/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/biblioteca/**", "/comprar/**").hasRole("CLIENTE")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}