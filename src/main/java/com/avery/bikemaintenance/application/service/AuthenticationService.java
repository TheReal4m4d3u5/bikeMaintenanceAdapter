package com.avery.bikemaintenance.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;

@Service
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount authenticate(
            String email,
            String rawPassword) {

        validateCredentials(email, rawPassword);

        UserAccount userAccount =
                userAccountRepository
                        .findByEmail(email)
                        .orElseThrow(
                                InvalidCredentialsException::new);

        if (!userAccount.isEnabled()) {
            throw new InvalidCredentialsException();
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        rawPassword,
                        userAccount.getPasswordHash());

        if (!passwordMatches) {
            throw new InvalidCredentialsException();
        }

        return userAccount;
    }

    private void validateCredentials(
            String email,
            String rawPassword) {

        if (email == null
                || email.isBlank()
                || rawPassword == null
                || rawPassword.isBlank()) {

            throw new InvalidCredentialsException();
        }
    }
}