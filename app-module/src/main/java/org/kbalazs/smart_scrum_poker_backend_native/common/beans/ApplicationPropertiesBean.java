package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationPropertiesBean
{
}
