package com.avery.bikemaintenance.adapter.inbound.graphql;

public record WorkOrderInput(
        String bikeId,
        String maintenanceIssueId,
        String description,
        String assignedTechnicianId) {
}