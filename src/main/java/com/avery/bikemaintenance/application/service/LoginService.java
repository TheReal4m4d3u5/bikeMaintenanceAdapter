package com.avery.bikemaintenance.application.service;

import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.domain.model.UserAccount;

@Service
public class LoginService {

    private final AuthenticationService
            authenticationService;

    private final JwtService jwtService;

    public LoginService(
            AuthenticationService authenticationService,
            JwtService jwtService) {

        this.authenticationService =
                authenticationService;

        this.jwtService =
                jwtService;
    }

    public AuthenticationResult login(
            String email,
            String rawPassword) {

        UserAccount authenticatedUser =
                authenticationService
                        .authenticate(
                                email,
                                rawPassword);

        String token =
                jwtService.generateToken(
                        authenticatedUser);

        return new AuthenticationResult(
                token,
                jwtService.getExpirationSeconds(),
                authenticatedUser);
    }
}
