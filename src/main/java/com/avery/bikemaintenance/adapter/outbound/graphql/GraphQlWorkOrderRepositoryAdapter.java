package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.WorkOrder;
import com.fasterxml.jackson.databind.JsonNode;

public class GraphQlWorkOrderRepositoryAdapter
        implements WorkOrderRepository {

    private static final String FIELDS = """
            workOrderId
            bikeId
            maintenanceIssueId
            description
            assignedTechnicianId
            status
            createdDate
            """;

    private final RemoteGraphQlClient client;

    public GraphQlWorkOrderRepositoryAdapter(
            RemoteGraphQlClient client) {

        this.client = client;
    }

    @Override
    public Optional<WorkOrder> findById(
            String workOrderId) {

        JsonNode data = client.execute(
                """
                query WorkOrderById(
                    $workOrderId: ID!
                ) {
                    workOrderById(
                        workOrderId: $workOrderId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(
                        "workOrderId",
                        workOrderId));

        JsonNode node =
                data.get("workOrderById");

        return node == null || node.isNull()
                ? Optional.empty()
                : Optional.of(
                        GraphQlDomainMapper
                                .toWorkOrder(node));
    }

    @Override
    public List<WorkOrder> findAll() {
        return queryList(
                """
                query WorkOrders {
                    workOrders {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(),
                "workOrders");
    }

    @Override
    public List<WorkOrder> findByBikeId(
            String bikeId) {

        return queryList(
                """
                query WorkOrdersByBikeId(
                    $bikeId: ID!
                ) {
                    workOrdersByBikeId(
                        bikeId: $bikeId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("bikeId", bikeId),
                "workOrdersByBikeId");
    }

    @Override
    public List<WorkOrder>
            findByAssignedTechnicianId(
                    String assignedTechnicianId) {

        return queryList(
                """
                query WorkOrdersByTechnician(
                    $assignedTechnicianId: ID!
                ) {
                    workOrdersByAssignedTechnicianId(
                        assignedTechnicianId:
                            $assignedTechnicianId
                    ) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of(
                        "assignedTechnicianId",
                        assignedTechnicianId),
                "workOrdersByAssignedTechnicianId");
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        Map<String, Object> input =
                new LinkedHashMap<>();

        input.put(
                "workOrderId",
                workOrder.getWorkOrderId());
        input.put("bikeId", workOrder.getBikeId());
        input.put(
                "maintenanceIssueId",
                workOrder.getMaintenanceIssueId());
        input.put(
                "description",
                workOrder.getDescription());
        input.put(
                "assignedTechnicianId",
                workOrder
                        .getAssignedTechnicianId());
        input.put("status", workOrder.getStatus());
        input.put(
                "createdDate",
                workOrder.getCreatedDate().toString());

        JsonNode data = client.execute(
                """
                mutation SaveWorkOrder(
                    $input:
                        ProviderWorkOrderInput!
                ) {
                    saveWorkOrder(input: $input) {
                        %s
                    }
                }
                """.formatted(FIELDS),
                Map.of("input", input));

        return GraphQlDomainMapper.toWorkOrder(
                data.path("saveWorkOrder"));
    }

    @Override
    public boolean existsById(
            String workOrderId) {

        return findById(workOrderId)
                .isPresent();
    }

    private List<WorkOrder> queryList(
            String document,
            Map<String, Object> variables,
            String resultField) {

        JsonNode data =
                client.execute(
                        document,
                        variables);

        List<WorkOrder> workOrders =
                new ArrayList<>();

        data.path(resultField)
                .forEach(node ->
                        workOrders.add(
                                GraphQlDomainMapper
                                        .toWorkOrder(node)));

        return List.copyOf(workOrders);
    }
}
