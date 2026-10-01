package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;

@Schema(description = "Response when a new user joins voting")
public record VoteNewJoinerResponse(
    @Schema(description = "Profile of the new joiner")
    UserProfile userProfile
)
{
}
