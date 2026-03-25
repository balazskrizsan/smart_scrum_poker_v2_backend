package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.services;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.enums.SizeEnum;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.StoryPointException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.value_objects.VoteValues;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class StoryPointCalculatorService
{
    private static final NavigableMap<Integer, Integer> POINTS_MAP = new TreeMap<>()
    {{
        put(4, 1);    // 1 1 1 1 = 4
        put(5, 2);    // 1 1 1 2 = 5
        put(7, 3);    // 1 1 2 2 = 6 | 1 1 1 3 = 6 | 1 1 2 3 = 7 | 1 2 2 2 = 7
        put(9, 5);    // 2 2 2 2 = 8 | 3 2 2 2 = 9
        put(11, 8);   // 3 3 2 2 = 10 | 3 3 3 2 = 11 | 3 3 3 1 = 10
        put(12, 13);  // 3 3 3 3 = 12
        put(20, 20);  // 1 1 1 10 = 14 | 3 3 3 10 = 19
        put(30, 50);  // 1 1 10 10 = 22 | 3 3 10 10 = 26
        put(Integer.MAX_VALUE, 100); // all the rest
    }};

    public Short calculate(@NonNull final VoteValues voteValues) throws StoryPointException
    {
        validateVoteValues(voteValues);

        if (voteValues.questionMark() || voteValues.coffeeMug())
        {
            return 0;
        }

        int total = voteValues.uncertainty().val()  // s=1/m=2/l=3/xxl=4
                    + voteValues.complexity().val() // s=1/m=2/l=3/xxl=4
                    + voteValues.effort().val()     // s=1/m=2/l=3/xxl=4
                    + voteValues.risk().val();      // s=1/m=2/l=3/xxl=4

        return POINTS_MAP.ceilingEntry(total).getValue().shortValue();
    }

    private void validateVoteValues(@NonNull final VoteValues voteValues) throws StoryPointException
    {
        validateSingleValue("uncertainty", voteValues.uncertainty());
        validateSingleValue("complexity", voteValues.complexity());
        validateSingleValue("effort", voteValues.effort());
        validateSingleValue("voteRisk", voteValues.risk());
    }

    private void validateSingleValue(final @NonNull String fieldName, @NonNull final SizeEnum size) throws
        StoryPointException
    {
        List<SizeEnum> allowedValues = Arrays.stream(SizeEnum.values()).toList();

        if (!allowedValues.contains(size))
        {
            throw new StoryPointException("Size validation error: " + fieldName + " is invalid: " + size.val());
        }
    }
}
