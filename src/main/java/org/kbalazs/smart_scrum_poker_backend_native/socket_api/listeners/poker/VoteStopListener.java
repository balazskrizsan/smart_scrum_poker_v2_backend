package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteStopResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteStartStopService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VoteStopListener
{
    private final SimpMessagingTemplate template;
    private final VoteStartStopService voteStartStopService;

    @MessageMapping("/poker/vote.stop/{pokerIdSecure}/{ticketId}")
    public void voteStopListener(
        @DestinationVariable("pokerIdSecure") UUID pokerIdSecure,
        @DestinationVariable("ticketId") Long ticketId
    )
        throws ApiException, PokerException
    {
        log.info("VoteStopListener:/poker/vote.stop/{}/{}", pokerIdSecure, ticketId);

        VotesWithVoteStat votesWithVoteStat = voteStartStopService.stop(pokerIdSecure, ticketId);

        template.convertAndSend(
            "/queue/reply-" + pokerIdSecure,
            new ResponseEntityBuilder<VoteStopResponse>()
                .socketDestination(SocketDestination.POKER_ROUND_STOP)
                .data(new VoteStopResponse(pokerIdSecure, ticketId, votesWithVoteStat))
                .build()
        );
    }
}
