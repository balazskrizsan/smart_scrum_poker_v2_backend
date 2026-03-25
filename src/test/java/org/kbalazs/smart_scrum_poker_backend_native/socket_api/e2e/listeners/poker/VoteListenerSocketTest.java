package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
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

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_VoteResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class, Insert1Poker.class, Insert3TicketsAllInactive.class})
    @SneakyThrows
    public void stoppingVote_returnsVoteStatistic()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        Vote testedVote = new VoteFakeBuilder().id(null).build();
        VoteRequest testedVoteRequest = new VoteRequestFakeBuilder().build();

        String testedDestination = "/app/poker/vote/" + testedPokerIdSecret + "/" + testedVote.ticketId();
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecret;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND_POKER_VOTE.getValue();
        VoteResponse expectedData = new VoteResponse(new InsecureUserFakeBuilder().build());

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_VoteResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedVoteRequest);

        ResponseEntity_ResponseData_VoteResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_VoteResponse(
        ResponseData_VoteResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_VoteResponse(
        VoteResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
