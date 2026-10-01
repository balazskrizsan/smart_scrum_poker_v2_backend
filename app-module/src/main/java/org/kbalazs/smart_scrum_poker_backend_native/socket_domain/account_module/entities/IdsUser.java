package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "IDS user entity")
public record IdsUser(
    @Schema(description = "User UUID")
    UUID id,
    @Schema(description = "Account creation timestamp")
    LocalDateTime createdAt
) {
}
