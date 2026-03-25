package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3SessionsFor2Users;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InsecureUserService_searchUsersWithActiveSession extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2InsecureUser.class, Insert3SessionsFor2Users.class})
    @SneakyThrows
    public void selectFromInsecureUsersAndSessionTable_returnsUsersWithSession()
    {
        // Arrange
        List<UUID> testedInsecureUserIdSecures = List.of(
            InsecureUserFakeBuilder.defaultIdSecure2,
            InsecureUserFakeBuilder.defaultIdSecure2
        );
        List<InsecureUser> expected = new InsecureUserFakeBuilder().build2AsList();

        // Act
        List<InsecureUser> actual = createInstance(InsecureUserService.class)
            .searchUsersWithActiveSession(testedInsecureUserIdSecures);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SqlPreset(presets = {Insert3InsecureUser.class})
    @SneakyThrows
    public void selectUsersWithoutSession_returnsEmptyList()
    {
        // Arrange
        List<UUID> testedInsecureUserIdSecures = List.of(
            InsecureUserFakeBuilder.defaultIdSecure1,
            InsecureUserFakeBuilder.defaultIdSecure2
        );

        // Act
        List<InsecureUser> actual = createInstance(InsecureUserService.class)
            .searchUsersWithActiveSession(testedInsecureUserIdSecures);

        // Assert
        assertThat(actual.isEmpty()).isTrue();
    }
}
