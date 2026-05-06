package com.cts.healthconnect.slotbooking.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth

	            // Swagger
	            .requestMatchers(
	                "/swagger-ui.html",
	                "/swagger-ui/**",
	                "/v3/api-docs/**"
	            ).permitAll()

	            // Generate slots → ADMIN only
	            .requestMatchers(HttpMethod.POST, "/api/slots/generate")
	            .hasRole("ADMIN")

	            // View slots → ADMIN, RECEPTION, DOCTOR
	            .requestMatchers(HttpMethod.GET, "/api/slots/**")
	            .hasAnyRole("ADMIN", "RECEPTION", "DOCTOR")

	            // Book slot → ADMIN, RECEPTION
	            .requestMatchers(HttpMethod.PUT, "/api/slots/book/*")
	            .hasAnyRole("ADMIN", "RECEPTION")

	            // Release slot → ADMIN, RECEPTION
	            .requestMatchers(HttpMethod.PATCH, "/api/slots/*/release")
	            .hasAnyRole("ADMIN", "RECEPTION")

	            // Everything else
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(
	            new JwtAuthenticationFilter(),
	            UsernamePasswordAuthenticationFilter.class
	        );

	    return http.build();
	    }
}