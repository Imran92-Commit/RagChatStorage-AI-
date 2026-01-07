package com.example.ragchat.service;

import com.example.ragchat.dto.AddMessageRequest;
import com.example.ragchat.entity.ChatMessage;
import java.util.UUID;

public interface MessageWriteService {
    ChatMessage addMessage(UUID sessionId, AddMessageRequest req);
}
