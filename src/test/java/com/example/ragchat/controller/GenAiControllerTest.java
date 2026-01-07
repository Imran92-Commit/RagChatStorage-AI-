package com.example.ragchat.controller;

import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.service.RagOrchestrator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenAiControllerTest {

    @Mock
    private RagOrchestrator rag;

    @InjectMocks
    private GenAiController controller;

    @Test
    void ask_returnsChatMessage_andStatusOk() {
        UUID sessionId = UUID.randomUUID();
        Map<String, String> body = Map.of("query", "hello world");

        ChatMessage expected = mock(ChatMessage.class);
        when(rag.ask(sessionId, "hello world")).thenReturn(expected);

        ResponseEntity<ChatMessage> response = controller.ask(sessionId, body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(rag, times(1)).ask(sessionId, "hello world");
    }

    @Test
    void ask_withMissingQuery_usesEmptyString() {
        UUID sessionId = UUID.randomUUID();
        Map<String, String> body = Map.of(); // no query

        ChatMessage expected = mock(ChatMessage.class);
        when(rag.ask(sessionId, "")).thenReturn(expected);

        ResponseEntity<ChatMessage> response = controller.ask(sessionId, body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(rag, times(1)).ask(sessionId, "");
    }
}