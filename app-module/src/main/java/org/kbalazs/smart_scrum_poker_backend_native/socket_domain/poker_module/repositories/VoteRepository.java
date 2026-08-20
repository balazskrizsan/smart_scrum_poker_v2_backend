package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.impl.DSL;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.VoteRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.VOTE;

@Repository
public class VoteRepository extends AbstractRepository {
    public void create(@NonNull Vote vote) {
        DSLContext ctx = getDSLContext();

        ctx.insertInto(VOTE)
            .set(ctx.newRecord(VOTE, vote))
            .set(field("calculated_point"), vote.calculatedPoint())
            .onDuplicateKeyUpdate()
            .set(field("story_point_config_id"), vote.storyPointConfigId())
            .set(field("vote_values"), JSON.valueOf(vote.voteValues()))
            .set(field("calculated_point"), vote.calculatedPoint())
            .execute();
    }

    public @NonNull Map<Long, Map<UUID, Vote>> getVotesWithTicketGroupByTicketIds(@NonNull List<Long> ticketIds) {
        return getDSLContext()
            .selectFrom(VOTE)
            .where(VOTE.TICKET_ID.in(ticketIds))
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

    public void deleteByTicketId(@NonNull Long ticketId) {
        getDSLContext()
            .deleteFrom(VOTE)
            .where(VOTE.TICKET_ID.eq(ticketId))
            .execute();
    }
}
