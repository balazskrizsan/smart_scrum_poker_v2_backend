package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.jooq.impl.DSL;
import org.kbalazs.smart_scrum_poker_backend_native.db.Tables;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.StoryPointConfigRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StoryPointConfigRepository extends AbstractRepository
{
    public StoryPointConfig create(@NonNull StoryPointConfig storyPointConfig)
    {
        StoryPointConfigRecord newRecord = getDSLContext().newRecord(Tables.STORY_POINT_CONFIG, storyPointConfig);
        newRecord.store();

        return newRecord.into(StoryPointConfig.class);
    }

    public Optional<StoryPointConfig> findById(@NonNull Long id)
    {
        return getDSLContext()
            .selectFrom(Tables.STORY_POINT_CONFIG)
            .where(Tables.STORY_POINT_CONFIG.ID.eq(id))
            .fetchOptionalInto(StoryPointConfig.class);
    }

    public Optional<StoryPointConfig> findDefaultConfig()
    {
        var x = "unsued";

        return getDSLContext()
            .selectFrom(Tables.STORY_POINT_CONFIG)
            .where(Tables.STORY_POINT_CONFIG.NAME.eq("Default Config"))
            .fetchOptionalInto(StoryPointConfig.class);
    }
}
