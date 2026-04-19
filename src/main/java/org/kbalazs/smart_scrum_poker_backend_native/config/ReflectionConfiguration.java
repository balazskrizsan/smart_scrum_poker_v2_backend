package org.kbalazs.smart_scrum_poker_backend_native.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_FIELDS;

@Configuration
@RegisterReflectionForBinding({
    org.springframework.http.ResponseEntity.class,
    ch.qos.logback.classic.LoggerContext.class,
    ch.qos.logback.core.spi.ContextAwareBase.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.FlywaySchemaHistoryRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserSessionsRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.InPokerIdsUsersRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.PokerRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.TicketRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.VoteRecord.class,
    org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.ConnectResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.DisconnectResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InPokerIdsUser.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.AddTicket.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPoker.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPokerResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StateRequest.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoterLeaving.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.AddTicketResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.MyPokersResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.RoundStartResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.SessionResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketClosed.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketDeleteResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketOpened.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteNewJoinerResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoterLeavingResponse.class,
    org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteStopResponse.class,
})
@ImportRuntimeHints(ReflectionConfiguration.AppRuntimeHintsRegistrar.class)
public class ReflectionConfiguration
{
    public static class AppRuntimeHintsRegistrar implements RuntimeHintsRegistrar
    {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader)
        {
            hints.reflection()
                .registerType(org.springframework.http.ResponseEntity.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(ch.qos.logback.classic.LoggerContext.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(ch.qos.logback.core.spi.ContextAwareBase.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.FlywaySchemaHistoryRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserSessionsRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.InPokerIdsUsersRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.PokerRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.TicketRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.VoteRecord.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.ConnectResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.common_module.value_objects.DisconnectResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InPokerIdsUser.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.AddTicket.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPoker.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPokerResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StateRequest.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoterLeaving.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteStat.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VotesWithVoteStat.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.AddTicketResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.MyPokersResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.RoundStartResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.SessionResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StartResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.StateResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketClosed.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketDeleteResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.TicketOpened.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteNewJoinerResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoterLeavingResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
                .registerType(org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteStopResponse.class, PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS)
            ;
        }
    }
}
