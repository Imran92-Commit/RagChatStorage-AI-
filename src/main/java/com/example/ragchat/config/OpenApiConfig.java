
package com.example.ragchat.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info().title("RAG Chat Storage API")
                        .version("v1").description("Manage chat sessions, messages, and GenAI"))
                .components(new Components().addSecuritySchemes("apiKeyAuth", new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("X-API-KEY")))
                .addSecurityItem(new SecurityRequirement().addList("apiKeyAuth"));
    }
}
