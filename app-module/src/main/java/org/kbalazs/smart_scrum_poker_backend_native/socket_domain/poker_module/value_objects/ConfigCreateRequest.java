package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects;

import lombok.NonNull;

import java.util.List;

public record ConfigCreateRequest(
    @NonNull String name,
    @NonNull List<SizeConfig> sizesConfig,
    @NonNull List<DimensionConfig> dimensionsConfig,
    @NonNull List<PointsMapping> pointsMapping
) {
    public record SizeConfig(
        @NonNull String name
    ) {}

    public record DimensionConfig(
        @NonNull String name,
        @NonNull List<SizeValue> sizeValues
    ) {}

    public record SizeValue(
        @NonNull String name,
        int value
    ) {}

    public record PointsMapping(
        @NonNull List<Integer> totalRange,
        int points
    ) {}
}
