
package com.example.ragchat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final Set<String> allowedKeys;

    public ApiKeyAuthFilter() {
        String keys = System.getenv().getOrDefault("APP_API_KEYS", "local-dev-key");
        allowedKeys = new HashSet<>(Arrays.asList(keys.split(",")));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return !p.startsWith("/api/v1/sessions");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null || !allowedKeys.contains(apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized: missing or invalid API key");
            return;
        }
        chain.doFilter(request, response);
    }
}
