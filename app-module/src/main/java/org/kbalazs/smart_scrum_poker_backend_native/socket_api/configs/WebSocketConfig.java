package org.kbalazs.smart_scrum_poker_backend_native.socket_api.configs;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.services.SocketLoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@EnableWebSocketMessageBroker
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
    ApplicationProperties applicationProperties;
    JwtChannelInterceptor jwtChannelInterceptor;
    SocketLoggerInterceptor socketLoggerInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/ws").setAllowedOrigins(applicationProperties.getSiteFrontendHost());
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry)
    {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)
    {
        registration.interceptors(
            socketLoggerInterceptor,
            jwtChannelInterceptor,
            new SecurityContextChannelInterceptor()
        );
    }
}
