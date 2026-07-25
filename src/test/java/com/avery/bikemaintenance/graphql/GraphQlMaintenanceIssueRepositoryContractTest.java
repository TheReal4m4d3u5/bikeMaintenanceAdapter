package com.avery.bikemaintenance.graphql;

import com.avery.bikemaintenance.adapter.outbound.graphql.GraphQlMaintenanceIssueRepositoryAdapter;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.contract.MaintenanceIssueRepositoryContractTest;

class GraphQlMaintenanceIssueRepositoryContractTest
        extends MaintenanceIssueRepositoryContractTest {

    @Override
    protected MaintenanceIssueRepository
            createRepository() {

        return new GraphQlMaintenanceIssueRepositoryAdapter(
                new FakeRemoteGraphQlClient());
    }
}
