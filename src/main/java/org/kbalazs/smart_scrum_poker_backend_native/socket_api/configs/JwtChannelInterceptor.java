package org.kbalazs.smart_scrum_poker_backend_native.socket_api.configs;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor
{
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel)
    {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null)
        {
            return message;
        }

        if (!StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            if (accessor.getUser() != null)
            {
                Authentication authentication = (Authentication) accessor.getUser();
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            log.warn("No valid Authorization header found for STOMP CONNECT");

            return message;
        }

        String token = authHeader.substring(7);

        try
        {
            Jwt jwt = jwtDecoder.decode(token);

            Authentication authentication = jwtAuthenticationConverter.convert(jwt);
            accessor.setUser(authentication);
            accessor.setHeader("simpUser", authentication);

            log.info("JWT authentication successful for user: {}", authentication.getName());
        } catch (JwtException e)
        {
            log.error("JWT authentication failed: {}", e.getMessage());

            throw new SecurityException("Invalid JWT token", e);
        }

        return message;
    }
}
