package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoryPointConfig(
    Long id,
    @NonNull String name,
    @NonNull String sizesConfig,      // JSON: [{"name": "small", "value": 1}, ...]
    @NonNull String dimensionsConfig, // JSON: [{"name": "risk", "sizeValues": {"small": 1, ...}}, ...]
    @NonNull String pointsMapping,    // JSON: [{"totalRange": [4, 5], "points": 1}, ...]
    @NonNull LocalDateTime createdAt,
    @NonNull UUID createdBy
)
{
}
