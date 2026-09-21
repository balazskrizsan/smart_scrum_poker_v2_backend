package org.kbalazs.smart_scrum_poker_backend_native.api.value_objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic response wrapper")
public record ResponseData<T>(
    @Schema(description = "Response data payload")
    T data,
    @Schema(description = "Indicates if the operation was successful")
    Boolean success,
    @Schema(description = "Error code, 0 if successful")
    int errorCode,
    @Schema(description = "Request identifier")
    String requestId,
    @Schema(description = "Socket response destination")
    String socketResponseDestination
)
{
}
