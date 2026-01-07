package com.example.ragchat.service;

import com.example.ragchat.entity.ChatSession;
import java.util.List;
import java.util.UUID;

public interface SessionReadService {
    ChatSession get(UUID id);
    List<ChatSession> listForUser(String userId);
}
