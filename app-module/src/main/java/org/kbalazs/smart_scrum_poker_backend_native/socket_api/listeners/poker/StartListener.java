package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.SocketResponseFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.SocketResponseWrapper;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.StartService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPoker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPokerResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StartListener
{
    StartService startService;
    SecurityContextFactory securityContextFactory;

    @AsyncListener(operation = @AsyncOperation(
        channelName = "/app/poker/start",
        description = "Start a new poker game"
    ))
    @MessageMapping("/poker/start")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasAuthority('poker.start')")
    public ResponseEntity_ResponseData_StartResponse startListener(@Payload StartRequest request)
        throws ApiException
    {
        UUID idsUserId = securityContextFactory.getCurrentUserId();

        StartPoker startPoker = RequestMapperService.mapToEntity(request, idsUserId);

        StartPokerResponse startPokerResponse = startService.start(startPoker.poker(), startPoker.tickets());

        ResponseData<StartResponse> responseData = new ResponseEntityBuilder<StartResponse>()
            .socketDestination(SocketDestination.POKER_START)
            .data(new StartResponse(startPokerResponse.poker()))
            .build()
            .getBody();

        return SocketResponseFactory.fromResponseData(responseData, ResponseEntity_ResponseData_StartResponse::new);
    }

    @Schema(description = "Combined ResponseEntity and ResponseData wrapper for poker game start")
    public record ResponseEntity_ResponseData_StartResponse(
        @Schema(description = "Poker game start response data")
        StartResponse data,
        @Schema(description = "Indicates if the operation was successful")
        Boolean success,
        @Schema(description = "Error code, 0 if successful")
        int errorCode,
        @Schema(description = "Request identifier")
        String requestId,
        @Schema(description = "Socket response destination")
        String socketResponseDestination
    ) implements SocketResponseWrapper<StartResponse>
    {
    }
}
