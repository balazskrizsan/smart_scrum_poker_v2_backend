package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InGamePlayer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class InGamePlayersRepository extends AbstractRepository
{
    public void onDuplicateKeyIgnoreAdd(@NonNull InGamePlayer inGamePlayer)
    {
        var ctx = getDSLContext();
        ctx.insertInto(inGamePlayersTable)
            .set(ctx.newRecord(inGamePlayersTable, inGamePlayer))
            .onDuplicateKeyIgnore()
            .execute();
    }

    public List<InGamePlayer> searchUserSecureIdsByPokerIdSecure(UUID pokerIdSecure)
    {
        return getDSLContext()
            .selectFrom(inGamePlayersTable)
            .where(inGamePlayersTable.POKER_ID_SECURE.eq(pokerIdSecure))
            .fetchInto(InGamePlayer.class);
    }
}
