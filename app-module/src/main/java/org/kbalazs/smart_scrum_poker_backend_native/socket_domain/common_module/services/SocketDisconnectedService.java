package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserSessionsService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.DisconnectResponse;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SocketDisconnectedService
{
    IdsUserSessionsService idsUserSessionsService;
    SocketConnectionHandlerService socketConnectionHandlerService;

    public DisconnectResponse disconnect(MessageHeaders headers)
        throws SessionException
    {
        UUID sessionId;
        IdsUserSession idsUserSession;
        try
        {
            sessionId = socketConnectionHandlerService.getSessionId(headers);
            idsUserSession = idsUserSessionsService.getInsecureUserSession(sessionId);
        }
        catch (SocketException e)
        {
            log.info("Socket disconnected without insecureUserId", e);

            return new DisconnectResponse(false, null);
        }

        idsUserSessionsService.removeBySessionId(sessionId);

        return new DisconnectResponse(
            idsUserSessionsService.countByIdsUserId(idsUserSession.idsUserId()) == 0,
            idsUserSession
        );
    }
}
