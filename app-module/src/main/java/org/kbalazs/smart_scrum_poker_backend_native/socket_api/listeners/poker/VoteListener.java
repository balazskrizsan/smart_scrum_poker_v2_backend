package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class VoteListener
{
    VoteService voteService;
    SecurityContextFactory securityContextFactory;

    @AsyncListener(operation = @AsyncOperation(
        channelName = "/app/poker/vote",
        description = "Submit a vote for a poker ticket"
    ))
    @MessageMapping("/poker/vote/{pokerPublicId}/{ticketId}") // @todo: check if params required
    @SendTo("/queue/reply-{pokerPublicId}")
    public ResponseEntity_ResponseData_VoteResponse voteListener(@Payload VoteRequest voteRequest)
        throws ApiException, StoryPointException, AccountException
    {
        var idsUserId = securityContextFactory.getCurrentUserId();

        var voteWithCalculatedPoint = voteService.vote(RequestMapperService.mapToEntity(voteRequest, idsUserId));

        ResponseEntity<ResponseData<VoteResponse>> response = new ResponseEntityBuilder<VoteResponse>()
            .socketDestination(SEND_POKER_VOTE)
            .data(new VoteResponse(voteWithCalculatedPoint.userProfile(), voteWithCalculatedPoint.calculatedPoint()))
            .build();

        ResponseData<VoteResponse> responseData = response.getBody();

        return new ResponseEntity_ResponseData_VoteResponse(
            new ResponseData_VoteResponse(
                Objects.requireNonNull(responseData).data(),
                responseData.success(),
                responseData.errorCode(),
                responseData.requestId(),
                responseData.socketResponseDestination()
            ),
            response.getHeaders(),
            response.getStatusCode(),
            response.getStatusCode().value()
        );
    }

    @Schema(description = "Combined ResponseEntity and ResponseData wrapper for poker vote")
    public record ResponseEntity_ResponseData_VoteResponse(
        @Schema(description = "Response data body")
        ResponseData_VoteResponse body,
        @Schema(description = "Response headers")
        HttpHeaders headers,
        @Schema(description = "HTTP status code")
        HttpStatusCode statusCode,
        @Schema(description = "HTTP status code value")
        int statusCodeValue
    )
    {
    }

    @Schema(description = "Response data wrapper")
    public record ResponseData_VoteResponse(
        @Schema(description = "Poker vote response data")
        VoteResponse data,
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
