package com.avery.bikemaintenance.application.service;

import com.avery.bikemaintenance.domain.model.UserAccount;

public record AuthenticationResult(
        String token,
        long expiresInSeconds,
        UserAccount userAccount) {
}
