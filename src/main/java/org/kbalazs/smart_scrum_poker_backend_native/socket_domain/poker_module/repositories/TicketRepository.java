package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.TicketRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.row;

@Repository
public class TicketRepository extends AbstractRepository
{
    public void createAll(@NonNull List<Ticket> tickets)
    {
        getDSLContext()
            .insertInto(ticketTable, ticketTable.ID_SECURE, ticketTable.POKER_ID, ticketTable.NAME)
            .valuesOfRows(tickets.stream().map(r -> row(r.idSecure(), r.pokerId(), r.name())).toList())
            .execute();
    }

    public List<Ticket> searchByPokerId(long pokerId)
    {
        return getDSLContext()
            .selectFrom(ticketTable)
            .where(ticketTable.POKER_ID.eq(pokerId))
            .orderBy(ticketTable.ID)
            .fetchInto(Ticket.class);
    }

    public void activate(long ticketId)
    {
        getDSLContext()
            .update(ticketTable)
            .set(ticketTable.ACTIVE, true)
            .where(ticketTable.ID.eq(ticketId))
            .execute();
    }

    public void deactivate(long ticketId)
    {
        getDSLContext()
            .update(ticketTable)
            .set(ticketTable.ACTIVE, false)
            .where(ticketTable.ID.eq(ticketId))
            .execute();
    }

    public void delete(long ticketId)
    {
        getDSLContext()
            .deleteFrom(ticketTable)
            .where(ticketTable.ID.eq(ticketId))
            .execute();
    }

    public Ticket addOne(@NonNull Ticket ticket)
    {
        TicketRecord addedTicket = getDSLContext().newRecord(ticketTable, ticket);
        addedTicket.store();

        return addedTicket.into(Ticket.class);
    }
}
