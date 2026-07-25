package com.avery.bikemaintenance.application.exception;

public class DuplicateEmailException
        extends RepositoryException {

    public DuplicateEmailException(String email) {
        super("An account already exists for email: " + email);
    }
}
