package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.ConfigCreateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.ConfigService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.ConfigCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigCreateListener
{
    ConfigService configService;
    SecurityContextFactory securityContextFactory;

    @MessageMapping("/poker/config/create")
    @SendToUser("/queue/reply")
    public ResponseEntity<ResponseData<ConfigCreateResponse>> createConfig(@Payload ConfigCreateRequest request)
        throws ApiException
    {
        var idsUserId = securityContextFactory.getCurrentUserId();

        StoryPointConfig config = configService.create(request, idsUserId);

        return new ResponseEntityBuilder<ConfigCreateResponse>()
            .socketDestination(SocketDestination.POKER_CONFIG_CREATE)
            .data(new ConfigCreateResponse(config))
            .build();
    }
}
