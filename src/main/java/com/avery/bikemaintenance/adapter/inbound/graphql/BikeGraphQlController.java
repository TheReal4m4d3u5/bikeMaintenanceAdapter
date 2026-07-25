package com.avery.bikemaintenance.adapter.inbound.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.avery.bikemaintenance.application.service.BikeService;
import com.avery.bikemaintenance.domain.model.Bike;

@Controller
public class BikeGraphQlController {

    private final BikeService bikeService;

    public BikeGraphQlController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @QueryMapping
    public List<Bike> bikes() {
        return bikeService.findAllBikes();
    }

    @QueryMapping
    public Bike bikeById(@Argument String bikeId) {
        return bikeService.findBikeById(bikeId)
                .orElse(null);
    }

    @MutationMapping
    public Bike saveBike(@Argument BikeInput input) {
        Bike bike = new Bike(
                input.bikeId(),
                input.model(),
                input.condition(),
                input.rideCount(),
                input.mileage());

        return bikeService.saveBike(bike);
    }
}