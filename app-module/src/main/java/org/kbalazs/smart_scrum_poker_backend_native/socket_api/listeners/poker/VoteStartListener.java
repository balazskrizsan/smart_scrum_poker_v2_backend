package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.RoundStartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteStartStopService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_ROUND_START;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteStartListener
{
    VoteStartStopService voteStartStopService;
    NotificationService notificationService;

    @MessageMapping("/poker/vote.start/{pokerPublicId}/{ticketId}")
    public void voteStartListener(
        @DestinationVariable("pokerPublicId") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException, PokerException
    {
        voteStartStopService.start(pokerIdSecure, ticketId);

        notificationService.notifyPokerGame(pokerIdSecure, new RoundStartResponse(ticketId), POKER_ROUND_START);
    }
}
