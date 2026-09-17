package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.POKER;

public class Insert1Poker implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        var poker = new PokerFakeBuilder().build();
        dslContext.insertInto(Poker.POKER)
            .set(POKER.ID, poker.id())
            .set(POKER.PUBLIC_ID, poker.publicId())
            .set(POKER.NAME, poker.name())
            .set(POKER.STORY_POINT_CONFIG_ID, poker.storyPointConfigId())
            .set(POKER.CREATED_AT, poker.createdAt())
            .set(POKER.CREATED_BY, poker.createdBy())
            .execute();
    }
}
