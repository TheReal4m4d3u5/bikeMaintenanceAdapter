package com.avery.bikemaintenance.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public BikeRepository bikeRepository(
            @Value("${app.repository.bike:memory}")
            String repositoryName) {

        RepositoryType repositoryType =
                RepositoryType.from(repositoryName);

        return BikeRepositoryFactory.create(repositoryType);
    }

    @Bean
    public WorkOrderRepository workOrderRepository(
            @Value("${app.repository.work-order:memory}")
            String repositoryName) {

        RepositoryType repositoryType =
                RepositoryType.from(repositoryName);

        return WorkOrderRepositoryFactory.create(
                repositoryType
        );
    }
    
    @Bean
    public MaintenanceIssueRepository maintenanceIssueRepository(
            @Value("${app.repository.maintenance-issue:memory}")
            String repositoryName) {

        RepositoryType repositoryType =
                RepositoryType.from(repositoryName);

        return MaintenanceIssueRepositoryFactory.create(
                repositoryType);
    }
    
}