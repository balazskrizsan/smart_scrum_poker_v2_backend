package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserSessionsRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUserSession;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.SessionException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IDS_USER_SESSIONS;

@Repository
public class IdsUserSessionsRepository extends AbstractRepository
{
    public boolean create(@NonNull IdsUserSession idsUserSession)
    {
        DSLContext ctx = getDSLContext();
        Record1<UUID> returning = ctx
            .insertInto(IDS_USER_SESSIONS)
            .set(ctx.newRecord(IDS_USER_SESSIONS, idsUserSession))
            .onDuplicateKeyIgnore()
            .returningResult(IDS_USER_SESSIONS.SESSION_ID)
            .fetchOne();

        return null != returning;
    }

    public int countByIdSecure(UUID idsUserId)
    {
        var ctx = getDSLContext();

        return ctx.fetchCount(
            ctx.selectFrom(IDS_USER_SESSIONS)
                .where(IDS_USER_SESSIONS.IDS_USER_ID.eq(idsUserId))
        );
    }

    public void removeBySessionId(UUID sessionId)
    {
        getDSLContext()
            .deleteFrom(IDS_USER_SESSIONS)
            .where(IDS_USER_SESSIONS.SESSION_ID.eq(sessionId))
            .execute();
    }

    public IdsUserSession getInsecureUserSession(UUID sessionId)
        throws SessionException
    {
        IdsUserSessionsRecord idsUserSessionRecord = getDSLContext()
            .selectFrom(IDS_USER_SESSIONS)
            .where(IDS_USER_SESSIONS.SESSION_ID.eq(sessionId))
            .fetchOne();

        if (null == idsUserSessionRecord)
        {
            throw new SessionException("Session not found: session_id#" + sessionId);
        }

        return idsUserSessionRecord.into(IdsUserSession.class);
    }
}
