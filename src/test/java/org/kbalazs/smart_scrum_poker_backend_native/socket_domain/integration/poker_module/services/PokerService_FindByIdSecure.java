package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.PokerService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerService_FindByIdSecure extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2InsecureUser.class, Insert2Poker.class})
    @SneakyThrows
    public void SelectFromFilledDb_ReturnsOnlyOneEntity()
    {
        // Arrange
        UUID testedPokerInsecureId = PokerFakeBuilder.defaultIdSecure1;
        Poker expected = new PokerFakeBuilder().build();

        // Act
        Poker actual = createInstance(PokerService.class).findByIdSecure(testedPokerInsecureId);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
