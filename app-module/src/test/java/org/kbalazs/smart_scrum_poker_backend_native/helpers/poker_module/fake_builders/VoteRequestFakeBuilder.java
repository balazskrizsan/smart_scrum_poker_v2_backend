package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
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
    Map<String, String> dimensionValues = new HashMap<>();

    public VoteRequestFakeBuilder() {
        // Default values for backward compatibility
        dimensionValues.put("uncertainty", "M");
        dimensionValues.put("complexity", "M");
        dimensionValues.put("effort", "M");
        dimensionValues.put("risk", "M");
    }

    public VoteRequest build()
    {
        return new VoteRequest(
            userIdSecure,
            pokerIdSecure,
            ticketId,
            dimensionValues
        );
    }
}
