package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import java.util.UUID;

public record VoterLeaving(UUID userIdSecure, UUID pokerIdSecure)
{
}
