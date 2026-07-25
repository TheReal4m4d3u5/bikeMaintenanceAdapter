package com.avery.bikemaintenance.configuration;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryMaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;

public final class MaintenanceIssueRepositoryFactory {

    private MaintenanceIssueRepositoryFactory() {
    }

    public static MaintenanceIssueRepository create(
            RepositoryType repositoryType) {

        return switch (repositoryType) {
            case MEMORY ->
                    new InMemoryMaintenanceIssueRepository();

            case POSTGRESQL ->
                    throw new IllegalStateException(
                            "PostgreSQL maintenance issue "
                            + "repository is not ready yet.");
        };
    }
}