package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.IdsUserApiRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.IdsUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class IdsUserService
{
    IdsUserRepository idsUserRepository;
    IdsUserApiRepository idsUserApiRepository;

    public @NonNull IdsUser createIfNotExists(@NonNull IdsUser idsUser)
        throws AccountException
    {
        IdsUser newUser = idsUserRepository.createIfNotExist(idsUser);

        log.info("New IdsUser created: {}", newUser); // TODO: test, monitor

        return newUser;
    }

    public IdsUser getById(@NonNull UUID idSecure)
        throws AccountException
    {
        return idsUserRepository.getById(idSecure);
    }

    public List<IdsUser> findByIdSecureList(@NonNull List<UUID> idSecureList)
    {
        return idsUserRepository.findByIdSecureList(idSecureList);
    }

    public @NonNull List<IdsUser> searchUsersWithActiveSession(@NonNull List<UUID> idSecures)
    {
        return idsUserRepository.searchUsersWithActiveSession(idSecures);
    }

    public @NonNull List<UserProfile> findProfileByIdsUserIdList(
        @NonNull List<UUID> inPokerIdsUserIds
    )
    {
        return idsUserApiRepository.findProfileByIdsUserIdList(inPokerIdsUserIds);
    }
}
