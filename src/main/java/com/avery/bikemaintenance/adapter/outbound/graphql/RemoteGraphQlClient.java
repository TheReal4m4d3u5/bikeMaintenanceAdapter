package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public interface RemoteGraphQlClient {

    JsonNode execute(
            String document,
            Map<String, Object> variables);
}
