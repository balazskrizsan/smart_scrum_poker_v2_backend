package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db.Tables;
import org.kbalazs.smart_scrum_poker_backend_native.db.tables.records.TicketRecord;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories.AbstractRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.entities.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.row;
import static org.kbalazs.smart_scrum_poker_backend_native.db.Tables.TICKET;

@Repository
public class TicketRepository extends AbstractRepository
{
    public void createAll(@NonNull List<Ticket> tickets)
    {
        getDSLContext()
            .insertInto(TICKET, TICKET.PUBLIC_ID, TICKET.POKER_ID, TICKET.NAME)
            .valuesOfRows(tickets.stream().map(r -> row(r.idSecure(), r.pokerId(), r.name())).toList())
            .execute();
    }

    public List<Ticket> searchByPokerId(long pokerId)
    {
        return getDSLContext()
            .selectFrom(TICKET)
            .where(TICKET.POKER_ID.eq(pokerId))
            .orderBy(TICKET.ID)
            .fetchInto(Ticket.class);
    }

    public void activate(long ticketId)
    {
        getDSLContext()
            .update(TICKET)
            .set(TICKET.ACTIVE, true)
            .where(TICKET.ID.eq(ticketId))
            .execute();
    }

    public void deactivate(long ticketId)
    {
        getDSLContext()
            .update(TICKET)
            .set(TICKET.ACTIVE, false)
            .where(TICKET.ID.eq(ticketId))
            .execute();
    }

    public void delete(long ticketId)
    {
        getDSLContext()
            .deleteFrom(TICKET)
            .where(TICKET.ID.eq(ticketId))
            .execute();
    }

    public Ticket addOne(@NonNull Ticket ticket)
    {
        TicketRecord addedTicket = getDSLContext().newRecord(TICKET, ticket);
        addedTicket.store();

        return addedTicket.into(Ticket.class);
    }
}
