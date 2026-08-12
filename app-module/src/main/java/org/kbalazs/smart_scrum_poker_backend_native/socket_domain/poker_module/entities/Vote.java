package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

public record Vote(
    Long id,
    Long ticketId,
    @With
    Long storyPointConfigId,
    @NonNull String voteValues,  // JSON: {"uncertainty": "S", "complexity": "M", "effort": "L", "risk": "S"}
    @With
    Short calculatedPoint,
    @NonNull LocalDateTime createdAt,
    @NonNull UUID createdBy
)
{
}
