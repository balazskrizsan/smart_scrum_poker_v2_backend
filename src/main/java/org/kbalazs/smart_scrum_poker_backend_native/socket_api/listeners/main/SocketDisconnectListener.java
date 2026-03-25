package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.main;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketNotificationHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.SocketDisconnectedService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.DisconnectResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketDisconnectListener
{
    private final ApplicationProperties applicationProperties;
    private final SocketNotificationHandlerService socketNotificationHandlerService;
    private final SocketDisconnectedService socketDisconnectedService;

    // @todo: test
    @EventListener
    public void socketDisconnectListener(@NonNull SessionDisconnectEvent event)
        throws AccountException, SessionException, SocketException
    {
        if (!applicationProperties.isEnabledSocketConnectAndDisconnectListeners())
        {
            log.warn("Disconnect listener disabled");

            return;
        }

        log.info("Socket connection closed: {}", event);

        DisconnectResponse response = socketDisconnectedService.disconnect(event.getMessage().getHeaders());

        if (response.shouldSendNotification())
        {
            socketNotificationHandlerService.notifyPokerGameWithLeavingSession(
                response.insecureUserSession().insecureUserIdSecure()
            );
        }
    }
}
