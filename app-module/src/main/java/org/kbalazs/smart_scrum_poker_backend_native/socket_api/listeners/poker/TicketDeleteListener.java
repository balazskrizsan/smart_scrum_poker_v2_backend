package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketDeleteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.TicketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_TICKET_DELETE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDeleteListener
{
    NotificationService notificationService;
    TicketService ticketService;

    @MessageMapping("/poker/ticket.delete/{pokerPublicId}/{ticketId}")
    public void ticketCloseListener(
        @DestinationVariable("pokerPublicId") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException
    {
        ticketService.delete(ticketId);

        notificationService.notifyPokerGame(pokerIdSecure, new TicketDeleteResponse(ticketId), POKER_TICKET_DELETE);
    }
}
