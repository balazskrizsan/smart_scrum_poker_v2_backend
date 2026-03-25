package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoterLeavingRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoterLeavingResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__VOTER_LEAVING;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoterLeavingListener
{
    NotificationService notificationService;

    @MessageMapping("/poker/voter_leaving")
    public void voteListener(@Payload VoterLeavingRequest voteRequest)
        throws ApiException
    {
        log.info("VoteListener:/poker/voter_leaving: {}", voteRequest);

        notificationService.notifyPokerGame(
            voteRequest.pokerIdSecure(),
            new VoterLeavingResponse(voteRequest.userIdSecure(), voteRequest.pokerIdSecure()),
            SEND__POKER__VOTER_LEAVING
        );
    }
}
