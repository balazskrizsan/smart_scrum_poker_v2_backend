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
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteStopResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
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
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_ROUND_STOP;

public class VoteStopListenerSocketTest extends AbstractE2eSocketTest
{
    @Test
    @SqlPreset(presets = {Insert1User.class, Insert1StoryPointConfig.class, Insert1Poker.class, Insert1Ticket.class})
    @SneakyThrows
    public void voteStop_returnsVoteStopResponse()
    {
        CompletableFuture<ResponseEntity_ResponseData_VoteStopResponse> responseFuture = new CompletableFuture<>();

        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = PokerFakeBuilder.defaultPublicId1;
        long testedTicketId = TicketFakeBuilder.defaultId1;
        EmptyPayload testedRequest = new EmptyPayload();

        String testedDestination = "/app/poker/vote.stop/" + testedPokerPublicId + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerPublicId;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_ROUND_STOP.getValue();
        VoteStopResponse expectedData = new VoteStopResponse(
            testedPokerPublicId,
            testedTicketId,
            new VotesWithVoteStat(Map.of(), new VoteStat(null, null, null))
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_VoteStopResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_VoteStopResponse actual = responseFuture.get(SOCKET_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data())
                .usingRecursiveComparison()
                .isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_VoteStopResponse(
        @NonNull ResponseData_VoteStopResponse body,
        @NonNull Map<String, String> headers,
        @NonNull String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_VoteStopResponse(
        @NonNull VoteStopResponse data,
        boolean success,
        int errorCode,
        @NonNull String requestId,
        @NonNull String socketResponseDestination
    )
    {
    }
}
