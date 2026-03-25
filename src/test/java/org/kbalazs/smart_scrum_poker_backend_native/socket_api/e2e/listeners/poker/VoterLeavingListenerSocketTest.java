package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoterLeavingRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoterLeavingResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoterLeaving;
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
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__VOTER_LEAVING;

public class VoterLeavingListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_VoterLeavingResponse> responseFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class, Insert1Poker.class, Insert3TicketsAllInactive.class})
    @SneakyThrows
    public void voterLeavesPoker_notifyPokerListeners()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedPokerIdSecret = PokerFakeBuilder.defaultIdSecure1;
        VoterLeaving testedLeavingRequest = new VoterLeavingRequestFakeBuilder().build();

        String testedDestination = "/app/poker/voter_leaving";
        String testedSubscribeUrl = "/queue/reply-" + testedPokerIdSecret;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND__POKER__VOTER_LEAVING.getValue();
        VoterLeavingResponse expectedData = new VoterLeavingResponse(
            InsecureUserFakeBuilder.defaultIdSecure1,
            PokerFakeBuilder.defaultIdSecure1
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_VoterLeavingResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedLeavingRequest);

        ResponseEntity_ResponseData_VoterLeavingResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isEqualTo(expectedData),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(expectedDestination)
        );
    }

    private record ResponseEntity_ResponseData_VoterLeavingResponse(
        ResponseData_VoterLeavingResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_VoterLeavingResponse(
        VoterLeavingResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
