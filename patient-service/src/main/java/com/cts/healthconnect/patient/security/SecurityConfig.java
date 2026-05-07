package com.cts.healthconnect.patient.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // ✅ FIXED: /count must be accessible by ANALYTICS service (ADMIN role)
                .requestMatchers("/api/patients/count").hasAnyRole("ADMIN", "COMPLIANCE_OFFICER")
                .requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "RECEPTION", "DOCTOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}