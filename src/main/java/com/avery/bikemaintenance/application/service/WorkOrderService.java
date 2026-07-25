package com.avery.bikemaintenance.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.AuthenticatedUser;
import com.avery.bikemaintenance.domain.model.Bike;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;
import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;
import com.avery.bikemaintenance.domain.model.WorkOrder;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final BikeRepository bikeRepository;
    private final MaintenanceIssueRepository issueRepository;
    private final UserAccountRepository userAccountRepository;
    private final WorkOrderAccessPolicy accessPolicy;

    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            BikeRepository bikeRepository,
            MaintenanceIssueRepository issueRepository,
            UserAccountRepository userAccountRepository,
            WorkOrderAccessPolicy accessPolicy) {

        this.workOrderRepository = workOrderRepository;
        this.bikeRepository = bikeRepository;
        this.issueRepository = issueRepository;
        this.userAccountRepository = userAccountRepository;
        this.accessPolicy = accessPolicy;
    }

    public WorkOrder createWorkOrder(
            String bikeId,
            String maintenanceIssueId,
            String description,
            String assignedTechnicianId) {

        Bike bike = requireBike(bikeId);
        MaintenanceIssue issue =
                requireIssue(maintenanceIssueId);

        if (!issue.getBikeId().equals(
                bike.getBikeId())) {

            throw new IllegalArgumentException(
                    "Maintenance issue does not belong to bike: "
                            + bikeId);
        }

        if (!"OPEN".equals(issue.getStatus())) {
            throw new IllegalStateException(
                    "A work order can be created only for an open maintenance issue.");
        }

        String normalizedTechnicianId =
                normalizeTechnicianId(
                        assignedTechnicianId);

        if (normalizedTechnicianId != null) {
            requireTechnician(
                    normalizedTechnicianId);
        }

        String status =
                normalizedTechnicianId == null
                        ? "OPEN"
                        : "ASSIGNED";

        WorkOrder workOrder =
                new WorkOrder(
                        UUID.randomUUID().toString(),
                        bikeId,
                        maintenanceIssueId,
                        description,
                        normalizedTechnicianId,
                        status,
                        LocalDate.now());

        WorkOrder savedWorkOrder =
                workOrderRepository.save(
                        workOrder);

        issue.markWorkOrderCreated();
        issueRepository.save(issue);

        return savedWorkOrder;
    }

    public Optional<WorkOrder> findById(
            String workOrderId) {

        return workOrderRepository.findById(
                workOrderId);
    }

    public List<WorkOrder> findAll() {
        return workOrderRepository.findAll();
    }

    public List<WorkOrder>
            findByAssignedTechnicianId(
                    String technicianId) {

        if (technicianId == null
                || technicianId.isBlank()) {

            throw new IllegalArgumentException(
                    "Technician ID is required.");
        }

        return workOrderRepository
                .findByAssignedTechnicianId(
                        technicianId);
    }

    public List<WorkOrder> findByBikeId(
            String bikeId) {

        return workOrderRepository
                .findByBikeId(bikeId);
    }

    public WorkOrder startWork(
            String workOrderId,
            AuthenticatedUser authenticatedUser) {

        WorkOrder workOrder =
                requireAuthorizedWorkOrder(
                        workOrderId,
                        authenticatedUser);

        Bike bike =
                requireBike(
                        workOrder.getBikeId());

        workOrder.startWork();
        bike.startRepair();

        bikeRepository.save(bike);

        return workOrderRepository.save(
                workOrder);
    }

    public WorkOrder closeWorkOrder(
            String workOrderId,
            String resultingBikeCondition,
            AuthenticatedUser authenticatedUser) {

        WorkOrder workOrder =
                requireAuthorizedWorkOrder(
                        workOrderId,
                        authenticatedUser);

        Bike bike =
                requireBike(
                        workOrder.getBikeId());

        MaintenanceIssue issue =
                requireIssue(
                        workOrder
                                .getMaintenanceIssueId());

        workOrder.close();
        bike.completeRepair(
                resultingBikeCondition);
        issue.resolve();

        bikeRepository.save(bike);
        issueRepository.save(issue);

        return workOrderRepository.save(
                workOrder);
    }

    private WorkOrder requireAuthorizedWorkOrder(
            String workOrderId,
            AuthenticatedUser authenticatedUser) {

        WorkOrder workOrder =
                requireWorkOrder(workOrderId);

        accessPolicy.verifyCanModify(
                authenticatedUser,
                workOrder);

        return workOrder;
    }

    private WorkOrder requireWorkOrder(
            String workOrderId) {

        return workOrderRepository
                .findById(workOrderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Work order does not exist: "
                                        + workOrderId));
    }

    private Bike requireBike(String bikeId) {
        return bikeRepository
                .findById(bikeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bike does not exist: "
                                        + bikeId));
    }

    private MaintenanceIssue requireIssue(
            String maintenanceIssueId) {

        return issueRepository
                .findById(maintenanceIssueId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Maintenance issue does not exist: "
                                        + maintenanceIssueId));
    }

    private UserAccount requireTechnician(
            String technicianId) {

        UserAccount account =
                userAccountRepository
                        .findById(technicianId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Technician account does not exist: "
                                                + technicianId));

        if (account.getRole()
                != UserRole.TECHNICIAN) {

            throw new IllegalArgumentException(
                    "Assigned account is not a technician: "
                            + technicianId);
        }

        if (!account.isEnabled()) {
            throw new IllegalArgumentException(
                    "Assigned technician account is disabled: "
                            + technicianId);
        }

        return account;
    }

    private static String normalizeTechnicianId(
            String technicianId) {

        return technicianId == null
                || technicianId.isBlank()
                        ? null
                        : technicianId.trim();
    }
}
