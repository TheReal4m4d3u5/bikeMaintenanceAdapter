package com.avery.bikemaintenance.adapter.inbound.graphql;

public record CreateBikeInput(
        String model,
        String condition,
        int rideCount,
        double mileage) {
}