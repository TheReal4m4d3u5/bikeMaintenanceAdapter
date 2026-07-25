# Bike Maintenance Adapter — Updated Design & Full Class Responsibility Report

Date: July 25, 2026 Purpose: This is the consolidated design after applying the GRASP review (D1–D3) and SOLID compliance report (C1–C6). It supersedes the previous class listings: every class and module in the updated system appears below with its responsibility and a status of Current (exists, unchanged), Updated (exists, responsibility changes), New (introduced by this design), Planned (assignment work still to build), or Removed.

## Design changes incorporated

Change	Source	Effect on the design
One Abstract Factory, one provider property	C1, C5	MaintenanceRepositoryFactory + three concrete families; app.repository.provider replaces four per-aggregate properties; old factories and RepositoryType removed
Identity excluded from the swappable family	D1 / C2	The factory creates the three maintenance repositories. User accounts and credentials are local security infrastructure and never route to the remote provider — no password hash ever crosses a trust boundary. A narrow CredentialRepository port serves authentication (ISP)
Contract tests per port	C3	Abstract test classes define port behavior; one subclass per implementation proves substitutability
Port exception vocabulary	C4	Adapters translate SQL/GraphQL/transport errors into application-defined exceptions
Login use case in the application layer	B2 (carried)	LoginService orchestrates authentication + token issuance; the REST controller is pure translation
Identity passed as a value object	C6 advisory	AuthenticatedUser(userId, role) replaces loose (userId, role) parameters
Close-work flow completed	D3	Closing a work order carries the resulting bike condition; Bike.completeRepair(...) validates it
Admin dashboard decomposition	C6	AdminDashboard delegates to fleet/issue/work-order panels

## Architecture Flow

(unchanged in shape): React → Inbound GraphQL/REST adapters → Application services → Repository ports → Selected adapter family (memory | SQL | remote GraphQL), with identity storage held locally outside the family.

## 1. Bootstrap and Configuration

Class	Status	Responsibility
BikeMaintenanceAdapterApplication	Current	Spring Boot entry point. Starts the application, component scanning, and the embedded web server. No business logic.
SecurityConfiguration	Current	Stateless Spring Security: JWT resource-server validation, public registration/login routes, authentication required on /graphql, JWT role claim → ROLE_* authorities, method-level authorization enabled for @PreAuthorize.
JwtConfiguration	Current	Decodes the configured signing secret, enforces minimum key length, creates the JwtEncoder/JwtDecoder, validates issuer and time claims.
MaintenanceRepositoryConfiguration	New	The composition root for persistence. Reads app.repository.provider, constructs the matching concrete MaintenanceRepositoryFactory, and exposes the three maintenance repository ports as beans obtained from that factory. The only class in the system that ever references a factory — services stay unaware a selection happened.
UserAccountConfiguration	Updated	Provides the local identity beans: the active UserAccountRepository, the CredentialRepository view, and the PasswordEncoder. Deliberately independent of the maintenance provider family (D1).
DevelopmentDataInitializer	Current	Seeds demo bikes, issues, work orders, and role accounts. Gated so it runs only in development profiles; must default off in production.
BikeRepositoryFactory, MaintenanceIssueRepositoryFactory, WorkOrderRepositoryFactory, RepositoryType	Removed	Per-aggregate selection replaced by family selection (C1). Their deletion is what upgrades the design from three simple factories to a true Abstract Factory.

## 2. Domain Model

Class	Status	Responsibility
Bike	Updated	Represents one fleet bicycle: ID, model, operational condition, ride count, mileage. Validates construction data and owns condition transitions — startRepair() moves it to UNDER_REPAIR, and the new completeRepair(resultingCondition) (D3) accepts the technician-reported outcome and rejects invalid transitions. Controllers and services never assign arbitrary condition values.
MaintenanceIssue	Current	A reported or scheduled maintenance need: issue ID, bike ID, reporting user ID, source type, description, severity, status. Owns its lifecycle — marking that a work order exists, resolving — and rejects illegal transitions.
WorkOrder	Updated	Authorized maintenance work: work-order ID, bike ID, linked issue ID, description, assigned technician ID, status, creation date. Owns assign(...), startWork(), and close(), each guarding its legal preconditions. The sole owner of technician assignment; the technician view of issues is derived through work orders.
UserAccount	Current	An application account: user ID, normalized email, display name, password hash, role, enabled flag. Normalizes email, keeps the hash internal, supports enable/disable. Never serialized outward directly.
UserRole	Current	Enum of ADMIN, TECHNICIAN, USER. Single vocabulary for backend authorization and frontend routing.
AuthenticatedUser	New	Small immutable value object (userId, role) built by inbound adapters from the validated JWT and handed to services that enforce caller-specific rules. Replaces loose string parameters (C6 advisory).

## 3. Repository Ports and Application-Layer Contracts

Ports are the Adapter pattern's target interfaces. Services depend on them exclusively.

Interface / Class	Status	Responsibility
BikeRepository	Current	Bike persistence contract: save, findById, findAll, existsById. In the swappable family.
MaintenanceIssueRepository	Current	Issue persistence contract, including lookup by ID, bike ID, and reporting user ID. In the swappable family.
WorkOrderRepository	Current	Work-order persistence contract, including lookup by ID, bike ID, and assigned technician ID. In the swappable family.
UserAccountRepository	Updated	Account persistence contract: save (registration), find by ID/email, findAll, findByRole, existsByEmail. Outside the family — always bound to local, trusted storage (D1).
CredentialRepository	New	Narrow authentication-only contract: look up the stored credentials (user ID, password hash, enabled flag) by email. The only port allowed to surface a hash, consumed solely by AuthenticationService (ISP). Implemented by the same local adapter as UserAccountRepository.
MaintenanceRepositoryFactory	New	The Abstract Factory interface, living in the application layer beside the ports (C5): declares bikeRepository(), maintenanceIssueRepository(), workOrderRepository(). Each concrete factory returns one complete, compatible provider family.
RepositoryException	New	Base unchecked exception for the port boundary (C4). Adapters translate provider-native failures (SQL exceptions, GraphQL transport errors, timeouts) into it so services handle failure identically across families.
DuplicateEmailException	New	Port-level signal for the unique-email rule, thrown consistently by every UserAccountRepository implementation.

## 4. Application Services

Class	Status	Responsibility
BikeService	Current	Bike use cases: generates bike IDs server-side, validates create/update input, retrieves bikes, delegates persistence to BikeRepository.
MaintenanceIssueService	Current	Issue use cases: validates the referenced bike, generates issue IDs, sets the reporting user from the authenticated principal, retrieves all or user-owned issues, resolves issues through domain methods.
WorkOrderService	Updated	Work-order use cases: validates bike/issue relationships, generates IDs, assigns technicians (verifying the account holds the TECHNICIAN role), starts work, and closes work — now accepting the resulting bike condition and coordinating WorkOrder.close() with Bike.completeRepair(...) (D3). Receives an AuthenticatedUser and enforces "admin or the assigned technician" for lifecycle actions, delegating that rule to WorkOrderAccessPolicy.
WorkOrderAccessPolicy	New	Encapsulates the caller-authorization rule for work-order actions in one place (C6): admins always; technicians only on orders assigned to them. Keeps permission logic out of workflow logic.
UserAccountService	Current	Registration and account queries: validates input, rejects duplicate emails, encodes passwords, generates user IDs, creates USER-role accounts, finds technicians for assignment dropdowns.
AuthenticationService	Updated	Verifies credentials through the narrow CredentialRepository: looks up by email, checks the enabled flag, compares the password against the stored hash, returns the authenticated identity. Fails with one generic error for unknown email and wrong password alike.
LoginService	New	The login use case (B2): calls AuthenticationService, then JwtService, and returns an AuthenticationResult (token, expiry, safe account data). Exists so the REST controller stays a pure adapter and future inbound routes (e.g., GraphQL login) reuse the same orchestration.
JwtService	Current	Creates signed JWTs carrying user ID, role, display name, subject, issuer, and expiration; exposes the configured expiry for responses.
InvalidCredentialsException	Current	Single generic authentication failure; never reveals which credential was wrong.

## 5. Inbound GraphQL Adapter

Class / Record	Status	Responsibility
BikeGraphQlController	Current	Maps bike queries and mutations to BikeService; role authorization via @PreAuthorize. No business or persistence logic.
MaintenanceIssueGraphQlController	Current	Maps issue operations to MaintenanceIssueService; builds the AuthenticatedUser from the JWT for ownership; exposes myMaintenanceIssues for standard users.
WorkOrderGraphQlController	Updated	Maps work-order operations to WorkOrderService; coarse role gate via @PreAuthorize, fine-grained assignment rule delegated to the service with the caller's AuthenticatedUser. The close mutation now carries the resulting bike condition (D3).
UserAccountGraphQlController	Current	Admin-only technicians query; maps accounts to TechnicianAccountResponse records.
CreateBikeInput	Current	Creation payload: model, condition, ride count, mileage. No ID — the server generates it.
BikeInput	Current	Update payload: existing bike ID plus updated values.
MaintenanceIssueInput	Current	Issue payload: bike ID, source type, description, severity. Reporting user comes from the JWT, never the client.
WorkOrderInput	Current	Work-order payload: bike ID, issue ID, description, optional assigned technician ID. Server generates the ID.
CloseWorkOrderInput	New	Close payload: work-order ID plus the technician-reported resulting bike condition (D3).
TechnicianAccountResponse	Current	Safe technician record (userId, displayName, email) for assignment dropdowns.

## 6. REST Authentication Adapter

Class / Record	Status	Responsibility
AuthenticationController	Updated	Pure inbound adapter for /api/auth/register, /login, /me: converts request records to UserAccountService/LoginService calls and results to response records. Orchestration lives in LoginService, not here.
RegisterRequest	Current	Carries email, display name, raw password. Cannot express a role — public registration is always USER.
LoginRequest	Current	Carries login credentials.
LoginResponse	Current	Carries the JWT, token type, expiration, and safe account data.
UserAccountResponse	Current	Safe account projection; excludes the password hash.
ApiExceptionHandler	Current	Translates exceptions to readable HTTP responses: validation → 400, InvalidCredentialsException → 401, port exceptions → 500 with generic messages (details go to logs, not clients).

## 7. In-Memory Provider (Current Family + Local Identity)

Class	Status	Responsibility
InMemoryBikeRepository	Current	BikeRepository on a concurrent map; deterministic dev/test storage.
InMemoryMaintenanceIssueRepository	Current	MaintenanceIssueRepository in memory; filters by bike and reporting user.
InMemoryWorkOrderRepository	Current	WorkOrderRepository in memory; filters by bike and assigned technician.
MemoryMaintenanceRepositoryFactory	New	Concrete Abstract Factory returning the three in-memory maintenance repositories as one family (each built once, returned as singletons).
InMemoryUserAccountRepository	Updated	Local identity storage implementing both UserAccountRepository and CredentialRepository; enforces unique emails via DuplicateEmailException. Outside the family.

## 8. SQL Provider Family — Planned (GoF Adapter)

Translates JDBC/JPA operations and relational rows into the port contracts and domain objects.

Class	Status	Responsibility
SqlBikeRepositoryAdapter	Planned	BikeRepository over SQL; maps rows/entities to Bike via the mapper; translates SQL failures to RepositoryException.
SqlMaintenanceIssueRepositoryAdapter	Planned	MaintenanceIssueRepository over SQL, including bike and reporting-user queries.
SqlWorkOrderRepositoryAdapter	Planned	WorkOrderRepository over SQL, including bike and technician queries.
SqlMaintenanceRepositoryFactory	Planned	Concrete Abstract Factory owning the SQL infrastructure (DataSource/JdbcTemplate or JPA repositories) and returning the complete SQL family.
BikeEntity / BikeRowMapper	Planned	Persistence-shape ↔ domain mapping for bikes; keeps JPA/SQL annotations out of Bike.
MaintenanceIssueEntity / mapper	Planned	Same for issues.
WorkOrderEntity / mapper	Planned	Same for work orders.
SqlUserAccountRepositoryAdapter + entity/mapper	Planned (optional)	Local SQL identity storage implementing UserAccountRepository + CredentialRepository when the database arrives. Selected independently of the maintenance family — identity never follows the family to a remote provider.

## 9. Remote GraphQL Provider Family — Planned (GoF Adapter)

Translates a remote GraphQL provider's documents, variables, response DTOs, and errors into the same port contracts.

Class	Status	Responsibility
GraphQlBikeRepositoryAdapter	Planned	BikeRepository via remote queries/mutations; maps provider DTOs to Bike; translates transport and provider errors to RepositoryException.
GraphQlMaintenanceIssueRepositoryAdapter	Planned	MaintenanceIssueRepository via the remote provider.
GraphQlWorkOrderRepositoryAdapter	Planned	WorkOrderRepository via the remote provider, including technician/bike filtering.
GraphQlMaintenanceRepositoryFactory	Planned	Concrete Abstract Factory owning the configured remote client and returning the complete remote family.
GraphQlBikeResponse, GraphQlMaintenanceIssueResponse, GraphQlWorkOrderResponse	Planned	DTOs matching the provider's response shapes, quarantined in the outbound package; converted to domain objects by the adapters.
(no user-account adapter)	—	Deliberate (D1): identity and credentials never route to the remote provider. The course report states the swappable family covers the maintenance domain, with identity excluded as a security trust boundary.

## 10. Test Classes — Planned (LSP proof, C3)

Class	Status	Responsibility
BikeRepositoryContractTest	Planned	Abstract test defining BikeRepository behavior: miss semantics, save/replace rules, collection copying, exception vocabulary.
MaintenanceIssueRepositoryContractTest	Planned	Same for issues, including ownership queries.
WorkOrderRepositoryContractTest	Planned	Same for work orders, including technician queries.
UserAccountRepositoryContractTest	Planned	Same for identity storage, including DuplicateEmailException and credential lookup.
Per-family subclasses	Planned	One small subclass per implementation (memory now; SQL and remote GraphQL when built) binding the abstract contract to a concrete adapter — the executable demonstration that "the same behavior works through both providers."

## 11. Frontend Modules

Module	Status	Responsibility
main.jsx	Current	Mounts React and wraps the app in AuthProvider.
App.jsx	Current	Chooses between authentication screens and DashboardRouter. Nothing else.
DashboardRouter.jsx	Current	Maps the authenticated role to the correct dashboard; unknown roles default to the least-privileged user view. Becomes the route-guard logic when React Router arrives.
AdminDashboard.jsx	Updated	Thin page composing the admin panels below; holds only cross-panel refresh coordination.
FleetPanel.jsx, IssuePanel.jsx, WorkOrderPanel.jsx	New	Extracted admin panels (C6): fleet CRUD and condition grouping; issue review/resolution; work-order creation, technician assignment, lifecycle, and history.
TechnicianDashboard.jsx	Current	Shows work orders assigned to the authenticated technician (issues derived through them), start/close actions — close collects the resulting bike condition (D3) — and work history.
UserDashboard.jsx	Current	Shows available bikes, lets the user report an issue, lists only the user's own issues.
LoginPage.jsx / RegisterPage.jsx	Current	Credential collection and public registration; dev-account panel excluded from production builds.
AccountBar.jsx	Current	Displays the signed-in identity and role; logout.
AuthContext.jsx	Current	Global auth state: current account, session restore via /api/auth/me, login/register/logout; registers the unauthorized-session callback with graphqlClient.
graphqlClient.js	Current	Single authenticated GraphQL transport: bearer header, JSON parsing, error unwrapping, and an setOnUnauthorized(callback) hook so it never imports UI state (dependency direction preserved).
tokenStorage.js	Current	Isolated JWT persistence: save, load, remove.
authApi.js	Current	REST calls for register, login, current account.
bikeApi.js, maintenanceIssueApi.js, workOrderApi.js, userAccountApi.js	Current	Resource-specific GraphQL operations delegating transport to graphqlClient.
roles.js	New (optional)	Frontend role constants mirroring UserRole; keeps role literals to one file.

## 12. Updated Collaboration Flows

Login. LoginPage submits credentials → AuthenticationController converts the request and calls LoginService → LoginService authenticates via AuthenticationService (which reads only CredentialRepository), obtains a token from JwtService, and returns an AuthenticationResult → the controller maps it to LoginResponse. The controller never orchestrates; the hash never leaves the application layer.

Close work (D3, new). The assigned technician (or an admin) submits the close mutation with the resulting bike condition → WorkOrderGraphQlController builds the AuthenticatedUser and calls WorkOrderService → WorkOrderAccessPolicy confirms the caller may act → WorkOrder.close() validates its transition → Bike.completeRepair(condition) validates the bike's transition → both are saved through their ports. The issue's resolution follows its own domain rule.

Switch provider family. Configuration sets app.repository.provider → MaintenanceRepositoryConfiguration builds the one matching concrete factory → the factory supplies the three maintenance repositories as beans → services, domain, inbound adapters, and React are untouched. Identity beans are wired independently and never switch to the remote provider.

## 13. Pattern Statements (for the course report)

Adapter. The repository ports are the target interfaces. JDBC/JPA and the remote GraphQL client are the incompatible adaptees. Each planned adapter translates provider-specific operations, data shapes, and failures into port operations, domain objects, and the port exception vocabulary. The in-memory implementations form a third interchangeable family used for development and as the contract-test baseline.

Abstract Factory. MaintenanceRepositoryFactory declares the creation of one complete, compatible family of maintenance repositories; Memory-, Sql-, and GraphQl-MaintenanceRepositoryFactory are the concrete factories; a single configuration property selects exactly one. Family compatibility is structurally guaranteed — a mixed family can no longer be configured. Identity storage is deliberately excluded from the family as a security trust boundary: authentication requires password hashes, and hashes must not transit to an external provider. Stating this exclusion, and its reason, turns a scope limitation into a demonstrated design judgment.

## Conclusion

The updated design keeps every prior architectural strength — ports and adapters, thin controllers, domain-owned transitions, role-secured GraphQL — and resolves the review findings: one true Abstract Factory with family-level selection (C1/C5), the credential contract made safe and honest (D1/C2), substitutability made provable by contract tests (C3) and a shared failure vocabulary (C4), login orchestration in the application layer (B2), the close-work flow fully specified (D3), and the two cohesion watch items addressed (C6). The remaining assignment work is Sections 8–10: two adapter families and the tests that prove they substitute — mechanical work inside contracts this design now makes explicit.
