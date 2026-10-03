package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.MyPokersRequest;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyPokersRequestFakeBuilder
{
    UUID idsUserId = IdsUserFakeBuilder.defaultId1;

    public MyPokersRequest build()
    {
        return new MyPokersRequest(idsUserId);
    }
}
