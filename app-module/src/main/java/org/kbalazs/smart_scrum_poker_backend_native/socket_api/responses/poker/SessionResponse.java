package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;

@Schema(description = "Response for user session information")
public record SessionResponse(
    @Schema(description = "IDS user information")
    IdsUser idsUser
)
{
}
