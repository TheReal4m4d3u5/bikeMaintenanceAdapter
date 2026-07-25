package com.avery.bikemaintenance.adapter.outbound.memory;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.WorkOrder;

@Repository
@ConditionalOnProperty(
        name = "app.repository.work-order",
        havingValue = "memory",
        matchIfMissing = true)
public class InMemoryWorkOrderRepository
        implements WorkOrderRepository {

    private final Map<String, WorkOrder> workOrders =
            new ConcurrentHashMap<>();

    @Override
    public Optional<WorkOrder> findById(String workOrderId) {
        return Optional.ofNullable(
                workOrders.get(workOrderId)
        );
    }

    @Override
    public List<WorkOrder> findAll() {
        return List.copyOf(workOrders.values());
    }
    
    @Override
    public List<WorkOrder> findByAssignedTechnicianId(
            String assignedTechnicianId) {

        return workOrders.values()
                .stream()
                .filter(workOrder ->
                        assignedTechnicianId.equals(
                                workOrder.getAssignedTechnicianId()))
                .toList();
    }

    @Override
    public List<WorkOrder> findByBikeId(String bikeId) {
        return workOrders.values()
                .stream()
                .filter(workOrder ->
                        workOrder.getBikeId().equals(bikeId))
                .toList();
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        workOrders.put(
                workOrder.getWorkOrderId(),
                workOrder
        );

        return workOrder;
    }

    @Override
    public boolean existsById(String workOrderId) {
        return workOrders.containsKey(workOrderId);
    }
}