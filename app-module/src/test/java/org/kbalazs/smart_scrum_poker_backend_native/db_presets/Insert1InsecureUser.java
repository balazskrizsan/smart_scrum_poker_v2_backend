package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IDS_USER;

public class Insert1InsecureUser implements IInsert
{
    @Override
    public void runParent()
    {

    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        var user = new IdsUserFakeBuilder().build();
        dslContext.insertInto(IdsUser.IDS_USER)
            .set(IDS_USER.ID, user.id())
            .set(IDS_USER.CREATED_AT, user.createdAt())
            .execute();
    }
}
