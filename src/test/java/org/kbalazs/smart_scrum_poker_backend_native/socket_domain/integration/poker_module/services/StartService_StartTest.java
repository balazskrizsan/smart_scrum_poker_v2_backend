package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.PokerRecord;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.common_module.mocker.UuidServiceMocker;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.StartService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.TicketService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPokerResponse;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StartService_StartTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class})
    @SneakyThrows
    public void insertValidStartData_createsPokerInDb()
    {
        // Arrange
        Poker testedPoker = new PokerFakeBuilder().buildNoId();
        List<Ticket> testedTickets = new TicketFakeBuilder().buildNoIdAsList();

        Poker expectedPoker = new PokerFakeBuilder().id(1L).build();
        Ticket expectedTicket = new TicketFakeBuilder().id(1).pokerId(1).build();

        var ticketUuidServiceMock = new UuidServiceMocker().mockGetRandom(TicketFakeBuilder.defaultIdSecure1).getMock();
        setOneTimeMock(TicketService.class, ticketUuidServiceMock);

        var pokerUuidServiceMock = new UuidServiceMocker().mockGetRandom(PokerFakeBuilder.defaultIdSecure1).getMock();

        // Act
        StartPokerResponse actualStartPokerResponse = createInstance(StartService.class, pokerUuidServiceMock)
            .start(testedPoker, testedTickets);

        // Assert
        PokerRecord actualPokerRecord = getDslContext().selectFrom(pokerTable).fetchOne();
        Poker actualPoker = actualPokerRecord.into(Poker.class);

        Ticket actualTicketRow = getDslContext().selectFrom(ticketTable).fetchOne().into(Ticket.class);

        assertAll(
            () -> assertThat(actualStartPokerResponse.poker()).isEqualTo(actualPoker),
            () -> assertThat(actualPoker).isEqualTo(expectedPoker),
            () -> assertThat(actualTicketRow).isEqualTo(expectedTicket)
        );
    }

    @Test
    @SqlPreset()
    @SneakyThrows
    public void insertWithoutDbUser_throwsAccountException()
    {
        // Arrange
        Poker testedPoker = new PokerFakeBuilder().buildNoId();
        List<Ticket> testedTickets = new TicketFakeBuilder().buildNoIdAsList();

        var ticketUuidServiceMock = new UuidServiceMocker().mockGetRandom(TicketFakeBuilder.defaultIdSecure1).getMock();
        setOneTimeMock(TicketService.class, ticketUuidServiceMock);

        var pokerUuidServiceMock = new UuidServiceMocker().mockGetRandom(PokerFakeBuilder.defaultIdSecure1).getMock();

        // Act - Assert
        assertThatThrownBy(() -> createInstance(StartService.class, pokerUuidServiceMock)
            .start(testedPoker, testedTickets)
        )
            .isInstanceOf(AccountException.class)
            .hasMessage("User not found; idSecure#10000000-0000-0000-0000-000000002001");
    }
}
