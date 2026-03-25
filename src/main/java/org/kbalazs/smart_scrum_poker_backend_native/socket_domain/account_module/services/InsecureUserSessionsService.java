package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.InsecureUserSessionsRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsecureUserSessionsService
{
    private final InsecureUserSessionsRepository insecureUserSessionsRepository;

    public boolean add(@NonNull InsecureUserSession insecureUserSession)
    {
        return insecureUserSessionsRepository.create(insecureUserSession);
    }

    public int countByIdSecure(@NonNull UUID uuidInsecureUserIdSecure)
    {
        return insecureUserSessionsRepository.countByIdSecure(uuidInsecureUserIdSecure);
    }

    public void removeBySessionId(@NonNull UUID sessionId)
    {
        insecureUserSessionsRepository.removeBySessionId(sessionId);
    }

    public InsecureUserSession getInsecureUserSession(@NonNull UUID sessionId) throws SessionException
    {
        return insecureUserSessionsRepository.getInsecureUserSession(sessionId);
    }
}
