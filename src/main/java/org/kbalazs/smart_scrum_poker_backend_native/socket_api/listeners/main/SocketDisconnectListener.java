package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.main;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketNotificationHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.SocketDisconnectedService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.DisconnectResponse;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class SocketDisconnectListener
{
    ApplicationProperties applicationProperties;
    SocketNotificationHandlerService socketNotificationHandlerService;
    SocketDisconnectedService socketDisconnectedService;

    // @todo: test
    @EventListener
    public void socketDisconnectListener(@NonNull SessionDisconnectEvent event)
        throws AccountException, SessionException
    {
        if (!applicationProperties.isEnabledSocketConnectAndDisconnectListeners())
        {
            return;
        }

        DisconnectResponse response = socketDisconnectedService.disconnect(event.getMessage().getHeaders());

        if (response.shouldSendNotification())
        {
            socketNotificationHandlerService.notifyPokerGameWithLeavingSession(
                response.idsUserSession().idsUserId()
            );
        }
    }
}
