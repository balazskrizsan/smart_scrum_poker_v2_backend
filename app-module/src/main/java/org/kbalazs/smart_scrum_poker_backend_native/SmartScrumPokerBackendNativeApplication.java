package org.kbalazs.smart_scrum_poker_backend_native;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = "org.kbalazs")
@EnableMethodSecurity
public class SmartScrumPokerBackendNativeApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SmartScrumPokerBackendNativeApplication.class, args);
    }
}
