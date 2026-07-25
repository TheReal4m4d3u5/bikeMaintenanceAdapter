package com.avery.bikemaintenance.adapter.inbound.graphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class RepositoryProviderGraphQlController {

    private final String repositoryProvider;

    public RepositoryProviderGraphQlController(
            @Value("${app.repository.provider:memory}")
            String repositoryProvider) {

        this.repositoryProvider =
                repositoryProvider;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @QueryMapping
    public String repositoryProvider() {
        return repositoryProvider;
    }
}
