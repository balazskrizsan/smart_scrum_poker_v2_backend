package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record Vote(
    Long id,
    Long ticketId,
    short uncertainty,
    short complexity,
    short effort,
    short risk,
    Short calculatedPoint,
    @NonNull LocalDateTime createdAt,
    @NonNull UUID createdBy
)
{
}
