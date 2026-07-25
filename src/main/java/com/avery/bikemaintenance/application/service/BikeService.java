package com.avery.bikemaintenance.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.inbound.BikeUseCase;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;

@Service
public class BikeService implements BikeUseCase {

    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @Override
    public Optional<Bike> findBikeById(String bikeId) {
        return bikeRepository.findById(bikeId);
    }

    @Override
    public List<Bike> findAllBikes() {
        return bikeRepository.findAll();
    }

    @Override
    public Bike createBike(
            String model,
            String condition,
            int rideCount,
            double mileage) {

        String bikeId =
                "BIKE-" + UUID.randomUUID().toString();

        Bike bike = new Bike(
                bikeId,
                model,
                condition,
                rideCount,
                mileage);

        return bikeRepository.save(bike);
    }

    @Override
    public Bike updateBike(
            String bikeId,
            String model,
            String condition,
            int rideCount,
            double mileage) {

        if (!bikeRepository.existsById(bikeId)) {
            throw new IllegalArgumentException(
                    "Bike does not exist: " + bikeId);
        }

        Bike bike = new Bike(
                bikeId,
                model,
                condition,
                rideCount,
                mileage);

        return bikeRepository.save(bike);
    }
}