package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteStartStopService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteStartStopService_StartTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2Poker.class, Insert3TicketsAllInactive.class, Insert5VotesFor2Poker3Ticket.class})
    @SneakyThrows
    public void startAStartableRound_broadcastToGame()
    {
        // Arrange
        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        long testedTicketId = TicketFakeBuilder.defaultId2;
        var expectedTickets = new ArrayList<>()
        {{
            add(new TicketFakeBuilder().build());
            add(new TicketFakeBuilder().isActive2(true).build2());
            add(new TicketFakeBuilder().build3());
        }};
        var expectedVotes = new ArrayList<>()
        {{
            add(new VoteFakeBuilder().build());
            add(new VoteFakeBuilder().build2());
            add(new VoteFakeBuilder().build5());
        }};

        // Act
        createInstance(VoteStartStopService.class).start(testedPokerIdSecret, testedTicketId);

        // Assert
        List<Ticket> actualTicket = getDslContext()
            .selectFrom(ticketTable)
            .orderBy(ticketTable.ID.asc())
            .fetchInto(Ticket.class);
        List<Vote> actualVotes = getDslContext()
            .selectFrom(voteTable)
            .orderBy(voteTable.TICKET_ID.asc())
            .fetchInto(Vote.class);

        assertAll(
            () -> assertThat(actualTicket).isEqualTo(expectedTickets),
            () -> assertThat(actualVotes).isEqualTo(expectedVotes)
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
        assertThatThrownBy(() -> createInstance(VoteStartStopService.class).start(testedPokerId, testedTicketId))
            .isInstanceOf(PokerException.class)
            .hasMessage(exceptedMessage);
    }
}
