package com.avery.bikemaintenance.adapter.inbound.rest;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        UserAccountResponse user) {
}