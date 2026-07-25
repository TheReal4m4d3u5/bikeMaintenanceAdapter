package com.avery.bikemaintenance.application.port.outbound;

import java.util.List;
import java.util.Optional;

import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;

public interface UserAccountRepository {

    UserAccount save(UserAccount userAccount);

    Optional<UserAccount> findById(String userId);

    Optional<UserAccount> findByEmail(String email);

    List<UserAccount> findAll();

    List<UserAccount> findByRole(UserRole role);

    boolean existsByEmail(String email);
}