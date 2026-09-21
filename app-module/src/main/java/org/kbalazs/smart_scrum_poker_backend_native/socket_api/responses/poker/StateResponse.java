package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Schema(description = "Complete state of a poker game including all tickets, users, and votes")
public record StateResponse(
    @Schema(description = "The poker game information")
    @NonNull Poker poker,
    @Schema(description = "List of tickets in the poker game")
    @NonNull List<Ticket> tickets,
    @Schema(description = "List of user profiles participating in the game")
    @NonNull List<UserProfile> userProfiles,
    @Schema(hidden = true)
    Map<Long, Map<UUID, Vote>> votes, // @todo: remove
    @Schema(description = "Owner")
    @NonNull IdsUser owner,
    @Schema(description = "List of IDS users with active sessions")
    @NonNull List<IdsUser> idsUsersWithSession,
    @Schema(description = "Map of ticket IDs to vote statistics", hidden = true)
    @NonNull Map<Long, VotesWithVoteStat> votesWithVoteStatList,
    @Schema(description = "The current IDS user")
    @NonNull IdsUser currentIdsUser,
    @Schema(description = "The current user profile")
    @NonNull UserProfile currentUserProfile,
    @Schema(description = "Story point configuration for the poker game")
    @NonNull StoryPointConfig storyPointConfig
)
{
}
