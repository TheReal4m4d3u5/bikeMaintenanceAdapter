package com.avery.bikemaintenance.application.port.inbound;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.domain.model.Bike;

public interface BikeUseCase {

    Optional<Bike> findBikeById(String bikeId);

    List<Bike> findAllBikes();

    Bike createBike(
            String model,
            String condition,
            int rideCount,
            double mileage);

    Bike updateBike(
            String bikeId,
            String model,
            String condition,
            int rideCount,
            double mileage);
}