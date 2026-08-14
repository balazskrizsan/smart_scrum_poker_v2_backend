package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import lombok.NonNull;

import java.util.UUID;

public record AddTicket(@NonNull UUID userIdSecure, @NonNull UUID pokerIdSecure, @NonNull String ticketName)
{
}
