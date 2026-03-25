package org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class InsecureUserFakeBuilder
{
    public static final long defaultId1 = 102001L;
    public static final long defaultId2 = 102002L;
    public static final long defaultId3 = 102003L;
    public static final UUID defaultIdSecure1 = UUID.fromString("10000000-0000-0000-0000-000000002001");
    public static final UUID defaultIdSecure2 = UUID.fromString("10000000-0000-0000-0000-000000002002");
    public static final UUID defaultIdSecure3 = UUID.fromString("10000000-0000-0000-0000-000000002003");
    public static final UUID defaultIdSecure4 = UUID.fromString("10000000-0000-0000-0000-000000002004");
    public static final String defaultUserName = "insecure user name";

    long id = defaultId1;
    long id2 = defaultId2;
    long id3 = defaultId3;
    UUID idSecure = defaultIdSecure1;
    UUID idSecure2 = defaultIdSecure2;
    UUID idSecure3 = defaultIdSecure3;
    String userName = defaultUserName;
    String userName2 = "insecure user name 2";
    String userName3 = "insecure user name 3";
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);

    public InsecureUser build()
    {
        return new InsecureUser(id, idSecure, userName, createdAt);
    }

    public InsecureUser build2()
    {
        return new InsecureUser(id2, idSecure2, userName2, createdAt);
    }

    public InsecureUser build3()
    {
        return new InsecureUser(id3, idSecure3, userName3, createdAt);
    }

    public List<InsecureUser> buildAsList()
    {
        return List.of(build());
    }

    public List<InsecureUser> build2AsList()
    {
        return List.of(build2());
    }

    public List<InsecureUser> build1and2AsList()
    {
        return List.of(build(), build2());
    }
}
