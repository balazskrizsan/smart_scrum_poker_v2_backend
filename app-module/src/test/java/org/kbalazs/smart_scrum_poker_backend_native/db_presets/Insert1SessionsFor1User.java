package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.IdsUserSessions;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserSessionFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IDS_USER_SESSIONS;

public class Insert1SessionsFor1User implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        var session = new IdsUserSessionFakeBuilder().build();
        dslContext.insertInto(IdsUserSessions.IDS_USER_SESSIONS)
            .set(IDS_USER_SESSIONS.IDS_USER_ID, session.idsUserId())
            .set(IDS_USER_SESSIONS.SESSION_ID, session.sessionId())
            .set(IDS_USER_SESSIONS.CREATED_AT, session.createdAt())
            .execute();
    }
}
