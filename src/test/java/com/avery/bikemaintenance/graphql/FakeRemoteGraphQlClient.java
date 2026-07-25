package com.avery.bikemaintenance.graphql;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.avery.bikemaintenance.adapter.outbound.graphql.RemoteGraphQlClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class FakeRemoteGraphQlClient
        implements RemoteGraphQlClient {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    private final Map<String, Map<String, Object>>
            bikes = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Object>>
            issues = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Object>>
            workOrders = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public JsonNode execute(
            String document,
            Map<String, Object> variables) {

        Map<String, Object> data =
                new LinkedHashMap<>();

        if (document.contains(
                "mutation SaveBike")) {

            Map<String, Object> input =
                    new LinkedHashMap<>(
                            (Map<String, Object>)
                                    variables.get("input"));

            bikes.put(
                    (String) input.get("bikeId"),
                    input);

            data.put("saveBike", input);
        } else if (document.contains(
                "query BikeById")) {

            data.put(
                    "bikeById",
                    bikes.get(
                            variables.get("bikeId")));
        } else if (document.contains(
                "query Bikes")) {

            data.put(
                    "bikes",
                    List.copyOf(
                            bikes.values()));
        } else if (document.contains(
                "mutation SaveMaintenanceIssue")) {

            Map<String, Object> input =
                    new LinkedHashMap<>(
                            (Map<String, Object>)
                                    variables.get("input"));

            issues.put(
                    (String) input.get(
                            "maintenanceIssueId"),
                    input);

            data.put(
                    "saveMaintenanceIssue",
                    input);
        } else if (document.contains(
                "query MaintenanceIssueById")) {

            data.put(
                    "maintenanceIssueById",
                    issues.get(
                            variables.get(
                                    "maintenanceIssueId")));
        } else if (document.contains(
                "query MaintenanceIssuesByBikeId")) {

            data.put(
                    "maintenanceIssuesByBikeId",
                    issues.values()
                            .stream()
                            .filter(issue ->
                                    variables.get("bikeId")
                                            .equals(
                                                    issue.get(
                                                            "bikeId")))
                            .toList());
        } else if (document.contains(
                "query MaintenanceIssuesByReporter")) {

            data.put(
                    "maintenanceIssuesByReportedByUserId",
                    issues.values()
                            .stream()
                            .filter(issue ->
                                    variables
                                            .get(
                                                    "reportedByUserId")
                                            .equals(
                                                    issue.get(
                                                            "reportedByUserId")))
                            .toList());
        } else if (document.contains(
                "query MaintenanceIssues")) {

            data.put(
                    "maintenanceIssues",
                    List.copyOf(
                            issues.values()));
        } else if (document.contains(
                "mutation SaveWorkOrder")) {

            Map<String, Object> input =
                    new LinkedHashMap<>(
                            (Map<String, Object>)
                                    variables.get("input"));

            workOrders.put(
                    (String) input.get(
                            "workOrderId"),
                    input);

            data.put("saveWorkOrder", input);
        } else if (document.contains(
                "query WorkOrderById")) {

            data.put(
                    "workOrderById",
                    workOrders.get(
                            variables.get(
                                    "workOrderId")));
        } else if (document.contains(
                "query WorkOrdersByBikeId")) {

            data.put(
                    "workOrdersByBikeId",
                    workOrders.values()
                            .stream()
                            .filter(workOrder ->
                                    variables.get("bikeId")
                                            .equals(
                                                    workOrder.get(
                                                            "bikeId")))
                            .toList());
        } else if (document.contains(
                "query WorkOrdersByTechnician")) {

            data.put(
                    "workOrdersByAssignedTechnicianId",
                    workOrders.values()
                            .stream()
                            .filter(workOrder ->
                                    variables
                                            .get(
                                                    "assignedTechnicianId")
                                            .equals(
                                                    workOrder.get(
                                                            "assignedTechnicianId")))
                            .toList());
        } else if (document.contains(
                "query WorkOrders")) {

            data.put(
                    "workOrders",
                    List.copyOf(
                            workOrders.values()));
        } else {
            throw new IllegalArgumentException(
                    "Unsupported test GraphQL document.");
        }

        return objectMapper.valueToTree(data);
    }
}
