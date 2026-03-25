package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.account_service;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3SessionsFor2Users;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserSessionsService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InsecureUserSessionsService_countByIdSecureTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2InsecureUser.class, Insert3SessionsFor2Users.class})
    @SneakyThrows
    public void countSessionsUserFromFilledDb_returnsUserSessionCountNumber()
    {
        // Arrange
        UUID testedInsecureUserIdSecure = InsecureUserFakeBuilder.defaultIdSecure1;
        int expectedRecords = 2;

        // Act
        int actual = createInstance(InsecureUserSessionsService.class).countByIdSecure(testedInsecureUserIdSecure);

        // Assert
        assertThat(actual).isEqualTo(expectedRecords);
    }
}
