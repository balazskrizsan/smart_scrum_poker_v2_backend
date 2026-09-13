package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoryPointConfig(
    Long id,
    @NonNull String name,
    @NonNull String sizesConfig,      // JSON: [{"name": "Size S"}, {"name": "Size M"}, ...]
    @NonNull String dimensionsConfig, // JSON: [{"name": "Uncertainty", "sizeValues": [{"name": "Size S", "value": 1}, ...]}, ...]
    @NonNull String pointsMapping,    // JSON: [{"totalRange": [0, 3], "points": 1}, ...]
    @NonNull LocalDateTime createdAt,
    @NonNull UUID createdBy
)
{
}
