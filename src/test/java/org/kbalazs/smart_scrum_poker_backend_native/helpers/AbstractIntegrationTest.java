package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InGamePlayers;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUserSessions;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractIntegrationTest extends AbstractTest
{
    @Autowired
    private JooqService jooqService;

    protected final Poker pokerTable = Poker.POKER;
    protected final Ticket ticketTable = Ticket.TICKET;
    protected final Vote voteTable = Vote.VOTE;
    protected final InsecureUser insecureUserTable = InsecureUser.INSECURE_USER;
    protected final InGamePlayers inGamePlayersTable = InGamePlayers.IN_GAME_PLAYERS;
    protected final InsecureUserSessions insecureUserSessionsTable = InsecureUserSessions.INSECURE_USER_SESSIONS;

    protected DSLContext getDslContext()
    {
        return jooqService.getDbContext();
    }
}
