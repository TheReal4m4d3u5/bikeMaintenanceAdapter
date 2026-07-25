package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;
import com.fasterxml.jackson.databind.JsonNode;

public class GraphQlMaintenanceIssueRepositoryAdapter
        implements MaintenanceIssueRepository {

    private static final String FIELDS = """
            maintenanceIssueId
            bikeId
            reportedByUserId
            sourceType
            description
            severity
            status
            reportedDate
            """;

    private final RemoteGraphQlClient client;

    public GraphQlMaintenanceIssueRepositoryAdapter(
            RemoteGraphQlClient client) {

        this.client = client;
    }

    @Override
    public Optional<MaintenanceIssue> findById(
            String maintenanceIssueId) {

        JsonNode data = client.execute(
                """
                query MaintenanceIssueById(
                    $maintenanceIssueId: ID!
                ) {
                    maintenanceIssueById(
                        maintenanceIssueId:
                            $maintenanceIssueId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(
                        "maintenanceIssueId",
                        maintenanceIssueId));

        JsonNode node =
                data.get(
                        "maintenanceIssueById");

        return node == null || node.isNull()
                ? Optional.empty()
                : Optional.of(
                        GraphQlDomainMapper
                                .toIssue(node));
    }

    @Override
    public List<MaintenanceIssue> findAll() {
        return queryList(
                """
                query MaintenanceIssues {
                    maintenanceIssues {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(),
                "maintenanceIssues");
    }

    @Override
    public List<MaintenanceIssue> findByBikeId(
            String bikeId) {

        return queryList(
                """
                query MaintenanceIssuesByBikeId(
                    $bikeId: ID!
                ) {
                    maintenanceIssuesByBikeId(
                        bikeId: $bikeId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("bikeId", bikeId),
                "maintenanceIssuesByBikeId");
    }

    @Override
    public List<MaintenanceIssue>
            findByReportedByUserId(
                    String reportedByUserId) {

        return queryList(
                """
                query MaintenanceIssuesByReporter(
                    $reportedByUserId: ID!
                ) {
                    maintenanceIssuesByReportedByUserId(
                        reportedByUserId:
                            $reportedByUserId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(
                        "reportedByUserId",
                        reportedByUserId),
                "maintenanceIssuesByReportedByUserId");
    }

    @Override
    public MaintenanceIssue save(
            MaintenanceIssue issue) {

        Map<String, Object> input =
                new LinkedHashMap<>();

        input.put(
                "maintenanceIssueId",
                issue.getMaintenanceIssueId());
        input.put("bikeId", issue.getBikeId());
        input.put(
                "reportedByUserId",
                issue.getReportedByUserId());
        input.put(
                "sourceType",
                issue.getSourceType());
        input.put(
                "description",
                issue.getDescription());
        input.put("severity", issue.getSeverity());
        input.put("status", issue.getStatus());
        input.put(
                "reportedDate",
                issue.getReportedDate().toString());

        JsonNode data = client.execute(
                """
                mutation SaveMaintenanceIssue(
                    $input:
                        ProviderMaintenanceIssueInput!
                ) {
                    saveMaintenanceIssue(
                        input: $input
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("input", input));

        return GraphQlDomainMapper.toIssue(
                data.path(
                        "saveMaintenanceIssue"));
    }

    @Override
    public boolean existsById(
            String maintenanceIssueId) {

        return findById(
                maintenanceIssueId)
                .isPresent();
    }

    private List<MaintenanceIssue> queryList(
            String document,
            Map<String, Object> variables,
            String resultField) {

        JsonNode data =
                client.execute(
                        document,
                        variables);

        List<MaintenanceIssue> issues =
                new ArrayList<>();

        data.path(resultField)
                .forEach(node ->
                        issues.add(
                                GraphQlDomainMapper
                                        .toIssue(node)));

        return List.copyOf(issues);
    }
}
