package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteFakeBuilder
{
}
