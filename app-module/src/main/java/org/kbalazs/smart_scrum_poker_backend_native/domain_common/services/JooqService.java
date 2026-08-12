package org.kbalazs.smart_scrum_poker_backend_native.domain_common.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqService
{
    private final ApplicationProperties applicationProperties;

    private static DSLContext dslContext = null;

    public DSLContext getDbContext()
    {
        if (null == dslContext)
        {
            HikariConfig config = new HikariConfig();
            config.setTransactionIsolation("TRANSACTION_READ_UNCOMMITTED");
            config.setJdbcUrl(applicationProperties.getDataSourceUrl());
            config.setUsername(applicationProperties.getDataSourceUsername());
            config.setPassword(applicationProperties.getDataSourcePassword());
            config.setMaximumPoolSize(10);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dslContext = DSL.using(new HikariDataSource(config), SQLDialect.POSTGRES);
        }

        return dslContext;
    }

    void transaction(@NonNull TransactionalRunnable transactional)
    {
        getDbContext().transaction(transactional);
    }
}
