package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteFakeBuilder
{
    public static final long defaultId = 103001L;
    public static final long defaultId2 = 103002L;
    public static final long defaultId3 = 103003L;
    public static final long defaultId4 = 103004L;
    public static final long defaultId5 = 103005L;
    public static final Long defaultTicketId = TicketFakeBuilder.defaultId1;
    public static final UUID defaultCreatedBy = IdsUserFakeBuilder.defaultId1;
    public static final UUID defaultCreatedBy2 = IdsUserFakeBuilder.defaultId2;
    public static final UUID defaultCreatedBy3 = IdsUserFakeBuilder.defaultId1;
    public static final UUID defaultCreatedBy4 = IdsUserFakeBuilder.defaultId3;
    public static final UUID defaultCreatedBy5 = IdsUserFakeBuilder.defaultId4;
    public static final short defaultUncertainty = 1;
    public static final short defaultComplexity = 2;
    public static final short defaultEffort = 3;
    public static final short defaultRisk = 1;
    public static final short defaultCalculatedPoint = 5;
    public static final short defaultCalculatedPoint4 = 13;

    Long id = defaultId;
    Long id2 = defaultId2;
    Long id3 = defaultId3;
    Long id4 = defaultId4;
    Long id5 = defaultId5;
    Long ticketId = TicketFakeBuilder.defaultId1;
    Long ticketId2 = TicketFakeBuilder.defaultId1;
    Long ticketId3 = TicketFakeBuilder.defaultId2;
    Long ticketId4 = TicketFakeBuilder.defaultId2;
    Long ticketId5 = TicketFakeBuilder.defaultId3;
    Long storyPointConfigId = null;
    String voteValues = "{\"Uncertainty\": \"Size S\", \"Complexity\": \"Size M\", \"Effort\": \"Size L\", \"Risk\": \"Size S\"}";
    short uncertainty = defaultUncertainty;
    short complexity = defaultComplexity;
    short effort = defaultEffort;
    short risk = defaultRisk;
    short calculatedPoint = defaultCalculatedPoint;
    short calculatedPoint4 = defaultCalculatedPoint4;
    LocalDateTime createdAt = LocalDateTime.of(2020, 11, 22, 11, 22, 33);
    UUID createdBy = defaultCreatedBy;
    UUID createdBy2 = defaultCreatedBy2;
    UUID createdBy3 = defaultCreatedBy3;
    UUID createdBy4 = defaultCreatedBy4;
    UUID createdBy5 = defaultCreatedBy5;

    public Vote build()
    {
        return new Vote(id, ticketId, storyPointConfigId, voteValues, calculatedPoint, createdAt, createdBy);
    }

    public Vote build2()
    {
        return new Vote(id2, ticketId2, storyPointConfigId, voteValues, calculatedPoint, createdAt, createdBy2);
    }

    public Vote build3()
    {
        return new Vote(id3, ticketId3, storyPointConfigId, voteValues, calculatedPoint, createdAt, createdBy3);
    }

    public Vote build4()
    {
        return new Vote(id4, ticketId4, storyPointConfigId, voteValues, calculatedPoint4, createdAt, createdBy4);
    }

    public Vote build5()
    {
        return new Vote(id5, ticketId5, storyPointConfigId, voteValues, calculatedPoint, createdAt, createdBy5);
    }

    public List<Vote> build3to5()
    {
        return List.of(
            new VoteFakeBuilder().build3(),
            new VoteFakeBuilder().build4(),
            new VoteFakeBuilder().build5()
        );
    }
}
