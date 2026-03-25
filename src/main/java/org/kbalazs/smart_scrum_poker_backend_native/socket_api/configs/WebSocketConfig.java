package org.kbalazs.smart_scrum_poker_backend_native.socket_api.configs;

import lombok.RequiredArgsConstructor;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.LocalDateTimeFactory;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserSessionsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
    private final ApplicationProperties applicationProperties;

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
}
