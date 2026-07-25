package com.avery.bikemaintenance.configuration;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryWorkOrderRepository;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;

public final class WorkOrderRepositoryFactory {

    private WorkOrderRepositoryFactory() {
    }

    public static WorkOrderRepository create(
            RepositoryType repositoryType) {

        return switch (repositoryType) {
            case MEMORY ->
                    new InMemoryWorkOrderRepository();

            case POSTGRESQL ->
                    throw new IllegalStateException(
                            "PostgreSQL work order repository "
                            + "is not ready yet.");
        };
    }
}