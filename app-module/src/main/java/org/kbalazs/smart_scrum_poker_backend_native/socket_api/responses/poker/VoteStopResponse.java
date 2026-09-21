package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;

import java.util.UUID;

@Schema(description = "Response when voting is stopped for a ticket")
public record VoteStopResponse(
    @Schema(description = "Poker game ID")
    UUID pokerIdSecure,
    @Schema(description = "ID of the finished ticket")
    long finishedTicketId,
    @Schema(description = "Vote results and statistics")
    VotesWithVoteStat voteResult
)
{
}
