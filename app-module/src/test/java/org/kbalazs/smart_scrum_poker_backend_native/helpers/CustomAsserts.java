package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.opentest4j.MultipleFailuresError;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomAsserts
{
    public static String UuidPattern = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    public static void softPokerAssert(Poker actual, Poker expected) throws MultipleFailuresError
    {
        assertAll(
            () -> assertThat(actual.id()).isEqualTo(expected.id()),
            () -> assertTrue(actual.publicId().toString().matches(UuidPattern)),
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.createdBy()).isNotNull(),
            () -> assertThat(actual.createdAt()).isAfter(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
        );
    }

    public static void softTicketAssert(Ticket actual, Ticket expected) throws MultipleFailuresError
    {
        assertAll(
            () -> assertThat(actual.id()).isEqualTo(expected.id()),
            () -> assertTrue(actual.publicId().toString().matches(UuidPattern)),
            () -> assertThat(actual.pokerId()).isEqualTo(expected.pokerId()),
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.isActive()).isEqualTo(expected.isActive())
        );
    }
}
