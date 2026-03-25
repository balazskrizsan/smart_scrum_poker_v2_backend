package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketClosed;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_TICKET_CLOSE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TicketCloseListener
{
    private final NotificationService notificationService;

    @MessageMapping("/poker/ticket.close/{pokerIdSecure}/{ticketId}")
    public void ticketCloseListener(
        @DestinationVariable("pokerIdSecure") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
    throws ApiException
    {
        log.info("TicketCloseListener:/poker/ticket.close/{}/{}", pokerIdSecure, ticketId);

        notificationService.notifyPokerGame(pokerIdSecure, new TicketClosed(ticketId), POKER_TICKET_CLOSE);
    }
}
