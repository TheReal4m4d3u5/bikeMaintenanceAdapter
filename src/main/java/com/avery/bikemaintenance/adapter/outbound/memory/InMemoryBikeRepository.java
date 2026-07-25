package com.avery.bikemaintenance.adapter.outbound.memory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;

public class InMemoryBikeRepository
        implements BikeRepository {

    private final Map<String, Bike> bikes =
            new ConcurrentHashMap<>();

    @Override
    public Optional<Bike> findById(String bikeId) {
        return Optional.ofNullable(bikes.get(bikeId));
    }

    @Override
    public List<Bike> findAll() {
        return bikes.values()
                .stream()
                .sorted(Comparator.comparing(Bike::getBikeId))
                .toList();
    }

    @Override
    public Bike save(Bike bike) {
        bikes.put(bike.getBikeId(), bike);
        return bike;
    }

    @Override
    public boolean existsById(String bikeId) {
        return bikes.containsKey(bikeId);
    }
}
