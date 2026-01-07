
package com.example.ragchat.controller;

import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.service.RagOrchestrator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/genai")
@Tag(name = "GenAI")
public class GenAiController {
    private final RagOrchestrator rag;
    public GenAiController(RagOrchestrator rag){ this.rag=rag; }

    @PostMapping("/ask")
    @Operation(summary = "Ask a question and get a GenAI response (RAG)")
    public ResponseEntity<ChatMessage> ask(@PathVariable("sessionId") UUID sessionId, @RequestBody Map<String,String> body){
        String query = body.getOrDefault("query", "");
        return ResponseEntity.ok(rag.ask(sessionId, query));
    }
}
