package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.TicketService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TicketService_DeleteTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {
        Insert3InsecureUser.class,
        Insert1Poker.class,
        Insert3TicketsAllInactive.class,
        Insert5VotesFor2Poker3Ticket.class
    })
    @SneakyThrows
    public void deleteTicketFromFilledDb_deletesTicket()
    {
        // Arrange
        long testedTicketId = TicketFakeBuilder.defaultId1;
        List<Ticket> expectedTickets = List.of(new TicketFakeBuilder().build2(), new TicketFakeBuilder().build3());
        List<Vote> expectedVotes = new VoteFakeBuilder().build3to5();

        // Act
        createInstance(TicketService.class).delete(testedTicketId);

        // Assert
        List<Ticket> actualTickets = getDslContext().selectFrom(ticketTable).fetchInto(Ticket.class);
        List<Vote> actualVotes = getDslContext().selectFrom(voteTable).fetchInto(Vote.class);

        assertAll(
            () -> assertThat(actualTickets).isEqualTo(expectedTickets),
            () -> assertThat(actualVotes).isEqualTo(expectedVotes)
        );

    }
}
