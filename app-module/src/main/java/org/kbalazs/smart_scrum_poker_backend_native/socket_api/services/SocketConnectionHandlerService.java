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

    public @NonNull UUID getIdsUserId(@NonNull MessageHeaders headers) throws SocketException
    {
        try
        {
            String idsUserId = Objects.requireNonNull(getNativeHeaders(headers))
                .get("idsUserId")
                .getFirst();

            return UUID.fromString(idsUserId);
        }
        catch (NullPointerException e)
        {
            throw new SocketException("idsUserId not found");
        }
    }

    private Map<String, List<String>> getNativeHeaders(@NonNull MessageHeaders headers)
    {
        GenericMessage<?> genericMessage = headers.get(CONNECT_MESSAGE_HEADER, GenericMessage.class);

        return genericMessage.getHeaders().get(NATIVE_HEADERS, Map.class);
    }
}
