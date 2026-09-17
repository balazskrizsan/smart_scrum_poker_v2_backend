package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import com.fasterxml.jackson.core.type.TypeReference;
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
            // Parse dimensions config - array format: [{"name": "Uncertainty", "sizeValues": [{"name": "Size S", "value": 1}, ...]}, ...]
            List<Map<String, Object>> dimensionsConfigArray = objectMapper.readValue(
                config.dimensionsConfig(),
                new TypeReference<List<Map<String, Object>>>() {}
            );

            // Convert array format to map for easier lookup: {"Uncertainty": {"Size S": 1, "Size M": 2, ...}, ...}
            Map<String, Map<String, Integer>> dimensionsConfig = new java.util.HashMap<>();
            for (Map<String, Object> dimension : dimensionsConfigArray)
            {
                String dimensionName = (String) dimension.get("name");
                List<Map<String, Object>> sizeValues = (List<Map<String, Object>>) dimension.get("sizeValues");
                
                Map<String, Integer> sizeValueMap = new java.util.HashMap<>();
                for (Map<String, Object> sizeValue : sizeValues)
                {
                    String sizeName = (String) sizeValue.get("name");
                    Integer value = (Integer) sizeValue.get("value");
                    sizeValueMap.put(sizeName.toLowerCase(), value);
                }
                
                dimensionsConfig.put(dimensionName.toLowerCase(), sizeValueMap);
            }

            // Calculate total based on dimension values
            int total = 0;
            for (Map.Entry<String, String> entry : voteValues.dimensionValues().entrySet())
            {
                String dimensionName = entry.getKey().toLowerCase();
                String selectedSize = entry.getValue().toLowerCase();
                
                Map<String, Integer> sizeValues = dimensionsConfig.get(dimensionName);
                if (sizeValues != null)
                {
                    Integer value = sizeValues.get(selectedSize);
                    if (value != null)
                    {
                        total += value;
                    }
                }
            }

            // Parse points mapping - array format: [{"totalRange": [0, 3], "points": 1}, ...]
            List<Map<String, Object>> pointsMappingArray = objectMapper.readValue(
                config.pointsMapping(),
                new TypeReference<>()
                {
                }
            );

            for (Map<String, Object> mapping : pointsMappingArray)
            {
                List<Integer> totalRange = (List<Integer>) mapping.get("totalRange");
                Integer points = (Integer) mapping.get("points");
                
                int minRange = totalRange.get(0);
                int maxRange = totalRange.get(1);

                if (total >= minRange && total <= maxRange)
                {
                    return points.shortValue();
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
