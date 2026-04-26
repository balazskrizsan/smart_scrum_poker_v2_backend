package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;

public record VoteNewJoinerResponse(UserProfile userProfile)
{
}
