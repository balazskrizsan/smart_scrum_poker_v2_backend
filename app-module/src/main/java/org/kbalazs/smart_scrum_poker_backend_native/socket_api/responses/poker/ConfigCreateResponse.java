package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;

@Schema(description = "Response when a story point configuration is created")
public record ConfigCreateResponse(
    @Schema(description = "The created configuration")
    @NonNull StoryPointConfig config
)
{
}
