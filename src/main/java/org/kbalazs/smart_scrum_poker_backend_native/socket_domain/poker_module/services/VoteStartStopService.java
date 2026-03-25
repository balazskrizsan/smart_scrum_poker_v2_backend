package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteStartStopService
{
    TicketService ticketService;
    VoteService voteService;
    PokerService pokerService;

    public void start(@NonNull UUID pokerIdSecure, long ticketId) throws PokerException
    {
        pokerService.findByIdSecure(pokerIdSecure);
        voteService.deleteVotesByTicketId(ticketId);
        ticketService.activate(ticketId);
    }

    public VotesWithVoteStat stop(@NonNull UUID pokerIdSecure, long ticketId) throws PokerException
    {
        pokerService.findByIdSecure(pokerIdSecure);
        ticketService.deactivate(ticketId);

        return voteService.getStatByTicketId(ticketId);
    }
}
