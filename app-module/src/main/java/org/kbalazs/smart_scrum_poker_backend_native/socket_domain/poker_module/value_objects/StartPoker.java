package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;

import java.util.List;

public record StartPoker(@NonNull Poker poker, @NonNull List<Ticket> tickets)
{
}
