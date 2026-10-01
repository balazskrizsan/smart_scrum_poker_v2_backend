package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.util.UUID;

@Schema(description = "Ticket entity for poker games")
public record Ticket(
    @Schema(description = "Database ID")
    Long id,
    @Schema(description = "Public UUID identifier")
    UUID publicId,
    @Schema(description = "Parent poker game ID")
    Long pokerId,
    @Schema(description = "Ticket name")
    @NonNull String name,
    @Schema(description = "Whether the ticket is currently active")
    boolean isActive
)
{
}
