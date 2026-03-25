package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;//package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3Poker2WithSameCreatedBy;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.MyPokersRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.MyPokersResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__MY_TICKETS;

public class MyPokersListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_MyPokersResponse> completableFuture = new CompletableFuture<>();

    @Test
    @SqlPreset(presets = {Insert3Poker2WithSameCreatedBy.class, Insert1InsecureUser.class}, truncateAfter = false)
    @SneakyThrows
    public void getMyPokers_notifyMyListenerWithCreatedPokers()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        MyPokersRequest testedPokerStartRequest = new MyPokersRequest(InsecureUserFakeBuilder.defaultIdSecure2);

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedDestination = SEND__POKER__MY_TICKETS.getValue();
        String testedSubscribeUrl = "/user/queue/reply";
        List<Poker> expectedPokers = List.of(
            new PokerFakeBuilder().build2(),
            new PokerFakeBuilder().createdBy3(InsecureUserFakeBuilder.defaultIdSecure2).build3()
        );

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            completableFuture,
            ResponseEntity_ResponseData_MyPokersResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send("/app/poker/my.tickets", testedPokerStartRequest);

        // Assert
        completableFuture.join();

        ResponseEntity_ResponseData_MyPokersResponse actualResponse = completableFuture.get();

        assertAll(
            () -> assertThat(actualResponse.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actualResponse.body().socketResponseDestination).isEqualTo(expectedDestination),
            () -> assertThat(actualResponse.body().data().pokers()).isEqualTo(expectedPokers)
        );
    }

    private record ResponseEntity_ResponseData_MyPokersResponse(
        ResponseData_MyPokersResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_MyPokersResponse(
        MyPokersResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
