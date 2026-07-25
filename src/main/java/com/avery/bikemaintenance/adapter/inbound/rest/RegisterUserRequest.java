package com.avery.bikemaintenance.adapter.inbound.rest;

public record RegisterUserRequest(
        String email,
        String displayName,
        String password) {
}