package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.*;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;

public class VoteListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_VoteResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {
        Insert1InsecureUser.class,
        Insert1StoryPointConfig.class,
        Insert1Poker.class,
        Insert1Ticket.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void vote_returnsVoteResponse()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecure = PokerFakeBuilder.defaultIdSecure1;
        VoteRequest testedVoteRequest = new VoteRequestFakeBuilder().build();

        String testedDestination = "/app/poker/vote/" + testedPokerIdSecure + "/" + testedVoteRequest.ticketId();
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecure;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND_POKER_VOTE.getValue();

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
            () -> assertThat(actual.body().data()).isNotNull(),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    @Test
    @SqlPreset(presets = {
        Insert1InsecureUser.class,
        Insert1StoryPointConfig.class,
        Insert1Poker.class,
        Insert1Ticket.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void vote_calculatesStoryPointCorrectly()
    {
        // Arrange
        UUID testedPokerIdSecure = PokerFakeBuilder.defaultIdSecure1;

        // Test data: complexity, risk -> expected calculated point
        // Based on config: complexity + risk determine points
        // Size values: XS=1, S=2, M=3, L=5, XL=8
        // pointsMapping: [1,2] -> 1, [3,5] -> 2, [6,8] -> 3, else -> 100
        VoteTestCase[] testCases = {
            // Total 1-2 range -> 1 point (complexity + risk)
            new VoteTestCase("XS", "XS", (short) 1),  // XS+XS = 1+1 = 2 -> 1

            // Total 3-5 range -> 2 points
            new VoteTestCase("S", "S", (short) 2),  // S+S = 2+2 = 4 -> 2
            new VoteTestCase("S", "M", (short) 2),  // S+M = 2+3 = 5 -> 2
            new VoteTestCase("M", "S", (short) 2),  // M+S = 3+2 = 5 -> 2
            new VoteTestCase("XS", "M", (short) 2),  // XS+M = 1+3 = 4 -> 2
            new VoteTestCase("M", "XS", (short) 2),  // M+XS = 3+1 = 4 -> 2
            new VoteTestCase("S", "XS", (short) 2),  // S+XS = 2+1 = 3 -> 2
            new VoteTestCase("XS", "S", (short) 2),  // XS+S = 1+2 = 3 -> 2

            // Total 6-8 range -> 3 points
            new VoteTestCase("M", "M", (short) 3),  // M+M = 3+3 = 6 -> 3
            new VoteTestCase("M", "L", (short) 3),  // M+L = 3+5 = 8 -> 3
            new VoteTestCase("L", "M", (short) 3),  // L+M = 5+3 = 8 -> 3
            new VoteTestCase("S", "L", (short) 3),  // S+L = 2+5 = 7 -> 3
            new VoteTestCase("L", "S", (short) 3),  // L+S = 5+2 = 7 -> 3

            // Out of range -> 100 points
            new VoteTestCase("L", "L", (short) 100), // L+L = 5+5 = 10 -> 100
            new VoteTestCase("L", "XL", (short) 100), // L+XL = 5+8 = 13 -> 100
            new VoteTestCase("XL", "L", (short) 100), // XL+L = 8+5 = 13 -> 100
            new VoteTestCase("XL", "XL", (short) 100), // XL+XL = 8+8 = 16 -> 100
        };

        for (VoteTestCase testCase : testCases)
        {
            responseFuture = new CompletableFuture<>();

            // Create new session for each iteration to avoid session expiration
            StompSession stompSession = getStompSession();

            Map<String, String> dimensionValues = new HashMap<>();
            dimensionValues.put("uncertainty", "M");  // ignored in calculation
            dimensionValues.put("complexity", testCase.complexity());
            dimensionValues.put("effort", "M");  // ignored in calculation
            dimensionValues.put("risk", testCase.risk());

            VoteRequest testedVoteRequest = new VoteRequest(
                IdsUserFakeBuilder.defaultId1,
                testedPokerIdSecure,
                VoteFakeBuilder.defaultTicketId,
                dimensionValues
            );

            String testedDestination = "/app/poker/vote/" + testedPokerIdSecure + "/" + testedVoteRequest.ticketId();
            String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecure;

            // Act
            StompFrameHandler stompHandler = buildStompFrameHandler(
                responseFuture,
                ResponseEntity_ResponseData_VoteResponse.class
            );
            stompSession.subscribe(testedSubscribeUrl, stompHandler);
            stompSession.send(testedDestination, testedVoteRequest);

            ResponseEntity_ResponseData_VoteResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

            // Disconnect session after each iteration
            stompSession.disconnect();

            // Assert - check that vote was processed successfully and calculated point is correct
            assertAll(
                () -> assertThat(actual.statusCode).isEqualTo(HttpStatus.OK.getReasonPhrase()),
                () -> assertThat(actual.body().data()).isNotNull(),
                () -> assertThat(actual.body().socketResponseDestination).isEqualTo(SEND_POKER_VOTE.getValue()),
                () -> assertThat(actual.body().data().calculatedPoint()).isEqualTo(testCase.expectedCalculatedPoint())
            );
        }
    }

    private record VoteTestCase(
        String complexity,
        String risk,
        short expectedCalculatedPoint
    )
    {
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
