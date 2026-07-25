package com.avery.bikemaintenance.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.avery.bikemaintenance.adapter.outbound.graphql.GraphQlMaintenanceRepositoryFactory;
import com.avery.bikemaintenance.adapter.outbound.graphql.JdkRemoteGraphQlClient;
import com.avery.bikemaintenance.adapter.outbound.graphql.RemoteGraphQlClient;
import com.avery.bikemaintenance.adapter.outbound.memory.MemoryMaintenanceRepositoryFactory;
import com.avery.bikemaintenance.adapter.outbound.sql.SqlMaintenanceRepositoryFactory;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceRepositoryFactory;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MaintenanceRepositoryConfiguration {

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "memory",
            matchIfMissing = true)
    public MaintenanceRepositoryFactory
            memoryMaintenanceRepositoryFactory() {

        return new MemoryMaintenanceRepositoryFactory();
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "sql")
    public DataSource maintenanceDataSource(
            @Value("${app.sql.url}")
            String url,
            @Value("${app.sql.username}")
            String username,
            @Value("${app.sql.password}")
            String password,
            @Value("${app.sql.driver-class-name:org.postgresql.Driver}")
            String driverClassName) {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName(
                driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "sql")
    public JdbcTemplate maintenanceJdbcTemplate(
            DataSource maintenanceDataSource) {

        return new JdbcTemplate(
                maintenanceDataSource);
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "sql")
    public MaintenanceRepositoryFactory
            sqlMaintenanceRepositoryFactory(
                    JdbcTemplate maintenanceJdbcTemplate) {

        return new SqlMaintenanceRepositoryFactory(
                maintenanceJdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "graphql")
    public RemoteGraphQlClient
            remoteGraphQlClient(
                    ObjectMapper objectMapper,
                    @Value("${app.graphql-provider.url}")
                    String providerUrl,
                    @Value("${app.graphql-provider.token:}")
                    String providerToken) {

        return new JdkRemoteGraphQlClient(
                objectMapper,
                providerUrl,
                providerToken);
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.repository.provider",
            havingValue = "graphql")
    public MaintenanceRepositoryFactory
            graphQlMaintenanceRepositoryFactory(
                    RemoteGraphQlClient client) {

        return new GraphQlMaintenanceRepositoryFactory(
                client);
    }

    @Bean
    public BikeRepository bikeRepository(
            MaintenanceRepositoryFactory factory) {

        return factory.bikeRepository();
    }

    @Bean
    public MaintenanceIssueRepository
            maintenanceIssueRepository(
                    MaintenanceRepositoryFactory factory) {

        return factory
                .maintenanceIssueRepository();
    }

    @Bean
    public WorkOrderRepository workOrderRepository(
            MaintenanceRepositoryFactory factory) {

        return factory.workOrderRepository();
    }
}
