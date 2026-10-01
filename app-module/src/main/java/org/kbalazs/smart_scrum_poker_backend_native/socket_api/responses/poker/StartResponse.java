package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;

@Schema(description = "Response when a poker game is started")
public record StartResponse(
    @Schema(description = "The created poker game")
    Poker poker
)
{
}
