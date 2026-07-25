package com.avery.bikemaintenance.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

@Service
public class MaintenanceIssueService {

    private final MaintenanceIssueRepository issueRepository;
    private final BikeRepository bikeRepository;

    public MaintenanceIssueService(
            MaintenanceIssueRepository issueRepository,
            BikeRepository bikeRepository) {

        this.issueRepository = issueRepository;
        this.bikeRepository = bikeRepository;
    }

    public MaintenanceIssue createIssue(
            String maintenanceIssueId,
            String bikeId,
            String sourceType,
            String description,
            String severity) {

        if (!bikeRepository.existsById(bikeId)) {
            throw new IllegalArgumentException(
                    "Bike does not exist: " + bikeId);
        }

        if (issueRepository.existsById(
                maintenanceIssueId)) {

            throw new IllegalArgumentException(
                    "Maintenance issue already exists: "
                            + maintenanceIssueId);
        }

        MaintenanceIssue issue =
                new MaintenanceIssue(
                        maintenanceIssueId,
                        bikeId,
                        sourceType,
                        description,
                        severity,
                        "OPEN",
                        LocalDate.now());

        return issueRepository.save(issue);
    }

    public Optional<MaintenanceIssue> findById(
            String maintenanceIssueId) {

        return issueRepository.findById(
                maintenanceIssueId);
    }

    public List<MaintenanceIssue> findAll() {
        return issueRepository.findAll();
    }

    public List<MaintenanceIssue> findByBikeId(
            String bikeId) {

        return issueRepository.findByBikeId(bikeId);
    }

    public MaintenanceIssue markWorkOrderCreated(
            String maintenanceIssueId) {

        MaintenanceIssue issue =
                requireIssue(maintenanceIssueId);

        issue.markWorkOrderCreated();

        return issueRepository.save(issue);
    }

    public MaintenanceIssue resolveIssue(
            String maintenanceIssueId) {

        MaintenanceIssue issue =
                requireIssue(maintenanceIssueId);

        issue.resolve();

        return issueRepository.save(issue);
    }

    private MaintenanceIssue requireIssue(
            String maintenanceIssueId) {

        return issueRepository
                .findById(maintenanceIssueId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Maintenance issue does not exist: "
                                        + maintenanceIssueId));
    }
}