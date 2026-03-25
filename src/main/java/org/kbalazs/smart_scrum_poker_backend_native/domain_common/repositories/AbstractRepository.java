package org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories;


import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InGamePlayers;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUserSessions;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
abstract public class AbstractRepository
{
    private JooqService jooqService;

    protected final InsecureUser insecureUserTable = InsecureUser.INSECURE_USER;
    protected final InsecureUserSessions insecureUserSessionsTable = InsecureUserSessions.INSECURE_USER_SESSIONS;
    protected final InGamePlayers inGamePlayersTable = InGamePlayers.IN_GAME_PLAYERS;
    protected final Vote voteTable = Vote.VOTE;
    protected final Ticket ticketTable = Ticket.TICKET;
    protected final Poker pokersTable = Poker.POKER;

    @Autowired
    public void setJooqService(JooqService jooqService)
    {
        this.jooqService = jooqService;
    }

    protected DSLContext getDSLContext()
    {
        return jooqService.getDbContext();
    }
}
