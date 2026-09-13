package org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IdsUserFakeBuilder
{
    public static final UUID defaultId1 = UUID.fromString("0f48291f-c079-4b10-a6d3-11b029c8d03a");
    public static final UUID defaultId2 = UUID.fromString("10000000-0000-0000-0000-000000002002");
    public static final UUID defaultId3 = UUID.fromString("10000000-0000-0000-0000-000000002003");
    public static final UUID defaultId4 = UUID.fromString("10000000-0000-0000-0000-000000002004");

    UUID id = defaultId1;
    UUID id2 = defaultId2;
    UUID id3 = defaultId3;
    UUID id4 = defaultId4;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);

    public IdsUser build()
    {
        return new IdsUser(id, createdAt);
    }

    public IdsUser build2()
    {
        return new IdsUser(id2, createdAt);
    }

    public IdsUser build3()
    {
        return new IdsUser(id3, createdAt);
    }

    public IdsUser build4()
    {
        return new IdsUser(id4, createdAt);
    }

    public List<IdsUser> buildAsList()
    {
        return List.of(build());
    }

    public List<IdsUser> build2AsList()
    {
        return List.of(build2());
    }

    public List<IdsUser> build1and2AsList()
    {
        return List.of(build(), build2());
    }
}
