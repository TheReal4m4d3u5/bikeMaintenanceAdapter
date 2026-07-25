# Project Update Summary

## Implemented

- Replaced four per-aggregate provider settings with `app.repository.provider`.
- Added the `MaintenanceRepositoryFactory` Abstract Factory.
- Added memory, SQL, and remote GraphQL concrete factory families.
- Implemented complete JDBC adapters for bikes, maintenance issues, and work orders.
- Added a separate Spring Boot remote GraphQL provider on port 8090.
- Implemented complete outbound GraphQL repository adapters.
- Kept user identity and credentials local and outside the swappable maintenance family.
- Added `CredentialRepository`, `StoredCredential`, and consistent repository exceptions.
- Added `AuthenticatedUser` and `WorkOrderAccessPolicy`.
- Completed the start/close repair lifecycle and resulting bike-condition selection.
- Added `LoginService` so the REST controller is a thin inbound adapter.
- Added provider-aware administrator display.
- Added repository contract tests for memory, SQL, and GraphQL adapters.
- Added Markdown setup, design-pattern, SOLID, and class-responsibility documentation.

## Validation Performed

- Domain, port, exception, and memory-adapter Java sources compiled with Java 21 as a compatibility syntax check.
- SQL adapter sources compiled against temporary Spring JDBC API stubs.
- GraphQL adapter sources compiled against temporary Jackson API stubs.
- Main application sources, except the unchanged security/bootstrap classes, compiled against temporary framework API stubs.
- The separate remote GraphQL provider compiled against temporary framework API stubs.
- Every frontend JavaScript and JSX source file parsed successfully with the TypeScript parser.

## Validation Limitation

A real Maven test run could not be completed in the execution environment because Maven and dependency downloads were unavailable. Run the following locally with Java 25:

```bash
./mvnw test
```

Then start each provider mode and perform the manual workflow checks described in the root `README.md`.
