package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record InPokerIdsUser(@NonNull UUID idsUserId, @NonNull Long pokerId, LocalDateTime createdAt)
{
    public InPokerIdsUser(@NonNull UUID idsUserId, @NonNull Long pokerId)
    {
        this(idsUserId, pokerId, null);
    }
}
