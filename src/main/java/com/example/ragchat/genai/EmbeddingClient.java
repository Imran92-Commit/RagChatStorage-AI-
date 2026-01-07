
package com.example.ragchat.genai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

@Service
public class EmbeddingClient {
    private final String apiKey = System.getenv().getOrDefault("OPENAI_API_KEY", "");
    private final String model = System.getenv().getOrDefault("OPENAI_EMBEDDINGS_MODEL", "text-embedding-3-small");
    private final ObjectMapper mapper = new ObjectMapper();

    public float[] embed(String text){
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            body.putArray("input").add(text);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/embeddings"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();
            HttpResponse<String> resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            var root = mapper.readTree(resp.body());
            var arr = root.path("data").path(0).path("embedding");
            float[] vec = new float[arr.size()];
            for(int i=0;i<arr.size();i++) vec[i] = (float)arr.get(i).asDouble();
            return vec;
        } catch (Exception e) {
            return null;
        }
    }
}
