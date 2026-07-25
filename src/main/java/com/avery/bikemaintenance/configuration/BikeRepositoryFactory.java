package com.avery.bikemaintenance.configuration;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryBikeRepository;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;

public final class BikeRepositoryFactory {

    private BikeRepositoryFactory() {
        // Prevent creation of factory objects.
    }

    public static BikeRepository create(
            RepositoryType repositoryType) {

        return switch (repositoryType) {
            case MEMORY -> new InMemoryBikeRepository();

            case POSTGRESQL -> throw new IllegalStateException(
                    "PostgreSQL repository is not ready yet.");
        };
    }
}