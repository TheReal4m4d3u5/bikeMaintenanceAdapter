package com.avery.bikemaintenance.memory;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryBikeRepository;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.contract.BikeRepositoryContractTest;

class InMemoryBikeRepositoryContractTest
        extends BikeRepositoryContractTest {

    @Override
    protected BikeRepository createRepository() {
        return new InMemoryBikeRepository();
    }
}
