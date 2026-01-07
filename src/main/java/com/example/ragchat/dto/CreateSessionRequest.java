
package com.example.ragchat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSessionRequest {
    @NotBlank
    @Size(max = 128)
    private String userId;
    @Size(max = 256)
    private String title;

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
}
