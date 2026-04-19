package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record StateResponse(
    Poker poker,
    List<Ticket> tickets,
    List<UserProfile> userProfiles,
    Map<Long, Map<UUID, Vote>> votes, // @todo: remove
    IdsUser owner,
    IdsUser currentIdsUser,
    List<IdsUser> idsUsersWithSession,
    Map<Long, VotesWithVoteStat> votesWithVoteStatList
)
{
}
