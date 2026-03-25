package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Ticket;
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

@Repository
public class PokerRepository extends AbstractRepository
{
    public Poker create(@NonNull Poker poker) throws PokerException
    {
        PokerRecord pokerRecord = getDSLContext().newRecord(pokersTable, poker);
        pokerRecord.store();

        if (pokerRecord.getId() == null)
        {
            throw new PokerException("Poker creation failed.");
        }

        return pokerRecord.into(Poker.class);
    }

    // @todo: test not found
    public Poker findByIdSecure(@NonNull UUID pokerIdSecure) throws PokerException
    {
        PokerRecord record = getDSLContext()
            .selectFrom(pokersTable)
            .where(pokersTable.ID_SECURE.eq(pokerIdSecure))
            .fetchOne();

        if (null == record)
        {
            throw new PokerException("Poker not found: id#" + pokerIdSecure);
        }

        return record.into(Poker.class);
    }

    public Map<UUID, Poker> searchWatchedPokers(@NonNull UUID insecureUserIdSecure)
    {
        return getDSLContext()
            .select(pokersTable.fields())
            .from(pokersTable)
            .leftJoin(inGamePlayersTable)
            .on(inGamePlayersTable.POKER_ID_SECURE.eq(pokersTable.ID_SECURE))
            .where(inGamePlayersTable.INSECURE_USER_ID_SECURE.eq(insecureUserIdSecure))
            .fetchInto(Poker.class)
            .stream()
            .collect(Collectors.toMap(Poker::idSecure, Function.identity()));
    }

    public List<Poker> searchByInsecureUserId(@NonNull UUID insecureUserIdSecure)
    {
        return getDSLContext()
            .selectFrom(pokersTable)
            .where(pokersTable.CREATED_BY.eq(insecureUserIdSecure))
            .fetchInto(Poker.class);
    }
}
