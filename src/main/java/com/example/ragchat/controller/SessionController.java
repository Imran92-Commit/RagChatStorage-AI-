
package com.example.ragchat.controller;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.dto.FavoriteRequest;
import com.example.ragchat.dto.RenameSessionRequest;
import com.example.ragchat.service.SessionReadService;
import com.example.ragchat.service.SessionWriteService;
import com.example.ragchat.entity.ChatSession;
import com.example.ragchat.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Chat Sessions")
public class SessionController {
    private final SessionReadService readService;
    private final SessionWriteService writeService;
    private final MessageWriteService messageWrite;

    public SessionController(SessionReadService readService, SessionWriteService writeService, MessageWriteService messageWrite) {
        this.readService = readService;
        this.writeService = writeService;
        this.messageWrite = messageWrite;
    }

    @PostMapping
    @Operation(summary = "Create a new chat session")
    public ResponseEntity<ChatSession> create(@Validated @RequestBody CreateSessionRequest req) {
        return ResponseEntity.ok(writeService.createSession(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a session by id")
    public ResponseEntity<ChatSession> get(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(readService.get(id));
    }

    @GetMapping
    @Operation(summary = "List sessions for a user")
    public ResponseEntity<List<ChatSession>> list(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(readService.listForUser(userId));
    }

    @PatchMapping("/{id}/rename")
    @Operation(summary = "Rename a chat session")
    public ResponseEntity<ChatSession> rename(@PathVariable("id") UUID id, @Validated @RequestBody RenameSessionRequest req) {
        return ResponseEntity.ok(writeService.rename(id, req.getTitle()));
    }

    @PatchMapping("/{id}/favorite")
    @Operation(summary = "Mark/unmark a session as favorite")
    public ResponseEntity<ChatSession> favorite(@PathVariable("id") UUID id, @RequestBody FavoriteRequest req) {
        return ResponseEntity.ok(writeService.favorite(id, req.isFavorite()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a session and its messages")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") UUID id) {
        writeService.delete(id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }
}
