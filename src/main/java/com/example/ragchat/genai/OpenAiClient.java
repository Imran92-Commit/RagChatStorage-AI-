
package com.example.ragchat.genai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

@Service
public class OpenAiClient implements LlmClient {
    private final String apiKey = System.getenv().getOrDefault("OPENAI_API_KEY", "");
    private final String model = System.getenv().getOrDefault("OPENAI_MODEL", "gpt-4o-mini");
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String complete(String prompt) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            ArrayNode messages = mapper.createArrayNode();
            ObjectNode system = mapper.createObjectNode(); system.put("role","system"); system.put("content","You are a helpful assistant."); messages.add(system);
            ObjectNode user = mapper.createObjectNode(); user.put("role","user"); user.put("content", prompt); messages.add(user);
            body.set("messages", messages);

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

            HttpResponse<String> resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            // naive parse
            ObjectNode root = (ObjectNode) mapper.readTree(resp.body());
            String text = root.path("choices").path(0).path("message").path("content").asText("");
            return text;
        } catch (Exception e) {
            return "(LLM error: " + e.getMessage() + ")";
        }
    }
}
