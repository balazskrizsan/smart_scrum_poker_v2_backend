package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.MyPokersRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.MyPokersResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.PokerService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import static lombok.AccessLevel.PRIVATE;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MyPokersListener
{
    SimpMessagingTemplate template;
    PokerService pokerService;

    @MessageMapping("/poker/my.pokers")
    @SendToUser("/queue/reply")
    public ResponseEntity<ResponseData<MyPokersResponse>> gameStateListener(@Payload MyPokersRequest myPokersRequest)
        throws ApiException
    {
        return new ResponseEntityBuilder<MyPokersResponse>()
            .socketDestination(SocketDestination.SEND__POKER__MY_POKERS)
            .data(new MyPokersResponse(
                pokerService.searchByIdsUserId(myPokersRequest.idsUserId())
            ))
            .build();
    }
}
