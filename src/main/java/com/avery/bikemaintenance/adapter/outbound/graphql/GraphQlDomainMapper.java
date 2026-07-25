package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.time.LocalDate;

import com.avery.bikemaintenance.application.exception.RepositoryException;
import com.avery.bikemaintenance.domain.model.Bike;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;
import com.avery.bikemaintenance.domain.model.WorkOrder;
import com.fasterxml.jackson.databind.JsonNode;

final class GraphQlDomainMapper {

    private GraphQlDomainMapper() {
    }

    static Bike toBike(JsonNode node) {
        try {
            return new Bike(
                    requiredText(node, "bikeId"),
                    requiredText(node, "model"),
                    requiredText(node, "condition"),
                    node.path("rideCount").asInt(),
                    node.path("mileage").asDouble());
        } catch (RuntimeException exception) {
            throw mappingFailure(
                    "bike",
                    exception);
        }
    }

    static MaintenanceIssue toIssue(JsonNode node) {
        try {
            return new MaintenanceIssue(
                    requiredText(
                            node,
                            "maintenanceIssueId"),
                    requiredText(node, "bikeId"),
                    optionalText(
                            node,
                            "reportedByUserId"),
                    requiredText(
                            node,
                            "sourceType"),
                    requiredText(
                            node,
                            "description"),
                    requiredText(
                            node,
                            "severity"),
                    requiredText(node, "status"),
                    LocalDate.parse(
                            requiredText(
                                    node,
                                    "reportedDate")));
        } catch (RuntimeException exception) {
            throw mappingFailure(
                    "maintenance issue",
                    exception);
        }
    }

    static WorkOrder toWorkOrder(JsonNode node) {
        try {
            return new WorkOrder(
                    requiredText(
                            node,
                            "workOrderId"),
                    requiredText(node, "bikeId"),
                    requiredText(
                            node,
                            "maintenanceIssueId"),
                    requiredText(
                            node,
                            "description"),
                    optionalText(
                            node,
                            "assignedTechnicianId"),
                    requiredText(node, "status"),
                    LocalDate.parse(
                            requiredText(
                                    node,
                                    "createdDate")));
        } catch (RuntimeException exception) {
            throw mappingFailure(
                    "work order",
                    exception);
        }
    }

    private static RepositoryException
            mappingFailure(
                    String resource,
                    RuntimeException cause) {

        if (cause instanceof RepositoryException
                repositoryException) {

            return repositoryException;
        }

        return new RepositoryException(
                "Unable to map remote GraphQL "
                        + resource
                        + " data.",
                cause);
    }

    private static String requiredText(
            JsonNode node,
            String field) {

        String value =
                optionalText(node, field);

        if (value == null || value.isBlank()) {
            throw new RepositoryException(
                    "Remote GraphQL response is missing "
                            + field
                            + ".");
        }

        return value;
    }

    private static String optionalText(
            JsonNode node,
            String field) {

        JsonNode value = node.get(field);

        return value == null || value.isNull()
                ? null
                : value.asText();
    }
}
