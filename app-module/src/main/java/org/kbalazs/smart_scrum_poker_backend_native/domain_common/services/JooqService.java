package org.kbalazs.smart_scrum_poker_backend_native.domain_common.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.TransactionalRunnable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqService
{
    private final DSLContext dslContext;

    public DSLContext getDbContext()
    {
        return dslContext;
    }

    void transaction(@NonNull TransactionalRunnable transactional)
    {
        dslContext.transaction(transactional);
    }
}
