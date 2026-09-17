package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert1Ticket implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        var ticket = new TicketFakeBuilder().build();
        var id = ticket.id();
        dslContext.insertInto(Ticket.TICKET)
            .set(Ticket.TICKET.ID, ticket.id())
            .set(Ticket.TICKET.PUBLIC_ID, ticket.publicId())
            .set(Ticket.TICKET.POKER_ID, ticket.pokerId())
            .set(Ticket.TICKET.NAME, ticket.name())
            .set(Ticket.TICKET.ACTIVE, ticket.isActive())
            .execute();
    }
}
