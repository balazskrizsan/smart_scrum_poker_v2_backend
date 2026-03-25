package org.kbalazs.smart_scrum_poker_backend_native.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.MDC;
import org.slf4j.Marker;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogbackMdcTurboFilter extends TurboFilter
{
    String serverEnv;
    LogBackState logBackState;

    public static final String LONG_TERM_MDC_NAME = "long_term";
    public static final String ENV_MDC_NAME = "env";

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable)
    {
        if (MDC.get(ENV_MDC_NAME) == null)
        {
            MDC.put(ENV_MDC_NAME, serverEnv);
        }

        MDC.put(LONG_TERM_MDC_NAME, String.valueOf(logBackState.getThreadLocalLongTermLogState().get()));

        return FilterReply.NEUTRAL;
    }
}
