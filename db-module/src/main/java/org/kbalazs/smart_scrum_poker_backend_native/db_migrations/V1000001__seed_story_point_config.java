package org.kbalazs.smart_scrum_poker_backend_native.db_migrations;

import org.flywaydb.core.api.migration.Context;
import org.jooq.DSLContext;

public class V1000001__seed_story_point_config extends AbstractBaseJooqMigration
{
    @Override
    public void migrate(Context context)
    {
        DSLContext dslContext = getDslContext(context);

        String defaultSizesConfig = """
            [
                {"name": "S", "value": 1},
                {"name": "M", "value": 2},
                {"name": "L", "value": 3},
                {"name": "XXL", "value": 10}
            ]
            """;

        String defaultDimensionsConfig = """
            [
                {
                    "name": "uncertainty",
                    "sizeValues": {"S": 1, "M": 2, "L": 3, "XXL": 10}
                },
                {
                    "name": "complexity",
                    "sizeValues": {"S": 1, "M": 2, "L": 3, "XXL": 10}
                },
                {
                    "name": "effort",
                    "sizeValues": {"S": 1, "M": 2, "L": 3, "XXL": 10}
                },
                {
                    "name": "risk",
                    "sizeValues": {"S": 1, "M": 2, "L": 3, "XXL": 10}
                }
            ]
            """;

        String defaultPointsMapping = """
            [
                {"totalRange": [4, 4], "points": 1},
                {"totalRange": [5, 5], "points": 2},
                {"totalRange": [6, 7], "points": 3},
                {"totalRange": [8, 9], "points": 5},
                {"totalRange": [10, 11], "points": 8},
                {"totalRange": [12, 12], "points": 13},
                {"totalRange": [13, 20], "points": 20},
                {"totalRange": [21, 30], "points": 50},
                {"totalRange": [31, 2147483647], "points": 100}
            ]
            """;

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.util.UUID createdBy = java.util.UUID.fromString("10000000-0000-0000-0000-000000000001");

        dslContext.execute(
            "INSERT INTO ids_user (id, created_at) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
            createdBy,
            now
        );

        dslContext.execute("""
                INSERT INTO story_point_config (name, sizes_config, dimensions_config, points_mapping, created_at, created_by)
                VALUES (?, ?::jsonb, ?::jsonb, ?::jsonb, ?, ?)
                """,
            "Default Config",
            defaultSizesConfig,
            defaultDimensionsConfig,
            defaultPointsMapping,
            now,
            createdBy
        );
    }
}
