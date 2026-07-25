package com.avery.bikemaintenance.domain.model;

import java.time.LocalDate;

public class WorkOrder {

    private final String workOrderId;
    private final String bikeId;
    private String description;
    private String assignedTechnician;
    private String status;
    private final LocalDate createdDate;
    private final String maintenanceIssueId;
    
    public WorkOrder(
            String workOrderId,
            String bikeId,
            String maintenanceIssueId,
            String description,
            String assignedTechnician,
            String status,
            LocalDate createdDate) {

        if (workOrderId == null || workOrderId.isBlank()) {
            throw new IllegalArgumentException(
                    "Work order ID is required.");
        }

        if (bikeId == null || bikeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Bike ID is required.");
        }

        if (maintenanceIssueId == null
                || maintenanceIssueId.isBlank()) {
            throw new IllegalArgumentException(
                    "Maintenance issue ID is required.");
        }

        this.workOrderId = workOrderId;
        this.bikeId = bikeId;
        this.maintenanceIssueId = maintenanceIssueId;
        this.description = description;
        this.assignedTechnician = assignedTechnician;
        this.status = status;
        this.createdDate = createdDate;
    }

    
    public String getMaintenanceIssueId() {
        return maintenanceIssueId;
    }
    
    public String getWorkOrderId() {
        return workOrderId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void assignTechnician(String assignedTechnician) {
        if (assignedTechnician == null ||
                assignedTechnician.isBlank()) {

            throw new IllegalArgumentException(
                    "Technician is required.");
        }

        this.assignedTechnician = assignedTechnician;
        this.status = "ASSIGNED";
    }

    public void startWork() {
        this.status = "IN_PROGRESS";
    }

    public void close() {
        this.status = "CLOSED";
    }
}