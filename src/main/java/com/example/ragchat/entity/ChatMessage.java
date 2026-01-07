
package com.example.ragchat.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    public enum Sender {USER, ASSISTANT}

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sender sender;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String context; // optional JSON/text
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // getters/setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ChatSession getSession() {
        return session;
    }

    public void setSession(ChatSession s) {
        this.session = s;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender s) {
        this.sender = s;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String c) {
        this.content = c;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String c) {
        this.context = c;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant c) {
        this.createdAt = c;
    }
}
