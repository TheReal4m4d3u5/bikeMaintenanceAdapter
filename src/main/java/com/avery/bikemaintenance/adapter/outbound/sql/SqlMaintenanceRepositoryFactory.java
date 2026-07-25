package com.avery.bikemaintenance.adapter.outbound.sql;

import org.springframework.jdbc.core.JdbcTemplate;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceRepositoryFactory;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;

public class SqlMaintenanceRepositoryFactory
        implements MaintenanceRepositoryFactory {

    private final BikeRepository bikeRepository;
    private final MaintenanceIssueRepository
            maintenanceIssueRepository;
    private final WorkOrderRepository workOrderRepository;

    public SqlMaintenanceRepositoryFactory(
            JdbcTemplate jdbcTemplate) {

        new SqlSchemaInitializer(jdbcTemplate);

        bikeRepository =
                new SqlBikeRepositoryAdapter(
                        jdbcTemplate);

        maintenanceIssueRepository =
                new SqlMaintenanceIssueRepositoryAdapter(
                        jdbcTemplate);

        workOrderRepository =
                new SqlWorkOrderRepositoryAdapter(
                        jdbcTemplate);
    }

    @Override
    public BikeRepository bikeRepository() {
        return bikeRepository;
    }

    @Override
    public MaintenanceIssueRepository
            maintenanceIssueRepository() {

        return maintenanceIssueRepository;
    }

    @Override
    public WorkOrderRepository workOrderRepository() {
        return workOrderRepository;
    }
}
