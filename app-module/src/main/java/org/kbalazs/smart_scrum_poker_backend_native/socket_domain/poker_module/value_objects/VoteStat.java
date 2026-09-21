package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Vote statistics for a ticket")
public record VoteStat(
    @Schema(description = "Average vote value")
    Double avg,
    @Schema(description = "Minimum vote value")
    Short min,
    @Schema(description = "Maximum vote value")
    Short max
)
{
}
