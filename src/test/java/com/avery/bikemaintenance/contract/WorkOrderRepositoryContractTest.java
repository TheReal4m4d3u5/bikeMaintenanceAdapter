package com.avery.bikemaintenance.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.WorkOrder;

public abstract class WorkOrderRepositoryContractTest {

    private WorkOrderRepository repository;

    protected abstract WorkOrderRepository
            createRepository();

    @BeforeEach
    void setUpRepository() {
        repository = createRepository();
    }

    @Test
    void missingWorkOrderReturnsEmpty() {
        assertTrue(
                repository.findById("missing").isEmpty());
    }

    @Test
    void supportsBikeAndTechnicianQueries() {
        repository.save(
                order(
                        "ORDER-1",
                        "BIKE-1",
                        "TECH-1"));

        repository.save(
                order(
                        "ORDER-2",
                        "BIKE-2",
                        "TECH-1"));

        assertEquals(
                1,
                repository.findByBikeId(
                        "BIKE-1")
                        .size());

        assertEquals(
                2,
                repository.findByAssignedTechnicianId(
                        "TECH-1")
                        .size());
    }

    @Test
    void savingSameIdReplacesStoredValue() {
        WorkOrder order =
                order(
                        "ORDER-1",
                        "BIKE-1",
                        "TECH-1");

        repository.save(order);
        order.startWork();
        repository.save(order);

        assertEquals(
                "IN_PROGRESS",
                repository.findById("ORDER-1")
                        .orElseThrow()
                        .getStatus());

        assertEquals(1, repository.findAll().size());
    }

    private static WorkOrder order(
            String orderId,
            String bikeId,
            String technicianId) {

        return new WorkOrder(
                orderId,
                bikeId,
                "ISSUE-" + orderId,
                "Repair bike",
                technicianId,
                "ASSIGNED",
                LocalDate.of(2026, 7, 25));
    }
}
