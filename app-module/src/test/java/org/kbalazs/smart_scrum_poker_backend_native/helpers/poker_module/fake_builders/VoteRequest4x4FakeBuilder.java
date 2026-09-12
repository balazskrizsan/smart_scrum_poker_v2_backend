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
public class VoteRequest4x4FakeBuilder
{
    UUID userIdSecure = VoteFakeBuilder.defaultCreatedBy;
    UUID pokerIdSecure =  PokerFakeBuilder.defaultPublicId1;
    long ticketId = VoteFakeBuilder.defaultTicketId;
    Map<String, String> dimensionValues = new HashMap<>();

    private static final Map<String, String> DIMENSION_SSSS = Map.of("uncertainty", "Size S", "complexity", "Size S", "effort", "Size S", "risk", "Size S");
    private static final Map<String, String> DIMENSION_SSSM = Map.of("uncertainty", "Size S", "complexity", "Size S", "effort", "Size S", "risk", "Size M");
    private static final Map<String, String> DIMENSION_SSMS = Map.of("uncertainty", "Size S", "complexity", "Size S", "effort", "Size M", "risk", "Size S");
    private static final Map<String, String> DIMENSION_SMSS = Map.of("uncertainty", "Size S", "complexity", "Size M", "effort", "Size S", "risk", "Size S");
    private static final Map<String, String> DIMENSION_MSSS = Map.of("uncertainty", "Size M", "complexity", "Size S", "effort", "Size S", "risk", "Size S");
    private static final Map<String, String> DIMENSION_MMMM = Map.of("uncertainty", "Size M", "complexity", "Size M", "effort", "Size M", "risk", "Size M");
    private static final Map<String, String> DIMENSION_MMML = Map.of("uncertainty", "Size M", "complexity", "Size M", "effort", "Size M", "risk", "Size L");
    private static final Map<String, String> DIMENSION_MLMM = Map.of("uncertainty", "Size M", "complexity", "Size L", "effort", "Size M", "risk", "Size M");
    private static final Map<String, String> DIMENSION_LMMM = Map.of("uncertainty", "Size L", "complexity", "Size M", "effort", "Size M", "risk", "Size M");
    private static final Map<String, String> DIMENSION_LLLL = Map.of("uncertainty", "Size L", "complexity", "Size L", "effort", "Size L", "risk", "Size L");

    public VoteRequest4x4FakeBuilder() {
        // Default values for backward compatibility
        dimensionValues.put("uncertainty", "Size M");
        dimensionValues.put("complexity", "Size M");
        dimensionValues.put("effort", "Size M");
        dimensionValues.put("risk", "Size M");
    }

    public VoteRequest build()
    {
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildSSSS()
    {
        dimensionValues = DIMENSION_SSSS;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildSSSM()
    {
        dimensionValues = DIMENSION_SSSM;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildSSMS()
    {
        dimensionValues = DIMENSION_SSMS;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildSMSS()
    {
        dimensionValues = DIMENSION_SMSS;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildMSSS()
    {
        dimensionValues = DIMENSION_MSSS;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildMMMM()
    {
        dimensionValues = DIMENSION_MMMM;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildMMML()
    {
        dimensionValues = DIMENSION_MMML;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildMLMM()
    {
        dimensionValues = DIMENSION_MLMM;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildLMMM()
    {
        dimensionValues = DIMENSION_LMMM;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }

    public VoteRequest buildLLLL()
    {
        dimensionValues = DIMENSION_LLLL;
        return new VoteRequest(pokerIdSecure, ticketId, dimensionValues);
    }
}
