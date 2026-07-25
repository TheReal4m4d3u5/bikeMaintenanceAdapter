package com.avery.bikemaintenance.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.avery.bikemaintenance.application.service.BikeService;
import com.avery.bikemaintenance.application.service.MaintenanceIssueService;
import com.avery.bikemaintenance.domain.model.Bike;

@Component
@ConditionalOnProperty(
        name = "app.seed-data",
        havingValue = "true")
public class DevelopmentDataInitializer
        implements CommandLineRunner {

    private final BikeService bikeService;
    private final MaintenanceIssueService maintenanceIssueService;

    public DevelopmentDataInitializer(
            BikeService bikeService,
            MaintenanceIssueService maintenanceIssueService) {

        this.bikeService = bikeService;
        this.maintenanceIssueService = maintenanceIssueService;
    }

    @Override
    public void run(String... args) {

        Bike bike = new Bike(
                "BIKE-1001",
                "Metro Commuter",
                "AVAILABLE",
                240,
                1148.5);

        bikeService.saveBike(bike);

        maintenanceIssueService.createIssue(
                "ISSUE-1001",
                "BIKE-1001",
                "DETECTED_FAULT",
                "Rear brake is not applying enough pressure",
                "HIGH");

        System.out.println(
                "Development sample data initialized.");
    }
}