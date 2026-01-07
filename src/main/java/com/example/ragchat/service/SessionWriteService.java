package com.example.ragchat.service;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.entity.ChatSession;
import java.util.UUID;

public interface SessionWriteService {
    ChatSession createSession(CreateSessionRequest req);
    ChatSession rename(UUID id, String title);
    ChatSession favorite(UUID id, boolean favorite);
    void delete(UUID id);
}
