package com.avery.bikemaintenance.adapter.outbound.memory;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceRepositoryFactory;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;

public class MemoryMaintenanceRepositoryFactory
        implements MaintenanceRepositoryFactory {

    private final BikeRepository bikeRepository =
            new InMemoryBikeRepository();

    private final MaintenanceIssueRepository
            maintenanceIssueRepository =
                    new InMemoryMaintenanceIssueRepository();

    private final WorkOrderRepository workOrderRepository =
            new InMemoryWorkOrderRepository();

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
