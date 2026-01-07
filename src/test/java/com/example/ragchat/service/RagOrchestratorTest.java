
package com.example.ragchat.service;

import com.example.ragchat.dto.AddMessageRequest; import com.example.ragchat.entity.ChatMessage; import com.example.ragchat.entity.ChatSession; import com.example.ragchat.genai.LlmClient; import com.example.ragchat.genai.RetrievalService; import com.example.ragchat.genai.RetrievedChunk; import com.example.ragchat.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test; import java.util.*; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;

public class RagOrchestratorTest {
    @Test void ask_ok(){ RetrievalService r=mock(RetrievalService.class); LlmClient llm=prompt -> "hi"; MessageWriteService msg=mock(MessageWriteService.class); ChatSessionRepository sessions=mock(ChatSessionRepository.class); java.util.UUID id = java.util.UUID.randomUUID(); ChatSession s = ChatSession.create("u1","t"); when(sessions.findById(id)).thenReturn(Optional.of(s)); when(r.retrieve(anyString(), anyInt())).thenReturn(List.of(new RetrievedChunk("1","text",0.1,"src"))); RagOrchestrator ro=new RagOrchestrator(r,llm,msg,sessions); ChatMessage m = new ChatMessage(); when(msg.addMessage(eq(id), any(AddMessageRequest.class))).thenReturn(m); ChatMessage res = ro.ask(id, "hello"); assertNotNull(res); }
}
