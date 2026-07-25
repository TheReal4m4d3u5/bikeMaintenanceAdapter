package com.avery.bikemaintenance.adapter.inbound.graphql;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.avery.bikemaintenance.application.service.MaintenanceIssueService;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

@Controller
public class MaintenanceIssueGraphQlController {

    private final MaintenanceIssueService maintenanceIssueService;

    public MaintenanceIssueGraphQlController(
            MaintenanceIssueService maintenanceIssueService) {

        this.maintenanceIssueService = maintenanceIssueService;
    }

    @QueryMapping
    public List<MaintenanceIssue> maintenanceIssues() {
        return maintenanceIssueService.findAll();
    }

    @PreAuthorize("hasRole('USER')")
    @QueryMapping
    public List<MaintenanceIssue> myMaintenanceIssues(
            @AuthenticationPrincipal Jwt jwt) {

        String reportedByUserId =
                jwt.getClaimAsString("userId");

        return maintenanceIssueService
                .findByReportedByUserId(
                        reportedByUserId);
    }
    
    @QueryMapping
    public MaintenanceIssue maintenanceIssueById(
            @Argument String maintenanceIssueId) {

        return maintenanceIssueService
                .findById(maintenanceIssueId)
                .orElse(null);
    }

    @QueryMapping
    public List<MaintenanceIssue> maintenanceIssuesByBikeId(
            @Argument String bikeId) {

        return maintenanceIssueService.findByBikeId(bikeId);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public MaintenanceIssue createMaintenanceIssue(
            @Argument MaintenanceIssueInput input,
            @AuthenticationPrincipal Jwt jwt) {

        String reportedByUserId =
                jwt.getClaimAsString("userId");

        
        return maintenanceIssueService.createIssue(
                input.bikeId(),
                reportedByUserId,
                input.sourceType(),
                input.description(),
                input.severity());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @MutationMapping
    public MaintenanceIssue resolveMaintenanceIssue(
            @Argument String maintenanceIssueId) {

        return maintenanceIssueService.resolveIssue(
                maintenanceIssueId);
    }
}