package com.avery.bikemaintenance.sql;

import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.contract.BikeRepositoryContractTest;

class SqlBikeRepositoryContractTest
        extends BikeRepositoryContractTest {

    @Override
    protected BikeRepository createRepository() {
        return SqlTestFactory.create()
                .bikeRepository();
    }
}
