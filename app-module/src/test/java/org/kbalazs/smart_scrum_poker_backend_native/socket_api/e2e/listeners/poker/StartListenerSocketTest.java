package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.StartRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.StoryPointConfigFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_START;

public class StartListenerSocketTest extends AbstractE2eSocketTest
{
    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class})
    @SneakyThrows
    public void start_returnsPokerResponse()
    {
        CompletableFuture<ResponseEntity_ResponseData_StartResponse> responseFuture = new CompletableFuture<>();

        // Arrange
        StompSession stompSession = getStompSession();

        StartRequest testedRequest = new StartRequestFakeBuilder().build();

        String testedDestination = "/app/poker/start";
        String testedSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_START.getValue();
        StartResponse expectedData = new StartResponse(
            new Poker(
                null,
                null,
                testedRequest.sprintTitle(),
                LocalDateTime.now(),
                IdsUserFakeBuilder.defaultId1,
                StoryPointConfigFakeBuilder.defaultId1
            )
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_StartResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_StartResponse actual = responseFuture.get(SOCKET_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data())
                .usingRecursiveComparison()
                .ignoringFields("poker.id", "poker.publicId", "poker.createdAt")
                .isEqualTo(expectedData),
            () -> assertThat(actual.body().data().poker().publicId()).isNotNull(),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_StartResponse(
        @NonNull ResponseData_StartResponse body,
        @NonNull Map<String, String> headers,
        @NonNull String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_StartResponse(
        @NonNull StartResponse data,
        boolean success,
        int errorCode,
        @NonNull String requestId,
        @NonNull String socketResponseDestination
    )
    {
    }
}
