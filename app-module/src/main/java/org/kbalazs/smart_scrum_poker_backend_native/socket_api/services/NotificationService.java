package org.kbalazs.smart_scrum_poker_backend_native.socket_api.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService
{
    private final SimpMessagingTemplate simpMessagingTemplate;

    public <T> void notifyPokerGame(
        @NonNull UUID pokerIdSecure,
        @NonNull T data,
        @NonNull SocketDestination socketDestination
    )
        throws ApiException
    {
        simpMessagingTemplate.convertAndSend(
            "/queue/reply-" + pokerIdSecure,
            new ResponseEntityBuilder<T>().socketDestination(socketDestination).data(data).build()
        );
    }
}
