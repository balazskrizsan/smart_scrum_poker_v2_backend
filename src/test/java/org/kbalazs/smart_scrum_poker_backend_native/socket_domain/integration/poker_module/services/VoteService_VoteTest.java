package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.integration.poker_module.services;

import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert1Poker;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.Insert3TicketsAllInactive;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractIntegrationTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.InsecureUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.InsecureUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VoteService_VoteTest extends AbstractIntegrationTest
{
    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class, Insert1Poker.class, Insert3TicketsAllInactive.class})
    @SneakyThrows
    public void successfulVoteCall_savedToDb()
    {
        // Arrange
        Vote testedVote = new VoteFakeBuilder().id(null).build();

        Vote expectedVote = new VoteFakeBuilder().id(1L).build();
        InsecureUser expectedInsecureUser = new InsecureUserFakeBuilder().build();

        // Act
        InsecureUser actualInsecureUser = createInstance(VoteService.class).vote(testedVote);

        // Assert
        Vote actualVote = getDslContext().selectFrom(voteTable).fetchOneInto(Vote.class);

        assertAll(
            () -> assertThat(actualVote).isEqualTo(expectedVote),
            () -> assertThat(actualInsecureUser).isEqualTo(expectedInsecureUser)
        );
    }

    @Test
    @SqlPreset()
    @SneakyThrows
    public void insertWithoutDbUser_ThrowsException()
    {
        // Arrange
        Vote testedVote = new VoteFakeBuilder().id(null).build();

        // Act - Assert
        assertThatThrownBy(() -> createInstance(VoteService.class).vote(testedVote))
            .isInstanceOf(AccountException.class)
            .hasMessage("User not found; idSecure#10000000-0000-0000-0000-000000002001");
    }

    @Test
    @SqlPreset(presets = {Insert1InsecureUser.class, Insert1Poker.class, Insert3TicketsAllInactive.class})
    @SneakyThrows
    public void sendVoteMultipleTimes_fromSecondTheFirstRowWillBeUpdated()
    {
        // Arrange
        Vote testedVote1 = new VoteFakeBuilder().id(null).uncertainty(SizeEnum.S.val()).complexity(SizeEnum.S.val()).effort(SizeEnum.S.val()).build();
        Vote testedVote2 = new VoteFakeBuilder().id(null).uncertainty(SizeEnum.L.val()).complexity(SizeEnum.L.val()).effort(SizeEnum.L.val()).build();

        Vote expectedVote = new VoteFakeBuilder().id(1L).uncertainty(SizeEnum.L.val()).complexity(SizeEnum.L.val()).effort(SizeEnum.L.val()).calculatedPoint((short) 13).build();

        // Act
        VoteService service = createInstance(VoteService.class);
        service.vote(testedVote1);
        service.vote(testedVote2);

        // Assert
        List<Vote> votes = getDslContext().selectFrom(voteTable).fetch().into(Vote.class);
        assertAll(
            () -> assertThat(votes.size()).isEqualTo(1),
            () -> assertThat(votes.getFirst()).isEqualTo(expectedVote)
        );
    }
}
