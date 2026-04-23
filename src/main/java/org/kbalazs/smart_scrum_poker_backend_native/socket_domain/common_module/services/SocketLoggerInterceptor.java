package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketLoggerInterceptor implements ChannelInterceptor
{
    @Override
    @Nullable
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel)
    {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();

        if (destination != null)
        {
            Object payload = message.getPayload();
            String payloadString = payload instanceof byte[] ? new String((byte[]) payload) : payload.toString();

            log.info("Socket incoming: {} | Payload: {}", destination, payloadString);
        }

        return message;
    }
}
