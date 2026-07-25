package com.avery.bikemaintenance.adapter.inbound.graphql;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.avery.bikemaintenance.application.service.WorkOrderService;
import com.avery.bikemaintenance.domain.model.WorkOrder;

@Controller
public class WorkOrderGraphQlController {

    private final WorkOrderService workOrderService;

    public WorkOrderGraphQlController(
            WorkOrderService workOrderService) {

        this.workOrderService = workOrderService;
    }

    @QueryMapping
    public List<WorkOrder> workOrders() {
        return workOrderService.findAll();
    }
    
    @PreAuthorize("hasRole('TECHNICIAN')")
    @QueryMapping
    public List<WorkOrder> myWorkOrders(
            @AuthenticationPrincipal Jwt jwt) {

        String technicianId =
                jwt.getClaimAsString("userId");

        return workOrderService
                .findByAssignedTechnicianId(
                        technicianId);
    }
    

    @QueryMapping
    public WorkOrder workOrderById(
            @Argument String workOrderId) {

        return workOrderService.findById(workOrderId)
                .orElse(null);
    }

    @QueryMapping
    public List<WorkOrder> workOrdersByBikeId(
            @Argument String bikeId) {

        return workOrderService.findByBikeId(bikeId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public WorkOrder createWorkOrder(
            @Argument WorkOrderInput input) {

    	return workOrderService.createWorkOrder(
    	        input.bikeId(),
    	        input.maintenanceIssueId(),
    	        input.description(),
    	        input.assignedTechnicianId());
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @MutationMapping
    public WorkOrder startWork(
            @Argument String workOrderId,
            @AuthenticationPrincipal Jwt jwt) {

        String authenticatedUserId =
                jwt.getClaimAsString("userId");

        String authenticatedRole =
                jwt.getClaimAsString("role");

        return workOrderService.startWork(
                workOrderId,
                authenticatedUserId,
                authenticatedRole);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @MutationMapping
    public WorkOrder closeWorkOrder(
            @Argument String workOrderId,
            @AuthenticationPrincipal Jwt jwt) {

        String authenticatedUserId =
                jwt.getClaimAsString("userId");

        String authenticatedRole =
                jwt.getClaimAsString("role");

        return workOrderService.closeWorkOrder(
                workOrderId,
                authenticatedUserId,
                authenticatedRole);
    }
}