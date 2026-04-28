package com.cts.healthconnect.analytics.security;
 
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
	            .requestMatchers("/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/**").permitAll()
	            .requestMatchers("/api/analytics/**")
	            .hasAnyRole("ADMIN", "MANAGER", "COMPLIANCE_OFFICER")
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(new JwtAuthenticationFilter(),
	            UsernamePasswordAuthenticationFilter.class);
 
	    return http.build();
	}
}