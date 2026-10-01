package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response when a ticket is opened")
public record TicketOpened(
    @Schema(description = "ID of the opened ticket")
    long openedTicketId
)
{
}
