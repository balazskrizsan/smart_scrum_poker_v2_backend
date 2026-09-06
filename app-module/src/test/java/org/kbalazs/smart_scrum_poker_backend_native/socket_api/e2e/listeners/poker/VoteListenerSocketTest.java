package org.kbalazs.smart_scrum_poker_backend_native.socket_api.e2e.listeners.poker;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kbalazs.smart_scrum_poker_backend_native.db.Tables;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.*;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractE2eSocketTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.account_module.fake_builders.IdsUserFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.PokerFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteRequest4x4FakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.VoteRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.responses.poker.VoteResponse;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.services.IdsUserService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Vote;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.VoteService;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.VoteFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_api.enums.SocketDestination.SEND_POKER_VOTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VoteListenerSocketTest extends AbstractE2eSocketTest
{
    @MockBean
    private IdsUserService idsUserService;

    @Autowired
    private VoteService voteService;

    private static Stream<VoteTestCase> voteTestCases() {
        return Stream.of(
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildSSSS(),
                (short) 1,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildSSSM(),
                (short) 3,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildSSMS(),
                (short) 2,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildSMSS(),
                (short) 2,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildMSSS(),
                (short) 2,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildMMMM(),
                (short) 5,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildMMML(),
                (short) 8,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildMLMM(),
                (short) 8,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildLMMM(),
                (short) 8,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            ),
            new VoteTestCase(
                IdsUserFakeBuilder.defaultId1,
                PokerFakeBuilder.defaultPublicId1,
                new VoteRequest4x4FakeBuilder().buildLLLL(),
                (short) 8,
                HttpStatus.OK.getReasonPhrase(),
                SEND_POKER_VOTE.getValue()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("voteTestCases")
    @SqlPreset(presets = {
        Insert1User.class,
        Insert1StoryPointConfig.class,
        Insert1Poker.class,
        Insert1Ticket.class,
        Insert1Session.class,
    })
    @SneakyThrows
    public void vote_returnsVoteResponse(VoteTestCase testCase)
    {
        CompletableFuture<ResponseEntity_ResponseData_VoteResponse> responseFuture = new CompletableFuture<>();

        // Arrange
        UUID mockUserId = testCase.userId();
        UserProfile mockUserProfile = new UserProfile(
            mockUserId.toString(),
            "Test User",
            "testuser"
        );
        when(idsUserService.findProfileByIdsUserId(any(UUID.class))).thenReturn(mockUserProfile);

        StompSession stompSession = getStompSession();

        UUID testedPokerPublicId = testCase.pokerPublicId();
        VoteRequest testedVoteRequest = testCase.voteRequest();

        String testedDestination = "/app/poker/vote/" + testedPokerPublicId + "/" + testedVoteRequest.ticketId();
        String testedSubscribeUrl = "/queue/reply-" + testedPokerPublicId;

        VoteResponse expectedResult = new VoteResponse(mockUserProfile, testCase.expectedCalculatedPoint());

        Vote expectedVote = new VoteFakeBuilder()
            .ticketId(testedVoteRequest.ticketId())
            .createdBy(mockUserId)
            .calculatedPoint(testCase.expectedCalculatedPoint())
            .build();

        // Act
        StompFrameHandler stompHandler = buildStompFrameHandler(
            responseFuture,
            ResponseEntity_ResponseData_VoteResponse.class
        );
        stompSession.subscribe(testedSubscribeUrl, stompHandler);
        stompSession.send(testedDestination, testedVoteRequest);

        ResponseEntity_ResponseData_VoteResponse actual = responseFuture.get(3, TimeUnit.SECONDS);

        Long voteId = getDslContext().selectFrom(Tables.VOTE).fetchOne().getId();
        Vote actualDbVote = voteService.findById(voteId);

        // Assert
        assertAll(
            () -> assertThat(actual.statusCode).isEqualTo(testCase.expectedHttpStatus()),
            () -> assertThat(actual.body().data()).usingRecursiveComparison().isEqualTo(expectedResult),
            () -> assertThat(actual.body().socketResponseDestination).isEqualTo(testCase.expectedDestination()),
            () -> assertThat(actualDbVote).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "storyPointConfigId", "voteValues")
                .isEqualTo(expectedVote)
        );
    }

    public record VoteTestCase(
        UUID userId,
        UUID pokerPublicId,
        VoteRequest voteRequest,
        short expectedCalculatedPoint,
        String expectedHttpStatus,
        String expectedDestination
    )
    {
    }

    private record ResponseEntity_ResponseData_VoteResponse(
        ResponseData_VoteResponse body,
        Map<String, String> headers,
        String statusCode,
        int statusCodeValue
    )
    {
    }

    private record ResponseData_VoteResponse(
        VoteResponse data,
        Boolean success,
        int errorCode,
        String requestId,
        String socketResponseDestination
    )
    {
    }
}
