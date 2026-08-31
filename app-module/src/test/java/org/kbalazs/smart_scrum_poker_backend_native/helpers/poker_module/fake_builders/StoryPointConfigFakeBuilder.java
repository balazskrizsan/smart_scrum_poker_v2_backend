package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;

import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryPointConfigFakeBuilder
{
    public static final long defaultId1 = 104001L;
    public static final long defaultId2 = 104002L;
    public static final long defaultId3 = 104003L;
    public static final UUID defaultIdSecure1 = UUID.fromString("20000000-0000-0000-0000-000000000001");
    public static final UUID defaultIdSecure2 = UUID.fromString("20000000-0000-0000-0000-000000000002");
    public static final UUID defaultIdSecure3 = UUID.fromString("20000000-0000-0000-0000-000000000003");
    public static final String defaultName = "Fibonacci";
    public static final String defaultName2 = "T-Shirt Sizes";
    public static final String defaultName3 = "Custom Scale";
    public static final String defaultSizesConfig = "[{\"name\":\"Size S\"},{\"name\":\"Size M\"},{\"name\":\"Size L\"}]";
    public static final String defaultDimensionsConfig = "[{\"name\":\"Uncertainty\",\"sizeValues\":[{\"name\":\"Size S\",\"value\":1},{\"name\":\"Size M\",\"value\":2},{\"name\":\"Size L\",\"value\":3}]},{\"name\":\"Complexity\",\"sizeValues\":[{\"name\":\"Size S\",\"value\":1},{\"name\":\"Size M\",\"value\":2},{\"name\":\"Size L\",\"value\":3}]},{\"name\":\"Effort\",\"sizeValues\":[{\"name\":\"Size S\",\"value\":1},{\"name\":\"Size M\",\"value\":2},{\"name\":\"Size L\",\"value\":3}]},{\"name\":\"Risk\",\"sizeValues\":[{\"name\":\"Size S\",\"value\":1},{\"name\":\"Size M\",\"value\":4},{\"name\":\"Size L\",\"value\":5}]}]";
    public static final String defaultPointsMapping = "[{\"totalRange\":[0,3],\"points\":1},{\"totalRange\":[4,6],\"points\":2},{\"totalRange\":[6,8],\"points\":3},{\"totalRange\":[9,10],\"points\":5},{\"totalRange\":[10,12],\"points\":8},{\"totalRange\":[13,100],\"points\":13}]";

    Long id = defaultId1;
    Long id2 = defaultId2;
    Long id3 = defaultId3;
    String name = defaultName;
    String name2 = defaultName2;
    String name3 = defaultName3;
    String sizesConfig = defaultSizesConfig;
    String dimensionsConfig = defaultDimensionsConfig;
    String pointsMapping = defaultPointsMapping;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);
    UUID createdBy = UUID.fromString("0f48291f-c079-4b10-a6d3-11b029c8d03a");
    UUID createdBy2 = defaultIdSecure2;
    UUID createdBy3 = defaultIdSecure3;

    public StoryPointConfig build()
    {
        return new StoryPointConfig(id, name, sizesConfig, dimensionsConfig, pointsMapping, createdAt, createdBy);
    }

    public StoryPointConfig build2()
    {
        return new StoryPointConfig(id2, name2, sizesConfig, dimensionsConfig, pointsMapping, createdAt, createdBy2);
    }

    public StoryPointConfig build3()
    {
        return new StoryPointConfig(id3, name3, sizesConfig, dimensionsConfig, pointsMapping, createdAt, createdBy3);
    }

    public StoryPointConfig buildNoId()
    {
        return new StoryPointConfig(null, name, sizesConfig, dimensionsConfig, pointsMapping, createdAt, createdBy);
    }
}
