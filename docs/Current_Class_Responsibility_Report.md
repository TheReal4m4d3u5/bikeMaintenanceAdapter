# Bike Maintenance Adapter — Current Class Responsibility Report

**Updated:** July 25, 2026

## 1. Bootstrap and Configuration

| Class | Responsibility |
|---|---|
| `BikeMaintenanceAdapterApplication` | Spring Boot entry point. Starts component scanning and the embedded web server. |
| `SecurityConfiguration` | Configures stateless JWT security, public authentication routes, protected GraphQL access, role conversion, and method-level authorization. |
| `JwtConfiguration` | Creates and validates JWT encoder and decoder infrastructure. |
| `MaintenanceRepositoryConfiguration` | Composition root for maintenance persistence. Selects one Abstract Factory using `app.repository.provider` and exposes its three repositories as Spring beans. |
| `UserAccountConfiguration` | Creates trusted local identity storage and the password encoder. |
| `DevelopmentDataInitializer` | Seeds demonstration maintenance data outside the production profile. |
| `DevelopmentUserAccountInitializer` | Seeds administrator, technician, and user accounts outside the production profile. |

## 2. Domain Model

| Class | Responsibility |
|---|---|
| `Bike` | Represents a fleet bike, validates its state, starts repair, and completes repair with a validated resulting condition. |
| `MaintenanceIssue` | Represents a reported maintenance need and owns work-order-created and resolved transitions. |
| `WorkOrder` | Represents authorized maintenance work and owns assignment, start, and close transitions. |
| `UserAccount` | Represents a local application identity, including normalized email, password hash, role, and enabled state. |
| `UserRole` | Defines `ADMIN`, `TECHNICIAN`, and `USER`. |
| `AuthenticatedUser` | Immutable caller identity passed into use cases requiring ownership or assignment authorization. |

## 3. Application Ports

| Interface or Record | Responsibility |
|---|---|
| `BikeUseCase` | Inbound application contract for bike operations. |
| `BikeRepository` | Target interface for bike persistence adapters. |
| `MaintenanceIssueRepository` | Target interface for maintenance-issue persistence adapters. |
| `WorkOrderRepository` | Target interface for work-order persistence adapters. |
| `UserAccountRepository` | Trusted local account persistence contract. It is intentionally outside the swappable maintenance family. |
| `CredentialRepository` | Narrow authentication-only port for retrieving stored password credentials by email. |
| `StoredCredential` | Credential projection containing only the fields authentication needs. |
| `MaintenanceRepositoryFactory` | GoF Abstract Factory interface returning one compatible family of bike, issue, and work-order repositories. |

## 4. Application Exceptions

| Class | Responsibility |
|---|---|
| `RepositoryException` | Common port-boundary failure used by SQL and GraphQL adapters. |
| `DuplicateEmailException` | Consistent unique-email violation exposed by identity storage. |
| `InvalidCredentialsException` | Generic authentication failure that does not reveal whether the email or password was incorrect. |

## 5. Application Services

| Class | Responsibility |
|---|---|
| `BikeService` | Creates server-generated bike IDs, validates updates, and delegates persistence through `BikeRepository`. |
| `MaintenanceIssueService` | Validates bike references, creates issues, enforces reporter ownership queries, and coordinates issue lifecycle operations. |
| `WorkOrderService` | Validates bike/issue relationships, validates technician assignments, creates work orders, starts work, closes work, updates bike conditions, and resolves linked issues. |
| `WorkOrderAccessPolicy` | Enforces that administrators may modify any work order and technicians may modify only their assigned work orders. |
| `UserAccountService` | Registers users, encodes passwords, and retrieves accounts and technicians. |
| `AuthenticationService` | Verifies credentials through the narrow `CredentialRepository` and then loads the authenticated account. |
| `LoginService` | Orchestrates authentication and JWT creation so the REST controller remains a thin adapter. |
| `AuthenticationResult` | Application result carrying the JWT, expiration, and authenticated account. |
| `JwtService` | Creates signed JWTs containing user ID, role, display name, issuer, and expiration. |

## 6. Inbound GraphQL Adapters

| Class or Record | Responsibility |
|---|---|
| `BikeGraphQlController` | Translates bike queries and mutations into `BikeService` calls. |
| `MaintenanceIssueGraphQlController` | Translates issue operations, derives reporting ownership from JWT claims, and enforces query roles. |
| `WorkOrderGraphQlController` | Translates work-order operations, constructs `AuthenticatedUser`, and delegates fine-grained authorization to the application layer. |
| `UserAccountGraphQlController` | Provides the administrator-only technician list. |
| `RepositoryProviderGraphQlController` | Exposes the active provider name for the administrator dashboard. |
| `CreateBikeInput` | Bike-creation GraphQL input without a client-supplied ID. |
| `BikeInput` | Bike-update GraphQL input containing an existing ID. |
| `MaintenanceIssueInput` | Issue-creation input; reporting identity is derived from the JWT. |
| `WorkOrderInput` | Work-order-creation input with an optional technician assignment. |
| `CloseWorkOrderInput` | Work-order-close input containing the resulting bike condition. |
| `TechnicianAccountResponse` | Safe technician projection containing ID, display name, and email. |

## 7. Inbound REST Authentication Adapter

| Class or Record | Responsibility |
|---|---|
| `AuthenticationController` | Translates registration, login, and current-user HTTP requests into application-service calls. |
| `RegisterUserRequest` | Public registration payload; cannot select a privileged role. |
| `LoginRequest` | Login credential payload. |
| `LoginResponse` | JWT response containing token metadata and safe account information. |
| `UserAccountResponse` | Safe account projection that excludes the password hash. |
| `ApiExceptionHandler` | Maps validation, lifecycle, authentication, and repository failures into HTTP responses. |

## 8. In-Memory Adapter Family

| Class | Responsibility |
|---|---|
| `InMemoryBikeRepository` | Implements `BikeRepository` with a concurrent map. |
| `InMemoryMaintenanceIssueRepository` | Implements issue storage and reporter/bike filtering in memory. |
| `InMemoryWorkOrderRepository` | Implements work-order storage and bike/technician filtering in memory. |
| `MemoryMaintenanceRepositoryFactory` | Concrete Abstract Factory that creates one compatible in-memory maintenance family. |
| `InMemoryUserAccountRepository` | Trusted local identity adapter implementing both account and credential ports. |

## 9. SQL Adapter Family

| Class | Responsibility |
|---|---|
| `SqlSchemaInitializer` | Creates the three maintenance tables when the SQL family starts. |
| `SqlBikeRepositoryAdapter` | Adapts JDBC bike rows and SQL operations to `BikeRepository`. |
| `SqlMaintenanceIssueRepositoryAdapter` | Adapts maintenance-issue SQL operations and query filters to the issue port. |
| `SqlWorkOrderRepositoryAdapter` | Adapts work-order SQL operations and query filters to the work-order port. |
| `SqlMaintenanceRepositoryFactory` | Concrete Abstract Factory owning the `JdbcTemplate` and returning the complete SQL family. |

## 10. Remote GraphQL Adapter Family

| Class | Responsibility |
|---|---|
| `RemoteGraphQlClient` | Infrastructure abstraction for executing remote GraphQL documents. |
| `JdkRemoteGraphQlClient` | Uses Java `HttpClient` and Jackson to call the separate GraphQL provider and translate transport/provider errors to `RepositoryException`. |
| `GraphQlDomainMapper` | Converts provider response nodes into domain objects. |
| `GraphQlBikeRepositoryAdapter` | Adapts remote bike queries and mutations to `BikeRepository`. |
| `GraphQlMaintenanceIssueRepositoryAdapter` | Adapts remote issue queries and mutations to the issue port. |
| `GraphQlWorkOrderRepositoryAdapter` | Adapts remote work-order queries and mutations to the work-order port. |
| `GraphQlMaintenanceRepositoryFactory` | Concrete Abstract Factory returning the complete remote GraphQL maintenance family. |

## 11. Separate Remote GraphQL Provider

| Class | Responsibility |
|---|---|
| `RemoteGraphQlProviderApplication` | Starts the separate provider on port 8090. |
| `ProviderStore` | Owns provider-side maintenance data independently from the main application. |
| `ProviderGraphQlController` | Exposes provider queries and save mutations used by the outbound GraphQL adapters. |
| `ProviderBike`, `ProviderMaintenanceIssue`, `ProviderWorkOrder` | Provider-specific data records. |
| `ProviderBikeInput`, `ProviderMaintenanceIssueInput`, `ProviderWorkOrderInput` | Provider mutation inputs. |

## 12. Contract Tests

| Test class | Responsibility |
|---|---|
| `BikeRepositoryContractTest` | Defines required bike-repository behavior. |
| `MaintenanceIssueRepositoryContractTest` | Defines required issue-repository behavior and query semantics. |
| `WorkOrderRepositoryContractTest` | Defines required work-order behavior and query semantics. |
| In-memory subclasses | Apply the contracts to the memory family. |
| SQL subclasses | Apply the contracts to JDBC adapters using isolated H2 test databases. |
| GraphQL subclasses | Apply the contracts to GraphQL adapters through a fake remote client. |

## 13. Frontend Modules

| Module | Responsibility |
|---|---|
| `main.jsx` | Mounts React and installs `AuthProvider`. |
| `App.jsx` | Chooses authentication screens or the role dashboard. |
| `DashboardRouter.jsx` | Routes `ADMIN`, `TECHNICIAN`, and `USER` to their dashboards. |
| `AdminDashboard.jsx` | Coordinates fleet, issues, work orders, technician assignment, provider display, and history. |
| `TechnicianDashboard.jsx` | Shows assigned work, starts repairs, selects repair outcomes, closes work, and shows issues. |
| `UserDashboard.jsx` | Shows bikes available for use, including functional bikes due for scheduled maintenance, and manages user-owned issue reporting. |
| `AccountBar.jsx` | Displays the authenticated account and provides logout. |
| `AuthContext.jsx` | Owns frontend authentication state and session restoration. |
| `graphqlClient.js` | Shared authenticated GraphQL transport and error handling. |
| `tokenStorage.js` | Isolates JWT persistence. |
| `authApi.js` | REST authentication operations. |
| `bikeApi.js` | Bike GraphQL operations. |
| `maintenanceIssueApi.js` | Maintenance-issue GraphQL operations. |
| `workOrderApi.js` | Work-order GraphQL operations, including resulting bike condition on close. |
| `userAccountApi.js` | Technician-list GraphQL query. |
| `repositoryProviderApi.js` | Active-provider GraphQL query used by the admin badge. |
