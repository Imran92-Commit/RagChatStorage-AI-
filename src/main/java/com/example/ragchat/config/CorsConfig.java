
package com.example.ragchat.config;

import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration c = new CorsConfiguration();
        String origins = System.getenv().getOrDefault("CORS_ALLOWED_ORIGINS", "*");
        if (origins.equals("*")) c.addAllowedOriginPattern("*");
        else Arrays.stream(origins.split(",")).forEach(c::addAllowedOrigin);
        c.addAllowedMethod("*");
        c.addAllowedHeader("*");
        c.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return new CorsFilter(s);
    }
}
