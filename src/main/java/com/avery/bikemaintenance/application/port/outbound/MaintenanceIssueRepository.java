package com.avery.bikemaintenance.application.port.outbound;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

public interface MaintenanceIssueRepository {

    Optional<MaintenanceIssue> findById(
            String maintenanceIssueId);

    List<MaintenanceIssue> findAll();

    List<MaintenanceIssue> findByBikeId(String bikeId);

    MaintenanceIssue save(MaintenanceIssue issue);

    boolean existsById(String maintenanceIssueId);
}