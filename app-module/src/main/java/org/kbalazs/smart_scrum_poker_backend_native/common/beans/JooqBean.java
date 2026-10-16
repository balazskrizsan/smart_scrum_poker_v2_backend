package org.kbalazs.smart_scrum_poker_backend_native.common.beans;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JooqBean
{
    @Bean
    public DSLContext dslContext(DataSource dataSource)
    {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}
