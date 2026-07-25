package com.avery.bikemaintenance.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount registerUser(
            String email,
            String displayName,
            String rawPassword) {

        validateRegistration(
                email,
                displayName,
                rawPassword);

        if (userAccountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account already exists for email: "
                            + email);
        }

        String userId = generateUserId();

        String passwordHash =
                passwordEncoder.encode(rawPassword);

        UserAccount userAccount =
                new UserAccount(
                        userId,
                        email,
                        displayName,
                        passwordHash,
                        UserRole.USER);

        return userAccountRepository.save(userAccount);
    }

    public UserAccount findById(String userId) {
        return userAccountRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User account does not exist: "
                                        + userId));
    }

    public UserAccount findByEmail(String email) {
        return userAccountRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User account does not exist: "
                                        + email));
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }
    
    public List<UserAccount> findTechnicians() {
        return userAccountRepository.findByRole(
                UserRole.TECHNICIAN);
    }

    private void validateRegistration(
            String email,
            String displayName,
            String rawPassword) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required");
        }

        if (displayName == null
                || displayName.isBlank()) {

            throw new IllegalArgumentException(
                    "Display name is required");
        }

        if (rawPassword == null
                || rawPassword.isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required");
        }

        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }
    }

    private String generateUserId() {
        String identifier = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "USER-" + identifier;
    }
}