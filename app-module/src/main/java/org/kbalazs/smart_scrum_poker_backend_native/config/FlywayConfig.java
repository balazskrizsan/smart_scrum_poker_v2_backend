package org.kbalazs.smart_scrum_poker_backend_native.config;

import org.kbalazs.smart_scrum_poker_backend_native.db_migrations.V000001__init;
import org.kbalazs.smart_scrum_poker_backend_native.db_migrations.V000002__add_story_point_config;
import org.kbalazs.smart_scrum_poker_backend_native.db_migrations.V1000001__seed_story_point_config;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig
{
    private static boolean isNativeImage()
    {
        return System.getProperty("org.graalvm.nativeimage.imagecode") != null;
    }

    @Bean
    public FlywayConfigurationCustomizer dbModuleJavaMigrationsCustomizer()
    {
        return configuration ->
        {
            if (isNativeImage())
            {
                // @todo: needs to be loaded from classpath without one by one
                configuration.javaMigrations(
                    new V000001__init(),
                    new V000002__add_story_point_config(),
                    new V1000001__seed_story_point_config()
                );
            }
        };
    }
}
