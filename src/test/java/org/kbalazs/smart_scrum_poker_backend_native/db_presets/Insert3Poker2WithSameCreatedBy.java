package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;

public class Insert3Poker2WithSameCreatedBy implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        new PokerFakeBuilder().build1to3_2withSameCreatedBy().forEach(poker ->
            dslContext.newRecord(Poker.POKER, poker).store()
        );
    }
}
