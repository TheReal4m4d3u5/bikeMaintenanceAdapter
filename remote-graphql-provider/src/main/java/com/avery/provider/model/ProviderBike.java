package com.avery.provider.model;

public record ProviderBike(
        String bikeId,
        String model,
        String condition,
        int rideCount,
        double mileage) {
}
