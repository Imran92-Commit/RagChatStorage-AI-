package com.example.ragchat.service;

import com.example.ragchat.entity.ChatSession;
import com.example.ragchat.repository.ChatMessageRepository;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;

@Component
public class MessagePurger {
    private final ChatMessageRepository msgRepo;
    public MessagePurger(ChatMessageRepository msgRepo){ this.msgRepo = msgRepo; }
    public void purgeMessages(ChatSession s){ msgRepo.deleteBySession(s); }
}

@Component
class AuditLogger {
    public void sessionCreated(java.util.UUID id, String user){
        LoggerFactory.getLogger(AuditLogger.class).info("session_created id={} user={}", id, user);
    }
    public void sessionDeleted(java.util.UUID id, String user){
        LoggerFactory.getLogger(AuditLogger.class).info("session_deleted id={} user={}", id, user);
    }
}
