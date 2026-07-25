package com.avery.bikemaintenance.domain.model;

import java.util.Set;

public class Bike {

    private static final Set<String> VALID_CONDITIONS =
            Set.of(
                    "AVAILABLE",
                    "DUE_FOR_SCHEDULED_MAINTENANCE",
                    "OUT_OF_SERVICE",
                    "UNDER_REPAIR",
                    "RETIRED");

    private static final Set<String>
            VALID_REPAIR_OUTCOMES =
                    Set.of(
                            "AVAILABLE",
                            "DUE_FOR_SCHEDULED_MAINTENANCE",
                            "OUT_OF_SERVICE",
                            "RETIRED");

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

        validateCondition(condition);

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

    public int getRideCount() {
        return rideCount;
    }

    public double getMileage() {
        return mileage;
    }

    public void startRepair() {
        if ("RETIRED".equals(condition)) {
            throw new IllegalStateException(
                    "A retired bike cannot be placed under repair.");
        }

        condition = "UNDER_REPAIR";
    }

    public void completeRepair(
            String resultingCondition) {

        if (!"UNDER_REPAIR".equals(condition)) {
            throw new IllegalStateException(
                    "The bike is not currently under repair.");
        }

        if (!VALID_REPAIR_OUTCOMES.contains(
                resultingCondition)) {

            throw new IllegalArgumentException(
                    "Invalid repair outcome: "
                            + resultingCondition);
        }

        condition = resultingCondition;
    }

    private static void validateCondition(
            String condition) {

        if (condition == null
                || condition.isBlank()
                || !VALID_CONDITIONS.contains(condition)) {

            throw new IllegalArgumentException(
                    "Invalid bike condition: " + condition);
        }
    }
}
