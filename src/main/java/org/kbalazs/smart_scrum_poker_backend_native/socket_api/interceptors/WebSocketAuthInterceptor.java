package org.kbalazs.smart_scrum_poker_backend_native.socket_api.interceptors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor
{
    private final JwtDecoder jwtDecoder;
    private final ApplicationProperties applicationProperties;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel)
    {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            log.info("Socket connection");
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer "))
            {
                String token = authHeader.substring(7);

                try
                {
                    Jwt jwt = jwtDecoder.decode(token);

                    List<String> roles = jwt.getClaimAsStringList("roles");
                    if (roles == null)
                    {
                        roles = new ArrayList<>();
                    }
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        jwt.getSubject(),
                        null,
                        roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );

                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    log.info("WebSocket authentication successful for user: {}", jwt.getSubject());
                }
                catch (JwtException e)
                {
                    log.error("JWT validation failed: {}", e.getMessage());
                    throw new IllegalArgumentException("Invalid JWT token");
                }
            }
            else
            {
                log.error("Missing or invalid Authorization header");
                throw new IllegalArgumentException("Authorization header required");
            }
        }

        return message;
    }
}
