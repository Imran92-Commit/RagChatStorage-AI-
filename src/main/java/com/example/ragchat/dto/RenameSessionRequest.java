
package com.example.ragchat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RenameSessionRequest {
    @NotBlank
    @Size(max = 256)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        this.title = t;
    }
}
