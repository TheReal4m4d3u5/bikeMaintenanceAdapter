# Adapter and Abstract Factory Implementation

## Assignment Focus

The project demonstrates two GoF design patterns using a SQL provider and a separate GraphQL provider.

## Adapter Pattern

The target interfaces are:

- `BikeRepository`
- `MaintenanceIssueRepository`
- `WorkOrderRepository`

The incompatible adaptees are:

- JDBC and relational database operations
- Remote GraphQL documents, variables, response DTOs, and transport failures

The SQL adapters translate database rows and SQL operations into domain objects and repository behavior:

- `SqlBikeRepositoryAdapter`
- `SqlMaintenanceIssueRepositoryAdapter`
- `SqlWorkOrderRepositoryAdapter`

The remote GraphQL adapters translate remote queries and mutations into the same contracts:

- `GraphQlBikeRepositoryAdapter`
- `GraphQlMaintenanceIssueRepositoryAdapter`
- `GraphQlWorkOrderRepositoryAdapter`

Application services depend only on the target interfaces. They do not know which provider is active.

## Abstract Factory Pattern

`MaintenanceRepositoryFactory` is the abstract factory. It creates a compatible family containing:

- `BikeRepository`
- `MaintenanceIssueRepository`
- `WorkOrderRepository`

Concrete factories are:

- `MemoryMaintenanceRepositoryFactory`
- `SqlMaintenanceRepositoryFactory`
- `GraphQlMaintenanceRepositoryFactory`

`MaintenanceRepositoryConfiguration` is the composition root. It selects exactly one concrete factory based on:

```properties
app.repository.provider=memory
```

Because one factory supplies the complete family, a mixed configuration such as SQL bikes with GraphQL work orders cannot occur.

## Identity Trust Boundary

`UserAccountRepository` and `CredentialRepository` remain local and are not created by `MaintenanceRepositoryFactory`. Authentication requires password hashes, so sending identity storage through a remote provider would violate the project’s trust boundary.

## SOLID and GRASP Support

The design supports:

- Dependency Inversion through repository ports
- Open/Closed through provider-family extension
- Liskov Substitution through shared contract tests
- Low Coupling through stable application-layer interfaces
- Polymorphism through interchangeable repository implementations
- Protected Variations by isolating provider-specific behavior in adapters
