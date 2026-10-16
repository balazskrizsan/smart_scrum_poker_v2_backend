package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceBean
{
    @Primary
    @Bean
    public HikariDataSource customDataSource(ApplicationProperties applicationProperties)
    {
        HikariConfig config = new HikariConfig();
        config.setTransactionIsolation("TRANSACTION_READ_UNCOMMITTED");
        config.setJdbcUrl(applicationProperties.getDataSourceUrl());
        config.setUsername(applicationProperties.getDataSourceUsername());
        config.setPassword(applicationProperties.getDataSourcePassword());
        config.setMaximumPoolSize(applicationProperties.getDataSourceHikariMaximumPoolSize());
        config.setMinimumIdle(applicationProperties.getDataSourceHikariMinimumIdle());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }
}
