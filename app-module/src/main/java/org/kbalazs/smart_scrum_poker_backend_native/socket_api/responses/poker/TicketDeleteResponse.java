package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response when a ticket is deleted")
public record TicketDeleteResponse(
    @Schema(description = "ID of the deleted ticket")
    long deletedTicketId
)
{
}
