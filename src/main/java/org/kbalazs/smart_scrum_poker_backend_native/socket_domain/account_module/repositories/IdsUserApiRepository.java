package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.dtos.IdsUserInfoBatchRequest;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.UserProfile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class IdsUserApiRepository extends AbstractIdsApiRepository
{
    private static final String USER_INFO_BATCH_ENDPOINT = "/api/userinfo/batch";

    public @NonNull List<UserProfile> findProfileByIdsUserIdList(@NonNull List<UUID> userIdList)
    {
        List<String> userIds = userIdList.stream().map(UUID::toString).toList();

        log.info("Fetching user profiles for IDs: {}", userIds);

        return getOrThrow(
            USER_INFO_BATCH_ENDPOINT,
            new IdsUserInfoBatchRequest(userIds),
            new ParameterizedTypeReference<>()
            {
            }
        );
    }

    public @NonNull UserProfile findProfileByIdsUserId(@NonNull UUID userId)
    {
        return this.findProfileByIdsUserIdList(List.of(userId)).getFirst();
    }
}
