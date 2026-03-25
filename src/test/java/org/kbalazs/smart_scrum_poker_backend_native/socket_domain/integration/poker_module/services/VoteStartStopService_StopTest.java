package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3Tickets2Inactive1Active;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteStartStopService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteStartStopService_StopTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1Poker.class, Insert3Tickets2Inactive1Active.class, Insert5VotesFor2Poker3Ticket.class})
    @SneakyThrows
    public void stopAStopableRound_broadcastToGame()
    {
        // Arrange
        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        long testedTicketId = TicketFakeBuilder.defaultId2;
        var expectedTickets = new TicketFakeBuilder().build1to3AsList();
        var expected = new VotesWithVoteStat(
            Map.of(
                VoteFakeBuilder.defaultCreatedBy3, new VoteFakeBuilder().build3(),
                VoteFakeBuilder.defaultCreatedBy4, new VoteFakeBuilder().build4()
            ),
            new VoteStat(9, VoteFakeBuilder.defaultCalculatedPoint, VoteFakeBuilder.defaultCalculatedPoint4)
        );

        // Act
        VotesWithVoteStat actual = createInstance(VoteStartStopService.class).stop(testedPokerIdSecret, testedTicketId);

        // Assert
        List<Ticket> actualTicket = getDslContext().selectFrom(ticketTable).orderBy(ticketTable.ID.asc()).fetchInto(Ticket.class);

        assertAll(
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actualTicket).isEqualTo(expectedTickets)
        );
    }

    @Test
    public void notExistingPoker_throwsException()
    {
        // Arrange
        UUID testedPokerId = PokerFakeBuilder.defaultIdSecure1;
        long testedTicketId = TicketFakeBuilder.defaultId1;
        String exceptedMessage = "Poker not found: id#" + PokerFakeBuilder.defaultIdSecure1;

        // Act - Assert
        assertThatThrownBy(() -> createInstance(VoteStartStopService.class).stop(testedPokerId, testedTicketId))
            .isInstanceOf(PokerException.class)
            .hasMessage(exceptedMessage);
    }
}
