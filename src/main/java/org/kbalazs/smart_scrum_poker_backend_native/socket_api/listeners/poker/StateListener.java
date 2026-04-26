package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteNewJoinerResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.StateService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class StateListener
{
    SimpMessagingTemplate template;
    StateService stateService;

    @MessageMapping("/poker/state/{pokerPublicId}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity<ResponseData<StateResponse>> gameStateListener(
        @DestinationVariable("pokerPublicId") UUID pokerPublicId
    )
        throws ApiException, PokerException, AccountException
    {
        StateResponse stateResponse = stateService.get(
            new StateRequest(pokerPublicId, RequestMapperService.getNow())
        );

        template.convertAndSend(
            "/queue/reply-" + pokerPublicId,
            new ResponseEntityBuilder<VoteNewJoinerResponse>()
                .socketDestination(SocketDestination.SEND_POKER_VOTE_NEW_JOINER)
                .data(new VoteNewJoinerResponse(stateResponse.currentUserProfile()))
                .build()
        );

        return new ResponseEntityBuilder<StateResponse>()
            .socketDestination(SocketDestination.POKER_STATE)
            .data(stateResponse)
            .build();
    }
}
