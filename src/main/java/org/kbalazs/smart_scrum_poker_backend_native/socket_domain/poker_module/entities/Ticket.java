package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;

import java.util.UUID;

public record Ticket(Long id, UUID idSecure, Long pokerId, @NonNull String name, boolean isActive)
{
}
