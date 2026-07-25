package com.avery.bikemaintenance.memory;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryMaintenanceIssueRepository;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.contract.MaintenanceIssueRepositoryContractTest;

class InMemoryMaintenanceIssueRepositoryContractTest
        extends MaintenanceIssueRepositoryContractTest {

    @Override
    protected MaintenanceIssueRepository createRepository() {
        return new InMemoryMaintenanceIssueRepository();
    }
}
