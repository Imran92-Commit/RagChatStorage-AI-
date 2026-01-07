package com.example.ragchat.config;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long capacity = Long.parseLong(System.getenv().getOrDefault("RATE_LIMIT_CAPACITY", "100"));
    private final long refillTokens = Long.parseLong(System.getenv().getOrDefault("RATE_LIMIT_REFILL_TOKENS", "100"));
    private final long refillPeriodSec = Long.parseLong(System.getenv().getOrDefault("RATE_LIMIT_REFILL_PERIOD_SECONDS", "60"));

    private Bucket bucketFor(String key) {
        return buckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(
                                capacity,
                                Refill.intervally(refillTokens, Duration.ofSeconds(refillPeriodSec))
                        )).build()
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        // Skip rate limiting for Actuator health endpoints
        return req.getRequestURI().startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String apiKey = req.getHeader("X-API-KEY");
        Bucket bucket = bucketFor(apiKey == null ? "anon" : apiKey);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        long resetEpoch = Instant.now().plusSeconds(refillPeriodSec).getEpochSecond();

        // Helpful rate-limit headers
        res.setHeader("X-RateLimit-Limit", String.valueOf(capacity));
        res.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, probe.getRemainingTokens())));
        res.setHeader("X-RateLimit-Reset", String.valueOf(resetEpoch));

        if (probe.isConsumed()) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.setContentType("application/json");

            // Use a Java text block to avoid escape issues inside JSON
            String body = String.format("""
                {"error":"RATE_LIMIT_EXCEEDED","resetAt":%d}
                """, resetEpoch);

            res.getWriter().write(body);
        }
    }
}