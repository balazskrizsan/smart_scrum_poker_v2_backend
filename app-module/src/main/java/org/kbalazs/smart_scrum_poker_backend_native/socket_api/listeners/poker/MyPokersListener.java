package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.MyPokersRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.SocketResponseFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.MyPokersResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.PokerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MyPokersListener
{
    SimpMessagingTemplate template;
    PokerService pokerService;

    @AsyncListener(operation = @AsyncOperation(
        channelName = "/poker/my.pokers",
        description = "Get all poker games for the current user"
    ))
    @MessageMapping("/poker/my.pokers")
    @SendToUser("/queue/reply")
    public ResponseEntity_ResponseData_MyPokersResponse gameStateListener(@Payload MyPokersRequest myPokersRequest)
        throws ApiException
    {
        return SocketResponseFactory.fromResponseEntity(
            new ResponseEntityBuilder<MyPokersResponse>()
                .socketDestination(SocketDestination.SEND__POKER__MY_POKERS)
                .data(new MyPokersResponse(
                    pokerService.searchByIdsUserId(myPokersRequest.idsUserId())
                ))
                .build(),
            ResponseData_MyPokersResponse::new,
            ResponseEntity_ResponseData_MyPokersResponse::new
        );
    }

    @Schema(description = "Combined ResponseEntity and ResponseData wrapper for my pokers")
    public record ResponseEntity_ResponseData_MyPokersResponse(
        @Schema(description = "Response data containing my pokers result")
        ResponseData_MyPokersResponse body,
        @Schema(description = "Response headers")
        Map<String, String> headers,
        @Schema(description = "HTTP status code phrase")
        String statusCode,
        @Schema(description = "HTTP status code value")
        int statusCodeValue
    )
    {
    }

    @Schema(description = "Response data wrapper for my pokers")
    public record ResponseData_MyPokersResponse(
        @Schema(description = "My pokers response data")
        MyPokersResponse data,
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
