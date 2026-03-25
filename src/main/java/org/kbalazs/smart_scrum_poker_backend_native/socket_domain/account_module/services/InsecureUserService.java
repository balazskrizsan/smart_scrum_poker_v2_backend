package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.InsecureUserRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.UuidService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsecureUserService
{
    private final InsecureUserRepository insecureUserRepository;
    private final InsecureUserSessionsService insecureUserSessionsService;
    private final UuidService uuidService;

    public InsecureUser create(@NonNull InsecureUser insecureUser, UUID sessionId) throws AccountException
    {
        UUID newUuid = uuidService.getRandom();

        InsecureUser newUser = insecureUserRepository.create(new InsecureUser(
            null,
            newUuid,
            insecureUser.userName(),
            insecureUser.createdAt()
        ));

        insecureUserSessionsService.add(new InsecureUserSession(newUser.idSecure(), sessionId, insecureUser.createdAt()));

        log.info("New insecure user created: {}", newUser); // TODO: test, monitor

        return newUser;
    }

    public InsecureUser findByIdSecure(@NonNull UUID idSecure) throws AccountException
    {
        return insecureUserRepository.findByIdSecure(idSecure);
    }

    public List<InsecureUser> findByIdSecureList(@NonNull List<UUID> idSecureList)
    {
        return insecureUserRepository.findByIdSecureList(idSecureList);
    }

    public List<InsecureUser> searchUsersWithActiveSession(@NonNull List<UUID> idSecures)
    {
        return insecureUserRepository.searchUsersWithActiveSession(idSecures);
    }
}
