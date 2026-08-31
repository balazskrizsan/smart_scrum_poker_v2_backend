package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.StoryPointConfigRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.ConfigCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigService
{
    StoryPointConfigRepository storyPointConfigRepository;
    ObjectMapper objectMapper;

    public StoryPointConfig create(@NonNull ConfigCreateRequest request, @NonNull UUID createdBy)
    {
        try
        {
            String sizesConfigJson = objectMapper.writeValueAsString(request.sizesConfig());
            String dimensionsConfigJson = objectMapper.writeValueAsString(request.dimensionsConfig());
            String pointsMappingJson = objectMapper.writeValueAsString(request.pointsMapping());

            StoryPointConfig config = new StoryPointConfig(
                null,
                request.name(),
                sizesConfigJson,
                dimensionsConfigJson,
                pointsMappingJson,
                LocalDateTime.now(),
                createdBy
            );

            return storyPointConfigRepository.create(config);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create story point config: " + e.getMessage(), e);
        }
    }
}
