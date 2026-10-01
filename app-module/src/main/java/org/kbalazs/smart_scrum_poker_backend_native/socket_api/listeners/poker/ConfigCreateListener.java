package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.poker;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.common.factories.SecurityContextFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.SocketResponseFactory;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.ConfigCreateResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.StoryPointConfig;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.ConfigService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.ConfigCreateRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigCreateListener
{
    ConfigService configService;
    SecurityContextFactory securityContextFactory;

    @AsyncListener(operation = @AsyncOperation(
        channelName = "/poker/config/create",
        description = "Create a new story point configuration"
    ))
    @MessageMapping("/poker/config/create")
    @SendToUser("/queue/reply")
    public ResponseEntity_ResponseData_ConfigCreateResponse createConfig(@Payload ConfigCreateRequest request)
        throws ApiException
    {
        var idsUserId = securityContextFactory.getCurrentUserId();

        StoryPointConfig config = configService.create(request, idsUserId);

        return SocketResponseFactory.fromResponseEntity(
            new ResponseEntityBuilder<ConfigCreateResponse>()
                .socketDestination(SocketDestination.POKER_CONFIG_CREATE)
                .data(new ConfigCreateResponse(config))
                .build(),
            ResponseData_ConfigCreateResponse::new,
            ResponseEntity_ResponseData_ConfigCreateResponse::new
        );
    }

    @Schema(description = "Combined ResponseEntity and ResponseData wrapper for config creation")
    public record ResponseEntity_ResponseData_ConfigCreateResponse(
        @Schema(description = "Response data containing config creation result")
        ResponseData_ConfigCreateResponse body,
        @Schema(description = "Response headers")
        Map<String, String> headers,
        @Schema(description = "HTTP status code phrase")
        String statusCode,
        @Schema(description = "HTTP status code value")
        int statusCodeValue
    )
    {
    }

    @Schema(description = "Response data wrapper for config creation")
    public record ResponseData_ConfigCreateResponse(
        @Schema(description = "Config creation response data")
        ConfigCreateResponse data,
        @Schema(description = "Indicates if the operation was successful")
        Boolean success,
        @Schema(description = "Error code, 0 if successful")
        int errorCode,
        @Schema(description = "Request identifier")
        String requestId,
        @Schema(description = "Socket response destination")
        String socketResponseDestination
    )
    {
    }
}
