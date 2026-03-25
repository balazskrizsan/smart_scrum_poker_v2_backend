package org.kbalazs.smart_scrum_poker_backend_native.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LogbackConfig {
    ApplicationProperties applicationProperties;
    LogBackState logBackState;

    @PostConstruct
    public void setupLogger() {
        String currentEnv = applicationProperties.getServerEnv();
        log.info(
            "LogbackConfig setup / logstash enabled: {}, env: {}, url: {}",
            applicationProperties.isLogbackLogstashEnabled(),
            currentEnv,
            applicationProperties.getLogbackLogstashFullHost()
        );

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        context.addTurboFilter(new LogbackMdcTurboFilter(currentEnv, logBackState));

        ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();
        rootLogger.setLevel(Level.INFO);
        if (applicationProperties.isLogbackLogstashEnabled()) {
            rootLogger.addAppender(getLogstashTcpSocketAppender(context));
        }

        rootLogger.addAppender(getLoggingEventConsoleAppender(context));

        log.info("LogbackConfig setup");
    }

    private @NonNull ConsoleAppender<ILoggingEvent> getLoggingEventConsoleAppender(@NonNull LoggerContext context) {
        log.info("LogbackConfig console created");

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(context);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%highlight(%d [%thread]) %green([env=%X{env}] [long_term=%X{long_term}]) %highlight(%-5level) %cyan(%logger{35}) - %msg%n");
        encoder.setCharset(java.nio.charset.StandardCharsets.UTF_8);
        encoder.start();

        appender.setEncoder(encoder);
        appender.start();

        return appender;
    }

    private LogstashTcpSocketAppender getLogstashTcpSocketAppender(@NonNull LoggerContext context) {
        log.info("LogbackConfig logstash created");

        LogstashTcpSocketAppender appender = new LogstashTcpSocketAppender();
        appender.setContext(context);
        appender.addDestination(applicationProperties.getLogbackLogstashFullHost());

        appender.setEncoder(new LogstashEncoder());
        appender.start();

        return appender;
    }
}
