package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.tests;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.tests.TestRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.tests.TestResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class TestListener
{
    NotificationService notificationService;
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/test/echo")
    @SendTo("/queue/test/echo")
    public TestResponse echo(@Payload TestRequest request)
    {
        return new TestResponse(request.message());
    }

    @MessageMapping("/test/echo/{id}")
    @SendToUser(value = "/queue/reply/{id}")
    public ResponseEntity<ResponseData<TestResponse>> echoWithNotification(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        return new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse(request.message()))
            .build();
    }

    @MessageMapping("/test/broadcast/{id}")
    @SendTo("/queue/test/broadcast")
    @SendToUser(value = "/queue/reply/{id}")
    public ResponseEntity<ResponseData<TestResponse>> echoBroadcastAndUser(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        return new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse(request.message()))
            .build();
    }

    @MessageMapping("/test/different/{id}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity<ResponseData<TestResponse>> echoDifferentData(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        simpMessagingTemplate.convertAndSend(
            "/queue/reply-" + id,
            new ResponseEntityBuilder<TestResponse>()
                .socketDestination(SocketDestination.TEST_ECHO)
                .data(new TestResponse("Broadcast: " + request.message()))
                .build()
        );

        return new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse("User: " + request.message()))
            .build();
    }

    @MessageMapping("/test/different-notification/{id}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity<ResponseData<TestResponse>> echoDifferentDataWithNotification(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        notificationService.notifyPokerGame(
            id,
            new TestResponse("Broadcast (Notification): " + request.message()),
            SocketDestination.TEST_ECHO
        );

        return new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse("User: " + request.message()))
            .build();
    }
}
