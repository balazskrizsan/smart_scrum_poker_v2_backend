package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1SessionsFor1User;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.ConfigCreateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.time.LocalDateTime;
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
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigCreateResponse expectedData = new ConfigCreateResponse(
            new StoryPointConfig(
                null,
                testedRequest.name(),
                objectMapper.writeValueAsString(testedRequest.sizesConfig()),
                objectMapper.writeValueAsString(testedRequest.dimensionsConfig()),
                objectMapper.writeValueAsString(testedRequest.pointsMapping()),
                LocalDateTime.now(),
                IdsUserFakeBuilder.defaultId1
            )
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_ConfigCreateResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_ConfigCreateResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertThat(actual.statusCode).isEqualTo(expectedHttpStatus);
        assertThat(actual.body().data())
            .usingRecursiveComparison()
            .ignoringFields(
                "config.id",
                "config.createdAt",
                "config.sizesConfig",
                "config.dimensionsConfig",
                "config.pointsMapping"
            )
            .isEqualTo(expectedData);
        assertThat(objectMapper.readTree(actual.body().data().config().sizesConfig()))
            .isEqualTo(objectMapper.readTree(expectedData.config().sizesConfig()));
        assertThat(objectMapper.readTree(actual.body().data().config().dimensionsConfig()))
            .isEqualTo(objectMapper.readTree(expectedData.config().dimensionsConfig()));
        assertThat(objectMapper.readTree(actual.body().data().config().pointsMapping()))
            .isEqualTo(objectMapper.readTree(expectedData.config().pointsMapping()));
        assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination);
    }

    private record ResponseEntity_ResponseData_ConfigCreateResponse(
        @NonNull ResponseData_ConfigCreateResponse body,
        @NonNull Map<String, String> headers,
        @NonNull String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_ConfigCreateResponse(
        @NonNull ConfigCreateResponse data,
        boolean success,
        int errorCode,
        @NonNull String requestId,
        @NonNull String socketResponseDestination
    )
    {
    }

    private record ConfigCreateResponse(@NonNull StoryPointConfig config)
    {
    }
}
