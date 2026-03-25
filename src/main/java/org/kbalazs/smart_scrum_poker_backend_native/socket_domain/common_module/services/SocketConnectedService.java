package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.LocalDateTimeFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserSessionsService;
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
    InsecureUserSessionsService insecureUserSessionsService;
    LocalDateTimeFactory localDateTimeFactory;

    // @todo: test
    public ConnectResponse connect(MessageHeaders headers) throws SocketException
    {
        UUID insecureUserIdSecure;
        try
        {
            insecureUserIdSecure = socketConnectionHandlerService.getInsecureUserIdSecure(headers);
        }
        catch (SocketException e)
        {
            log.info("Socket connection without insecureUserId"); // @todo: test, monitor

            return new ConnectResponse(false, null);
        }
        UUID simpSessionId = socketConnectionHandlerService.getSessionId(headers);

        InsecureUserSession insecureUserSession = new InsecureUserSession(
            insecureUserIdSecure,
            simpSessionId,
            localDateTimeFactory.create()
        );

        insecureUserSessionsService.add(insecureUserSession);
        return new ConnectResponse(
            insecureUserSessionsService.countByIdSecure(insecureUserIdSecure) == 1,
            insecureUserSession
        );
    }
}
