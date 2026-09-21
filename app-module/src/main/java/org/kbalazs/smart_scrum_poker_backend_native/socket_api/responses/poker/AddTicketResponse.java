package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;

@Schema(description = "Response when a ticket is added to a poker game")
public record AddTicketResponse(
    @Schema(description = "The added ticket")
    @NonNull Ticket ticket
)
{
}
