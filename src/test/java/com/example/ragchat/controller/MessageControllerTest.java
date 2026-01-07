/*
package com.example.ragchat.controller;

import com.example.ragchat.dto.AddMessageRequest;
import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.service.MessageReadService;
import com.example.ragchat.service.MessageWriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageReadService read;

    @MockBean
    private MessageWriteService write;

    @Test
    void addMessage_returnsMessage() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(new AddMessageRequest()); // adjust fields if AddMessageRequest has required ctor/fields

        ChatMessage returned = Mockito.mock(ChatMessage.class);
        when(write.addMessage(eq(sessionId), any(AddMessageRequest.class))).thenReturn(returned);

        mockMvc.perform(post("/api/v1/sessions/{sessionId}/messages", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verify(write).addMessage(eq(sessionId), any(AddMessageRequest.class));
    }

    @Test
    void list_returnsPage() throws Exception {
        UUID sessionId = UUID.randomUUID();
        List<ChatMessage> content = Collections.singletonList(Mockito.mock(ChatMessage.class));
        Page<ChatMessage> page = new PageImpl<>(content);

        when(read.getMessages(sessionId, 0, 20)).thenReturn(page);

        mockMvc.perform(get("/api/v1/sessions/{sessionId}/messages", sessionId)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(read).getMessages(sessionId, 0, 20);
    }
}*/
