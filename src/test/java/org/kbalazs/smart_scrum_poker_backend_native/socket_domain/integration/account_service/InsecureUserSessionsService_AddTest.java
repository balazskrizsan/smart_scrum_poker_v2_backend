package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.account_service;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserSessionFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserSessionsService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class InsecureUserSessionsService_AddTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class})
    @SneakyThrows
    public void addTwoItemsWithOnDuplicateKeyIgnore_createsOneRecord()
    {
        // Arrange
        InsecureUserSession testedInsecureUserSession = new InsecureUserSessionFakeBuilder().build();
        List<InsecureUserSession> expectedInsecureUserSession = new InsecureUserSessionFakeBuilder().buildAsList();

        // Act
        InsecureUserSessionsService service = createInstance(InsecureUserSessionsService.class);
        boolean actualHasInsert1 = service.add(testedInsecureUserSession);
        boolean actualHasInsert2 = service.add(testedInsecureUserSession);

        // Assert
        List<InsecureUserSession> actual = getDslContext()
            .selectFrom(insecureUserSessionsTable)
            .fetchInto(InsecureUserSession.class);

        assertAll(
            () -> assertThat(actual).isEqualTo(expectedInsecureUserSession),
            () -> assertThat(actualHasInsert1).isEqualTo(true),
            () -> assertThat(actualHasInsert2).isEqualTo(false)
        );
    }
}
