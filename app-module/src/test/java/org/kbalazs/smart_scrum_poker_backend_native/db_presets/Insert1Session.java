package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.IdsUserSessions;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.UUID;

public class Insert1Session implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.insertInto(IdsUserSessions.IDS_USER_SESSIONS)
            .set(IdsUserSessions.IDS_USER_SESSIONS.IDS_USER_ID, VoteFakeBuilder.defaultCreatedBy)
            .set(IdsUserSessions.IDS_USER_SESSIONS.SESSION_ID, UUID.fromString("fb626462-e30e-edab-c536-b64b87b058be"))
            .set(IdsUserSessions.IDS_USER_SESSIONS.CREATED_AT, LocalDateTime.of(2020, 11, 22, 11, 22, 33))
            .execute();
    }
}
