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

        dslContext.createTable("ids_user")
            .column("id", UUID.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("insecure_user__unique___id").unique("id")
            )
            .execute();

        dslContext.createTable("poker")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("public_id", UUID.nullable(false))
            .column("name", VARCHAR.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .column("created_by", UUID.nullable(false))
            .constraints(
                constraint("poker__pk___id").primaryKey("id"),
                constraint("poker__unique___public_id").unique("public_id"),
                constraint("poker__fk___created_by___ids_user__id___on_delete_cascade")
                    .foreignKey("created_by")
                    .references("ids_user", "id")
                    .onDeleteCascade()
            )
            .execute();

        dslContext.createTable("ticket")
            .column("id", BIGINT.nullable(false))
            .column("public_id", UUID.nullable(false))
            .column("poker_id", BIGINT.nullable(false))
            .column("name", VARCHAR.nullable(false))
            .column("active", BOOLEAN.nullable(false).defaultValue(false))
            .constraints(
                constraint("ticket__pk___id").primaryKey("id"),
                constraint("ticket__unique___public_id").unique("public_id"),
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
            .column("created_by", UUID.nullable(false))
            .constraints(
                constraint("vote__pk___id").primaryKey("id"),
                constraint("vote__unique___ticket_id___created_by").unique("ticket_id", "created_by"),
                constraint("vote__fk___ticket_id___ticket__id___on_delete_cascade")
                    .foreignKey("ticket_id")
                    .references("ticket", "id")
                    .onDeleteCascade(),
                constraint("vote__fk___created_by___ids_user__id___on_delete_cascade")
                    .foreignKey("created_by")
                    .references("ids_user", "id")
                    .onDeleteCascade()
            )
            .execute();

        dslContext.createTable("in_poker_ids_users")
            .column("ids_user_id", UUID.nullable(false))
            .column("poker_id", BIGINT.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("in_poker_ids_users__pk___ids_user_id___poker_id")
                    .primaryKey("ids_user_id", "poker_id"),
                constraint("in_poker_ids_users__fk___ids_user_id___ids_user__id___on_delete_cascade")
                    .foreignKey("ids_user_id")
                    .references("ids_user", "id")
                    .onDeleteCascade(),
                constraint("in_poker_ids_users__fk___poker_id___poker__id___on_delete_cascade")
                    .foreignKey("poker_id")
                    .references("poker", "id")
                    .onDeleteCascade()
            )
            .execute();

        dslContext.createTable("ids_user_sessions")
            .column("ids_user_id", UUID.nullable(false))
            .column("session_id", UUID.nullable(false))
            .column("created_at", TIMESTAMP.nullable(false))
            .constraints(
                constraint("ids_user_sessions__pk___ids_user_id___session_id")
                    .primaryKey("ids_user_id", "session_id"),
                constraint("ids_user_sessions__unique___session_id").unique("session_id"),
                constraint("ids_user_sessions__fk___ids_user_id___ids_user__id")
                    .foreignKey("ids_user_id")
                    .references("ids_user", "id")
            )
            .execute();
    }
}
