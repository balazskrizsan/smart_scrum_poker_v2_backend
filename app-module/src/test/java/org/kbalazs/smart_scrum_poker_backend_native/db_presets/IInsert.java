package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.jooq.DSLContext;

public interface IInsert
{
    void runParent();

    void run(DSLContext dslContext);
}
