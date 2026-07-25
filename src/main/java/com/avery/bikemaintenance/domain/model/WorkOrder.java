package com.avery.bikemaintenance.domain.model;

import java.time.LocalDate;
import java.util.Set;

public class WorkOrder {

    private static final Set<String> VALID_STATUSES =
            Set.of(
                    "OPEN",
                    "ASSIGNED",
                    "IN_PROGRESS",
                    "CLOSED");

    private final String workOrderId;
    private final String bikeId;
    private final String maintenanceIssueId;
    private final String description;
    private String assignedTechnicianId;
    private String status;
    private final LocalDate createdDate;

    public WorkOrder(
            String workOrderId,
            String bikeId,
            String maintenanceIssueId,
            String description,
            String assignedTechnicianId,
            String status,
            LocalDate createdDate) {

        this.workOrderId =
                requireText(workOrderId, "Work order ID");

        this.bikeId =
                requireText(bikeId, "Bike ID");

        this.maintenanceIssueId =
                requireText(
                        maintenanceIssueId,
                        "Maintenance issue ID");

        this.description =
                requireText(description, "Description");

        if (status == null
                || !VALID_STATUSES.contains(status)) {

            throw new IllegalArgumentException(
                    "Invalid work-order status: " + status);
        }

        if (createdDate == null) {
            throw new IllegalArgumentException(
                    "Created date is required.");
        }

        this.assignedTechnicianId =
                normalizeOptional(
                        assignedTechnicianId);

        this.status = status;
        this.createdDate = createdDate;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getMaintenanceIssueId() {
        return maintenanceIssueId;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void assignTechnician(
            String technicianId) {

        if ("CLOSED".equals(status)) {
            throw new IllegalStateException(
                    "A closed work order cannot be reassigned.");
        }

        assignedTechnicianId =
                requireText(
                        technicianId,
                        "Technician ID");

        status = "ASSIGNED";
    }

    public void startWork() {
        if (!"OPEN".equals(status)
                && !"ASSIGNED".equals(status)) {

            throw new IllegalStateException(
                    "Only an open or assigned work order can be started.");
        }

        status = "IN_PROGRESS";
    }

    public void close() {
        if (!"IN_PROGRESS".equals(status)) {
            throw new IllegalStateException(
                    "Only an in-progress work order can be closed.");
        }

        status = "CLOSED";
    }

    private static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required.");
        }

        return value.trim();
    }

    private static String normalizeOptional(
            String value) {

        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
