package com.avery.bikemaintenance.application.port.outbound;

public interface MaintenanceRepositoryFactory {

    BikeRepository bikeRepository();

    MaintenanceIssueRepository maintenanceIssueRepository();

    WorkOrderRepository workOrderRepository();
}
