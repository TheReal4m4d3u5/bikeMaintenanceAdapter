package com.avery.bikemaintenance.adapter.outbound.graphql;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceRepositoryFactory;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;

public class GraphQlMaintenanceRepositoryFactory
        implements MaintenanceRepositoryFactory {

    private final BikeRepository bikeRepository;
    private final MaintenanceIssueRepository
            maintenanceIssueRepository;
    private final WorkOrderRepository workOrderRepository;

    public GraphQlMaintenanceRepositoryFactory(
            RemoteGraphQlClient client) {

        bikeRepository =
                new GraphQlBikeRepositoryAdapter(
                        client);

        maintenanceIssueRepository =
                new GraphQlMaintenanceIssueRepositoryAdapter(
                        client);

        workOrderRepository =
                new GraphQlWorkOrderRepositoryAdapter(
                        client);
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
