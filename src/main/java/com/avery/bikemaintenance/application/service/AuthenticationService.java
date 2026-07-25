package com.avery.bikemaintenance.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.application.port.outbound.CredentialRepository;
import com.avery.bikemaintenance.application.port.outbound.StoredCredential;
import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;

@Service
public class AuthenticationService {

    private final CredentialRepository credentialRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            CredentialRepository credentialRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.credentialRepository =
                credentialRepository;

        this.userAccountRepository =
                userAccountRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    public UserAccount authenticate(
            String email,
            String rawPassword) {

        validateCredentials(
                email,
                rawPassword);

        StoredCredential credential =
                credentialRepository
                        .findCredentialsByEmail(
                                email)
                        .orElseThrow(
                                InvalidCredentialsException::new);

        if (!credential.enabled()
                || !passwordEncoder.matches(
                        rawPassword,
                        credential.passwordHash())) {

            throw new InvalidCredentialsException();
        }

        return userAccountRepository
                .findById(credential.userId())
                .orElseThrow(
                        InvalidCredentialsException::new);
    }

    private static void validateCredentials(
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
