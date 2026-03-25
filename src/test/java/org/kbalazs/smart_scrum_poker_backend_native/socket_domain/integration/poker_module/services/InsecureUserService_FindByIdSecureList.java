package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InsecureUserService_FindByIdSecureList extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert3InsecureUser.class})
    @SneakyThrows
    public void ThreeUsersDbPresetReadOneByIdSecureList_returnsOneInList()
    {
        // Arrange
        List<UUID> testedIdScecureList = new ArrayList<>()
        {{
            add(InsecureUserFakeBuilder.defaultIdSecure2);
        }};
        List<InsecureUser> expectedInsecureUsers = new InsecureUserFakeBuilder().build2AsList();


        // Act
        List<InsecureUser> actual = createInstance(InsecureUserService.class).findByIdSecureList(testedIdScecureList);

        // Assert
        assertThat(actual).isEqualTo(expectedInsecureUsers);
    }
}
