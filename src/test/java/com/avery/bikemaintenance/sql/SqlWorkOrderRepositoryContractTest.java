package com.avery.bikemaintenance.sql;

import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.contract.WorkOrderRepositoryContractTest;

class SqlWorkOrderRepositoryContractTest
        extends WorkOrderRepositoryContractTest {

    @Override
    protected WorkOrderRepository createRepository() {
        return SqlTestFactory.create()
                .workOrderRepository();
    }
}
