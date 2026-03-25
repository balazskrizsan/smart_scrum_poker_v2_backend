package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteRequestFakeBuilder
{
    UUID userIdSecure = VoteFakeBuilder.defaultCreatedBy;
    UUID pokerIdSecure =  PokerFakeBuilder.defaultIdSecure1;
    long ticketId = VoteFakeBuilder.defaultTicketId;
    short voteUncertainty = VoteFakeBuilder.defaultUncertainty;
    short voteComplexity = VoteFakeBuilder.defaultComplexity;
    short voteEffort = VoteFakeBuilder.defaultEffort;
    short voteRisk = VoteFakeBuilder.defaultRisk;

    public VoteRequest build()
    {
        return new VoteRequest(
            userIdSecure,
            pokerIdSecure,
            ticketId,
            voteUncertainty,
            voteComplexity,
            voteEffort,
            voteRisk
        );
    }
}
