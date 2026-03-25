package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InGamePlayers;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.InGamePlayerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert1InGamePlayerForPoker2 implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(
            InGamePlayers.IN_GAME_PLAYERS,
            new InGamePlayerFakeBuilder().pokerIdSecure(PokerFakeBuilder.defaultIdSecure2).build()
        ).store();
    }
}
