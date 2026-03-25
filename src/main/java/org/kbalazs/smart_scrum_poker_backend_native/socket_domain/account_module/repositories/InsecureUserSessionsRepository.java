package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.InsecureUserSessionsRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InsecureUserSessionsRepository extends AbstractRepository
{
    public boolean create(@NonNull InsecureUserSession insecureUserSession)
    {
        DSLContext ctx = getDSLContext();
        Record1<UUID> returning = ctx
            .insertInto(insecureUserSessionsTable)
            .set(ctx.newRecord(insecureUserSessionsTable, insecureUserSession))
            .onDuplicateKeyIgnore()
            .returningResult(insecureUserSessionsTable.SESSION_ID)
            .fetchOne();

        return null != returning;
    }

    public int countByIdSecure(UUID uuidInsecureUserIdSecure)
    {
        var ctx = getDSLContext();

        return ctx.fetchCount(
            ctx.selectFrom(insecureUserSessionsTable)
                .where(insecureUserSessionsTable.INSECURE_USER_ID_SECURE.eq(uuidInsecureUserIdSecure))
        );
    }

    public void removeBySessionId(UUID sessionId)
    {
        getDSLContext()
            .deleteFrom(insecureUserSessionsTable)
            .where(insecureUserSessionsTable.SESSION_ID.eq(sessionId))
            .execute();
    }

    public InsecureUserSession getInsecureUserSession(UUID sessionId) throws SessionException
    {
        InsecureUserSessionsRecord insecureUserSessionRecord = getDSLContext()
            .selectFrom(insecureUserSessionsTable)
            .where(insecureUserSessionsTable.SESSION_ID.eq(sessionId))
            .fetchOne();

        // @todo: test
        if (null == insecureUserSessionRecord)
        {
            throw new SessionException("Session not found: session_id#" + sessionId);
        }

        return insecureUserSessionRecord.into(InsecureUserSession.class);
    }
}
