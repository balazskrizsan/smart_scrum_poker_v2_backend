package org.kbalazs.smart_scrum_poker_backend_native.socket_api.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.api.exceptions.ApiException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.SessionResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
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
    private final IdsUserService idsUserService;

    public void notifyPokerGameWithNewSession(@NonNull UUID idsUserId) throws AccountException
    {
        notifyPokerGame(
            idsUserId,
            SESSION_CREATED_OR_UPDATED,
            "Notify poker game closed session: {}, pokers: {}"
        );
    }

    public void notifyPokerGameWithLeavingSession(@NonNull UUID idsUserId) throws AccountException
    {
        notifyPokerGame(
            idsUserId,
            SESSION_CLOSED,
            "Notify poker game with new session: {}, pokers: {}"
        );
    }

    private void notifyPokerGame(
        @NonNull UUID idsUserId,
        @NonNull SocketDestination socketDestination,
        @NonNull String logMessage
    )
        throws AccountException
    {
        IdsUser user = idsUserService.getById(idsUserId);
        Map<UUID, Poker> pokers = pokerService.searchWatchedPokers(idsUserId);
        log.info(logMessage, idsUserId, pokers.keySet());

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
