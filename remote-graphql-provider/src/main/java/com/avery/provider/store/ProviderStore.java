package com.avery.provider.store;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.avery.provider.model.ProviderBike;
import com.avery.provider.model.ProviderMaintenanceIssue;
import com.avery.provider.model.ProviderWorkOrder;

@Component
public class ProviderStore {

    private final Map<String, ProviderBike> bikes =
            new ConcurrentHashMap<>();

    private final Map<String, ProviderMaintenanceIssue>
            issues = new ConcurrentHashMap<>();

    private final Map<String, ProviderWorkOrder>
            workOrders = new ConcurrentHashMap<>();

    public ProviderStore() {
        ProviderBike bike =
                new ProviderBike(
                        "REMOTE-BIKE-1001",
                        "Remote Metro Commuter",
                        "AVAILABLE",
                        75,
                        421.3);

        bikes.put(bike.bikeId(), bike);
    }

    public List<ProviderBike> bikes() {
        return bikes.values()
                .stream()
                .sorted(Comparator.comparing(
                        ProviderBike::bikeId))
                .toList();
    }

    public Optional<ProviderBike> bikeById(
            String bikeId) {

        return Optional.ofNullable(
                bikes.get(bikeId));
    }

    public ProviderBike saveBike(
            ProviderBike bike) {

        bikes.put(bike.bikeId(), bike);
        return bike;
    }

    public List<ProviderMaintenanceIssue> issues() {
        return issues.values()
                .stream()
                .sorted(Comparator.comparing(
                        ProviderMaintenanceIssue
                                ::maintenanceIssueId))
                .toList();
    }

    public Optional<ProviderMaintenanceIssue>
            issueById(String issueId) {

        return Optional.ofNullable(
                issues.get(issueId));
    }

    public List<ProviderMaintenanceIssue>
            issuesByBikeId(String bikeId) {

        return issues.values()
                .stream()
                .filter(issue ->
                        issue.bikeId().equals(bikeId))
                .toList();
    }

    public List<ProviderMaintenanceIssue>
            issuesByReporter(
                    String reporterId) {

        return issues.values()
                .stream()
                .filter(issue ->
                        reporterId.equals(
                                issue.reportedByUserId()))
                .toList();
    }

    public ProviderMaintenanceIssue saveIssue(
            ProviderMaintenanceIssue issue) {

        issues.put(
                issue.maintenanceIssueId(),
                issue);

        return issue;
    }

    public List<ProviderWorkOrder> workOrders() {
        return workOrders.values()
                .stream()
                .sorted(Comparator.comparing(
                        ProviderWorkOrder
                                ::workOrderId))
                .toList();
    }

    public Optional<ProviderWorkOrder>
            workOrderById(String workOrderId) {

        return Optional.ofNullable(
                workOrders.get(workOrderId));
    }

    public List<ProviderWorkOrder>
            workOrdersByBikeId(String bikeId) {

        return workOrders.values()
                .stream()
                .filter(workOrder ->
                        workOrder.bikeId()
                                .equals(bikeId))
                .toList();
    }

    public List<ProviderWorkOrder>
            workOrdersByTechnician(
                    String technicianId) {

        return workOrders.values()
                .stream()
                .filter(workOrder ->
                        technicianId.equals(
                                workOrder
                                        .assignedTechnicianId()))
                .toList();
    }

    public ProviderWorkOrder saveWorkOrder(
            ProviderWorkOrder workOrder) {

        workOrders.put(
                workOrder.workOrderId(),
                workOrder);

        return workOrder;
    }
}
