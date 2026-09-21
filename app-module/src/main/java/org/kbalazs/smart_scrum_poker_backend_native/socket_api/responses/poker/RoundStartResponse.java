package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response when voting round is started")
public record RoundStartResponse(
    @Schema(description = "ID of the started ticket")
    long startedTicketId
)
{
}
