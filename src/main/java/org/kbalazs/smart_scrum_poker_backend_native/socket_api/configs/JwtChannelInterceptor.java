package org.kbalazs.smart_scrum_poker_backend_native.socket_api.configs;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.LocalDateTimeFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.springframework.lang.Nullable;
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

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class JwtChannelInterceptor implements ChannelInterceptor
{
    JwtDecoder jwtDecoder;
    JwtAuthenticationConverter jwtAuthenticationConverter;
    IdsUserService idsUserService;
    SocketConnectionHandlerService socketConnectionHandlerService;
    LocalDateTimeFactory localDateTimeFactory;

    @Override
    @Nullable
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel)
    {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null)
        {
            return message;
        }

        if (!StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            setSecurityContext(accessor);

            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            log.info("No valid Authorization header found for STOMP CONNECT");

            return message;
        }

        String token = authHeader.substring(7);
        try
        {
            preSendLogic(message, token, accessor);
        }
        catch (JwtException e)
        {
            log.error("JWT authentication failed: {}", e.getMessage());

            throw new SecurityException("Invalid JWT token", e);
        }

        return message;
    }

    private void preSendLogic(Message<?> message, String token, StompHeaderAccessor accessor)
    {
        Jwt jwt = jwtDecoder.decode(token);

        Authentication authentication = jwtAuthenticationConverter.convert(jwt);
        accessor.setUser(authentication);
        accessor.setHeader("simpUser", authentication);
        accessor.addNativeHeader("idsUserId", authentication.getName());

        log.info("JWT authentication successful for IdsUserId: {}", authentication.getName());

        createIdsUserIfNotExists(message, authentication);
    }

    private static void setSecurityContext(@Nonnull StompHeaderAccessor accessor)
    {
        if (accessor.getUser() != null)
        {
            Authentication authentication = (Authentication) accessor.getUser();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void createIdsUserIfNotExists(@Nonnull Message<?> message, @Nonnull Authentication authentication)
    {
        try
        {
            idsUserService.createIfNotExists(
                new IdsUser(UUID.fromString(authentication.getName()), localDateTimeFactory.create())
            );
        }
        catch (AccountException e)
        {
            log.error("Failed to create IdsUser: {}", e.getMessage(), e);
        }
    }
}
