package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.main;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketNotificationHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.SocketConnectedService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.ConnectResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketConnectListener
{
    private final ApplicationProperties applicationProperties;
    private final SocketNotificationHandlerService socketNotificationHandlerService;
    private final SocketConnectedService socketConnectedService;

    // @todo: test
    @EventListener
    public void socketConnectListener(@NonNull SessionConnectedEvent event) throws AccountException, SocketException
    {
        if (!applicationProperties.isEnabledSocketConnectAndDisconnectListeners())
        {
            log.warn("Connect listener disabled");

            return;
        }

        log.info("Socket connection opened: {}", event);

        ConnectResponse response = socketConnectedService.connect(event.getMessage().getHeaders());

        if (response.shouldSendNotification())
        {
            socketNotificationHandlerService.notifyPokerGameWithNewSession(
                response.insecureUserSession().insecureUserIdSecure()
            );
        }
    }
}
