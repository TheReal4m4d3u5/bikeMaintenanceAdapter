package com.avery.bikemaintenance.adapter.inbound.graphql;

public record WorkOrderInput(
        String workOrderId,
        String bikeId,
        String maintenanceIssueId,
        String description,
        String assignedTechnician) {
}