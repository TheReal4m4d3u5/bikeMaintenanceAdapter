package com.avery.provider.graphql;

public record ProviderWorkOrderInput(
        String workOrderId,
        String bikeId,
        String maintenanceIssueId,
        String description,
        String assignedTechnicianId,
        String status,
        String createdDate) {
}
