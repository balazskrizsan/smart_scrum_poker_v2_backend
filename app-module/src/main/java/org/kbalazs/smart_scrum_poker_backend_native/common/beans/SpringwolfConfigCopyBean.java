package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpringwolfConfigCopyBean implements ApplicationRunner
{
    private static final Logger logger = LoggerFactory.getLogger(SpringwolfConfigCopyBean.class);
    private static final String SPRINGWOLFF_PATH = "/springwolf/docs";

    ApplicationProperties applicationProperties;

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Override
    public void run(ApplicationArguments args) throws IOException
    {
        RestTemplate restTemplate = restTemplate();
        Path targetDir = Paths.get("app-module/src/main/resources/springwolff").toAbsolutePath();
        Path targetPath = targetDir.resolve("springwolff.json");

        String springwolfUrl = applicationProperties.getServerFullHost() + SPRINGWOLFF_PATH;

        try
        {
            logger.info("Downloading Springwolf config from: {}", springwolfUrl);
            String jsonContent = restTemplate.getForObject(springwolfUrl, String.class);

            if (jsonContent == null || jsonContent.isEmpty())
            {
                logger.warn("Received empty response from Springwolf URL");
                return;
            }

            if (!Files.exists(targetDir))
            {
                Files.createDirectories(targetDir);
                logger.info("Created directory: {}", targetDir);
            }

            Files.writeString(targetPath, jsonContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            logger.info("Springwolf config saved to: {}", targetPath);
        }
        catch (Exception e)
        {
            logger.error("Failed to download or save Springwolf config: {}", e.getMessage(), e);
        }
    }
}
