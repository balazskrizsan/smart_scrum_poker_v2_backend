package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.jooq.impl.DSL;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StoryPointConfigRepository extends AbstractRepository
{
    public StoryPointConfig create(@NonNull StoryPointConfig storyPointConfig)
    {
        getDSLContext().insertInto(DSL.table("story_point_config"))
            .set(getDSLContext().newRecord(DSL.table("story_point_config"), storyPointConfig))
            .execute();
        return storyPointConfig;
    }

    public Optional<StoryPointConfig> findById(@NonNull Long id)
    {
        return getDSLContext()
            .selectFrom(DSL.table("story_point_config"))
            .where(DSL.field("id").eq(id))
            .fetchOptionalInto(StoryPointConfig.class);
    }

    public Optional<StoryPointConfig> findDefaultConfig()
    {
        return getDSLContext()
            .selectFrom(DSL.table("story_point_config"))
            .where(DSL.field("name").eq("Default Config"))
            .fetchOptionalInto(StoryPointConfig.class);
    }
}
