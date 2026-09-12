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
        String dimensionsConfig = """
            [
                {"name": "Uncertainty", "sizeValues": [{"name": "Size S", "value": 1}, {"name": "Size M", "value": 2}, {"name": "Size L", "value": 3}]},
                {"name": "Complexity", "sizeValues": [{"name": "Size S", "value": 1}, {"name": "Size M", "value": 2}, {"name": "Size L", "value": 3}]},
                {"name": "Effort", "sizeValues": [{"name": "Size S", "value": 1}, {"name": "Size M", "value": 2}, {"name": "Size L", "value": 3}]},
                {"name": "Risk", "sizeValues": [{"name": "Size S", "value": 1}, {"name": "Size M", "value": 4}, {"name": "Size L", "value": 5}]}
            ]
            """;
        String pointsMapping = """
            [
                {"totalRange": [0, 4], "points": 1},
                {"totalRange": [5, 6], "points": 2},
                {"totalRange": [7, 8], "points": 3},
                {"totalRange": [9, 10], "points": 5},
                {"totalRange": [11, 15], "points": 8},
                {"totalRange": [16, 100], "points": 13}
            ]
            """;
        
        var record = dslContext.newRecord(StoryPointConfig.STORY_POINT_CONFIG);
        record.set(StoryPointConfig.STORY_POINT_CONFIG.ID, StoryPointConfigFakeBuilder.defaultId1);
        record.set(StoryPointConfig.STORY_POINT_CONFIG.NAME, StoryPointConfigFakeBuilder.defaultName);
        record.set(StoryPointConfig.STORY_POINT_CONFIG.SIZES_CONFIG, JSONB.valueOf(StoryPointConfigFakeBuilder.defaultSizesConfig));
        record.set(StoryPointConfig.STORY_POINT_CONFIG.DIMENSIONS_CONFIG, JSONB.valueOf(dimensionsConfig));
        record.set(StoryPointConfig.STORY_POINT_CONFIG.POINTS_MAPPING, JSONB.valueOf(pointsMapping));
        record.set(StoryPointConfig.STORY_POINT_CONFIG.CREATED_AT, LocalDateTime.of(2020, 11, 22, 11, 22, 33));
        record.set(StoryPointConfig.STORY_POINT_CONFIG.CREATED_BY, IdsUserFakeBuilder.defaultId1);
        record.store();
    }
}
