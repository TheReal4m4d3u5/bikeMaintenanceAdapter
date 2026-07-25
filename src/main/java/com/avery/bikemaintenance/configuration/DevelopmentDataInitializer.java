package com.avery.bikemaintenance.configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.avery.bikemaintenance.application.service.BikeService;
import com.avery.bikemaintenance.application.service.MaintenanceIssueService;
import com.avery.bikemaintenance.domain.model.Bike;

@Component
@Profile("!prod")
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

        if (!bikeService.findAllBikes().isEmpty()) {
            return;
        }

    	Bike bike = bikeService.createBike(
    	        "Metro Commuter",
    	        "AVAILABLE",
    	        240,
    	        1148.5);

    	maintenanceIssueService.createIssue(
    	        bike.getBikeId(),
    	        null,
    	        "DETECTED_FAULT",
    	        "Rear brake is not working",
    	        "HIGH");

        System.out.println(
                "Development sample data initialized.");
    }
}
