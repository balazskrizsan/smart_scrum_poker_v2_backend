package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;

import java.util.Arrays;

public enum SizeEnum {
    S(1), M(2), L(3), XXL(10);

    final short value;

    SizeEnum(int v) {
        this.value = (short) v;
    }

    public short val() {
        return value;
    }

    public static SizeEnum of(short value) throws StoryPointException {
        return Arrays.stream(values())
            .filter(s -> s.value == value)
            .findFirst()
            .orElseThrow(
                () -> new StoryPointException("Invalid size value: " + value)
            );
    }
}
