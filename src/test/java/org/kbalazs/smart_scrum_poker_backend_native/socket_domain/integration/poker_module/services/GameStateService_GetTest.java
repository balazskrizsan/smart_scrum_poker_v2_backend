package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert2Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3SessionsFor2Users;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert5VotesFor2Poker3Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.GameStateRequestFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.GameStateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.GameStateService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.GameStateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStateService_GetTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {
        Insert3InsecureUser.class,
        Insert3SessionsFor2Users.class,
        Insert2Poker.class,
        Insert5TicketsAllInactive.class,
        Insert5VotesFor2Poker3Ticket.class
    })
    @SneakyThrows
    public void RequestingAGameState_returnGameStateAndSetTheInGamePlayer()
    {
        // Arrange
        GameStateRequest testedGameStateRequest = new GameStateRequestFakeBuilder().build();

        GameStateResponse expected = new GameStateResponse(
            new PokerFakeBuilder().build(),
            new TicketFakeBuilder().build1to3AsList(),
            new InsecureUserFakeBuilder().buildAsList(),
            Map.of(
                TicketFakeBuilder.defaultId1, Map.of(
                    VoteFakeBuilder.defaultCreatedBy, new VoteFakeBuilder().build(),
                    VoteFakeBuilder.defaultCreatedBy2, new VoteFakeBuilder().build2()
                ),
                TicketFakeBuilder.defaultId2, Map.of(
                    VoteFakeBuilder.defaultCreatedBy3, new VoteFakeBuilder().build3(),
                    VoteFakeBuilder.defaultCreatedBy4, new VoteFakeBuilder().build4()
                ),
                TicketFakeBuilder.defaultId3, Map.of(
                    VoteFakeBuilder.defaultCreatedBy5, new VoteFakeBuilder().build5()
                )
            ),
            new InsecureUserFakeBuilder().build(),
            new InsecureUserFakeBuilder().build(),
            new InsecureUserFakeBuilder().buildAsList(),
            new HashMap<>()
            {{
                put(TicketFakeBuilder.defaultId1, new VotesWithVoteStat(
                    Map.of(
                        VoteFakeBuilder.defaultCreatedBy, new VoteFakeBuilder().build(),
                        VoteFakeBuilder.defaultCreatedBy2, new VoteFakeBuilder().build2()
                    ),
                    new VoteStat(5, (short) 5, (short) 5)
                ));
                put(TicketFakeBuilder.defaultId2, new VotesWithVoteStat(
                    Map.of(
                        VoteFakeBuilder.defaultCreatedBy3, new VoteFakeBuilder().build3(),
                        VoteFakeBuilder.defaultCreatedBy4, new VoteFakeBuilder().build4()
                    ),
                    new VoteStat(9, (short) 5, (short) 13)
                ));
                put(TicketFakeBuilder.defaultId3, new VotesWithVoteStat(
                    Map.of(VoteFakeBuilder.defaultCreatedBy5, new VoteFakeBuilder().build5()),
                    new VoteStat(5, (short) 5, (short) 5)
                ));
            }}
        );

        // Act
        GameStateResponse actual = createInstance(GameStateService.class).get(testedGameStateRequest);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
