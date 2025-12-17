package com.es.unex.cum.mdai.Steem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User; // ★ Importante
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
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/", "/login", "/registro/**", "/registroDesarrollador",
                                "/images/**", "/css/**", "/js/**", "/error").permitAll()
                        // ★ 1. NUEVO: Reglas para el Administrador
                        // Se coloca ANTES de las reglas generales para asegurar prioridad
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/cliente/**", "/comprar/**").hasRole("CLIENTE")
                        .requestMatchers("/dashboard/**").hasRole("DESARROLLADOR")
                        .requestMatchers("/usuario/**", "/biblioteca/**").hasAnyRole("CLIENTE", "DESARROLLADOR")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // ★ Opcional: Podrías redirigir al admin a su panel si detectas el rol
                        // pero por defecto irá a "/"
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

    /**
     * ★ MODIFICADO: Aquí implementamos el "Hardcode" del admin.
     * Interceptamos la llamada: si el usuario es "admin", devolvemos el usuario en memoria.
     * Si no, dejamos que el 'userDetailsService' original busque en la base de datos.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(username -> {
            // Imprimimos en consola para depurar (bórralo cuando funcione)
            System.out.println("Intento de login recibiendo usuario: " + username);

            // 1. Usamos un EMAIL para evitar problemas con el input type="email" del HTML
            if ("admin@steem.com".equals(username)) {
                return User.builder()
                        .username("admin@steem.com")
                        // 2. Generamos el hash al vuelo para asegurar que 'admin123' funciona
                        .password(new BCryptPasswordEncoder().encode("admin123"))
                        .roles("ADMIN")
                        .build();
            }
            // Si no es el admin, busca en la BD
            return userDetailsService.loadUserByUsername(username);
        });

        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}