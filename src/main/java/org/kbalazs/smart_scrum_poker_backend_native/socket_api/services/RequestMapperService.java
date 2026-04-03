package org.kbalazs.smart_scrum_poker_backend_native.socket_api.services;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.account.InsecureUserCreateRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.AddTicketRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.AddTicket;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.StartPoker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class RequestMapperService {
    public static LocalDateTime getNow() {
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static StartPoker mapToEntity(@NonNull StartRequest request, UUID idsUserId) {
        return new StartPoker(
            new Poker(
                null,
                null,
                request.sprintTitle(),
                getNow(),
                idsUserId
            ),
            request.ticketNames().stream().map(tn -> new Ticket(null, null, null, tn, false)).toList()
        );
    }

    public static Vote mapToEntity(@NonNull final VoteRequest voteRequest) {
        return new Vote(
            null,
            voteRequest.ticketId(),
            voteRequest.voteUncertainty(),
            voteRequest.voteComplexity(),
            voteRequest.voteEffort(),
            voteRequest.voteRisk(),
            null,
            getNow(),
            voteRequest.userIdSecure()
        );
    }

    public static IdsUser mapToEntity(@NonNull final InsecureUserCreateRequest insecureUserCreateRequest) {
        return new IdsUser(null, getNow());
    }

    public static AddTicket mapToEntity(@NonNull final AddTicketRequest request) {
        return new AddTicket(request.userIdSecure(), request.pokerIdSecure(), request.ticketName());
    }
}
