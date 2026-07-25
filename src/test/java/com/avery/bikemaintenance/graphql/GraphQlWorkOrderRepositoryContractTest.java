package com.avery.bikemaintenance.graphql;

import com.avery.bikemaintenance.adapter.outbound.graphql.GraphQlWorkOrderRepositoryAdapter;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.contract.WorkOrderRepositoryContractTest;

class GraphQlWorkOrderRepositoryContractTest
        extends WorkOrderRepositoryContractTest {

    @Override
    protected WorkOrderRepository createRepository() {
        return new GraphQlWorkOrderRepositoryAdapter(
                new FakeRemoteGraphQlClient());
    }
}
