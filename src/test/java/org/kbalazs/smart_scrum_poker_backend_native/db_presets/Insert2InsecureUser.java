package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert2InsecureUser implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(InsecureUser.INSECURE_USER, new InsecureUserFakeBuilder().build()).store();
        dslContext.newRecord(InsecureUser.INSECURE_USER, new InsecureUserFakeBuilder().build2()).store();
    }
}
