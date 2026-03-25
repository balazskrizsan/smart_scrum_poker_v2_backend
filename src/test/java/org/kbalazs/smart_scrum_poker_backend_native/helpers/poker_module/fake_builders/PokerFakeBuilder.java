package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
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
    public static final UUID defaultIdSecure1 = UUID.fromString("10000000-0000-0000-0000-000000000001");
    public static final UUID defaultIdSecure2 = UUID.fromString("10000000-0000-0000-0000-000000000002");
    public static final UUID defaultIdSecure3 = UUID.fromString("10000000-0000-0000-0000-000000000003");
    public static final String defaultSprintName = "sprint #1";
    public static final String defaultSprintName2 = "sprint #2";
    public static final String defaultSprintName3 = "sprint #3";

    long id = defaultId1;
    long id2 = defaultId2;
    long id3 = defaultId3;
    UUID idSecure = defaultIdSecure1;
    UUID idSecure2 = defaultIdSecure2;
    UUID idSecure3 = defaultIdSecure3;
    String sprintName = defaultSprintName;
    String sprintName2 = defaultSprintName2;
    String sprintName3 = defaultSprintName3;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);
    UUID createdBy = InsecureUserFakeBuilder.defaultIdSecure1;
    UUID createdBy2 = InsecureUserFakeBuilder.defaultIdSecure2;
    UUID createdBy3 = InsecureUserFakeBuilder.defaultIdSecure3;

    public Poker build()
    {
        return new Poker(id, idSecure, sprintName, createdAt, createdBy);
    }

    public Poker build2()
    {
        return new Poker(id2, idSecure2, sprintName2, createdAt, createdBy2);
    }

    public Poker build3()
    {
        return new Poker(id3, idSecure3, sprintName3, createdAt, createdBy3);
    }

    public List<Poker> build1to3_2withSameCreatedBy()
    {
        return List.of(build(), build2(), new PokerFakeBuilder().createdBy3(createdBy2).build3());
    }

    public Poker buildNoId()
    {
        return new Poker(null, idSecure, sprintName, createdAt, createdBy);
    }
}
