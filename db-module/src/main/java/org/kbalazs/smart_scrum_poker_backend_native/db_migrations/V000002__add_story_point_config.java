package org.kbalazs.smart_scrum_poker_backend_native.db_migrations;

import org.flywaydb.core.api.migration.Context;
import org.jooq.DSLContext;

import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.SQLDataType.*;

public class V000002__add_story_point_config extends AbstractBaseJooqMigration
{
    @Override
    public void migrate(Context context)
    {
        DSLContext dslContext = getDslContext(context);

        // Create story_point_config table with JSONB for dynamic configuration
        dslContext.createTable("story_point_config")
            .column("id", BIGINT.nullable(false).identity(true))
            .column("name", VARCHAR.nullable(false))
            .column("sizes_config", JSONB.nullable(false)) // [{"name": "small", "value": 1}, {"name": "medium", "value": 2}, ...]
            .column("dimensions_config", JSONB.nullable(false)) // [{"name": "risk", "sizeValues": {"small": 1, "medium": 2, ...}}, ...]
            .column("points_mapping", JSONB.nullable(false)) // [{"totalRange": [4, 5], "points": 1}, ...]
            .column("created_at", TIMESTAMP.nullable(false))
            .column("created_by", UUID.nullable(false))
            .constraints(
                constraint("story_point_config__pk___id").primaryKey("id"),
                constraint("story_point_config__fk___created_by___ids_user__id___on_delete_cascade")
                    .foreignKey("created_by")
                    .references("ids_user", "id")
                    .onDeleteCascade()
            )
            .execute();

        // Add story_point_config_id to poker table
        dslContext.alterTable("poker")
            .addColumn("story_point_config_id", BIGINT.nullable(true))
            .execute();

        dslContext.alterTable("poker").add(
                constraint("poker__fk___story_point_config_id___story_point_config__id___on_delete_set_null")
                    .foreignKey("story_point_config_id")
                    .references("story_point_config", "id")
                    .onDeleteCascade()
            )
            .execute();

        // Add story_point_config_id to vote table
        dslContext.alterTable("vote")
            .addColumn("story_point_config_id", BIGINT.nullable(true))
            .execute();

        dslContext.alterTable("vote")
            .add(
                constraint("vote__fk___story_point_config_id___story_point_config__id___on_delete_set_null")
                    .foreignKey("story_point_config_id")
                    .references("story_point_config", "id")
                    .onDeleteSetNull()
            )
            .execute();

        // Add vote_values JSONB column to vote table for dynamic values
        dslContext.alterTable("vote")
            .addColumn("vote_values", JSONB.nullable(true))
            .execute();

        // Drop old hardcoded columns from vote table
        dslContext.alterTable("vote")
            .dropColumn("uncertainty")
            .execute();

        dslContext.alterTable("vote")
            .dropColumn("complexity")
            .execute();

        dslContext.alterTable("vote")
            .dropColumn("effort")
            .execute();

        dslContext.alterTable("vote")
            .dropColumn("risk")
            .execute();

        dslContext.alterTable("vote")
            .dropColumn("calculated_point")
            .execute();
    }
}
