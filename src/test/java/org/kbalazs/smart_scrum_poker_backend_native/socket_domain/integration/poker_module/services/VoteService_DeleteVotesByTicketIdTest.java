package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3Tickets2Inactive1Active;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteService_DeleteVotesByTicketIdTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1Poker.class, Insert3Tickets2Inactive1Active.class, Insert5VotesFor2Poker3Ticket.class})
    @SneakyThrows
    public void insertedVotes_deletedAfterCall()
    {
        // Arrange
        val expected = List.of(
            new VoteFakeBuilder().build3(),
            new VoteFakeBuilder().build4(),
            new VoteFakeBuilder().build5()
        );

        // Act
        createInstance(VoteService.class).deleteVotesByTicketId(VoteFakeBuilder.defaultTicketId);

        // Assert
        List<Vote> actual = getDslContext().selectFrom(voteTable).fetchInto(Vote.class);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }
}
