package com.avery.bikemaintenance.adapter.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avery.bikemaintenance.application.service.AuthenticationService;
import com.avery.bikemaintenance.application.service.JwtService;
import com.avery.bikemaintenance.application.service.UserAccountService;
import com.avery.bikemaintenance.domain.model.UserAccount;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserAccountService userAccountService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(
            UserAccountService userAccountService,
            AuthenticationService authenticationService,
            JwtService jwtService) {

        this.userAccountService = userAccountService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccountResponse register(
            @RequestBody RegisterUserRequest request) {

        UserAccount registeredUser =
                userAccountService.registerUser(
                        request.email(),
                        request.displayName(),
                        request.password());

        return UserAccountResponse.from(
                registeredUser);
    }
    
    @GetMapping("/me")
    public UserAccountResponse currentUser(
            @AuthenticationPrincipal Jwt jwt) {

        String userId =
                jwt.getClaimAsString("userId");

        UserAccount currentUser =
                userAccountService.findById(userId);

        return UserAccountResponse.from(
                currentUser);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        UserAccount authenticatedUser =
                authenticationService.authenticate(
                        request.email(),
                        request.password());

        String token =
                jwtService.generateToken(
                        authenticatedUser);

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                UserAccountResponse.from(
                        authenticatedUser));
    }
}