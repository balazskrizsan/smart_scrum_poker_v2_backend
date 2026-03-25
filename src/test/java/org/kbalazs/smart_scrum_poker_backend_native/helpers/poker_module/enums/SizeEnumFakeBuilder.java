package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeEnumFakeBuilder {
    public static final short defaultSize = 1;

    short size = defaultSize;

    @SneakyThrows
    public SizeEnum build() {
        SizeEnum mockEnum = mock(SizeEnum.class);
        when(mockEnum.val()).thenReturn(size);

        return mockEnum;
    }
}
