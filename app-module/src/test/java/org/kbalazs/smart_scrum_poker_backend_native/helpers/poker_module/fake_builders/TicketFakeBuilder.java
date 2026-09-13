package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketFakeBuilder
{
    public static final long defaultId1 = 101001L;
    public static final long defaultId2 = 101002L;
    public static final long defaultId3 = 101003L;
    public static final long defaultId4 = 101004L;
    public static final long defaultId5 = 101005L;
    public static final UUID defaultIdSecure1 = UUID.fromString("10000000-0000-0000-0000-000000001001");
    public static final UUID defaultIdSecure2 = UUID.fromString("10000000-0000-0000-0000-000000001002");
    public static final UUID defaultIdSecure3 = UUID.fromString("10000000-0000-0000-0000-000000001003");
    public static final UUID defaultIdSecure4 = UUID.fromString("10000000-0000-0000-0000-000000001004");
    public static final UUID defaultIdSecure5 = UUID.fromString("10000000-0000-0000-0000-000000001005");
    public static final String defaultName1 = "ticket #1";
    public static final String defaultName2 = "ticket #2";

    long id = defaultId1;
    long id2 = defaultId2;
    long id3 = defaultId3;
    long id4 = defaultId4;
    long id5 = defaultId5;
    UUID idSecret = defaultIdSecure1;
    UUID idSecret2 = defaultIdSecure2;
    UUID idSecret3 = defaultIdSecure3;
    UUID idSecret4 = defaultIdSecure4;
    UUID idSecret5 = defaultIdSecure5;
    long pokerId = PokerFakeBuilder.defaultId1;
    long pokerId2 = PokerFakeBuilder.defaultId1;
    long pokerId3 = PokerFakeBuilder.defaultId1;
    long pokerId4 = PokerFakeBuilder.defaultId2;
    long pokerId5 = PokerFakeBuilder.defaultId2;
    String name = defaultName1;
    String name2 = defaultName2;
    String name3 = "ticket #3";
    String name4 = "ticket #4";
    String name5 = "ticket #5";
    boolean isActive = false;
    boolean isActive2 = false;
    boolean isActive3 = false;
    boolean isActive4 = false;
    boolean isActive5 = false;

    public List<Ticket> buildNoIdAsList()
    {
        return new ArrayList<>()
        {{
            add(buildNoId());
        }};
    }

    public List<Ticket> build1to3AsList()
    {
        return List.of(build(), build2(), build3());
    }

    public Ticket build()
    {
        return new Ticket(id, idSecret, pokerId, name, isActive);
    }

    public Ticket build2()
    {
        return new Ticket(id2, idSecret2, pokerId2, name2, isActive2);
    }

    public Ticket build3()
    {
        return new Ticket(id3, idSecret3, pokerId3, name3, isActive3);
    }

    public Ticket build4()
    {
        return new Ticket(id4, idSecret4, pokerId4, name4, isActive4);
    }

    public Ticket build5()
    {
        return new Ticket(id5, idSecret5, pokerId5, name5, isActive5);
    }

    public Ticket buildNoId()
    {
        return new Ticket(null, null, null, name, isActive);
    }
}
