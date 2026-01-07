
package com.example.ragchat.service;

import com.example.ragchat.dto.AddMessageRequest;
import com.example.ragchat.entity.ChatMessage;
import com.example.ragchat.entity.ChatSession;
import com.example.ragchat.exception.NotFoundException;
import com.example.ragchat.repository.ChatMessageRepository;
import com.example.ragchat.repository.ChatSessionRepository;
import org.springframework.data.domain.Page; import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class MessageService implements MessageReadService, MessageWriteService {
    private final ChatMessageRepository msgRepo; private final ChatSessionRepository sessRepo;
    public MessageService(ChatMessageRepository m, ChatSessionRepository s){ this.msgRepo=m; this.sessRepo=s; }
    @Transactional public ChatMessage addMessage(UUID sessionId, AddMessageRequest req){
        ChatSession session=sessRepo.findById(sessionId).orElseThrow(() -> new NotFoundException("Session not found"));
        ChatMessage m=new ChatMessage(); m.setSession(session);
        m.setSender(ChatMessage.Sender.valueOf(req.getSender().toUpperCase())); m.setContent(req.getContent()); m.setContext(req.getContext());
        return msgRepo.save(m);} 
    public Page<ChatMessage> getMessages(UUID sessionId, int page, int size){
        ChatSession session=sessRepo.findById(sessionId).orElseThrow(() -> new NotFoundException("Session not found"));
        return msgRepo.findBySessionOrderByCreatedAtAsc(session, PageRequest.of(page,size)); }
}
