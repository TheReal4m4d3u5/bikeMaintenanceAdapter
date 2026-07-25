package com.avery.bikemaintenance.adapter.inbound.graphql;

public record MaintenanceIssueInput(
        String maintenanceIssueId,
        String bikeId,
        String sourceType,
        String description,
        String severity) {
}