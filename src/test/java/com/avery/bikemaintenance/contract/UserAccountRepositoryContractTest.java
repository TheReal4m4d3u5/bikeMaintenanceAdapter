package com.avery.bikemaintenance.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avery.bikemaintenance.application.exception.DuplicateEmailException;
import com.avery.bikemaintenance.application.port.outbound.CredentialRepository;
import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;

public abstract class UserAccountRepositoryContractTest {

    private UserAccountRepository accountRepository;
    private CredentialRepository credentialRepository;

    protected abstract UserAccountRepository
            createAccountRepository();

    protected abstract CredentialRepository
            credentialRepository(
                    UserAccountRepository
                            accountRepository);

    @BeforeEach
    void setUpRepository() {
        accountRepository =
                createAccountRepository();

        credentialRepository =
                credentialRepository(
                        accountRepository);
    }

    @Test
    void savesAndFindsNormalizedEmail() {
        accountRepository.save(
                account(
                        "USER-1",
                        "Test@Example.com"));

        assertTrue(
                accountRepository
                        .findByEmail(
                                "test@example.com")
                        .isPresent());
    }

    @Test
    void rejectsDuplicateEmail() {
        accountRepository.save(
                account(
                        "USER-1",
                        "test@example.com"));

        assertThrows(
                DuplicateEmailException.class,
                () ->
                        accountRepository.save(
                                account(
                                        "USER-2",
                                        "TEST@example.com")));
    }

    @Test
    void credentialViewReturnsStoredHash() {
        UserAccount account =
                account(
                        "USER-1",
                        "test@example.com");

        accountRepository.save(account);

        var credential =
                credentialRepository
                        .findCredentialsByEmail(
                                account.getEmail())
                        .orElseThrow();

        assertEquals(
                account.getUserId(),
                credential.userId());

        assertEquals(
                account.getPasswordHash(),
                credential.passwordHash());
    }

    private static UserAccount account(
            String userId,
            String email) {

        return new UserAccount(
                userId,
                email,
                "Test User",
                "{noop}password",
                UserRole.USER);
    }
}
