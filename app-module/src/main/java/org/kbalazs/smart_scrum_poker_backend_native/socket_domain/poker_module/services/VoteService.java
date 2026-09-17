package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.PokerRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.StoryPointConfigRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.TicketRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.VoteRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteService
{
    IdsUserService idsUserService;
    StoryPointCalculatorService storyPointCalculatorService;
    VoteRepository voteRepository;
    StoryPointConfigRepository storyPointConfigRepository;
    PokerRepository pokerRepository;
    TicketRepository ticketRepository;
    ObjectMapper objectMapper;

    public VoteWithCalculatedPoint vote(@NonNull Vote vote)
        throws StoryPointException, AccountException
    {
        UserProfile idsUser = idsUserService.findProfileByIdsUserId(vote.createdBy());

        Ticket ticket = ticketRepository.findById(vote.ticketId())
            .orElseThrow(() -> new StoryPointException("Ticket not found: " + vote.ticketId()));

        Poker poker = pokerRepository.findById(ticket.pokerId())
            .orElseThrow(() -> new StoryPointException("Poker not found for ticket: " + vote.ticketId()));

        StoryPointConfig config = poker.storyPointConfigId() != null
            ? storyPointConfigRepository.findById(poker.storyPointConfigId())
                .orElseThrow(() -> new StoryPointException("Story point config not found: " + poker.storyPointConfigId()))
            : storyPointConfigRepository.findDefaultConfig()
                .orElseThrow(() -> new StoryPointException("Default story point config not found"));

        try
        {
            // Parse vote values JSON
            Map<String, String> dimensionValues = objectMapper.readValue(
                vote.voteValues(),
                new TypeReference<>()
                {
                }
            );

            short calculatedPoint = storyPointCalculatorService.calculate(
                new VoteValues(false, false, dimensionValues),
                config
            );

            Vote calculatedVote = vote
                .withStoryPointConfigId(config.id())
                .withCalculatedPoint(calculatedPoint);

            voteRepository.create(calculatedVote);

            return new VoteWithCalculatedPoint(idsUser, calculatedPoint);
        }
        catch (Exception e)
        {
            throw new StoryPointException("Error processing vote: " + e.getMessage(), e);
        }
    }

    // @todo: rename to search
    public Map<Long, Map<UUID, Vote>> getVotesWithTicketGroupByTicketIds(@NonNull List<Long> ticketIds)
    {
        return voteRepository.getVotesWithTicketGroupByTicketIds(ticketIds);
    }

    public @NonNull Map<UUID, Vote> searchVotesWithTicketGroupByTicketId(long ticketId)
    {
        Map<Long, Map<UUID, Vote>> result = voteRepository.getVotesWithTicketGroupByTicketIds(List.of(ticketId));

        return result.isEmpty() ? Map.of() : result.get(ticketId);
    }

    public @NonNull VotesWithVoteStat getStatByTicketId(long ticketId)
    {
        Map<UUID, Vote> votes = searchVotesWithTicketGroupByTicketId(ticketId);

        return calculateStat(votes);
    }

    public void deleteVotesByTicketId(@NonNull Long ticketId)
    {
        // @todo: archive before delete
        voteRepository.deleteByTicketId(ticketId);
    }

    public Map<Long, VotesWithVoteStat> getStatByTicketIds(@NonNull List<Long> tickedIds)
    {
        Map<Long, Map<UUID, Vote>> votes = voteRepository.getVotesWithTicketGroupByTicketIds(tickedIds);

        Map<Long, VotesWithVoteStat> votesWithVoteStats = new HashMap<>();

        votes.forEach((key, voteMap) ->
            {
            if (voteMap != null) // @todo: test
            {
                votesWithVoteStats.put(key, calculateStat(voteMap));
            }
            });

        return votesWithVoteStats;
    }

    private @NonNull VotesWithVoteStat calculateStat(@NonNull Map<UUID, Vote> votes)
    {
        if (votes.isEmpty())
        {
            return new VotesWithVoteStat(votes, new VoteStat(null, null, null));
        }

        Supplier<Stream<Vote>> valueStreamSupplier = () -> votes.values().stream();
        Supplier<Stream<Short>> calculatedPointStreamSupplier = () ->
            valueStreamSupplier.get().map(Vote::calculatedPoint);

        double avg = valueStreamSupplier.get().mapToDouble(Vote::calculatedPoint).average().orElseThrow();
        short min = calculatedPointStreamSupplier.get().min(Short::compare).orElseThrow();
        short max = calculatedPointStreamSupplier.get().max(Short::compare).orElseThrow();

        return new VotesWithVoteStat(votes, new VoteStat(avg, min, max));
    }

    public Vote findById(@NonNull Long id)
    {
        return voteRepository.findById(id);
    }

    public record VoteWithCalculatedPoint(UserProfile userProfile, short calculatedPoint)
    {
    }
}
