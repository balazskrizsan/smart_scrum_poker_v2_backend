package org.kbalazs.smart_scrum_poker_backend_native.api.value_objects;

public record ResponseData<T>(
    T data,
    Boolean success,
    int errorCode,
    String requestId,
    String socketResponseDestination
)
{
}
