package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;

import java.util.Map;
import java.util.UUID;

public record VotesWithVoteStat(Map<UUID, Vote> votes, VoteStat voteStat)
{
}
