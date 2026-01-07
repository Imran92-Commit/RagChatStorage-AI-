
package com.example.ragchat.service;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.entity.ChatSession;
import com.example.ragchat.exception.NotFoundException;
import com.example.ragchat.repository.ChatSessionRepository;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List; import java.util.UUID;

@Service
public class SessionService implements SessionReadService, SessionWriteService {
    private static final Logger log=LoggerFactory.getLogger(SessionService.class);
    private final ChatSessionRepository repo;
    private final MessagePurger purger; private final AuditLogger audit;
    public SessionService(ChatSessionRepository repo, MessagePurger purger, AuditLogger audit){ this.repo=repo; this.purger=purger; this.audit=audit; }

    @Transactional public ChatSession createSession(CreateSessionRequest req){ ChatSession s=ChatSession.create(req.getUserId(), req.getTitle()); ChatSession saved=repo.save(s); audit.sessionCreated(saved.getId(), saved.getUserId()); return saved; }
    public ChatSession get(UUID id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Session not found")); }
    public List<ChatSession> listForUser(String userId){ return repo.findByUserIdOrderByUpdatedAtDesc(userId); }
    @Transactional public ChatSession rename(UUID id, String title){ ChatSession s=get(id); s.setTitle(title); return repo.save(s);} 
    @Transactional public ChatSession favorite(UUID id, boolean favorite){ ChatSession s=get(id); s.setFavorite(favorite); return repo.save(s);} 
    @Transactional public void delete(UUID id){ ChatSession s=get(id); purger.purgeMessages(s); repo.delete(s); audit.sessionDeleted(id, s.getUserId()); }
}
