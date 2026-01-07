
package com.example.ragchat.service;

import com.example.ragchat.dto.CreateSessionRequest; import com.example.ragchat.entity.ChatSession; import com.example.ragchat.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test; import org.mockito.Mockito; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;

public class SessionServiceTest {
    @Test void createSession_works(){ ChatSessionRepository repo=mock(ChatSessionRepository.class); MessagePurger purger=mock(MessagePurger.class); AuditLogger audit=new AuditLogger(); SessionService svc=new SessionService(repo,purger,audit); CreateSessionRequest req=new CreateSessionRequest(); req.setUserId("u1"); req.setTitle("My Chat"); ChatSession saved=new ChatSession(); when(repo.save(Mockito.any(ChatSession.class))).thenReturn(saved); ChatSession result=svc.createSession(req); assertNotNull(result); verify(repo,times(1)).save(Mockito.any(ChatSession.class)); }
}
