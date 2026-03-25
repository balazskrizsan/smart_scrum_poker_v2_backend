package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert2Poker implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(Poker.POKER, new PokerFakeBuilder().build()).store();
        dslContext.newRecord(Poker.POKER, new PokerFakeBuilder().build2()).store();
    }
}
