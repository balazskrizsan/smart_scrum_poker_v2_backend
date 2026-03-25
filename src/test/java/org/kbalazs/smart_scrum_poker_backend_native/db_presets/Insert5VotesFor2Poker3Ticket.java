package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert5VotesFor2Poker3Ticket implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(Vote.VOTE, new VoteFakeBuilder().build()).store();
        dslContext.newRecord(Vote.VOTE, new VoteFakeBuilder().build2()).store();
        dslContext.newRecord(Vote.VOTE, new VoteFakeBuilder().build3()).store();
        dslContext.newRecord(Vote.VOTE, new VoteFakeBuilder().build4()).store();
        dslContext.newRecord(Vote.VOTE, new VoteFakeBuilder().build5()).store();
    }
}
