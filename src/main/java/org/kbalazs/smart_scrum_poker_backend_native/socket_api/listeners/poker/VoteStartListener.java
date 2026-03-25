package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
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
public class VoteStartListener
{
    private final VoteStartStopService voteStartStopService;
    private final NotificationService notificationService;

    @MessageMapping("/poker/vote.start/{pokerIdSecure}/{ticketId}")
    public void voteStartListener(
        @DestinationVariable("pokerIdSecure") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException, PokerException
    {
        log.info("VoteStartListener:/poker/vote.start/{}/{}", pokerIdSecure, ticketId);

        voteStartStopService.start(pokerIdSecure, ticketId);

        notificationService.notifyPokerGame(pokerIdSecure, new RoundStartResponse(ticketId), POKER_ROUND_START);
    }
}
