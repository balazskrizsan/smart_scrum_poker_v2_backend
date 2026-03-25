package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.account.InsecureUserCreateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InsecureUserCreateListener
{
    private final InsecureUserService insecureUserService;
    private final SocketConnectionHandlerService socketConnectionHandlerService;

    @MessageMapping("/account/insecure.user.create")
    @SendToUser("/queue/reply")
    public ResponseEntity<ResponseData<InsecureUser>> insecureUserCreateHandler(
        @Payload InsecureUserCreateRequest insecureUserCreateRequest,
        MessageHeaders headers
    )
        throws AccountException, ApiException, SocketException
    {
        log.info("Listener:/account/insecure.user.create: {}", insecureUserCreateRequest);

        InsecureUser newInsecureUser = insecureUserService.create(
            RequestMapperService.mapToEntity(insecureUserCreateRequest),
            socketConnectionHandlerService.getSessionId(headers)
        );

        return new ResponseEntityBuilder<InsecureUser>()
            .socketDestination(SocketDestination.SEND_ACCOUNT_INSECURE_USER_CREATE)
            .data(newInsecureUser)
            .build();
    }
}
