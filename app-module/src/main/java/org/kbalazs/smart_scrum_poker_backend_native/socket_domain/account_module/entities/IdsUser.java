package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities;

import java.time.LocalDateTime;
import java.util.UUID;

// @todo: nullable handlers
public record IdsUser(UUID id, LocalDateTime createdAt) {
}
