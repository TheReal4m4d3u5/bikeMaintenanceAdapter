package com.avery.bikemaintenance.application.service;

import java.util.List;
import java.util.Optional;

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
    public Bike saveBike(Bike bike) {
        return bikeRepository.save(bike);
    }
}