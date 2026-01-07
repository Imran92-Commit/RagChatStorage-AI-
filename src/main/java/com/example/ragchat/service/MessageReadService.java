package com.example.ragchat.service;

import com.example.ragchat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface MessageReadService {
    Page<ChatMessage> getMessages(UUID sessionId, int page, int size);
}
