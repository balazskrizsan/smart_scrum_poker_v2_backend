package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//@todo: do we need this?
public class DataSourceBean
{
    @Primary
    @Bean
    public DataSource customDataSource()
    {
        ApplicationProperties ap = new ApplicationProperties();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(ap.getDriverClassName());
        dataSource.setUrl(ap.getDataSourceUrl());
        dataSource.setUsername(ap.getDataSourceUsername());
        dataSource.setPassword(ap.getDataSourcePassword());

        return dataSource;
    }
}
