package com.avery.bikemaintenance.adapter.outbound.postgresql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bikes")
public class BikeEntity {

    @Id
    @Column(name = "bike_id", nullable = false, updatable = false)
    private String bikeId;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String condition;

    @Column(name = "ride_count", nullable = false)
    private int rideCount;

    @Column(nullable = false)
    private double mileage;

    protected BikeEntity() {
        // Required by JPA
    }

    public BikeEntity(
            String bikeId,
            String model,
            String condition,
            int rideCount,
            double mileage) {

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
}