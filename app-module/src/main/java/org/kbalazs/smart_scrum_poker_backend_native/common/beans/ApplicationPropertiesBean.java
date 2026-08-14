package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationPropertiesBean
{
    @Bean(name = "applicationProperties")
    public ApplicationProperties applicationProperties()
    {
        return new ApplicationProperties();
    }
}
