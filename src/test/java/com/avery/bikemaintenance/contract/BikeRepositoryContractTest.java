package com.avery.bikemaintenance.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;

public abstract class BikeRepositoryContractTest {

    private BikeRepository repository;

    protected abstract BikeRepository createRepository();

    @BeforeEach
    void setUpRepository() {
        repository = createRepository();
    }

    @Test
    void missingBikeReturnsEmpty() {
        assertTrue(
                repository.findById("missing").isEmpty());
        assertFalse(
                repository.existsById("missing"));
    }

    @Test
    void saveAndRetrieveBike() {
        Bike bike =
                new Bike(
                        "BIKE-1",
                        "Commuter",
                        "AVAILABLE",
                        10,
                        25.5);

        repository.save(bike);

        assertTrue(repository.existsById("BIKE-1"));
        assertEquals(
                "Commuter",
                repository.findById("BIKE-1")
                        .orElseThrow()
                        .getModel());
    }

    @Test
    void savingSameIdReplacesStoredValue() {
        repository.save(
                new Bike(
                        "BIKE-1",
                        "Original",
                        "AVAILABLE",
                        1,
                        2.0));

        repository.save(
                new Bike(
                        "BIKE-1",
                        "Updated",
                        "OUT_OF_SERVICE",
                        2,
                        3.0));

        Bike stored =
                repository.findById("BIKE-1")
                        .orElseThrow();

        assertEquals("Updated", stored.getModel());
        assertEquals(
                "OUT_OF_SERVICE",
                stored.getCondition());
        assertEquals(1, repository.findAll().size());
    }
}
