
package com.example.ragchat.genai;

import com.example.ragchat.entity.ChatSession;

import java.util.List;

public class PromptBuilder {

    public static String buildPrompt(ChatSession session,
                                     List<RetrievedChunk> ctx,
                                     String userText) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are a helpful assistant for user ")
                .append(session.getUserId())
                .append(".\n");

        sb.append("Use the provided CONTEXT. If insufficient, say so.\n\n");

        sb.append("CONTEXT:\n");
        for (RetrievedChunk c : ctx) {
            sb.append(" - [")
                    .append(c.source)
                    .append("] ")
                    .append(c.text)
                    .append("\n");
        }

        sb.append("\nQUESTION: ")
                .append(userText)
                .append("\n");

        return sb.toString();
    }
}