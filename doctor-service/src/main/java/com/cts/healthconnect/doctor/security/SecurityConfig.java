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
	            .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
	            .requestMatchers("/api/doctors/**")
	            .hasRole("ADMIN")



	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(new JwtAuthenticationFilter(),
	            UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
}