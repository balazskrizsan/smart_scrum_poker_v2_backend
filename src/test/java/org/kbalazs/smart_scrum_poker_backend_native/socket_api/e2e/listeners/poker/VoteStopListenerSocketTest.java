package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteStopResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_ROUND_STOP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteStopListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_RoundEndResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1Poker.class, Insert3TicketsAllInactive.class, Insert5VotesFor2Poker3Ticket.class})
    @SneakyThrows
    public void stoppingVote_returnsVoteStatistic()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        long testedTicketId = TicketFakeBuilder.defaultId2;
        String testedDestination = "/app/poker/vote.stop/" + testedPokerIdSecret + "/" + testedTicketId;
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecret;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = POKER_ROUND_STOP.getValue();
        VoteStopResponse expectedResponse = new VoteStopResponse(
            PokerFakeBuilder.defaultIdSecure1,
            TicketFakeBuilder.defaultId2,
            new VotesWithVoteStat(
                Map.of(
                    VoteFakeBuilder.defaultCreatedBy3, new VoteFakeBuilder().build3(),
                    VoteFakeBuilder.defaultCreatedBy4, new VoteFakeBuilder().build4()
                ),
                new VoteStat(9, VoteFakeBuilder.defaultCalculatedPoint, VoteFakeBuilder.defaultCalculatedPoint4)
            )
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_RoundEndResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, "{}");

        ResponseEntity_ResponseData_RoundEndResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isEqualTo(expectedResponse),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_RoundEndResponse(
        ResponseData_RoundEndResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_RoundEndResponse(
        VoteStopResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
