package com.avery.bikemaintenance.adapter.outbound.sql;

import org.springframework.jdbc.core.JdbcTemplate;

public class SqlSchemaInitializer {

    public SqlSchemaInitializer(
            JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS bikes (
                    bike_id VARCHAR(80) PRIMARY KEY,
                    model VARCHAR(160) NOT NULL,
                    condition VARCHAR(80) NOT NULL,
                    ride_count INTEGER NOT NULL,
                    mileage DOUBLE PRECISION NOT NULL
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS maintenance_issues (
                    maintenance_issue_id VARCHAR(80) PRIMARY KEY,
                    bike_id VARCHAR(80) NOT NULL,
                    reported_by_user_id VARCHAR(80),
                    source_type VARCHAR(80) NOT NULL,
                    description VARCHAR(1000) NOT NULL,
                    severity VARCHAR(40) NOT NULL,
                    status VARCHAR(80) NOT NULL,
                    reported_date DATE NOT NULL
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS work_orders (
                    work_order_id VARCHAR(80) PRIMARY KEY,
                    bike_id VARCHAR(80) NOT NULL,
                    maintenance_issue_id VARCHAR(80) NOT NULL,
                    description VARCHAR(1000) NOT NULL,
                    assigned_technician_id VARCHAR(80),
                    status VARCHAR(80) NOT NULL,
                    created_date DATE NOT NULL
                )
                """);
    }
}
