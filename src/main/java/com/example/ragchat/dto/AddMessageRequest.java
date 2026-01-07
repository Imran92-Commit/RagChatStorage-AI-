
package com.example.ragchat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddMessageRequest {
    @NotBlank
    @Pattern(regexp = "USER|ASSISTANT")
    private String sender; // USER or ASSISTANT
    @NotBlank
    @Size(max = 10000)
    private String content;
    private String context; // optional

    public String getSender() {
        return sender;
    }

    public void setSender(String s) {
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
}
