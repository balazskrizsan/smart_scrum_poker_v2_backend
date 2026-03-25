package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.AddTicketRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.AddTicketResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.NotificationService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.TicketService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.AddTicket;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND__POKER__NEW_TICKET_CREATE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddTicketListener
{
    private final TicketService ticketService;
    private final NotificationService notificationService;

    @MessageMapping("/poker/new.ticket.create")
    public void addTicketListener(@Payload AddTicketRequest request) throws PokerException, ApiException
    {
        log.info("AddTicketListener:/poker/start: {}", request);

        AddTicket mappedRequest = RequestMapperService.mapToEntity(request);

        Ticket ticket = ticketService.addOne(mappedRequest);

        notificationService.notifyPokerGame(
            mappedRequest.pokerIdSecure(),
            new AddTicketResponse(ticket),
            SEND__POKER__NEW_TICKET_CREATE
        );
    }
}
