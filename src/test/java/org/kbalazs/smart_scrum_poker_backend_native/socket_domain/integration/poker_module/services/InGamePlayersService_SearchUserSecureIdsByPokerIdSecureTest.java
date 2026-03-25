package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InGamePlayer;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.InGamePlayerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InGamePlayer;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.InGamePlayersService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InGamePlayersService_SearchUserSecureIdsByPokerIdSecureTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1InGamePlayer.class})
    @SneakyThrows
    public void searchFromOneInsertedRecord_returnsOneElementInList()
    {
        // Arrange
        UUID testedPokerIdSecure = PokerFakeBuilder.defaultIdSecure1;
        List<InGamePlayer> expectedInGamePlayer = new InGamePlayerFakeBuilder().buildAsList();

        // Act
        List<InGamePlayer> actualInGamePlayers = createInstance(InGamePlayersService.class)
            .searchUserSecureIdsByPokerIdSecure(testedPokerIdSecure);

        // Assert
        assertThat(actualInGamePlayers).isEqualTo(expectedInGamePlayer);
    }
}
