
package com.example.ragchat.service;

import com.example.ragchat.dto.AddMessageRequest;
import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.entity.ChatSession;
import com.example.ragchat.exception.NotFoundException;
import com.example.ragchat.genai.LlmClient;
import com.example.ragchat.genai.RetrievalService;
import com.example.ragchat.genai.RetrievedChunk;
import com.example.ragchat.genai.PromptBuilder;
import com.example.ragchat.repository.ChatSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RagOrchestrator {

    private final RetrievalService retrieval;
    private final LlmClient llm;
    private final MessageWriteService messages;
    private final ChatSessionRepository sessions;
    private final ObjectMapper mapper = new ObjectMapper();

    public RagOrchestrator(RetrievalService retrieval,
                           LlmClient llm,
                           MessageWriteService messages,
                           ChatSessionRepository sessions) {
        this.retrieval = retrieval;
        this.llm = llm;
        this.messages = messages;
        this.sessions = sessions;
    }

    @Transactional
    public ChatMessage ask(UUID sessionId, String userText) {
        ChatSession session = sessions.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        // 1) Save user message
        AddMessageRequest userReq = new AddMessageRequest();
        userReq.setSender("USER");
        userReq.setContent(userText);
        messages.addMessage(sessionId, userReq);

        // 2) Retrieve context
        int k = Integer.parseInt(System.getenv().getOrDefault("RAG_TOP_K", "5"));
        List<RetrievedChunk> ctx = retrieval.retrieve(userText, k);

        // 3) Build prompt
        String prompt = PromptBuilder.buildPrompt(session, ctx, userText);

        // 4) LLM completion
        String completion = llm.complete(prompt);

        // 5) Save assistant message with context (JSON)
        AddMessageRequest aiReq = new AddMessageRequest();
        aiReq.setSender("ASSISTANT");
        aiReq.setContent(completion);
        aiReq.setContext(contextJson(ctx));
        return messages.addMessage(sessionId, aiReq);
    }

    private String contextJson(List<RetrievedChunk> ctx) {
        ObjectNode root = mapper.createObjectNode();
        ArrayNode chunks = mapper.createArrayNode();

        for (RetrievedChunk c : ctx) {
            ObjectNode node = mapper.createObjectNode();
            node.put("id", c.id);
            node.put("score", c.score);
            node.put("source", c.source);
            chunks.add(node);
        }

        root.set("chunks", chunks);
        return root.toString();
    }
}