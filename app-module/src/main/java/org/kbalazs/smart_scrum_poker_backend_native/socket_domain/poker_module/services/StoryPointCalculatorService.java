package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.StoryPointConfigRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoryPointCalculatorService
{
    StoryPointConfigRepository storyPointConfigRepository;
    ObjectMapper objectMapper;

    public Short calculate(@NonNull final VoteValues voteValues, @NonNull final StoryPointConfig config) throws StoryPointException
    {
        if (voteValues.questionMark() || voteValues.coffeeMug())
        {
            return 0;
        }

        try
        {
            // Parse dimensions config - new format: array of objects with sizeValues as array
            List<Map<String, Object>> dimensionsConfig = objectMapper.readValue(
                config.dimensionsConfig(),
                new TypeReference<List<Map<String, Object>>>() {}
            );

            // Calculate total based on dimension values
            int total = 0;
            for (Map<String, Object> dimension : dimensionsConfig)
            {
                String dimensionName = (String) dimension.get("name");
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> sizeValues = (List<Map<String, Object>>) dimension.get("sizeValues");

                String selectedSize = voteValues.dimensionValues().get(dimensionName);
                if (selectedSize != null && sizeValues != null)
                {
                    // Find the value for the selected size in the sizeValues array
                    for (Map<String, Object> sizeValue : sizeValues)
                    {
                        if (selectedSize.equals(sizeValue.get("name")))
                        {
                            total += ((Number) sizeValue.get("value")).intValue();
                            break;
                        }
                    }
                }
            }

            // Parse points mapping and find matching range
            List<Map<String, Object>> pointsMapping = objectMapper.readValue(
                config.pointsMapping(),
                new TypeReference<List<Map<String, Object>>>() {}
            );

            for (Map<String, Object> mapping : pointsMapping)
            {
                @SuppressWarnings("unchecked")
                List<Integer> range = (List<Integer>) mapping.get("totalRange");
                int minRange = range.get(0);
                int maxRange = range.get(1);

                if (total >= minRange && total <= maxRange)
                {
                    return ((Number) mapping.get("points")).shortValue();
                }
            }

            // Default fallback
            return 100;
        }
        catch (Exception e)
        {
            throw new StoryPointException("Error calculating story points: " + e.getMessage(), e);
        }
    }

    public Short calculate(@NonNull final VoteValues voteValues) throws StoryPointException
    {
        return calculate(voteValues, getDefaultConfig());
    }

    private StoryPointConfig getDefaultConfig() throws StoryPointException
    {
        return storyPointConfigRepository.findDefaultConfig()
            .orElseThrow(() -> new StoryPointException("Default story point config not found"));
    }
}
