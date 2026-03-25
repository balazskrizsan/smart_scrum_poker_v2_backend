package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public record InGamePlayer(UUID insecureUserIdSecure, UUID pokerIdSecure, LocalDateTime createdAt)
{
}
