package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.IdsUserSessionsRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdsUserSessionsService
{
    private final IdsUserSessionsRepository idsUserSessionsRepository;

    public boolean add(@NonNull IdsUserSession idsUserSession)
    {
        log.info("Adding ids user session: {}", idsUserSession);

        return idsUserSessionsRepository.create(idsUserSession);
    }

    public int countByIdsUserId(@NonNull UUID uuididsUserId)
    {
        return idsUserSessionsRepository.countByIdSecure(uuididsUserId);
    }

    public void removeBySessionId(@NonNull UUID sessionId)
    {
        idsUserSessionsRepository.removeBySessionId(sessionId);
    }

    public IdsUserSession getInsecureUserSession(@NonNull UUID sessionId) throws SessionException
    {
        return idsUserSessionsRepository.getInsecureUserSession(sessionId);
    }
}
