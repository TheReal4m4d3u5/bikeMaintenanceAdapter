package com.avery.bikemaintenance.adapter.inbound.graphql;

public record BikeInput(
        String bikeId,
        String model,
        String condition,
        int rideCount,
        double mileage) {
}