package com.avery.provider.graphql;

public record ProviderMaintenanceIssueInput(
        String maintenanceIssueId,
        String bikeId,
        String reportedByUserId,
        String sourceType,
        String description,
        String severity,
        String status,
        String reportedDate) {
}
