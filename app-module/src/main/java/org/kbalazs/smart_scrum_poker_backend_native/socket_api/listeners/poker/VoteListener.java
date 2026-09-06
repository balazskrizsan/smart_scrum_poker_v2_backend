package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static lombok.AccessLevel.PRIVATE;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class VoteListener
{
    VoteService voteService;
    NotificationService notificationService;
    SecurityContextFactory securityContextFactory;

    @MessageMapping("/poker/vote/{pokerPublicId}/{ticketId}") // @todo: check if params required
    @SendTo("/queue/reply-{pokerPublicId}")
    public ResponseEntity<ResponseData<VoteResponse>> voteListener(@Payload VoteRequest voteRequest)
        throws ApiException, StoryPointException, AccountException
    {
        var idsUserId = securityContextFactory.getCurrentUserId();

        var voteWithCalculatedPoint = voteService.vote(RequestMapperService.mapToEntity(voteRequest, idsUserId));

        return new ResponseEntityBuilder<VoteResponse>()
            .socketDestination(SEND_POKER_VOTE)
            .data(new VoteResponse(voteWithCalculatedPoint.userProfile(), voteWithCalculatedPoint.calculatedPoint()))
            .build();
    }
}
