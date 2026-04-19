package org.kbalazs.smart_scrum_poker_backend_native.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ApplicationProperties
{
    @Value("${server.port}")
    private String serverPort;

    @Value("${server.env}")
    private String serverEnv;

    @Value("${server.socket.full.host}")
    private String serverSocketFullHost;

    @Value("${site.frontend.host}")
    private String siteFrontendHost;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int dataSourceHikariMaximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int dataSourceHikariMinimumIdle;

    @Value("${socket.is-enabled-socket-connect-and-disconnect-listeners}")
    public boolean isEnabledSocketConnectAndDisconnectListeners;

    public String siteP12KeyStoreFilePath()
    {
        return "classpath:keystore/certificate.p12";
    }

    @Value("${server.ssl.key-store-type}")
    public String serverSslKeyStoreType;

    @Value("${server.ssl.key-store-password}")
    public String serverSslKeyStorePassword;

    @Value("${native.reflection-configuration-generator.enabled}")
    private boolean nativeReflectionConfigurationGeneratorEnabled;

    @Value("${socket.message-broker-stats-logging-period-seconds}")
    private long socketMessageBrokerStatsLoggingPeriodSeconds;

    @Value("${logback.logstash.enabled}")
    private boolean logbackLogstashEnabled;

    @Value("${logback.logstash.full_host}")
    private String logbackLogstashFullHost;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String oauth2JwtIssuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String oauth2JwtJwkSetUri;

    @Value("${ids.api.base-url}")
    private String idsApiBaseUrl;
}
