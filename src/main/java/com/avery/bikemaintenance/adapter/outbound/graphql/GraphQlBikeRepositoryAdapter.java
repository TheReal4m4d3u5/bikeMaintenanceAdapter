package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;
import com.fasterxml.jackson.databind.JsonNode;

public class GraphQlBikeRepositoryAdapter
        implements BikeRepository {

    private static final String FIELDS = """
            bikeId
            model
            condition
            rideCount
            mileage
            """;

    private final RemoteGraphQlClient client;

    public GraphQlBikeRepositoryAdapter(
            RemoteGraphQlClient client) {

        this.client = client;
    }

    @Override
    public Optional<Bike> findById(String bikeId) {
        JsonNode data = client.execute(
                """
                query BikeById($bikeId: ID!) {
                    bikeById(bikeId: $bikeId) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("bikeId", bikeId));

        JsonNode node = data.get("bikeById");

        return node == null || node.isNull()
                ? Optional.empty()
                : Optional.of(
                        GraphQlDomainMapper
                                .toBike(node));
    }

    @Override
    public List<Bike> findAll() {
        JsonNode data = client.execute(
                """
                query Bikes {
                    bikes {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of());

        List<Bike> bikes = new ArrayList<>();

        data.path("bikes")
                .forEach(node ->
                        bikes.add(
                                GraphQlDomainMapper
                                        .toBike(node)));

        return List.copyOf(bikes);
    }

    @Override
    public Bike save(Bike bike) {
        Map<String, Object> input =
                new LinkedHashMap<>();

        input.put("bikeId", bike.getBikeId());
        input.put("model", bike.getModel());
        input.put(
                "condition",
                bike.getCondition());
        input.put(
                "rideCount",
                bike.getRideCount());
        input.put("mileage", bike.getMileage());

        JsonNode data = client.execute(
                """
                mutation SaveBike(
                    $input: ProviderBikeInput!
                ) {
                    saveBike(input: $input) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("input", input));

        return GraphQlDomainMapper.toBike(
                data.path("saveBike"));
    }

    @Override
    public boolean existsById(String bikeId) {
        return findById(bikeId).isPresent();
    }
}
