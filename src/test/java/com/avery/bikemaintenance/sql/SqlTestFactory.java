package com.avery.bikemaintenance.sql;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.avery.bikemaintenance.adapter.outbound.sql.SqlMaintenanceRepositoryFactory;

final class SqlTestFactory {

    private SqlTestFactory() {
    }

    static SqlMaintenanceRepositoryFactory create() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName(
                "org.h2.Driver");

        dataSource.setUrl(
                "jdbc:h2:mem:"
                        + UUID.randomUUID()
                        + ";MODE=PostgreSQL"
                        + ";DB_CLOSE_DELAY=-1");

        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return new SqlMaintenanceRepositoryFactory(
                new JdbcTemplate(dataSource));
    }
}
