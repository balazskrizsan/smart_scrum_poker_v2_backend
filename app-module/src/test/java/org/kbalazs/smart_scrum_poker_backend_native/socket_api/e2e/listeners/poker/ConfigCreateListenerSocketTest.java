package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1SessionsFor1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.ConfigCreateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_CONFIG_CREATE;

public class ConfigCreateListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_ConfigCreateResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1SessionsFor1User.class,
    })
    @SneakyThrows
    public void createConfig_returnsConfigResponse()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        ConfigCreateRequest testedRequest = new ConfigCreateRequest(
            "Test Config",
            List.of(
                new ConfigCreateRequest.SizeConfig("Size S"),
                new ConfigCreateRequest.SizeConfig("Size M"),
                new ConfigCreateRequest.SizeConfig("Size L")
            ),
            List.of(
                new ConfigCreateRequest.DimensionConfig(
                    "Uncertainty",
                    List.of(
                        new ConfigCreateRequest.SizeValue("Size S", 1),
                        new ConfigCreateRequest.SizeValue("Size M", 2),
                        new ConfigCreateRequest.SizeValue("Size L", 3)
                    )
                ),
                new ConfigCreateRequest.DimensionConfig(
                    "Complexity",
                    List.of(
                        new ConfigCreateRequest.SizeValue("Size S", 1),
                        new ConfigCreateRequest.SizeValue("Size M", 2),
                        new ConfigCreateRequest.SizeValue("Size L", 3)
                    )
                )
            ),
            List.of(
                new ConfigCreateRequest.PointsMapping(List.of(0, 3), 1),
                new ConfigCreateRequest.PointsMapping(List.of(4, 6), 2),
                new ConfigCreateRequest.PointsMapping(List.of(7, 10), 3)
            )
        );

        String testedDestination = "/app/poker/config/create";
        String testedSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_CONFIG_CREATE.getValue();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_ConfigCreateResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_ConfigCreateResponse actual = responseFuture.get(20, TimeUnit.SECONDS);

        // Assert
        assertThat(actual.statusCode).isEqualTo(expectedHttpStatus);
        assertThat(actual.body().data()).isNotNull();
        assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination);
        assertThat(actual.body().data().config().id()).isEqualTo(1L);
        assertThat(actual.body().data().config().name()).isEqualTo("Test Config");
    }

    private record ResponseEntity_ResponseData_ConfigCreateResponse(
        ResponseData_ConfigCreateResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_ConfigCreateResponse(
        ConfigCreateResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }

    private record ConfigCreateResponse(StoryPointConfig config)
    {
    }
}
