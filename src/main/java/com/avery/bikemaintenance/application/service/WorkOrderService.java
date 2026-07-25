package com.avery.bikemaintenance.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.WorkOrder;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;


@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final BikeRepository bikeRepository;
    private final MaintenanceIssueRepository issueRepository;
    
    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            BikeRepository bikeRepository,
            MaintenanceIssueRepository issueRepository) {

        this.workOrderRepository = workOrderRepository;
        this.bikeRepository = bikeRepository;
        this.issueRepository = issueRepository;
    }

    public WorkOrder createWorkOrder(
            String workOrderId,
            String bikeId,
            String maintenanceIssueId,
            String description,
            String assignedTechnician) {

        if (!bikeRepository.existsById(bikeId)) {
            throw new IllegalArgumentException(
                    "Bike does not exist: " + bikeId);
        }

        MaintenanceIssue issue = issueRepository
                .findById(maintenanceIssueId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Maintenance issue does not exist: "
                                        + maintenanceIssueId));

        if (!issue.getBikeId().equals(bikeId)) {
            throw new IllegalArgumentException(
                    "Maintenance issue does not belong to bike: "
                            + bikeId);
        }

        if (workOrderRepository.existsById(workOrderId)) {
            throw new IllegalArgumentException(
                    "Work order already exists: " + workOrderId);
        }

        String status =
                assignedTechnician == null
                        || assignedTechnician.isBlank()
                        ? "OPEN"
                        : "ASSIGNED";

        WorkOrder workOrder = new WorkOrder(
                workOrderId,
                bikeId,
                maintenanceIssueId,
                description,
                assignedTechnician,
                status,
                LocalDate.now());

        WorkOrder savedWorkOrder =
                workOrderRepository.save(workOrder);

        issue.markWorkOrderCreated();
        issueRepository.save(issue);

        return savedWorkOrder;
    }

    public Optional<WorkOrder> findById(
            String workOrderId) {

        return workOrderRepository.findById(workOrderId);
    }

    public List<WorkOrder> findAll() {
        return workOrderRepository.findAll();
    }

    public List<WorkOrder> findByBikeId(String bikeId) {
        return workOrderRepository.findByBikeId(bikeId);
    }

    public WorkOrder startWork(String workOrderId) {
        WorkOrder workOrder = requireWorkOrder(workOrderId);
        workOrder.startWork();

        return workOrderRepository.save(workOrder);
    }

    public WorkOrder closeWorkOrder(String workOrderId) {
        WorkOrder workOrder = requireWorkOrder(workOrderId);
        workOrder.close();

        return workOrderRepository.save(workOrder);
    }

    private WorkOrder requireWorkOrder(
            String workOrderId) {

        return workOrderRepository.findById(workOrderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Work order does not exist: "
                                        + workOrderId
                        )
                );
    }
}