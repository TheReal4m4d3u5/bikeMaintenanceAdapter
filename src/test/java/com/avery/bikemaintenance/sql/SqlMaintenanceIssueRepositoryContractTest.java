package com.avery.bikemaintenance.sql;

import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.contract.MaintenanceIssueRepositoryContractTest;

class SqlMaintenanceIssueRepositoryContractTest
        extends MaintenanceIssueRepositoryContractTest {

    @Override
    protected MaintenanceIssueRepository
            createRepository() {

        return SqlTestFactory.create()
                .maintenanceIssueRepository();
    }
}
