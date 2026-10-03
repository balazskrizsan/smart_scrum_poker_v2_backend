package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.AddTicketRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.AddTicketRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.AddTicketResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__NEW_TICKET_CREATE;

public class AddTicketListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_AddTicketResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class})
    @SneakyThrows
    public void addTicket_returnsTicketResponse()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        AddTicketRequest testedRequest = new AddTicketRequestFakeBuilder().build();

        String testedDestination = "/app/poker/new.ticket.create";
        String testedSubscribeUrl = "/queue/reply-" + testedRequest.pokerIdSecure();

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND__POKER__NEW_TICKET_CREATE.getValue();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_AddTicketResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_AddTicketResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isNotNull(),
            () -> assertThat(actual.body().data().ticket()).isNotNull(),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_AddTicketResponse(
        ResponseData_AddTicketResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_AddTicketResponse(
        AddTicketResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
