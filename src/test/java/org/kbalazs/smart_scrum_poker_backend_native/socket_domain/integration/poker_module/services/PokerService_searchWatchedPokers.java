package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InGamePlayerForPoker2;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2Poker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.PokerService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerService_searchWatchedPokers extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert2InsecureUser.class, Insert2Poker.class, Insert1InGamePlayerForPoker2.class})
    @SneakyThrows
    public void selectFilledDb_returnsSelectedPokerAsIdMap()
    {
        // Arrange
        UUID testedInsecureUserIdSecure = InsecureUserFakeBuilder.defaultIdSecure1;
        Map<UUID, Poker> expected = Map.of(PokerFakeBuilder.defaultIdSecure2, new PokerFakeBuilder().build2());

        // Act
        Map<UUID, Poker> actual = createInstance(PokerService.class).searchWatchedPokers(testedInsecureUserIdSecure);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
