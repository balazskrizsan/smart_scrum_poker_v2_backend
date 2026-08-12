package org.kbalazs.smart_scrum_poker_backend_native.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class ApplicationProperties
{
    private String serverPort;

    private String serverEnv;

    private String springApplicationName;

    private String serverSocketFullHost;

    private String siteFrontendHost;

    private String dataSourceUrl;

    private String dataSourceUsername;

    private String dataSourcePassword;

    private String driverClassName;

    private int dataSourceHikariMaximumPoolSize;

    private int dataSourceHikariMinimumIdle;

    public boolean isEnabledSocketConnectAndDisconnectListeners;

    public String siteP12KeyStoreFilePath()
    {
        return "classpath:keystore/certificate.p12";
    }

    public String serverSslKeyStoreType;

    public String serverSslKeyStorePassword;

    private boolean nativeReflectionConfigurationGeneratorEnabled;

    private long socketMessageBrokerStatsLoggingPeriodSeconds;

    private boolean logbackLogstashEnabled;

    private String logbackLogstashFullHost;

    private String logbackLogType;

    private String oauth2JwtIssuerUri;

    private String oauth2JwtJwkSetUri;

    private String idsApiBaseUrl;
}
