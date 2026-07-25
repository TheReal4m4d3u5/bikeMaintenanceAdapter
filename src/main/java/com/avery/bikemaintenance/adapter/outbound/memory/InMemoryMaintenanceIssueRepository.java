package com.avery.bikemaintenance.adapter.outbound.memory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

public class InMemoryMaintenanceIssueRepository
        implements MaintenanceIssueRepository {

    private final Map<String, MaintenanceIssue> issues =
            new ConcurrentHashMap<>();

    @Override
    public Optional<MaintenanceIssue> findById(
            String maintenanceIssueId) {

        return Optional.ofNullable(
                issues.get(maintenanceIssueId));
    }

    @Override
    public List<MaintenanceIssue> findAll() {
        return List.copyOf(issues.values());
    }

    @Override
    public List<MaintenanceIssue> findByBikeId(
            String bikeId) {

        return issues.values()
                .stream()
                .filter(issue ->
                        issue.getBikeId().equals(bikeId))
                .toList();
    }

    @Override
    public MaintenanceIssue save(
            MaintenanceIssue issue) {

        issues.put(
                issue.getMaintenanceIssueId(),
                issue);

        return issue;
    }

    @Override
    public boolean existsById(
            String maintenanceIssueId) {

        return issues.containsKey(maintenanceIssueId);
    }
}