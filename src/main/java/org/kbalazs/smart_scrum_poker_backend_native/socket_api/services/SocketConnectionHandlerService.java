package org.kbalazs.smart_scrum_poker_backend_native.socket_api.services;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER;
import static org.springframework.messaging.support.NativeMessageHeaderAccessor.NATIVE_HEADERS;

@Service
public class SocketConnectionHandlerService
{
    public UUID getSessionId(@NonNull MessageHeaders headers) throws SocketException
    {
        try
        {
            return UUID.fromString(Objects.requireNonNull(headers.get("simpSessionId")).toString());
        }
        catch (NullPointerException e)
        {
            throw new SocketException("simpSessionId not found");
        }
    }

    public UUID getInsecureUserIdSecure(@NonNull MessageHeaders headers) throws SocketException
    {
        try
        {
            String insecureUserIdSecure = Objects.requireNonNull(getNativeHeaders(headers))
                .get("insecureUserIdSecure")
                .getFirst();

            return UUID.fromString(insecureUserIdSecure);
        }
        catch (NullPointerException e)
        {
            throw new SocketException("insecureUserIdSecure not found");
        }
    }

    private Map<String, List<String>> getNativeHeaders(@NonNull MessageHeaders headers)
    {
        GenericMessage<?> genericMessage = headers.get(CONNECT_MESSAGE_HEADER, GenericMessage.class);

        return genericMessage.getHeaders().get(NATIVE_HEADERS, Map.class);
    }
}
