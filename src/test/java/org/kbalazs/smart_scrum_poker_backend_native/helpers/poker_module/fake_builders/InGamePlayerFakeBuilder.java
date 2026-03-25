package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InGamePlayer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
public class InGamePlayerFakeBuilder
{
    private UUID insecureUserIdSecure = InsecureUserFakeBuilder.defaultIdSecure1;
    private UUID pokerIdSecure = PokerFakeBuilder.defaultIdSecure1;
    private LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);

    public InGamePlayer build()
    {
        return new InGamePlayer(insecureUserIdSecure, pokerIdSecure, createdAt);
    }

    public List<InGamePlayer> buildAsList()
    {
        return new ArrayList<>()
        {{
            add(new InGamePlayer(insecureUserIdSecure, pokerIdSecure, createdAt));
        }};
    }
}
