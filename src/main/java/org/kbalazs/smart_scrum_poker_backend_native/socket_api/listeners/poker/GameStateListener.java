package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.GameStateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteNewJoinerResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.GameStateService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.GameStateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameStateListener
{
    private final SimpMessagingTemplate template;
    private final GameStateService gameStateService;

    @MessageMapping("/poker/game.state/{pokerIdSecure}/{insecureUserId}")
    @SendToUser(value = "/queue/reply")
    public ResponseEntity<ResponseData<GameStateResponse>> gameStateListener(
        @DestinationVariable("pokerIdSecure") UUID pokerIdSecure,
        @DestinationVariable("insecureUserId") UUID insecureUserId
    ) throws ApiException, PokerException, AccountException
    {
        log.info("Listener:/poker/game.state/{}/{}", pokerIdSecure, insecureUserId);
        GameStateResponse gameStateResponse = gameStateService.get(
            new GameStateRequest(pokerIdSecure, insecureUserId, RequestMapperService.getNow())
        );

        template.convertAndSend(
            "/queue/reply-" + pokerIdSecure,
            new ResponseEntityBuilder<VoteNewJoinerResponse>()
                .socketDestination(SocketDestination.SEND_POKER_VOTE_NEW_JOINER)
                .data(new VoteNewJoinerResponse(gameStateResponse.currentInsecureUser()))
                .build()
        );

        return new ResponseEntityBuilder<GameStateResponse>()
            .socketDestination(SocketDestination.POKER_GAME_STATE)
            .data(gameStateResponse)
            .build();
    }
}
