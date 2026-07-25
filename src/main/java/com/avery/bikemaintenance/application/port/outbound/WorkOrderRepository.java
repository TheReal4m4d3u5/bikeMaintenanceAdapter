package com.avery.bikemaintenance.application.port.outbound;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.domain.model.WorkOrder;

public interface WorkOrderRepository {

    Optional<WorkOrder> findById(String workOrderId);

    List<WorkOrder> findAll();

    List<WorkOrder> findByBikeId(String bikeId);

    List<WorkOrder> findByAssignedTechnicianId(
            String assignedTechnicianId);

    WorkOrder save(WorkOrder workOrder);

    boolean existsById(String workOrderId);
}