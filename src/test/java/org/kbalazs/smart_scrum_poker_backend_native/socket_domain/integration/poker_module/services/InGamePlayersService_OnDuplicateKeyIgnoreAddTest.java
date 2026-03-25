package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.InGamePlayerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InGamePlayer;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.InGamePlayersService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class InGamePlayersService_OnDuplicateKeyIgnoreAddTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1Poker.class})
    @SneakyThrows
    public void AddOnePlayerToTheGame_DbRecordCreates()
    {
        // Arrange
        InGamePlayer testedInGamePlayer = new InGamePlayerFakeBuilder().build();
        InGamePlayer expectedInGamePlayer = new InGamePlayerFakeBuilder().build();

        // Act
        createInstance(InGamePlayersService.class).onDuplicateKeyIgnoreAdd(testedInGamePlayer);

        // Assert
        InGamePlayer actualGamePlayers = getDslContext().selectFrom(inGamePlayersTable).fetchOneInto(InGamePlayer.class);

        assertThat(actualGamePlayers).isEqualTo(expectedInGamePlayer);
    }

    @Test
    @SqlPreset(presets = {Insert1Poker.class, Insert3TicketsAllInactive.class})
    @SneakyThrows
    public void AddSamePlayerTwiceToTheGame_IgnoreTheSecond()
    {
        // Arrange
        InGamePlayer testedInGamePlayer1 = new InGamePlayerFakeBuilder().build();
        InGamePlayer testedInGamePlayer2 = new InGamePlayerFakeBuilder()
            .createdAt(LocalDateTime.of(2040, 11, 22, 11, 22, 33))
            .build();
        InGamePlayer expectedInGamePlayer = new InGamePlayerFakeBuilder().build();

        // Act
        InGamePlayersService service = createInstance(InGamePlayersService.class);
        service.onDuplicateKeyIgnoreAdd(testedInGamePlayer1);
        service.onDuplicateKeyIgnoreAdd(testedInGamePlayer2);

        // Assert
        InGamePlayer actualGamePlayers = getDslContext().selectFrom(inGamePlayersTable).fetchOneInto(InGamePlayer.class);

        assertThat(actualGamePlayers).isEqualTo(expectedInGamePlayer);
    }
}
