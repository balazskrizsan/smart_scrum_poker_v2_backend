package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Session;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.EmptyPayload;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse;
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
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_STATE;

public class StateListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_StateResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class, Insert1Session.class})
    @SneakyThrows
    public void state_returnsStateResponse()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = PokerFakeBuilder.defaultPublicId1;
        EmptyPayload testedRequest = new EmptyPayload();

        String testedDestination = "/app/poker/state/" + testedPokerPublicId;
        String testedSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_STATE.getValue();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_StateResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_StateResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isNotNull(),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_StateResponse(
        ResponseData_StateResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_StateResponse(
        StateResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
