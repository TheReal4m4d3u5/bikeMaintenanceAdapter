package com.avery.provider.model;

public record ProviderWorkOrder(
        String workOrderId,
        String bikeId,
        String maintenanceIssueId,
        String description,
        String assignedTechnicianId,
        String status,
        String createdDate) {
}
