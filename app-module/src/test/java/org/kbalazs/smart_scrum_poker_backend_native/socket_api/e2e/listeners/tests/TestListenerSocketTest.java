package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.tests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Session;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1User;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.tests.TestRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.tests.TestResponse;
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

public class TestListenerSocketTest extends AbstractE2eSocketTest
{
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> responseFuture = new CompletableFuture<>();
    CompletableFuture<TestResponse> simpleResponseFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> broadcastResponseFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> userResponseFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> broadcastDifferentFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> userDifferentFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> broadcastNotificationFuture = new CompletableFuture<>();
    CompletableFuture<ResponseEntity_ResponseData_TestResponse> userNotificationFuture = new CompletableFuture<>();


    @Test
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void echo_AppTestEchoListener_responseToSendToQueueTestEcho()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        String testedMessage = "Hello World";
        TestRequest testedRequest = new TestRequest(testedMessage);

        String testedDestination = "/app/test/echo";
        String testedSubscribeUrl = "/queue/test/echo";

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            simpleResponseFuture,
            TestResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        TestResponse actual = simpleResponseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertThat(actual.message()).isEqualTo(testedMessage);
    }

    @Test
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void echo_AppTestEchoIdListener_responseToSendToUserUserQueueReplyId()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedId = IdsUserFakeBuilder.defaultId1;
        String testedMessage = "Hello World";
        TestRequest testedRequest = new TestRequest(testedMessage);

        String testedDestination = "/app/test/echo/" + testedId;
        String testedSubscribeUrl = "/user/queue/reply/" + testedId;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_TestResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_TestResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(actual.body().data()).isNotNull(),
            () -> assertThat(actual.body().data().message()).isEqualTo(testedMessage)
        );
    }

    @Test
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void echo_AppTestEchoIdListener_responseToSendBothUserQueueReplyId()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedId = IdsUserFakeBuilder.defaultId1;
        String testedMessage = "Hello World";
        TestRequest testedRequest = new TestRequest(testedMessage);

        String testedDestination = "/app/test/broadcast/" + testedId;
        String broadcastSubscribeUrl = "/queue/test/broadcast";
        String userSubscribeUrl = "/user/queue/reply/" + testedId;

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();

        // Act
        StompFrameHandler broadcastHandler = buildStompFrameHandler(
            broadcastResponseFuture,
            ResponseEntity_ResponseData_TestResponse.class
        );
        StompFrameHandler userHandler = buildStompFrameHandler(
            userResponseFuture,
            ResponseEntity_ResponseData_TestResponse.class
        );

        stompSession.subscribe(broadcastSubscribeUrl, broadcastHandler);
        stompSession.subscribe(userSubscribeUrl, userHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_TestResponse broadcastActual = broadcastResponseFuture.get(3, TimeUnit.SECONDS);
        ResponseEntity_ResponseData_TestResponse userActual = userResponseFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(broadcastActual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(broadcastActual.body().data()).isNotNull(),
            () -> assertThat(broadcastActual.body().data().message()).isEqualTo(testedMessage),
            () -> assertThat(userActual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(userActual.body().data()).isNotNull(),
            () -> assertThat(userActual.body().data().message()).isEqualTo(testedMessage)
        );
    }

    @Test
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void echoDifferentData_sendsDifferentMessages()
    {
        // Arrange
        StompSession stompSession = getStompSession();

        UUID testedId = IdsUserFakeBuilder.defaultId1;
        String testedMessage = "Hello World";
        TestRequest testedRequest = new TestRequest(testedMessage);

        String testedDestination = "/app/test/different/" + testedId;
        String broadcastSubscribeUrl = "/queue/reply-" + testedId;
        String userSubscribeUrl = "/user/queue/reply";

        String expectedHttpStatus = HttpStatus.OK.getReasonPhrase();
        String expectedBroadcastMessage = "Broadcast: " + testedMessage;
        String expectedUserMessage = "User: " + testedMessage;

        // Act
        StompFrameHandler broadcastHandler = buildStompFrameHandler(
            broadcastDifferentFuture,
            ResponseEntity_ResponseData_TestResponse.class
        );
        StompFrameHandler userHandler = buildStompFrameHandler(
            userDifferentFuture,
            ResponseEntity_ResponseData_TestResponse.class
        );

        stompSession.subscribe(broadcastSubscribeUrl, broadcastHandler);
        stompSession.subscribe(userSubscribeUrl, userHandler);
        stompSession.send(testedDestination, testedRequest);

        ResponseEntity_ResponseData_TestResponse broadcastActual = broadcastDifferentFuture.get(3, TimeUnit.SECONDS);
        ResponseEntity_ResponseData_TestResponse userActual = userDifferentFuture.get(3, TimeUnit.SECONDS);

        // Assert
        assertAll(
            () -> assertThat(broadcastActual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(broadcastActual.body().data()).isNotNull(),
            () -> assertThat(broadcastActual.body().data().message()).isEqualTo(expectedBroadcastMessage),
            () -> assertThat(userActual.statusCode).isEqualTo(expectedHttpStatus),
            () -> assertThat(userActual.body().data()).isNotNull(),
            () -> assertThat(userActual.body().data().message()).isEqualTo(expectedUserMessage)
        );
    }

    private record ResponseEntity_ResponseData_TestResponse(
        ResponseData_TestResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_TestResponse(
        TestResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
