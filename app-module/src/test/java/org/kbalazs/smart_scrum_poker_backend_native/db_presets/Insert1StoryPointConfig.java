package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.StoryPointConfigFakeBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.STORY_POINT_CONFIG;

public class Insert1StoryPointConfig implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(StoryPointConfig.STORY_POINT_CONFIG, new StoryPointConfigFakeBuilder().build()).store();
    }
}
