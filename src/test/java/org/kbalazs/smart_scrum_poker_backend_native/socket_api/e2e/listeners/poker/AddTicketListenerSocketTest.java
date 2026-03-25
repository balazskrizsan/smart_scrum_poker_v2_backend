package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;//package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.AddTicketRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.AddTicketResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__NEW_TICKET_CREATE;

public class AddTicketListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_NewTicketResponse> completableFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1Poker.class, Insert1InsecureUser.class})
    @SneakyThrows
    public void addTicketToCreatedPoker_notifyListenersForNewElement()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        AddTicketRequest testedPokerStartRequest = new AddTicketRequest(
            InsecureUserFakeBuilder.defaultIdSecure1,
            PokerFakeBuilder.defaultIdSecure1,
            TicketFakeBuilder.defaultName1
        );

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND__POKER__NEW_TICKET_CREATE.getValue();
        int expectedTicketsInDb = 1;
        String testedSubscribeUrl = "/queue/reply-" + PokerFakeBuilder.defaultIdSecure1;

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            completableFuture,
            ResponseEntity_ResponseData_NewTicketResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send("/app/poker/new.ticket.create", testedPokerStartRequest);

        // Assert
        completableFuture.join();

        ResponseEntity_ResponseData_NewTicketResponse actualResponse = completableFuture.get();

        List<Ticket> actualDbTickets = getDslContext().selectFrom(ticketTable).fetchInto(Ticket.class);

        assertAll(
            () -> assertThat(actualResponse.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actualResponse.body().socketResponseDestination).isEqualTo(expectedDestination),
            () -> assertThat(actualResponse.body().data().ticket()).isEqualTo(actualDbTickets.getFirst()),
            () -> assertThat(actualDbTickets.size()).isEqualTo(expectedTicketsInDb)
        );
    }

    private record ResponseEntity_ResponseData_NewTicketResponse(
        ResponseData_NewTicketResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_NewTicketResponse(
        AddTicketResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
