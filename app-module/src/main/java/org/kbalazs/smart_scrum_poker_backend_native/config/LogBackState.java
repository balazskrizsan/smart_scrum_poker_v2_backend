package org.kbalazs.smart_scrum_poker_backend_native.config;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LogBackState
{
    ThreadLocal<String> threadLocalLongTermLogState = ThreadLocal.withInitial(() -> "false");

    public void setThreadLocalLongTermLogState(boolean state)
    {
        this.threadLocalLongTermLogState.set(String.valueOf(state));
    }
}
