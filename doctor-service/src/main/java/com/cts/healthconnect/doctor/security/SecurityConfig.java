package com.cts.healthconnect.doctor.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // 1. Swagger — always public
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // 2. Prescription endpoints — DOCTOR + ADMIN
                //    MUST be declared BEFORE the general /api/doctors/** rule
                .requestMatchers("/api/doctors/prescriptions/**")
                    .permitAll()

                // 3. Doctor CRUD (create, activate, deactivate) — ADMIN only
                .requestMatchers("/api/doctors/**")
                    .hasRole("ADMIN")

                // 4. Everything else needs authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}