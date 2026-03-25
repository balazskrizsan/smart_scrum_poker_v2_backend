package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketDeleteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_TICKET_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TicketDeleteSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_TicketDeleted> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {
        Insert3InsecureUser.class,
        Insert1Poker.class,
        Insert3TicketsAllInactive.class,
        Insert5VotesFor2Poker3Ticket.class
    })
    @SneakyThrows
    public void stoppingVote_returnsVoteStatistic()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        VoteRequest testedVoteRequest = new VoteRequestFakeBuilder().build();
        long testedTicketId = TicketFakeBuilder.defaultId1;

        String testedDestination = "/app/poker/ticket.delete/" + testedPokerIdSecret + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecret;
        System.out.println(testedDestination);
        System.out.println(testedSubscribeUrl);

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_TICKET_DELETE.getValue();
        TicketDeleteResponse expectedData = new TicketDeleteResponse(TicketFakeBuilder.defaultId1);

        List<Ticket> expectedTickets = List.of(new TicketFakeBuilder().build2(), new TicketFakeBuilder().build3());
        List<Vote> expectedVotes = new VoteFakeBuilder().build3to5();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_TicketDeleted.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedVoteRequest);

        ResponseEntity_ResponseData_TicketDeleted actual = responseFuture.get(1, TimeUnit.SECONDS);

        List<Ticket> actualTickets = getDslContext().selectFrom(ticketTable).fetchInto(Ticket.class);
        List<Vote> actualVotes = getDslContext().selectFrom(voteTable).fetchInto(Vote.class);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination),
            () -> assertThat(actualTickets).isEqualTo(expectedTickets),
            () -> assertThat(actualVotes).isEqualTo(expectedVotes)
        );
    }

    private record ResponseEntity_ResponseData_TicketDeleted(
        ResponseData_TicketDeleted body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_TicketDeleted(
        TicketDeleteResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
