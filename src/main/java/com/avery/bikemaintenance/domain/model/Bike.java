package com.avery.bikemaintenance.domain.model;

public class Bike {

    private final String bikeId;
    private final String model;
    private String condition;
    private final int rideCount;
    private final double mileage;

    public Bike(
            String bikeId,
            String model,
            String condition,
            int rideCount,
            double mileage) {

        if (bikeId == null || bikeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Bike ID is required.");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException(
                    "Bike model is required.");
        }

        if (condition == null || condition.isBlank()) {
            throw new IllegalArgumentException(
                    "Bike condition is required.");
        }

        if (rideCount < 0) {
            throw new IllegalArgumentException(
                    "Ride count cannot be negative.");
        }

        if (mileage < 0) {
            throw new IllegalArgumentException(
                    "Mileage cannot be negative.");
        }

        this.bikeId = bikeId;
        this.model = model;
        this.condition = condition;
        this.rideCount = rideCount;
        this.mileage = mileage;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getModel() {
        return model;
    }

    public String getCondition() {
        return condition;
    }
    
    public void startRepair() {

        if ("RETIRED".equals(condition)) {
            throw new IllegalStateException(
                    "A retired bike cannot be placed under repair.");
        }

        condition = "UNDER_REPAIR";
    }

    public int getRideCount() {
        return rideCount;
    }

    public double getMileage() {
        return mileage;
    }
}