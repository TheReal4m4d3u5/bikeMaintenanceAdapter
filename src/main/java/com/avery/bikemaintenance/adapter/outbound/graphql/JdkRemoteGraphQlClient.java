package com.avery.bikemaintenance.adapter.outbound.graphql;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import com.avery.bikemaintenance.application.exception.RepositoryException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdkRemoteGraphQlClient
        implements RemoteGraphQlClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI endpoint;
    private final String bearerToken;

    public JdkRemoteGraphQlClient(
            ObjectMapper objectMapper,
            String endpoint,
            String bearerToken) {

        this.httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(5))
                        .build();

        this.objectMapper = objectMapper;
        this.endpoint = URI.create(endpoint);
        this.bearerToken =
                bearerToken == null
                        ? ""
                        : bearerToken.trim();
    }

    @Override
    public JsonNode execute(
            String document,
            Map<String, Object> variables) {

        Map<String, Object> requestBody =
                new LinkedHashMap<>();

        requestBody.put("query", document);
        requestBody.put(
                "variables",
                variables == null
                        ? Map.of()
                        : variables);

        HttpRequest.Builder requestBuilder =
                HttpRequest.newBuilder(endpoint)
                        .timeout(Duration.ofSeconds(15))
                        .header(
                                "Content-Type",
                                "application/json");

        if (!bearerToken.isBlank()) {
            requestBuilder.header(
                    "Authorization",
                    "Bearer " + bearerToken);
        }

        try {
            HttpRequest request =
                    requestBuilder
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    objectMapper
                                                        .writeValueAsString(
                                                                requestBody)))
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString());

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                throw new RepositoryException(
                        "Remote GraphQL provider returned HTTP "
                                + response.statusCode()
                                + ".");
            }

            JsonNode responseNode =
                    objectMapper.readTree(
                            response.body());

            JsonNode errors =
                    responseNode.path("errors");

            if (errors.isArray()
                    && !errors.isEmpty()) {

                throw new RepositoryException(
                        "Remote GraphQL provider error: "
                                + errors.toString());
            }

            JsonNode data =
                    responseNode.get("data");

            if (data == null || data.isNull()) {
                throw new RepositoryException(
                        "Remote GraphQL provider returned no data.");
            }

            return data;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new RepositoryException(
                    "Remote GraphQL request was interrupted.",
                    exception);
        } catch (JsonProcessingException exception) {
            throw new RepositoryException(
                    "Unable to process the remote GraphQL response.",
                    exception);
        } catch (IOException exception) {
            throw new RepositoryException(
                    "Unable to connect to the remote GraphQL provider.",
                    exception);
        }
    }
}
