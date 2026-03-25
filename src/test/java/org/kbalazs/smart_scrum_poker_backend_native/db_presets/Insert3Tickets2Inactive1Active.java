package org.kbalazs.smart_scrum_poker_backend_native.db_presets;

import org.kbalazs.smart_scrum_poker_backend_native.db.tables.Ticket;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders.TicketFakeBuilder;
import lombok.NonNull;
import org.jooq.DSLContext;

public class Insert3Tickets2Inactive1Active implements IInsert
{
    @Override
    public void runParent()
    {
    }

    @Override
    public void run(@NonNull DSLContext dslContext)
    {
        dslContext.newRecord(Ticket.TICKET, new TicketFakeBuilder().build()).store();
        dslContext.newRecord(Ticket.TICKET, new TicketFakeBuilder().isActive2(true).build2()).store();
        dslContext.newRecord(Ticket.TICKET, new TicketFakeBuilder().build3()).store();
    }
}
