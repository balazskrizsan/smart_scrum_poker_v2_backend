package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PokerFakeBuilder
{
    public static final long defaultId1 = 100001L;
    public static final long defaultId2 = 100002L;
    public static final long defaultId3 = 100003L;
    public static final UUID defaultPublicId1 = UUID.fromString("10000000-0000-0000-0000-000000000001");
    public static final UUID defaultPublicId2 = UUID.fromString("10000000-0000-0000-0000-000000000002");
    public static final UUID defaultPublicId3 = UUID.fromString("10000000-0000-0000-0000-000000000003");
    public static final String defaultSprintName = "sprint #1";
    public static final String defaultSprintName2 = "sprint #2";
    public static final String defaultSprintName3 = "sprint #3";

    Long id = defaultId1;
    Long id2 = defaultId2;
    Long id3 = defaultId3;
    UUID publicId = defaultPublicId1;
    UUID publicId2 = defaultPublicId2;
    UUID publicId3 = defaultPublicId3;
    String name = defaultSprintName;
    String name2 = defaultSprintName2;
    String name3 = defaultSprintName3;
    Long storyPointConfigId = StoryPointConfigFakeBuilder.defaultId1;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);
    UUID createdBy = UUID.fromString("0f48291f-c079-4b10-a6d3-11b029c8d03a");
    UUID createdBy2 = defaultPublicId2;
    UUID createdBy3 = defaultPublicId3;

    public Poker build()
    {
        return new Poker(id, publicId, name, createdAt, createdBy, storyPointConfigId);
    }

    public Poker build2()
    {
        return new Poker(id2, publicId2, name2, createdAt, createdBy2, storyPointConfigId);
    }

    public Poker build3()
    {
        return new Poker(id3, publicId3, name3, createdAt, createdBy3, storyPointConfigId);
    }

    public List<Poker> build1to3_2withSameCreatedBy()
    {
        return List.of(build(), build2(), new PokerFakeBuilder().createdBy3(createdBy2).build3());
    }

    public Poker buildNoId()
    {
        return new Poker(null, publicId, name, createdAt, createdBy, storyPointConfigId);
    }
}
