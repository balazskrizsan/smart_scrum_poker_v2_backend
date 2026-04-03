package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.StartService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPoker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPokerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StartListener
{
    StartService startService;
    SecurityContextFactory securityContextFactory;

    @MessageMapping("/poker/start")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasAuthority('poker.start')")
    public ResponseEntity<ResponseData<StartResponse>> startListener(@Payload StartRequest request)
        throws PokerException, ApiException, AccountException
    {
        log.info("StartListener:/poker/start: {}", request);

        UUID idsUserId = securityContextFactory.getCurrentUserId();

        StartPoker startPoker = RequestMapperService.mapToEntity(request, idsUserId);

        StartPokerResponse startPokerResponse = startService.start(startPoker.poker(), startPoker.tickets());

        return new ResponseEntityBuilder<StartResponse>()
            .socketDestination(SocketDestination.POKER_START)
            .data(new StartResponse(startPokerResponse.poker()))
            .build();
    }
}
