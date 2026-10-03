package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.EmptyPayload;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketOpened;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__TICKET_OPEN;

public class TicketOpenListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_TicketOpened> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class, Insert1Ticket.class})
    @SneakyThrows
    public void ticketOpen_returnsTicketOpenedResponse()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = PokerFakeBuilder.defaultPublicId1;
        long testedTicketId = TicketFakeBuilder.defaultId1;
        EmptyPayload testedRequest = new EmptyPayload();

        String testedDestination = "/app/poker/ticket.open/" + testedPokerPublicId + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerPublicId;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND__POKER__TICKET_OPEN.getValue();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_TicketOpened.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_TicketOpened actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isNotNull(),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_TicketOpened(
        ResponseData_TicketOpened body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_TicketOpened(
        TicketOpened data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
