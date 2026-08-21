package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    @MessageMapping("/poker/vote/{pokerPublicId}/{ticketId}") // @todo: check if params required
    public void voteListener(@Payload VoteRequest voteRequest)
        throws ApiException, StoryPointException, AccountException
    {
        var voteWithCalculatedPoint = voteService.vote(RequestMapperService.mapToEntity(voteRequest));

        notificationService.notifyPokerGame(
            voteRequest.pokerIdSecure(),
            new VoteResponse(voteWithCalculatedPoint.userProfile(), voteWithCalculatedPoint.calculatedPoint()),
            SEND_POKER_VOTE
        );
    }
}
