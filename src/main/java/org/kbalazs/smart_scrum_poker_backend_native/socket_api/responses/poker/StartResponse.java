package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;

public record StartResponse(Poker poker)
{
}
