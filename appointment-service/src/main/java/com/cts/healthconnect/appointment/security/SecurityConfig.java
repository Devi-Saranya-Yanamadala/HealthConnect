package com.cts.healthconnect.appointment.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // Book appointment
                .requestMatchers(HttpMethod.POST, "/api/appointments")
                    .hasAnyRole("RECEPTION", "ADMIN")

                // Cancel appointment
                .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/cancel")
                    .hasAnyRole("RECEPTION", "DOCTOR", "ADMIN")

                // Complete appointment
                .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/complete")
                    .hasAnyRole("DOCTOR", "ADMIN")

                // Reschedule
                .requestMatchers(HttpMethod.PUT, "/api/appointments/reschedule")
                    .hasAnyRole("RECEPTION", "ADMIN")

                // Count endpoints — accessible by ADMIN and analytics service
                .requestMatchers(HttpMethod.GET, "/api/appointments/count")
                    .hasAnyRole("ADMIN", "COMPLIANCE_OFFICER")

                // ✅ ADDED: date-wise count for analytics calendar
                .requestMatchers(HttpMethod.GET, "/api/appointments/count/by-date")
                    .hasAnyRole("ADMIN", "COMPLIANCE_OFFICER")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}