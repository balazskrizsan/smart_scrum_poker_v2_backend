package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteService_GetVotesWithTicketGroupByTicketIdsTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {
        Insert3InsecureUser.class,
        Insert1Poker.class,
        Insert3TicketsAllInactive.class,
        Insert5VotesFor2Poker3Ticket.class
    })
    @SneakyThrows
    public void selectFromDb_returnsGroupedVotes()
    {
        // Arrange
        List<Long> testedTicketIds = List.of(TicketFakeBuilder.defaultId1, TicketFakeBuilder.defaultId2);
        Map<Long, Map<UUID, Vote>> expected = Map.of(
            TicketFakeBuilder.defaultId1, Map.of(
                VoteFakeBuilder.defaultCreatedBy, new VoteFakeBuilder().build(),
                VoteFakeBuilder.defaultCreatedBy2, new VoteFakeBuilder().build2()
            ),
            TicketFakeBuilder.defaultId2, Map.of(
                VoteFakeBuilder.defaultCreatedBy3, new VoteFakeBuilder().build3(),
                VoteFakeBuilder.defaultCreatedBy4, new VoteFakeBuilder().build4()
            )
        );

        // Act
        Map<Long, Map<UUID, Vote>> actual = createInstance(VoteService.class)
            .getVotesWithTicketGroupByTicketIds(testedTicketIds);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
