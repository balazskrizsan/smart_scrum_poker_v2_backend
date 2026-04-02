package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.IdsUserRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities.IdsUser;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.exceptions.AccountException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IDS_USER;
import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IDS_USER_SESSIONS;

@Repository
public class IdsUserRepository extends AbstractRepository
{
    public @NonNull IdsUser createIfNotExist(@NonNull IdsUser newIdsUser)
        throws AccountException
    {
        Optional<IdsUser> idsUser = findById(newIdsUser.id());
        if (idsUser.isPresent())
        {
            return idsUser.get();
        }

        IdsUserRecord user = getDSLContext().newRecord(IDS_USER, newIdsUser);
        user.insert();

        if (user.getId() == null)
        {
            throw new AccountException("Insecure user creation failed.");
        }

        return user.into(IdsUser.class);
    }

    public @NonNull Optional<IdsUser> findById(@NonNull UUID id)
    {
        IdsUser user = getDSLContext()
            .selectFrom(IDS_USER)
            .where(IDS_USER.ID.eq(id))
            .fetchOneInto(IdsUser.class);

        return Optional.ofNullable(user);
    }

    public @NonNull IdsUser getById(@NonNull UUID id)
        throws AccountException
    {
        IdsUserRecord user = getDSLContext()
            .selectFrom(IDS_USER)
            .where(IDS_USER.ID.eq(id))
            .fetchOne();

        if (null == user)
        {
            throw new AccountException("User not found; id#" + id);
        }

        return user.into(IdsUser.class);
    }

    public List<IdsUser> findByIdSecureList(@NonNull List<UUID> idSecureList)
    {
        return getDSLContext()
            .selectFrom(IDS_USER)
            .where(IDS_USER.ID.in(idSecureList))
            .fetchInto(IdsUser.class);
    }

    public List<IdsUser> searchUsersWithActiveSession(@NonNull List<UUID> idSecures)
    {
        return getDSLContext()
            .selectDistinct(IDS_USER.fields())
            .from(IDS_USER)
            .rightJoin(IDS_USER_SESSIONS)
            .on(IDS_USER.ID.eq(IDS_USER_SESSIONS.IDS_USER_ID))
            .where(IDS_USER_SESSIONS.IDS_USER_ID.in(idSecures))
            .fetchInto(IdsUser.class);
    }
}
