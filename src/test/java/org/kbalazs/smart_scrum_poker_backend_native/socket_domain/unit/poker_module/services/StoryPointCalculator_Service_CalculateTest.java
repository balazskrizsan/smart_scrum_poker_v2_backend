package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.unit.poker_module.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.AbstractTest;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.enums.SizeEnumFakeBuilder;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services.StoryPointCalculatorService;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum.L;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum.M;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum.S;
import static org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum.XXL;

public class StoryPointCalculator_Service_CalculateTest extends AbstractTest {
    @Test
    @SneakyThrows
    public void calculationWithBadValues_throwsException() {
        // Arrange
        var testedVote = new VoteValues(
            false,
            false,
            L,
            M,
            XXL,
            new SizeEnumFakeBuilder().size((short) 4).build()
        );

        // Act - Assert
        assertThatThrownBy(() -> createInstance(StoryPointCalculatorService.class).calculate(testedVote))
            .isInstanceOf((StoryPointException.class))
            .hasMessage("Size validation error: voteRisk is invalid: 4");
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("validScenarios_returnsWithExpectedStoryPoint_provider")
    public void validScenarios_returnsWithExpectedStoryPoint(VoteValues voteValues, Short expectedCalculation) {
        // Arrange - In provider
        // Act
        var actualCalculation = createInstance(StoryPointCalculatorService.class).calculate(voteValues);

        // Assert
        assertThat(actualCalculation).isEqualTo(expectedCalculation);
    }

    private static Stream<Arguments> validScenarios_returnsWithExpectedStoryPoint_provider() {
        return Stream.of(
            Arguments.of(
                new VoteValues(true, false, S, S, S, S),
                (short) 0
            ),
            Arguments.of(
                new VoteValues(false, true, S, S, S, S),
                (short) 0
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, S, S),
                (short) 1
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, M, M),
                (short) 3
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, L, L),
                (short) 5
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, L, M),
                (short) 3
            ),
            Arguments.of(
                new VoteValues(false, false, S, L, L, L),
                (short) 8
            ),
            Arguments.of(
                new VoteValues(false, false, M, L, L, L),
                (short) 8
            ),
            Arguments.of(
                new VoteValues(false, false, L, L, L, L),
                (short) 13
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, S, XXL),
                (short) 20
            ),
            Arguments.of(
                new VoteValues(false, false, L, L, L, XXL),
                (short) 20
            ),
            Arguments.of(
                new VoteValues(false, false, S, S, XXL, XXL),
                (short) 50
            ),
            Arguments.of(
                new VoteValues(false, false, L, L, XXL, XXL),
                (short) 50
            ),
            Arguments.of(
                new VoteValues(false, false, S, XXL, XXL, XXL),
                (short) 100
            ),
            Arguments.of(
                new VoteValues(false, false, L, XXL, XXL, XXL),
                (short) 100
            ),
            Arguments.of(
                new VoteValues(false, false, XXL, XXL, XXL, XXL),
                (short) 100
            ),
            Arguments.of(
                new VoteValues(false, false, XXL, XXL, XXL, XXL),
                (short) 100
            )
        );
    }
}
