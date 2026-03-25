package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum;

public record VoteValues(
    boolean coffeeMug,
    boolean questionMark,
    @NonNull SizeEnum uncertainty,
    @NonNull SizeEnum complexity,
    @NonNull SizeEnum effort,
    @NonNull SizeEnum risk
) {
}
