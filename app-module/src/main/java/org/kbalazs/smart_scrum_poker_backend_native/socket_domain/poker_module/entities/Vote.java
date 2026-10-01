package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Vote entity for poker tickets")
public record Vote(
    @Schema(description = "Vote ID")
    Long id,
    @Schema(description = "Associated ticket ID")
    Long ticketId,
    @With
    @Schema(description = "Story point configuration ID")
    Long storyPointConfigId,
    @Schema(description = "Vote values as string")
    @NonNull String voteValues,
    @With
    @Schema(description = "Calculated point value")
    Short calculatedPoint,
    @Schema(description = "Vote creation timestamp")
    @NonNull LocalDateTime createdAt,
    @Schema(description = "ID of user who cast the vote")
    @NonNull UUID createdBy
)
{
}
