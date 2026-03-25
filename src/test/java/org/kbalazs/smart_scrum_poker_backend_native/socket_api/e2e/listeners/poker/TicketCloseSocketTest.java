package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketClosed;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_TICKET_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TicketCloseSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_TicketClosed> responseFuture = new CompletableFuture<>();

    @Test
    @SneakyThrows
    public void stoppingVote_returnsVoteStatistic()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        VoteRequest testedVoteRequest = new VoteRequestFakeBuilder().build();
        long testedTicketId = TicketFakeBuilder.defaultId1;

        String testedDestination = "/app/poker/ticket.close/" + testedPokerIdSecret + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecret;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_TICKET_CLOSE.getValue();
        TicketClosed expectedData = new TicketClosed(TicketFakeBuilder.defaultId1);

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_TicketClosed.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedVoteRequest);

        ResponseEntity_ResponseData_TicketClosed actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_TicketClosed(
        ResponseData_TicketClosed body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_TicketClosed(
        TicketClosed data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
