package com.avery.bikemaintenance.adapter.outbound.memory;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.avery.bikemaintenance.application.exception.DuplicateEmailException;
import com.avery.bikemaintenance.application.port.outbound.CredentialRepository;
import com.avery.bikemaintenance.application.port.outbound.StoredCredential;
import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;

public class InMemoryUserAccountRepository
        implements UserAccountRepository, CredentialRepository {

    private final Map<String, UserAccount> accounts =
            new ConcurrentHashMap<>();

    @Override
    public UserAccount save(UserAccount userAccount) {
        validateUniqueEmail(userAccount);

        accounts.put(
                userAccount.getUserId(),
                userAccount);

        return userAccount;
    }

    @Override
    public Optional<UserAccount> findById(String userId) {
        return Optional.ofNullable(accounts.get(userId));
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        return accounts.values()
                .stream()
                .filter(account ->
                        account.getEmail().equals(normalizedEmail))
                .findFirst();
    }

    @Override
    public Optional<StoredCredential>
            findCredentialsByEmail(String email) {

        return findByEmail(email)
                .map(account ->
                        new StoredCredential(
                                account.getUserId(),
                                account.getPasswordHash(),
                                account.isEnabled()));
    }

    @Override
    public List<UserAccount> findAll() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparing(
                        UserAccount::getUserId))
                .toList();
    }

    @Override
    public List<UserAccount> findByRole(UserRole role) {
        return accounts.values()
                .stream()
                .filter(account ->
                        account.getRole() == role)
                .sorted(Comparator.comparing(
                        UserAccount::getUserId))
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    private void validateUniqueEmail(
            UserAccount userAccount) {

        findByEmail(userAccount.getEmail())
                .filter(existingAccount ->
                        !existingAccount.getUserId().equals(
                                userAccount.getUserId()))
                .ifPresent(existingAccount -> {
                    throw new DuplicateEmailException(
                            userAccount.getEmail());
                });
    }
}
