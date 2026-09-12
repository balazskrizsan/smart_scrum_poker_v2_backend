package org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession;

import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IdsUserSessionFakeBuilder
{
    public static final UUID defaultId1 = UUID.fromString("0f48291f-c079-4b10-a6d3-11b029c8d03a");
    public static final UUID defaultSessionId1 = UUID.fromString("fb626462-e30e-edab-c536-b64b87b058be");

    UUID idsUserId = defaultId1;
    UUID sessionId = defaultSessionId1;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);

    public IdsUserSession build()
    {
        return new IdsUserSession(idsUserId, sessionId, createdAt);
    }
}
