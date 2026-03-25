package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;


import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GameStateResponse(
    Poker poker,
    List<Ticket> tickets,
    List<InsecureUser> inGameInsecureUsers,
    Map<Long, Map<UUID, Vote>> votes, // @todo: remove
    InsecureUser owner,
    InsecureUser currentInsecureUser,
    List<InsecureUser> inGameInsecureUsersWithSession,
    Map<Long, VotesWithVoteStat> votesWithVoteStatList
)
{
}
