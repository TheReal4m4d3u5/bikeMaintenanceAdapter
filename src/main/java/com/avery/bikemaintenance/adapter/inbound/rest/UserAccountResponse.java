package com.avery.bikemaintenance.adapter.inbound.rest;

import com.avery.bikemaintenance.domain.model.UserAccount;

public record UserAccountResponse(
        String userId,
        String email,
        String displayName,
        String role,
        boolean enabled) {

    public static UserAccountResponse from(
            UserAccount userAccount) {

        return new UserAccountResponse(
                userAccount.getUserId(),
                userAccount.getEmail(),
                userAccount.getDisplayName(),
                userAccount.getRole().name(),
                userAccount.isEnabled());
    }
}