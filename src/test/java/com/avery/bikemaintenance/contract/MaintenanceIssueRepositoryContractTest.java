package com.avery.bikemaintenance.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

public abstract class MaintenanceIssueRepositoryContractTest {

    private MaintenanceIssueRepository repository;

    protected abstract MaintenanceIssueRepository
            createRepository();

    @BeforeEach
    void setUpRepository() {
        repository = createRepository();
    }

    @Test
    void missingIssueReturnsEmpty() {
        assertTrue(
                repository.findById("missing").isEmpty());
    }

    @Test
    void supportsBikeAndReporterQueries() {
        repository.save(
                issue(
                        "ISSUE-1",
                        "BIKE-1",
                        "USER-1"));

        repository.save(
                issue(
                        "ISSUE-2",
                        "BIKE-2",
                        "USER-1"));

        assertEquals(
                1,
                repository.findByBikeId(
                        "BIKE-1")
                        .size());

        assertEquals(
                2,
                repository.findByReportedByUserId(
                        "USER-1")
                        .size());
    }

    @Test
    void savingSameIdReplacesStoredValue() {
        MaintenanceIssue issue =
                issue(
                        "ISSUE-1",
                        "BIKE-1",
                        "USER-1");

        repository.save(issue);
        issue.markWorkOrderCreated();
        repository.save(issue);

        assertEquals(
                "WORK_ORDER_CREATED",
                repository.findById("ISSUE-1")
                        .orElseThrow()
                        .getStatus());

        assertEquals(1, repository.findAll().size());
    }

    private static MaintenanceIssue issue(
            String issueId,
            String bikeId,
            String reporterId) {

        return new MaintenanceIssue(
                issueId,
                bikeId,
                reporterId,
                "USER_COMPLAINT",
                "Brake concern",
                "MEDIUM",
                "OPEN",
                LocalDate.of(2026, 7, 25));
    }
}
