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
                {"name": "Size S"},
                {"name": "Size M"},
                {"name": "Size L"},
                {"name": "Size XXL"}
            ]
            """;

        String defaultDimensionsConfig = """
            [
                {
                    "name": "Uncertainty",
                    "sizeValues": [
                        {"name": "Size S", "value": 1},
                        {"name": "Size M", "value": 2},
                        {"name": "Size L", "value": 3},
                        {"name": "Size XXL", "value": 10}
                    ]
                },
                {
                    "name": "Complexity",
                    "sizeValues": [
                        {"name": "Size S", "value": 1},
                        {"name": "Size M", "value": 2},
                        {"name": "Size L", "value": 3},
                        {"name": "Size XXL", "value": 10}
                    ]
                },
                {
                    "name": "Effort",
                    "sizeValues": [
                        {"name": "Size S", "value": 1},
                        {"name": "Size M", "value": 2},
                        {"name": "Size L", "value": 3},
                        {"name": "Size XXL", "value": 10}
                    ]
                },
                {
                    "name": "Risk",
                    "sizeValues": [
                        {"name": "Size S", "value": 1},
                        {"name": "Size M", "value": 2},
                        {"name": "Size L", "value": 3},
                        {"name": "Size XXL", "value": 10}
                    ]
                }
            ]
            """;

        String defaultPointsMapping = """
            [
                {"totalRange": [0, 3], "points": 1},
                {"totalRange": [4, 6], "points": 2},
                {"totalRange": [6, 8], "points": 3},
                {"totalRange": [9, 10], "points": 5},
                {"totalRange": [10, 12], "points": 8},
                {"totalRange": [13, 100], "points": 13}
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
