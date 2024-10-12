package com.eventos.recuerdos.eventify_project.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF temporalmente para probar el endpoint
                .authorizeHttpRequests(auth -> auth
                        // Definir los endpoints que no requieren autenticación
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/usuarios", "/usuarios/crearUsuario").permitAll()

                        .anyRequest().authenticated()  // Los demás endpoints requieren autenticación
                )
                .httpBasic(Customizer.withDefaults());  // Mantener autenticación básica (considerar migrar a JWT en el futuro)

        return http.build();
    }

}

