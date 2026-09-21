package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Session;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.StoryPointConfigFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.EmptyPayload;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_STATE;
import static org.mockito.Mockito.when;

public class StateListenerSocketTest extends AbstractE2eSocketTest
{
    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class, Insert1Session.class})
    @SneakyThrows
    public void state_returnsStateResponse()
    {
        CompletableFuture<ResponseEntity_ResponseData_StateResponse> responseFuture = new CompletableFuture<>();

        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = PokerFakeBuilder.defaultPublicId1;
        EmptyPayload testedRequest = new EmptyPayload();

        String testedDestination = "/app/poker/state/" + testedPokerPublicId;
        String testedSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_STATE.getValue();

        IdsUser expectedIdsUser = new IdsUserFakeBuilder().build();
        UserProfile expectedUserProfile = new UserProfile(
            IdsUserFakeBuilder.defaultId1.toString(),
            "Test User",
            "testuser"
        );
        when(idsUserService.getById(IdsUserFakeBuilder.defaultId1)).thenReturn(expectedIdsUser);
        when(idsUserService.findProfileByIdsUserId(IdsUserFakeBuilder.defaultId1)).thenReturn(expectedUserProfile);
        when(idsUserService.searchUsersWithActiveSession(List.of(IdsUserFakeBuilder.defaultId1))).thenReturn(List.of(expectedIdsUser));
        when(idsUserService.findProfileByIdsUserIdList(List.of(IdsUserFakeBuilder.defaultId1))).thenReturn(List.of(expectedUserProfile));

        StateResponse expectedData = new StateResponse(
            new PokerFakeBuilder().build(),
            List.of(),
            List.of(expectedUserProfile),
            Map.of(),
            expectedIdsUser,
            List.of(expectedIdsUser),
            Map.of(),
            expectedIdsUser,
            expectedUserProfile,
            new StoryPointConfigFakeBuilder()
                .name("Default Config")
                .pointsMapping("""
                    [
                        {"totalRange": [0, 4], "points": 1},
                        {"totalRange": [5, 6], "points": 2},
                        {"totalRange": [7, 8], "points": 3},
                        {"totalRange": [9, 10], "points": 5},
                        {"totalRange": [11, 15], "points": 8},
                        {"totalRange": [16, 100], "points": 13}
                    ]
                    """)
                .build()
        );

        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_StateResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_StateResponse actual = responseFuture.get(SOCKET_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data())
                .usingRecursiveComparison()
                .ignoringFields(
                    "storyPointConfig.sizesConfig",
                    "storyPointConfig.dimensionsConfig",
                    "storyPointConfig.pointsMapping"
                )
                .isEqualTo(expectedData),
            () -> assertThat(objectMapper.readTree(actual.body().data().storyPointConfig().sizesConfig()))
                .isEqualTo(objectMapper.readTree(expectedData.storyPointConfig().sizesConfig())),
            () -> assertThat(objectMapper.readTree(actual.body().data().storyPointConfig().dimensionsConfig()))
                .isEqualTo(objectMapper.readTree(expectedData.storyPointConfig().dimensionsConfig())),
            () -> assertThat(objectMapper.readTree(actual.body().data().storyPointConfig().pointsMapping()))
                .isEqualTo(objectMapper.readTree(expectedData.storyPointConfig().pointsMapping())),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_StateResponse(
        @NonNull ResponseData_StateResponse body,
        @NonNull Map<String, String> headers,
        @NonNull String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_StateResponse(
        @NonNull StateResponse data,
        boolean success,
        int errorCode,
        @NonNull String requestId,
        @NonNull String socketResponseDestination
    )
    {
    }
}
