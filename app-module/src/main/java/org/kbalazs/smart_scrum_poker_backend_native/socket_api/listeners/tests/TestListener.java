package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.tests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.tests.TestRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.SocketResponseFactory;
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

import java.util.Map;
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
    public ResponseEntity_ResponseData_TestResponse echoWithNotification(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        ResponseEntity<ResponseData<TestResponse>> responseEntity = new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse(request.message()))
            .build();

        return SocketResponseFactory.fromResponseEntity(
            responseEntity,
            ResponseData_TestResponse::new,
            ResponseEntity_ResponseData_TestResponse::new
        );
    }

    @MessageMapping("/test/broadcast/{id}")
    @SendTo("/queue/test/broadcast")
    @SendToUser(value = "/queue/reply/{id}")
    public ResponseEntity_ResponseData_TestResponse echoBroadcastAndUser(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        ResponseEntity<ResponseData<TestResponse>> responseEntity = new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse(request.message()))
            .build();

        return SocketResponseFactory.fromResponseEntity(
            responseEntity,
            ResponseData_TestResponse::new,
            ResponseEntity_ResponseData_TestResponse::new
        );
    }

    @MessageMapping("/test/different/{id}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity_ResponseData_TestResponse echoDifferentData(
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

        ResponseEntity<ResponseData<TestResponse>> responseEntity = new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse("User: " + request.message()))
            .build();

        return SocketResponseFactory.fromResponseEntity(
            responseEntity,
            ResponseData_TestResponse::new,
            ResponseEntity_ResponseData_TestResponse::new
        );
    }

    @MessageMapping("/test/different-notification/{id}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity_ResponseData_TestResponse echoDifferentDataWithNotification(
        @Payload TestRequest request,
        @DestinationVariable("id") UUID id
    ) throws ApiException
    {
        notificationService.notifyPokerGame(
            id,
            new TestResponse("Broadcast (Notification): " + request.message()),
            SocketDestination.TEST_ECHO
        );

        ResponseEntity<ResponseData<TestResponse>> responseEntity = new ResponseEntityBuilder<TestResponse>()
            .socketDestination(SocketDestination.TEST_ECHO)
            .data(new TestResponse("User: " + request.message()))
            .build();

        return SocketResponseFactory.fromResponseEntity(
            responseEntity,
            ResponseData_TestResponse::new,
            ResponseEntity_ResponseData_TestResponse::new
        );
    }

    @Schema(description = "Combined ResponseEntity and ResponseData wrapper for test response")
    public record ResponseEntity_ResponseData_TestResponse(
        @Schema(description = "Response data containing test result")
        ResponseData_TestResponse body,
        @Schema(description = "Response headers")
        Map<String, String> headers,
        @Schema(description = "HTTP status code phrase")
        String statusCode,
        @Schema(description = "HTTP status code value")
        int statusCodeValue
    )
    {
    }

    @Schema(description = "Response data wrapper for test response")
    public record ResponseData_TestResponse(
        @Schema(description = "Test response data")
        TestResponse data,
        @Schema(description = "Indicates if the operation was successful")
        Boolean success,
        @Schema(description = "Error code, 0 if successful")
        int errorCode,
        @Schema(description = "Request identifier")
        String requestId,
        @Schema(description = "Socket response destination")
        String socketResponseDestination
    )
    {
    }
}
