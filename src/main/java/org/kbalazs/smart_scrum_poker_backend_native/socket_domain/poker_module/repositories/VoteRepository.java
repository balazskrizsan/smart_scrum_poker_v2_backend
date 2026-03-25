package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.VoteRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class VoteRepository extends AbstractRepository
{
    public void create(@NonNull Vote vote)
    {
        DSLContext ctx = getDSLContext();

        ctx.insertInto(voteTable)
            .set(ctx.newRecord(voteTable, vote))
            .onDuplicateKeyUpdate()
            .set(voteTable.UNCERTAINTY, vote.uncertainty())
            .set(voteTable.COMPLEXITY, vote.complexity())
            .set(voteTable.EFFORT, vote.effort())
            .set(voteTable.RISK, vote.risk())
            .set(voteTable.CALCULATED_POINT, vote.calculatedPoint())
            .execute();
    }

    public Map<Long, Map<UUID, Vote>> getVotesWithTicketGroupByTicketIds(@NonNull List<Long> ticketIds)
    {
        return getDSLContext()
            .selectFrom(voteTable)
            .where(voteTable.TICKET_ID.in(ticketIds))
            .collect(
                Collectors.groupingBy(
                    VoteRecord::getTicketId,
                    Collectors.mapping(
                        r -> r.into(Vote.class),
                        Collectors.toMap(Vote::createdBy, Function.identity())
                    )
                )
            );
    }

    public void deleteByTicketId(@NonNull Long ticketId)
    {
        getDSLContext()
            .deleteFrom(voteTable)
            .where(voteTable.TICKET_ID.eq(ticketId))
            .execute();
    }
}
