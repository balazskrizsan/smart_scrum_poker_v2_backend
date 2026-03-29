package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.GameStateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InGamePlayer;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.GameStateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameStateService
{
    PokerService pokerService;
    IdsUserService idsUserService;
    InGamePlayersService inGamePlayersService;
    TicketService ticketService;
    VoteService voteService;

    public GameStateResponse get(@NonNull GameStateRequest gameStateRequest) throws PokerException, AccountException
    {
        UUID pokerIdSecure = gameStateRequest.pokerIdSecure();
        UUID insecureUserId = gameStateRequest.insecureUserId();

        IdsUser currentIdsUser = idsUserService.findByIdSecure(insecureUserId);
        Poker poker = pokerService.findByIdSecure(pokerIdSecure);

        List<Ticket> tickets = ticketService.searchByPokerId(poker.id());

        inGamePlayersService.onDuplicateKeyIgnoreAdd(
            new InGamePlayer(insecureUserId, pokerIdSecure, gameStateRequest.now())
        );

        List<InGamePlayer> inGamePlayers = inGamePlayersService.searchUserSecureIdsByPokerIdSecure(pokerIdSecure);
        List<UUID> inGameUsersIdSecures = inGamePlayers.stream().map(InGamePlayer::insecureUserIdSecure).toList();

        List<IdsUser> idsUsers = idsUserService.findByIdSecureList(inGameUsersIdSecures);

        List<Long> ticketIdList = tickets.stream().map(Ticket::id).toList();

        Map<Long, Map<UUID, Vote>> votes = voteService.getVotesWithTicketGroupByTicketIds(ticketIdList);
        Map<Long, VotesWithVoteStat> votesWithVoteStatList = voteService.getStatByTicketIds(ticketIdList);

        IdsUser owner = idsUserService.findByIdSecure(poker.createdBy());

        List<IdsUser> usersWithSession = idsUserService.searchUsersWithActiveSession(inGameUsersIdSecures);

        return new GameStateResponse(
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
