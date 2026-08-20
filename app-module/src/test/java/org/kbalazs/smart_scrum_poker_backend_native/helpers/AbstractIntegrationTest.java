package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractIntegrationTest extends AbstractTest
{
    @Autowired
    private JooqService jooqService;

    protected DSLContext getDslContext()
    {
        return jooqService.getDbContext();
    }
}
