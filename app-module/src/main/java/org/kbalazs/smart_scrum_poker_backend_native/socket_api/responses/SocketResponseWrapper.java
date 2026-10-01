package org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Base interface for socket response wrappers.
 * All socket response records should implement this interface to ensure consistent structure.
 */
public interface SocketResponseWrapper<T>
{
    @Schema(description = "Response data payload")
    T data();

    @Schema(description = "Indicates if the operation was successful")
    Boolean success();

    @Schema(description = "Error code, 0 if successful")
    int errorCode();

    @Schema(description = "Request identifier")
    String requestId();

    @Schema(description = "Socket response destination")
    String socketResponseDestination();
}
