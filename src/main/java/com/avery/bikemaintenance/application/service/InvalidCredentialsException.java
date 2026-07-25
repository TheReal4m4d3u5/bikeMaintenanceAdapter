package com.avery.bikemaintenance.application.service;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}