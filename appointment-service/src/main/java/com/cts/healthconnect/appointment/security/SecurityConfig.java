package com.cts.healthconnect.appointment.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ✅ Swagger & OpenAPI
                .requestMatchers(
                		"/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // ✅ PATCH — Cancel appointment
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/appointments/*/cancel"
                ).hasAnyRole("RECEPTION", "DOCTOR")

                // ✅ Book appointment
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/appointments"
                ).hasAnyRole("RECEPTION")

                // ✅ Complete appointment
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/appointments/*/complete"
                ).hasRole("DOCTOR")

                // ✅ Appointment count
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/appointments/count"
                ).hasRole("ADMIN")

                // ✅ Everything else requires auth
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}