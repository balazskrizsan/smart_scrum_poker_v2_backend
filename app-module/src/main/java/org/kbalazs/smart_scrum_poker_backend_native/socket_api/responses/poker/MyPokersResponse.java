package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;

import java.util.List;

@Schema(description = "Response containing user's poker games")
public record MyPokersResponse(
    @Schema(description = "List of poker games")
    @NonNull List<Poker> pokers
)
{
}
