package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Poker game entity")
public record Poker(
    @Schema(description = "Database ID")
    Long id,
    @Schema(description = "Public UUID identifier")
    UUID publicId,
    @Schema(description = "Poker game name")
    @NonNull String name,
    @Schema(description = "Creation timestamp")
    @NonNull LocalDateTime createdAt,
    @Schema(description = "ID of user who created the game")
    @NonNull UUID createdBy,
    @Schema(description = "Story point configuration ID")
    Long storyPointConfigId
)
{
}
