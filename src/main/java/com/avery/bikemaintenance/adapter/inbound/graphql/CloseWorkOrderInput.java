package com.avery.bikemaintenance.adapter.inbound.graphql;

public record CloseWorkOrderInput(
        String workOrderId,
        String resultingBikeCondition) {
}
