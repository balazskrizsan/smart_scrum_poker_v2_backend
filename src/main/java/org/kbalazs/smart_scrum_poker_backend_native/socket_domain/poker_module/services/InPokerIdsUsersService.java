package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InPokerIdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.InPokerIdsUsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InPokerIdsUsersService
{
    InPokerIdsUsersRepository inPokerIdsUsersRepository;

    public void onDuplicateKeyIgnoreAdd(@NonNull InPokerIdsUser inPokerIdsUser)
    {
        inPokerIdsUsersRepository.onDuplicateKeyIgnoreAdd(inPokerIdsUser);
    }

    public List<InPokerIdsUser> searchUserSecureIdsByPokerIdSecure(UUID pokerIdSecure)
    {
        return inPokerIdsUsersRepository.searchUserSecureIdsByPokerIdSecure(pokerIdSecure);
    }
}
