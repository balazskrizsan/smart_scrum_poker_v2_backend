package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InPokerIdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StateService
{
    PokerService pokerService;
    IdsUserService idsUserService;
    InPokerIdsUsersService inPokerIdsUsersService;
    TicketService ticketService;
    VoteService voteService;
    SecurityContextFactory securityContextFactory;

    public StateResponse get(@NonNull StateRequest stateRequest) throws PokerException, AccountException
    {
        UUID pokerPublicId = stateRequest.pokerPublicId();
        UUID currentIdsUserId = securityContextFactory.getCurrentUserId();

        IdsUser currentIdsUser = idsUserService.getById(currentIdsUserId);
        Poker poker = pokerService.findByPublicId(pokerPublicId);

        List<Ticket> tickets = ticketService.searchByPokerId(poker.id());

        inPokerIdsUsersService.onDuplicateKeyIgnoreAdd(new InPokerIdsUser(currentIdsUserId, poker.id()));

        List<InPokerIdsUser> inPokerIdsUsers = inPokerIdsUsersService.searchUserSecureIdsByPokerIdSecure(pokerPublicId);
        List<UUID> inGameUsersIdSecures = inPokerIdsUsers.stream().map(InPokerIdsUser::idsUserId).toList();

        List<IdsUser> idsUsers = idsUserService.findByIdSecureList(inGameUsersIdSecures);

        List<Long> ticketIdList = tickets.stream().map(Ticket::id).toList();

        Map<Long, Map<UUID, Vote>> votes = voteService.getVotesWithTicketGroupByTicketIds(ticketIdList);
        Map<Long, VotesWithVoteStat> votesWithVoteStatList = voteService.getStatByTicketIds(ticketIdList);

        IdsUser owner = idsUserService.getById(poker.createdBy());

        List<IdsUser> usersWithSession = idsUserService.searchUsersWithActiveSession(inGameUsersIdSecures);

        return new StateResponse(
            poker,
            tickets,
            idsUsers,
            votes,
            owner,
            currentIdsUser,
            usersWithSession,
            votesWithVoteStatList // @todo: select only finished votes
        );
    }
}
