package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import lombok.NonNull;

import java.util.Map;

public record VoteValues(
    boolean coffeeMug,
    boolean questionMark,
    @NonNull Map<String, String> dimensionValues  // {"uncertainty": "S", "complexity": "M", ...}
) {
}
