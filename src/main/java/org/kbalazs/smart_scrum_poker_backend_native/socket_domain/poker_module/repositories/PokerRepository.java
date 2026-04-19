package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.PokerRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Poker;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.exceptions.PokerException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.IN_POKER_IDS_USERS;
import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.POKER;

@Repository
public class PokerRepository extends AbstractRepository {
    public Poker create(@NonNull Poker poker) throws PokerException {
        PokerRecord pokerRecord = getDSLContext().newRecord(POKER, poker);
        pokerRecord.store();

        if (pokerRecord.getId() == null) {
            throw new PokerException("Poker creation failed.");
        }

        return pokerRecord.into(Poker.class);
    }

    public Poker findByPublicId(@NonNull UUID pokerPublicId) throws PokerException {
        PokerRecord record = getDSLContext()
            .selectFrom(POKER)
            .where(POKER.PUBLIC_ID.eq(pokerPublicId))
            .fetchOne();

        if (null == record) {
            throw new PokerException("Poker not found: id#" + pokerPublicId);
        }

        return record.into(Poker.class);
    }

    public Map<UUID, Poker> searchWatchedPokers(@NonNull UUID idsUserId) {
        return getDSLContext()
            .select(POKER.fields())
            .from(POKER)
            .leftJoin(IN_POKER_IDS_USERS)
            .on(IN_POKER_IDS_USERS.POKER_ID.eq(POKER.ID))
            .where(IN_POKER_IDS_USERS.IDS_USER_ID.eq(idsUserId))
            .fetchInto(Poker.class)
            .stream()
            .collect(Collectors.toMap(Poker::publicId, Function.identity()));
    }

    public List<Poker> searchByInsecureUserId(@NonNull UUID idsUserId) {
        return getDSLContext()
            .selectFrom(POKER)
//            .where(POKER.CREATED_BY.eq(idsUserId))
            .fetchInto(Poker.class);
    }
}
