package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.StartRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.kbalazs.smart_scrum_poker_backend_native.helpers.CustomAsserts.softPokerAssert;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StartListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_StartResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class})
    @SneakyThrows
    public void stoppingVote_returnsVoteStatistic()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        StartRequest testedRequest = new StartRequestFakeBuilder().build();

        String testedDestination = "/app/poker/start";
        String testedSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_START.getValue();
        StartResponse expectedData = new StartResponse(new PokerFakeBuilder().id(1L).build());

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_StartResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_StartResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> softPokerAssert(actual.body().data().poker(), expectedData.poker()),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
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
