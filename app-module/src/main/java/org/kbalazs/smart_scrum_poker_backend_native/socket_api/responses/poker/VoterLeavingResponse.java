package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.util.UUID;

@Schema(description = "Response when a voter leaves the game")
public record VoterLeavingResponse(
    @Schema(description = "ID of the leaving user")
    @NonNull UUID userIdSecure,
    @Schema(description = "ID of the poker game")
    @NonNull UUID pokerIdSecure
)
{
}
