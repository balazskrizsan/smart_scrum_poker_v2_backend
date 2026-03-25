package org.kbalazs.smart_scrum_poker_backend_native.socket_api.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.SessionResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.InsecureUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.PokerService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SESSION_CLOSED;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SESSION_CREATED_OR_UPDATED;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketNotificationHandlerService
{
    private final PokerService pokerService;
    private final NotificationService notificationService;
    private final InsecureUserService insecureUserService;

    public void notifyPokerGameWithNewSession(@NonNull UUID insecureUserIdSecure) throws AccountException
    {
        notifyPokerGame(
            insecureUserIdSecure,
            SESSION_CREATED_OR_UPDATED,
            "Notify poker game closed session: {}, pokers: {}"
        );
    }

    // @todo test
    public void notifyPokerGameWithLeavingSession(@NonNull UUID insecureUserIdSecure) throws AccountException
    {
        notifyPokerGame(
            insecureUserIdSecure,
            SESSION_CLOSED,
            "Notify poker game with new session: {}, pokers: {}"
        );
    }

    private void notifyPokerGame(
        @NonNull UUID insecureUserIdSecure,
        @NonNull SocketDestination socketDestination,
        @NonNull String logMessage
    )
        throws AccountException
    {
        InsecureUser user = insecureUserService.findByIdSecure(insecureUserIdSecure);
        Map<UUID, Poker> pokers = pokerService.searchWatchedPokers(insecureUserIdSecure);
        log.info(logMessage, insecureUserIdSecure, pokers.keySet());

        pokers.keySet().forEach(pokerIdSecure -> {
            try
            {
                notificationService.notifyPokerGame(pokerIdSecure, new SessionResponse(user), socketDestination);
            }
            catch (ApiException e)
            {
                log.error("NotifyPokerGame error: {}", e.getMessage(), e);
            }
        });
    }
}
