package org.kbalazs.smart_scrum_poker_backend_native.socket_api.listeners.account;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.builders.ResponseEntityBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.api.value_objects.ResponseData;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.exceptions.SocketException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.account.InsecureUserCreateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.RequestMapperService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.services.SocketConnectionHandlerService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class InsecureUserCreateListener {
    IdsUserService idsUserService;
    SocketConnectionHandlerService socketConnectionHandlerService;

    @MessageMapping("/account/insecure.user.create")
    @SendToUser("/queue/reply")
    public ResponseEntity<ResponseData<IdsUser>> insecureUserCreateHandler(
        @Payload InsecureUserCreateRequest insecureUserCreateRequest,
        MessageHeaders headers
    )
        throws AccountException, ApiException, SocketException {
        log.info("Listener:/account/insecure.user.create: {}", insecureUserCreateRequest);

        IdsUser newIdsUser = idsUserService.createIfNotExists(
            RequestMapperService.mapToEntity(insecureUserCreateRequest),
            socketConnectionHandlerService.getSessionId(headers)
        );

        return new ResponseEntityBuilder<IdsUser>()
            .socketDestination(SocketDestination.SEND_ACCOUNT_INSECURE_USER_CREATE)
            .data(newIdsUser)
            .build();
    }
}
