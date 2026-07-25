package com.avery.bikemaintenance.memory;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryUserAccountRepository;
import com.avery.bikemaintenance.application.port.outbound.CredentialRepository;
import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.contract.UserAccountRepositoryContractTest;

class InMemoryUserAccountRepositoryContractTest
        extends UserAccountRepositoryContractTest {

    @Override
    protected UserAccountRepository
            createAccountRepository() {

        return new InMemoryUserAccountRepository();
    }

    @Override
    protected CredentialRepository
            credentialRepository(
                    UserAccountRepository
                            accountRepository) {

        return (CredentialRepository)
                accountRepository;
    }
}
