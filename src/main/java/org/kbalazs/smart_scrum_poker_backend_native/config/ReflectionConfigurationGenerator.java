package org.kbalazs.smart_scrum_poker_backend_native.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAwareBase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.common.io_module.services.FileService;
import org.kbalazs.common.native_build_module.exceptions.RuntimeHintsReflection;
import org.kbalazs.common.native_build_module.services.RuntimeHintsReflectionGenerator;
import org.kbalazs.common.templating_module.services.MustacheService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ReflectionConfigurationGenerator
{
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void generate() throws RuntimeHintsReflection, IOException
    {
        boolean isNativeReflectionConfigurationGeneratorEnabled = applicationProperties
            .isNativeReflectionConfigurationGeneratorEnabled();

        log.info("ReflectionConfigurationGenerator status: {}", isNativeReflectionConfigurationGeneratorEnabled);

        if (!isNativeReflectionConfigurationGeneratorEnabled)
        {
            return;
        }

        new RuntimeHintsReflectionGenerator(new MustacheService(), new FileService())
            .generate(
                "src/main/java/org/kbalazs/smart_scrum_poker_backend_native/config/ReflectionConfiguration.java",
                List.of(
                    "org.kbalazs.smart_scrum_poker_backend_native.db.tables.records",
                    "org.kbalazs.smart_scrum_poker_backend_native.api.value_objects",
                    "org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities",
                    "org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects",
                    "org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities",
                    "org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects",
                    "org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker"
                ),
                List.of(
                    ResponseEntity.class,
                    LoggerContext.class,
                    ContextAwareBase.class
                )
            );
    }
}
