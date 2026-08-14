package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;


import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;

public record AddTicketResponse(@NonNull Ticket ticket)
{
}
