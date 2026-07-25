package com.avery.bikemaintenance.memory;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryWorkOrderRepository;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.contract.WorkOrderRepositoryContractTest;

class InMemoryWorkOrderRepositoryContractTest
        extends WorkOrderRepositoryContractTest {

    @Override
    protected WorkOrderRepository createRepository() {
        return new InMemoryWorkOrderRepository();
    }
}
