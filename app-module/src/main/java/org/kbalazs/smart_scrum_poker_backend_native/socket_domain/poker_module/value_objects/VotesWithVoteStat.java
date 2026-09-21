package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;

import java.util.Map;
import java.util.UUID;

@Schema(description = "Container for votes and their statistics")
public record VotesWithVoteStat(
    @Schema(description = "Map of user IDs to their votes")
    Map<UUID, Vote> votes,
    @Schema(description = "Vote statistics")
    VoteStat voteStat
)
{
}
