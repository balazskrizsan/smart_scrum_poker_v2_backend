package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUserSessions;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserSessionFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert3SessionsFor2Users implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(InsecureUserSessions.INSECURE_USER_SESSIONS, new InsecureUserSessionFakeBuilder().build()).store();
        dslContext.newRecord(InsecureUserSessions.INSECURE_USER_SESSIONS, new InsecureUserSessionFakeBuilder().build2()).store();
        dslContext.newRecord(InsecureUserSessions.INSECURE_USER_SESSIONS, new InsecureUserSessionFakeBuilder().build3()).store();
    }
}
