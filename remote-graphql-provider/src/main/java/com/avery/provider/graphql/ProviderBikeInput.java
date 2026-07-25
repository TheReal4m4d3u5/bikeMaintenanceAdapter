package com.avery.provider.graphql;

public record ProviderBikeInput(
        String bikeId,
        String model,
        String condition,
        int rideCount,
        double mileage) {
}
