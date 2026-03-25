package org.kbalazs.smart_scrum_poker_backend_native.db_migrations;

import org.flywaydb.core.api.migration.Context;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.SQLDataType.*;

@Component
public class V000001__init extends AbstractBaseJooqMigration
{
    @Override
    public void migrate(Context context)
    {
        DSLContext dslContext = getDslContext(context);

        dslContext.createTable("poker")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("id_secure", UUID.nullable(false))
            .column("name", VARCHAR.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .column("created_by", UUID.nullable(true))
            .constraints(
                constraint("poker__pk___id").primaryKey("id"),
                constraint("poker__unique___id_secure").unique("id_secure")
            )
            .execute();

        dslContext.createTable("ticket")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("id_secure", UUID.nullable(false))
            .column("poker_id", BIGINT.nullable(false))
            .column("name", VARCHAR.nullable(false))
            .column("active", BOOLEAN.nullable(false).defaultValue(false))
            .constraints(
                constraint("ticket__pk___id").primaryKey("id"),
                constraint("ticket__unique___id_secure").unique("id_secure"),
                constraint("ticket__fk___poker_id___poker__id___on_delete_cascade")
                    .foreignKey("poker_id")
                    .references("poker", "id")
                    .onDeleteCascade()
            )
            .execute();

        dslContext.createTable("vote")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("ticket_id", BIGINT.nullable(false).identity(true))
            .column("uncertainty", SMALLINT.nullable(false))
            .column("complexity", SMALLINT.nullable(false))
            .column("effort", SMALLINT.nullable(false))
            .column("risk", SMALLINT.nullable(false))
            .column("calculated_point", SMALLINT.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .column("created_by", UUID.nullable(true))
            .constraints(
                constraint("vote__pk___id").primaryKey("id"),
                constraint("vote__unique___ticket_id___created_by").unique("ticket_id", "created_by"),
                constraint("vote_fk___ticket_id___ticket__id")
                    .foreignKey("ticket_id")
                    .references("ticket", "id")
                    .onDeleteCascade()
            )
            .execute();

        dslContext.createTable("insecure_user")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("id_secure", UUID.nullable(false))
            .column("user_name", VARCHAR.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("insecure_user__pk___id").primaryKey("id"),
                constraint("insecure_user__unique___id_secure").unique("id_secure")
            )
            .execute();

        dslContext.createTable("in_game_players")
            .column("insecure_user_id_secure", UUID.nullable(false))
            .column("poker_id_secure", UUID.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("in_game_players__pk___insecure_user_id_secure___poker_id_secure")
                    .primaryKey("insecure_user_id_secure", "poker_id_secure")
            )
            .execute();

        dslContext.createTable("insecure_user_sessions")
            .column("insecure_user_id_secure", UUID.nullable(false))
            .column("session_id", UUID.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("insecure_user_sessions__pk___insecure_user_id_secure___session_id")
                    .primaryKey("insecure_user_id_secure", "session_id"),
                constraint("insecure_user_sessions__unique___session_id").unique("session_id"),
                constraint("insecure_user_sessions__fk___insecure_user_id_secure___insecure_user__id_secure")
                    .foreignKey("insecure_user_id_secure")
                    .references("insecure_user", "id_secure")
            )
            .execute();
    }
}
