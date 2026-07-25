package com.avery.provider.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.avery.provider.model.ProviderBike;
import com.avery.provider.model.ProviderMaintenanceIssue;
import com.avery.provider.model.ProviderWorkOrder;
import com.avery.provider.store.ProviderStore;

@Controller
public class ProviderGraphQlController {

    private final ProviderStore store;

    public ProviderGraphQlController(
            ProviderStore store) {

        this.store = store;
    }

    @QueryMapping
    public List<ProviderBike> bikes() {
        return store.bikes();
    }

    @QueryMapping
    public ProviderBike bikeById(
            @Argument String bikeId) {

        return store.bikeById(bikeId)
                .orElse(null);
    }

    @QueryMapping
    public List<ProviderMaintenanceIssue>
            maintenanceIssues() {

        return store.issues();
    }

    @QueryMapping
    public ProviderMaintenanceIssue
            maintenanceIssueById(
                    @Argument
                    String maintenanceIssueId) {

        return store
                .issueById(
                        maintenanceIssueId)
                .orElse(null);
    }

    @QueryMapping
    public List<ProviderMaintenanceIssue>
            maintenanceIssuesByBikeId(
                    @Argument String bikeId) {

        return store.issuesByBikeId(
                bikeId);
    }

    @QueryMapping
    public List<ProviderMaintenanceIssue>
            maintenanceIssuesByReportedByUserId(
                    @Argument
                    String reportedByUserId) {

        return store.issuesByReporter(
                reportedByUserId);
    }

    @QueryMapping
    public List<ProviderWorkOrder> workOrders() {
        return store.workOrders();
    }

    @QueryMapping
    public ProviderWorkOrder workOrderById(
            @Argument String workOrderId) {

        return store
                .workOrderById(workOrderId)
                .orElse(null);
    }

    @QueryMapping
    public List<ProviderWorkOrder>
            workOrdersByBikeId(
                    @Argument String bikeId) {

        return store.workOrdersByBikeId(
                bikeId);
    }

    @QueryMapping
    public List<ProviderWorkOrder>
            workOrdersByAssignedTechnicianId(
                    @Argument
                    String assignedTechnicianId) {

        return store.workOrdersByTechnician(
                assignedTechnicianId);
    }

    @MutationMapping
    public ProviderBike saveBike(
            @Argument ProviderBikeInput input) {

        return store.saveBike(
                new ProviderBike(
                        input.bikeId(),
                        input.model(),
                        input.condition(),
                        input.rideCount(),
                        input.mileage()));
    }

    @MutationMapping
    public ProviderMaintenanceIssue
            saveMaintenanceIssue(
                    @Argument
                    ProviderMaintenanceIssueInput
                            input) {

        return store.saveIssue(
                new ProviderMaintenanceIssue(
                        input.maintenanceIssueId(),
                        input.bikeId(),
                        input.reportedByUserId(),
                        input.sourceType(),
                        input.description(),
                        input.severity(),
                        input.status(),
                        input.reportedDate()));
    }

    @MutationMapping
    public ProviderWorkOrder saveWorkOrder(
            @Argument
            ProviderWorkOrderInput input) {

        return store.saveWorkOrder(
                new ProviderWorkOrder(
                        input.workOrderId(),
                        input.bikeId(),
                        input.maintenanceIssueId(),
                        input.description(),
                        input.assignedTechnicianId(),
                        input.status(),
                        input.createdDate()));
    }
}
