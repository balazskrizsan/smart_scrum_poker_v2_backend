package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VoteListener {
    private final VoteService voteService;
    private final NotificationService notificationService;

    @MessageMapping("/poker/vote/{pokerPublicId}/{ticketId}")
    public void voteListener(
        @Payload VoteRequest voteRequest,
        @NonNull @DestinationVariable("pokerPublicId") UUID pokerIdSecure,
        @NonNull @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException, StoryPointException, AccountException
    {
        log.info("VoteListener:/poker/vote/{}/{}: {}", pokerIdSecure, ticketId, voteRequest);

        IdsUser idsUser = voteService.vote(RequestMapperService.mapToEntity(voteRequest));

        notificationService.notifyPokerGame(pokerIdSecure, new VoteResponse(idsUser), SEND_POKER_VOTE);
    }
}
