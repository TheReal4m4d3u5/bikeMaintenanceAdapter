package com.avery.bikemaintenance.graphql;

import com.avery.bikemaintenance.adapter.outbound.graphql.GraphQlBikeRepositoryAdapter;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.contract.BikeRepositoryContractTest;

class GraphQlBikeRepositoryContractTest
        extends BikeRepositoryContractTest {

    @Override
    protected BikeRepository createRepository() {
        return new GraphQlBikeRepositoryAdapter(
                new FakeRemoteGraphQlClient());
    }
}
