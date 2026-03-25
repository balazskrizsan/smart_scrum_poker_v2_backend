package org.kbalazs.smart_scrum_poker_backend_native.socket_api.configs;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Configuration
@RequiredArgsConstructor
public class WebSocketLogConfig
{
    private final WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    @Value("${socket.message-broker-stats-logging-period-seconds}")
    private long socketMessageBrokerStatsLoggingPeriodSeconds; // @todo: load by ApplicationProperties

    @PostConstruct
    public void init()
    {
        webSocketMessageBrokerStats.setLoggingPeriod(socketMessageBrokerStatsLoggingPeriodSeconds * 60000);
    }
}
