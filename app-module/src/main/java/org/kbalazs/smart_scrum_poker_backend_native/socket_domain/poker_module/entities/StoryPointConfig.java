package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Story point configuration for poker games")
public record StoryPointConfig(
    @Schema(description = "Configuration ID")
    Long id,
    @Schema(description = "Configuration name")
    @NonNull String name,
    @Schema(description = "JSON configuration for sizes")
    @NonNull String sizesConfig,
    @Schema(description = "JSON configuration for dimensions")
    @NonNull String dimensionsConfig,
    @Schema(description = "JSON mapping for points calculation")
    @NonNull String pointsMapping,
    @Schema(description = "Creation timestamp")
    @NonNull LocalDateTime createdAt,
    @Schema(description = "ID of user who created this configuration")
    @NonNull UUID createdBy
)
{
}
