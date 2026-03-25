package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.kbalazs.smart_scrum_poker_backend_native.helpers.CustomAsserts.softPokerAssert;
import static org.kbalazs.smart_scrum_poker_backend_native.helpers.CustomAsserts.softTicketAssert;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteStartListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_StartResponse> completableFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class})
    @SneakyThrows
    public void startAPoker_createsDbFromStompPublish()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        var testedPokerStartRequest = new StartRequest(
            PokerFakeBuilder.defaultSprintName,
            List.of(TicketFakeBuilder.defaultName1, TicketFakeBuilder.defaultName2),
            InsecureUserFakeBuilder.defaultIdSecure1
        );

        Poker expectedDbPoker = new PokerFakeBuilder().id(1L).build();
        Ticket expectedDbTicket0 = new TicketFakeBuilder().id(1L).pokerId(expectedDbPoker.id()).build();
        Ticket expectedDbTicket1 = new TicketFakeBuilder().id2(2L).pokerId2(expectedDbPoker.id()).build2();
        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_START.getValue();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            completableFuture,
            ResponseEntity_ResponseData_StartResponse.class
        );
        stompSession.subscribe("/user/queue/reply", stompHandler);
        stompSession.send("/app/poker/start", testedPokerStartRequest);

        // Assert
        completableFuture.join();

        ResponseEntity_ResponseData_StartResponse actualResponse = completableFuture.get();

        Poker actualDbPoker = getDslContext().selectFrom(pokerTable).fetchOneInto(Poker.class);
        List<Ticket> actualDbTickets = getDslContext().selectFrom(ticketTable).fetchInto(Ticket.class);
        Ticket actualDbTickets0 = actualDbTickets.get(0);
        Ticket actualDbTickets1 = actualDbTickets.get(1);

        assertAll(
            () -> assertThat(actualResponse.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actualResponse.body().socketResponseDestination).isEqualTo(expectedDestination),
            () -> assertThat(actualResponse.body().data().poker()).isEqualTo(actualDbPoker),
            () -> softPokerAssert(actualDbPoker, expectedDbPoker),
            () -> softTicketAssert(actualDbTickets0, expectedDbTicket0),
            () -> softTicketAssert(actualDbTickets1, expectedDbTicket1)
        );
    }

    private record ResponseEntity_ResponseData_StartResponse(
        ResponseData_StartResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_StartResponse(
        StartResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
