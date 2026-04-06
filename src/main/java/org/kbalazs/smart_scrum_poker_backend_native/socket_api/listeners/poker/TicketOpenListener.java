package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketOpened;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__TICKET_OPEN;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TicketOpenListener
{
    private final NotificationService notificationService;

    // @todo: test
    @MessageMapping("/poker/ticket.open/{pokerPublicId}/{ticketId}")
    public void ticketCloseListener(
        @DestinationVariable("pokerPublicId") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
    throws ApiException
    {
        log.info("TicketOpenListener:/poker/ticket.close/{}/{}", pokerIdSecure, ticketId);

        notificationService.notifyPokerGame(pokerIdSecure, new TicketOpened(ticketId), SEND__POKER__TICKET_OPEN);
    }
}
