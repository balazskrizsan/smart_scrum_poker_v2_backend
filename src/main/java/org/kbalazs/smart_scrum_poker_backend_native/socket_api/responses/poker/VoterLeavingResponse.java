package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import lombok.NonNull;

import java.util.UUID;

public record VoterLeavingResponse(@NonNull UUID userIdSecure, @NonNull UUID pokerIdSecure)
{
}
