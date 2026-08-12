package org.kbalazs.smart_scrum_poker_backend_native.domain_common.repositories;

import org.jooq.DSLContext;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
abstract public class AbstractRepository {
    private JooqService jooqService;

    @Autowired
    public void setJooqService(JooqService jooqService) {
        this.jooqService = jooqService;
    }

    protected DSLContext getDSLContext() {
        return jooqService.getDbContext();
    }
}
