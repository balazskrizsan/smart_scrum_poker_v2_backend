package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.PokerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PokerService
{
    PokerRepository pokerRepository;

    // @todo: unit test
    public Poker create(@NonNull Poker poker) throws PokerException
    {
        return pokerRepository.create(poker);
    }

    public Poker findByPublicId(@NonNull UUID pokerIdSecure) throws PokerException
    {
        return pokerRepository.findByPublicId(pokerIdSecure);
    }

    public Map<UUID, Poker> searchWatchedPokers(@NonNull UUID idsUserId)
    {
        return pokerRepository.searchWatchedPokers(idsUserId);
    }

    public List<Poker> searchByInsecureUserId(@NonNull UUID idsUserId)
    {
        return pokerRepository.searchByInsecureUserId(idsUserId);
    }
}
