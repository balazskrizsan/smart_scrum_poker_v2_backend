package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
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

    public IdsUser vote(@NonNull Vote vote) throws StoryPointException, AccountException
    {
        IdsUser idsUser = idsUserService.findByIdSecure(vote.createdBy());

        Vote calculatedVote = new Vote(
            vote.id(),
            vote.ticketId(),
            vote.uncertainty(),
            vote.complexity(),
            vote.effort(),
            vote.risk(),
            storyPointCalculatorService.calculate(
                new VoteValues(
                    false,
                    false,
                    SizeEnum.of(vote.uncertainty()),
                    SizeEnum.of(vote.complexity()),
                    SizeEnum.of(vote.effort()),
                    SizeEnum.of(vote.risk())
                )
            ),
            vote.createdAt(),
            vote.createdBy()
        );

        voteRepository.create(calculatedVote);

        return idsUser;
    }

    // @todo: rename to search
    public Map<Long, Map<UUID, Vote>> getVotesWithTicketGroupByTicketIds(@NonNull List<Long> ticketIds)
    {
        return voteRepository.getVotesWithTicketGroupByTicketIds(ticketIds);
    }

    // @todo: test
    public Map<UUID, Vote> searchVotesWithTicketGroupByTicketId(long ticketId)
    {
        Map<Long, Map<UUID, Vote>> result = voteRepository.getVotesWithTicketGroupByTicketIds(List.of(ticketId));

        return null == result ? Map.of() : result.get(ticketId);
    }

    public VotesWithVoteStat getStatByTicketId(long ticketId)
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

        votes.forEach((key, voteMap) -> {
            if (voteMap != null) // @todo: test
            {
                votesWithVoteStats.put(key, calculateStat(voteMap));
            }
        });

        return votesWithVoteStats;
    }

    private @NonNull VotesWithVoteStat calculateStat(@NonNull Map<UUID, Vote> votes)
    {
        Supplier<Stream<Vote>> valueStreamSupplier = () -> votes.values().stream();
        Supplier<Stream<Short>> calculatedPointStreamSupplier = () ->
            valueStreamSupplier.get().map(Vote::calculatedPoint);

        double avg = valueStreamSupplier.get().mapToDouble(Vote::calculatedPoint).average().orElseThrow();
        short min = calculatedPointStreamSupplier.get().min(Short::compare).orElseThrow();
        short max = calculatedPointStreamSupplier.get().max(Short::compare).orElseThrow();

        return new VotesWithVoteStat(votes, new VoteStat(avg, min, max));
    }
}
