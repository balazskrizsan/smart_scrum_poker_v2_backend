package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.LocalDateTimeFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserSessionsService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.ConnectResponse;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SocketConnectedService
{
    SocketConnectionHandlerService socketConnectionHandlerService;
    IdsUserSessionsService idsUserSessionsService;
    LocalDateTimeFactory localDateTimeFactory;

    public ConnectResponse connect(MessageHeaders headers)
        throws SocketException
    {
        UUID idsUserId;
        try
        {
            idsUserId = socketConnectionHandlerService.getIdsUserId(headers);
        }
        catch (SocketException e)
        {
            log.info("Socket connection without ids user id");

            return new ConnectResponse(false, null);
        }

        log.info("Socket connection with ids user id#{}", idsUserId);

        UUID simpSessionId = socketConnectionHandlerService.getSessionId(headers);

        IdsUserSession idsUserSession = new IdsUserSession(idsUserId, simpSessionId, localDateTimeFactory.create());

        idsUserSessionsService.add(idsUserSession);

        return new ConnectResponse(
            idsUserSessionsService.countByIdsUserId(idsUserId) == 1,
            idsUserSession
        );
    }
}
