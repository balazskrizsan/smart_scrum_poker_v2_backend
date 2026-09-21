package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.NonNull;
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
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.RoundStartResponse;
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
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_ROUND_START;

public class VoteStartListenerSocketTest extends AbstractE2eSocketTest
{
    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class, Insert1Ticket.class})
    @SneakyThrows
    public void voteStart_returnsRoundStartResponse()
    {
        CompletableFuture<ResponseEntity_ResponseData_RoundStartResponse> responseFuture = new CompletableFuture<>();

        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = PokerFakeBuilder.defaultPublicId1;
        long testedTicketId = TicketFakeBuilder.defaultId1;
        EmptyPayload testedRequest = new EmptyPayload();

        String testedDestination = "/app/poker/vote.start/" + testedPokerPublicId + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerPublicId;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_ROUND_START.getValue();
        RoundStartResponse expectedData = new RoundStartResponse(testedTicketId);

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_RoundStartResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_RoundStartResponse actual = responseFuture.get(SOCKET_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data())
                .usingRecursiveComparison()
                .isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_RoundStartResponse(
        @NonNull ResponseData_RoundStartResponse body,
        @NonNull Map<String, String> headers,
        @NonNull String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_RoundStartResponse(
        @NonNull RoundStartResponse data,
        boolean success,
        int errorCode,
        @NonNull String requestId,
        @NonNull String socketResponseDestination
    )
    {
    }
}
