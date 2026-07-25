package com.avery.bikemaintenance.adapter.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avery.bikemaintenance.application.service.AuthenticationResult;
import com.avery.bikemaintenance.application.service.LoginService;
import com.avery.bikemaintenance.application.service.UserAccountService;
import com.avery.bikemaintenance.domain.model.UserAccount;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserAccountService
            userAccountService;

    private final LoginService loginService;

    public AuthenticationController(
            UserAccountService userAccountService,
            LoginService loginService) {

        this.userAccountService =
                userAccountService;

        this.loginService =
                loginService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccountResponse register(
            @RequestBody
            RegisterUserRequest request) {

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
            @AuthenticationPrincipal
            Jwt jwt) {

        UserAccount currentUser =
                userAccountService.findById(
                        jwt.getClaimAsString(
                                "userId"));

        return UserAccountResponse.from(
                currentUser);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        AuthenticationResult result =
                loginService.login(
                        request.email(),
                        request.password());

        return new LoginResponse(
                result.token(),
                "Bearer",
                result.expiresInSeconds(),
                UserAccountResponse.from(
                        result.userAccount()));
    }
}
