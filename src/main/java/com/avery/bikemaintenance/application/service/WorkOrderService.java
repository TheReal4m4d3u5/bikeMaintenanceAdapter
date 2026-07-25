package com.avery.bikemaintenance.application.service;

import com.avery.bikemaintenance.domain.model.Bike;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
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

	public WorkOrderService(WorkOrderRepository workOrderRepository, BikeRepository bikeRepository,
			MaintenanceIssueRepository issueRepository) {

		this.workOrderRepository = workOrderRepository;
		this.bikeRepository = bikeRepository;
		this.issueRepository = issueRepository;
	}

	public WorkOrder createWorkOrder(String bikeId, String maintenanceIssueId, String description,
			String assignedTechnicianId) {

		String workOrderId = UUID.randomUUID().toString();

		if (!bikeRepository.existsById(bikeId)) {
			throw new IllegalArgumentException("Bike does not exist: " + bikeId);
		}

		MaintenanceIssue issue = issueRepository.findById(maintenanceIssueId).orElseThrow(
				() -> new IllegalArgumentException("Maintenance issue does not exist: " + maintenanceIssueId));

		if (!issue.getBikeId().equals(bikeId)) {
			throw new IllegalArgumentException("Maintenance issue does not belong to bike: " + bikeId);
		}


		String status = assignedTechnicianId == null || assignedTechnicianId.isBlank() ? "OPEN" : "ASSIGNED";

		WorkOrder workOrder = new WorkOrder(
		        workOrderId,
		        bikeId,
		        maintenanceIssueId,
		        description,
		        assignedTechnicianId,
		        status,
		        LocalDate.now()
		);

		WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

		issue.markWorkOrderCreated();
		issueRepository.save(issue);

		return savedWorkOrder;
	}

	public Optional<WorkOrder> findById(String workOrderId) {

		return workOrderRepository.findById(workOrderId);
	}

	public List<WorkOrder> findAll() {
		return workOrderRepository.findAll();
	}
	
	public List<WorkOrder> findByAssignedTechnicianId(
	        String assignedTechnicianId) {

	    if (assignedTechnicianId == null
	            || assignedTechnicianId.isBlank()) {

	        throw new IllegalArgumentException(
	                "Technician ID is required.");
	    }

	    return workOrderRepository
	            .findByAssignedTechnicianId(
	                    assignedTechnicianId);
	}

	public List<WorkOrder> findByBikeId(String bikeId) {
		return workOrderRepository.findByBikeId(bikeId);
	}

	public WorkOrder startWork(
	        String workOrderId,
	        String authenticatedUserId,
	        String authenticatedRole) {

	    WorkOrder workOrder = findAuthorizedWorkOrder(
	            workOrderId,
	            authenticatedUserId,
	            authenticatedRole);

	    Bike bike = bikeRepository
	            .findById(workOrder.getBikeId())
	            .orElseThrow(() ->
	                    new IllegalArgumentException(
	                            "Bike does not exist: "
	                                    + workOrder.getBikeId()));

	    workOrder.startWork();
	    bike.startRepair();

	    bikeRepository.save(bike);

	    return workOrderRepository.save(workOrder);
	}

	public WorkOrder closeWorkOrder(
	        String workOrderId,
	        String authenticatedUserId,
	        String authenticatedRole) {

	    WorkOrder workOrder = findAuthorizedWorkOrder(
	            workOrderId,
	            authenticatedUserId,
	            authenticatedRole);

	    workOrder.close();

	    return workOrderRepository.save(workOrder);
	}

	private WorkOrder requireWorkOrder(String workOrderId) {

		return workOrderRepository.findById(workOrderId)
				.orElseThrow(() -> new IllegalArgumentException("Work order does not exist: " + workOrderId));

	}
	
	private WorkOrder findAuthorizedWorkOrder(
	        String workOrderId,
	        String authenticatedUserId,
	        String authenticatedRole) {

	    WorkOrder workOrder = workOrderRepository
	            .findById(workOrderId)
	            .orElseThrow(() ->
	                    new IllegalArgumentException(
	                            "Work order does not exist: "
	                                    + workOrderId));

	    if ("ADMIN".equals(authenticatedRole)) {
	        return workOrder;
	    }

	    boolean assignedToTechnician =
	            "TECHNICIAN".equals(authenticatedRole)
	            && authenticatedUserId != null
	            && authenticatedUserId.equals(
	                    workOrder.getAssignedTechnicianId());

	    if (!assignedToTechnician) {
	        throw new AccessDeniedException(
	                "You are not assigned to this work order.");
	    }

	    return workOrder;
	}
}