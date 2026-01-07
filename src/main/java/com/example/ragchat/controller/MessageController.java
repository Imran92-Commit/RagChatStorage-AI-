
package com.example.ragchat.controller;

import com.example.ragchat.dto.AddMessageRequest;
import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.service.MessageReadService;
import com.example.ragchat.service.MessageWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/messages")
@Tag(name = "Chat Messages")
public class MessageController {
    private final MessageReadService read;
    private final MessageWriteService write;

    public MessageController(MessageReadService r, MessageWriteService w) {
        this.read = r;
        this.write = w;
    }

    @PostMapping
    @Operation(summary = "Add a message to a session")
    public ResponseEntity<ChatMessage> add(@PathVariable("sessionId") UUID sessionId, @Validated @RequestBody AddMessageRequest req) {
        return ResponseEntity.ok(write.addMessage(sessionId, req));
    }

    @GetMapping
    @Operation(summary = "Get paginated messages for a session")
    public ResponseEntity<Page<ChatMessage>> list(@PathVariable("sessionId") UUID sessionId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok(read.getMessages(sessionId, page, size));
    }
}
