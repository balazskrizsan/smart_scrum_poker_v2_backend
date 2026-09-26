package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.EmptyPayload;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketClosed;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.POKER_TICKET_CLOSE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class TicketCloseListener
{
    NotificationService notificationService;

    @AsyncListener(operation = @AsyncOperation(
        channelName = "/poker/ticket.close/{pokerPublicId}/{ticketId}",
        description = "Close a ticket in a poker game"
    ))
    @MessageMapping("/poker/ticket.close/{pokerPublicId}/{ticketId}")
    public void ticketCloseListener(
        @SuppressWarnings("unused") @Payload(required = false) EmptyPayload payload,
        @DestinationVariable("pokerPublicId") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException
    {
        notificationService.notifyPokerGame(pokerIdSecure, new TicketClosed(ticketId), POKER_TICKET_CLOSE);
    }
}
