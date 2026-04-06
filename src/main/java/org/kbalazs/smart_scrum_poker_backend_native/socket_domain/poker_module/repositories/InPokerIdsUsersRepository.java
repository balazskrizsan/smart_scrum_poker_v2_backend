package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.InPokerIdsUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IN_POKER_IDS_USERS;

@Repository
public class InPokerIdsUsersRepository extends AbstractRepository
{
    public void onDuplicateKeyIgnoreAdd(@NonNull InPokerIdsUser inPokerIdsUser)
    {
        var ctx = getDSLContext();
        ctx.insertInto(IN_POKER_IDS_USERS)
            .set(ctx.newRecord(IN_POKER_IDS_USERS, inPokerIdsUser))
            .onDuplicateKeyIgnore()
            .execute();
    }

    public List<InPokerIdsUser> searchUserSecureIdsByPokerIdSecure(UUID pokerIdSecure)
    {
        return getDSLContext()
            .selectFrom(IN_POKER_IDS_USERS)
//            .where(IN_POKER_IDS_USERS.POKER_ID.eq(pokerPublicId))
            .fetchInto(InPokerIdsUser.class);
    }
}
