package com.cts.healthconnect.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ Frontend origin (Vite / React)
        config.addAllowedOrigin("http://localhost:5173");

        // ✅ Allow all HTTP methods
        config.addAllowedMethod("*");

        // ✅ Allow all headers (Authorization, Content-Type, etc.)
        config.addAllowedHeader("*");

        // ✅ Needed if credentials/cookies ever used
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}