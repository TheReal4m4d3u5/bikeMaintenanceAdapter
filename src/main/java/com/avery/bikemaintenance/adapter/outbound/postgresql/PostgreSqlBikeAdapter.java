package com.avery.bikemaintenance.adapter.outbound.postgresql;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;

public class PostgreSqlBikeAdapter implements BikeRepository {

    private UnsupportedOperationException underConstruction() {
        return new UnsupportedOperationException(
                "PostgreSQL repository is under construction.");
    }

    @Override
    public Optional<Bike> findById(String bikeId) {
        throw underConstruction();
    }

    @Override
    public List<Bike> findAll() {
        throw underConstruction();
    }

    @Override
    public Bike save(Bike bike) {
        throw underConstruction();
    }

    @Override
    public boolean existsById(String bikeId) {
        throw underConstruction();
    }
}