
package com.example.ragchat.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
public class ChatSession {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private boolean favorite = false;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();
    @Version
    private long version;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public static ChatSession create(String userId, String title) {
        ChatSession s = new ChatSession();
        s.setUserId(userId);
        s.setTitle(title == null || title.isBlank() ? "New Chat" : title);
        return s;
    }

    // getters/setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String u) {
        this.userId = u;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean f) {
        this.favorite = f;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant c) {
        this.createdAt = c;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant u) {
        this.updatedAt = u;
    }
}
