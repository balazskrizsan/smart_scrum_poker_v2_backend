package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoterLeaving;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoterLeavingRequestFakeBuilder
{
    UUID userIdSecure = InsecureUserFakeBuilder.defaultIdSecure1;
    UUID pokerIdSecure = PokerFakeBuilder.defaultIdSecure1;

    public VoterLeaving build()
    {
        return new VoterLeaving(userIdSecure, pokerIdSecure);
    }
}
