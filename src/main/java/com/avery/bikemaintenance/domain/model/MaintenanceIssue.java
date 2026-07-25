package com.avery.bikemaintenance.domain.model;

import java.time.LocalDate;

public class MaintenanceIssue {

    private final String maintenanceIssueId;
    private final String bikeId;
    private final String sourceType;
    private final String description;
    private final String severity;
    private String status;
    private final LocalDate reportedDate;

    public MaintenanceIssue(
            String maintenanceIssueId,
            String bikeId,
            String sourceType,
            String description,
            String severity,
            String status,
            LocalDate reportedDate) {

        if (maintenanceIssueId == null
                || maintenanceIssueId.isBlank()) {
            throw new IllegalArgumentException(
                    "Maintenance issue ID is required.");
        }

        if (bikeId == null || bikeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Bike ID is required.");
        }

        if (sourceType == null || sourceType.isBlank()) {
            throw new IllegalArgumentException(
                    "Issue source type is required.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Issue description is required.");
        }

        if (severity == null || severity.isBlank()) {
            throw new IllegalArgumentException(
                    "Issue severity is required.");
        }

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Issue status is required.");
        }

        if (reportedDate == null) {
            throw new IllegalArgumentException(
                    "Reported date is required.");
        }

        this.maintenanceIssueId = maintenanceIssueId;
        this.bikeId = bikeId;
        this.sourceType = sourceType;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.reportedDate = reportedDate;
    }

    public String getMaintenanceIssueId() {
        return maintenanceIssueId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void markWorkOrderCreated() {
        status = "WORK_ORDER_CREATED";
    }

    public void resolve() {
        status = "RESOLVED";
    }
}