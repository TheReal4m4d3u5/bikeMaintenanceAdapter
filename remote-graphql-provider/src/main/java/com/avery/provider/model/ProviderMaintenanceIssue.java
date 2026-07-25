package com.avery.provider.model;

public record ProviderMaintenanceIssue(
        String maintenanceIssueId,
        String bikeId,
        String reportedByUserId,
        String sourceType,
        String description,
        String severity,
        String status,
        String reportedDate) {
}
