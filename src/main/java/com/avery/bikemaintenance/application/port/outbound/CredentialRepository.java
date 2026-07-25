package com.avery.bikemaintenance.application.port.outbound;

import java.util.Optional;

public interface CredentialRepository {

    Optional<StoredCredential> findCredentialsByEmail(
            String email);
}
