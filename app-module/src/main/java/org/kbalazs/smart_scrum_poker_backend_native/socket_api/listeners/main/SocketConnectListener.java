package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.main;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketNotificationHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.SocketConnectedService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.ConnectResponse;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class SocketConnectListener
{
    ApplicationProperties applicationProperties;
    SocketNotificationHandlerService socketNotificationHandlerService;
    SocketConnectedService socketConnectedService;

    // @todo: test
    @EventListener
    public void socketConnectListener(@NonNull SessionConnectedEvent event)
        throws AccountException, SocketException
    {
        if (!applicationProperties.isEnabledSocketConnectAndDisconnectListeners())
        {
            return;
        }

        ConnectResponse response = socketConnectedService.connect(event.getMessage().getHeaders());

        if (response.shouldSendNotification())
        {
            socketNotificationHandlerService.notifyPokerGameWithNewSession(
                response.idsUserSession().idsUserId()
            );
        }
    }
}
