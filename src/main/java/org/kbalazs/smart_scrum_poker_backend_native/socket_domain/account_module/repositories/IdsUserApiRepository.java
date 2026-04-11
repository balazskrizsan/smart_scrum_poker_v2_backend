package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class IdsUserApiRepository
{
    public void findProfileByIdsUserIdList(@NonNull List<UUID> inPokerIdsUserIds)
    {

    }
}
