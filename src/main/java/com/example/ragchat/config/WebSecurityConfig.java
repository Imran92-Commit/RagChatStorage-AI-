
package com.example.ragchat.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;

@Configuration
public class WebSecurityConfig {
    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyFilterRegistration(ApiKeyAuthFilter f) {
        FilterRegistrationBean<ApiKeyAuthFilter> r = new FilterRegistrationBean<>();
        r.setFilter(f);
        r.addUrlPatterns("/*");
        r.setOrder(1);
        return r;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(RateLimitFilter f) {
        FilterRegistrationBean<RateLimitFilter> r = new FilterRegistrationBean<>();
        r.setFilter(f);
        r.addUrlPatterns("/*");
        r.setOrder(2);
        return r;
    }
}
