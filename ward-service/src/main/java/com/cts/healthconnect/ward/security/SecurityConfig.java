package com.cts.healthconnect.ward.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import feign.Request.HttpMethod;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/**").permitAll()
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .requestMatchers("/api/wards/**")
	            .hasAnyRole("ADMIN", "NURSE")




	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(new JwtAuthenticationFilter(),
	            UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
}