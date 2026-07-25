package com.avery.bikemaintenance.application.port.outbound;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.domain.model.Bike;

public interface BikeRepository {

    Optional<Bike> findById(String bikeId);

    List<Bike> findAll();

    Bike save(Bike bike);

    boolean existsById(String bikeId);
}