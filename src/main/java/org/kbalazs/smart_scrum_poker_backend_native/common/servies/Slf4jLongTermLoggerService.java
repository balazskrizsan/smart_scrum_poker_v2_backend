package org.kbalazs.smart_scrum_poker_backend_native.common.servies;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kbalazs.smart_scrum_poker_backend_native.config.LogBackState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Slf4jLongTermLoggerService
{
    private final LogBackState logBackState;

    // @todo: possible memory leak, time limit should be on the class
    private Map<String, Logger> loggers = new HashMap<>();

    public void info(@NonNull Class<?> clazz, @NonNull String message, @Nullable Object... args)
    {
        Logger logger = loggers.computeIfAbsent(clazz.getName(), k -> LoggerFactory.getLogger(clazz));

        try
        {
            logBackState.setThreadLocalLongTermLogState(true);
            logger.info(message, args);
        }
        finally
        {
            logBackState.setThreadLocalLongTermLogState(false);
        }
    }
}
