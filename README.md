# Bike Maintenance Adapter

A full-stack ICS 372 project demonstrating two Gang of Four design patterns:

- **Adapter** — SQL/JDBC and a separate remote GraphQL provider are translated into the same repository ports.
- **Abstract Factory** — one selected factory supplies a complete and compatible family of bike, maintenance-issue, and work-order repositories.

The application uses Spring Boot, Java, GraphQL, React, Vite, JWT authentication, and role-based dashboards.

## Architecture

```text
React frontend
    |
    v
Spring Boot GraphQL and REST inbound adapters
    |
    v
Application services
    |
    v
Repository ports
    |
    v
MaintenanceRepositoryFactory
    |-- MemoryMaintenanceRepositoryFactory
    |-- SqlMaintenanceRepositoryFactory
    `-- GraphQlMaintenanceRepositoryFactory
```

User accounts and credentials remain in trusted local storage. They are intentionally excluded from the remote GraphQL family so password hashes never cross the external-provider boundary.

## Requirements

- Java 25
- Node.js and npm
- PostgreSQL when running the SQL provider

## Run with the in-memory provider

The in-memory provider is the default.

### Backend

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

### Frontend

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the address printed by Vite, normally `http://localhost:5173`.

## Run with the SQL provider

Create a PostgreSQL database, for example:

```sql
CREATE DATABASE bike_maintenance;
```

Set the connection values as environment variables:

```bash
export BIKE_SQL_URL=jdbc:postgresql://localhost:5432/bike_maintenance
export BIKE_SQL_USERNAME=postgres
export BIKE_SQL_PASSWORD=postgres
```

Run the backend with the SQL profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=sql
```

The SQL factory creates the required maintenance tables if they do not already exist.

## Run with the remote GraphQL provider

The remote GraphQL provider is a separate Spring Boot application included in:

```text
remote-graphql-provider/
```

Start it first:

```bash
cd remote-graphql-provider
./mvnw spring-boot:run
```

It runs at `http://localhost:8090/graphql`.

In a second terminal, start the main backend with the GraphQL profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=graphql
```

The main application’s outbound GraphQL adapters call the separate provider and translate its queries, mutations, DTOs, and errors into the same repository contracts used by the memory and SQL families.

## Select a provider directly

The central property is:

```properties
app.repository.provider=memory
```

Supported values:

```text
memory
sql
graphql
```

Profile-specific property files are included:

```text
application-memory.properties
application-sql.properties
application-graphql.properties
```

## Development accounts

When `app.seed-data=true`, the application creates:

```text
Administrator: admin@example.com / Admin123!
Technician:    technician@example.com / Tech12345!
User:          user@example.com / User12345!
```

Seed data is disabled when the `prod` profile is active.

## Tests

Run:

```bash
./mvnw test
```

Repository contract tests apply the same behavioral expectations to:

- In-memory repositories
- SQL repositories
- Remote GraphQL repository adapters

These tests provide the Liskov Substitution evidence that each provider family can replace another behind unchanged ports.

## Work-order lifecycle

```text
OPEN or ASSIGNED
    |
    | Start work
    v
IN_PROGRESS
    |
    | Close with resulting bike condition
    v
CLOSED
```

Starting work changes the linked bike to `UNDER_REPAIR`. Closing work requires one resulting bike condition:

- `AVAILABLE`
- `DUE_FOR_SCHEDULED_MAINTENANCE`
- `OUT_OF_SERVICE`
- `RETIRED`

## Documentation

- [Project description](docs/Project_Description.md)
- [Current class responsibilities](docs/Current_Class_Responsibility_Report.md)
- [Updated design and class responsibilities](docs/Updated_Design_Class_Responsibility_Report.md)
- [SOLID compliance report](docs/SOLID_Compliance_Report.md)
- [Design-pattern implementation](docs/Design_Patterns.md)
- [Project update summary](docs/Project_Update_Summary.md)
