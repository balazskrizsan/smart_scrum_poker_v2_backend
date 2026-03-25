package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.account_service;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3SessionsFor2Users;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserSessionFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserSessionsService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InsecureUserSessionsService_getInsecureUserSessionTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2InsecureUser.class, Insert3SessionsFor2Users.class})
    @SneakyThrows
    public void selectFromFilledDb_returnSelectedInsecureUserSession() throws SessionException
    {
        // Arrange
        UUID testedSessionId = InsecureUserSessionFakeBuilder.defaultSessionId;
        InsecureUserSession expected = new InsecureUserSessionFakeBuilder().build();

        // Act
        InsecureUserSession actual = createInstance(InsecureUserSessionsService.class)
            .getInsecureUserSession(testedSessionId);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
