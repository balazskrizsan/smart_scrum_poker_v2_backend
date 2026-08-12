package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;

import java.util.UUID;

public record VoteStopResponse(UUID pokerIdSecure, long finishedTicketId, VotesWithVoteStat voteResult)
{
}
