# Bike Maintenance Adapter System

## Project Description

The **Bike Maintenance Adapter System** is a full-stack software design project focused on applying object-oriented design patterns and the SOLID and GRASP design principles to a realistic bike-share maintenance workflow. The application supports administrators, maintenance technicians, and bike users as they manage fleet availability, report maintenance issues, assign and complete work orders, and track the operational condition of each bike.

The primary purpose of the project is not simply to create a working maintenance application, but to demonstrate how software can be structured so that it remains understandable, testable, extensible, and resistant to change. The system is organized around a Spring Boot backend, a React frontend, and a GraphQL API. Business rules are separated from user-interface, security, database, and external-service concerns.

## Gang of Four Design Patterns

The project demonstrates two Gang of Four design patterns: **Adapter** and **Abstract Factory**.

### Adapter Pattern

The Adapter pattern allows different data providers, including a SQL database and a separate GraphQL service, to be used through the same repository interfaces. Each adapter translates provider-specific operations and data representations into the domain objects expected by the application.

### Abstract Factory Pattern

The Abstract Factory pattern creates a complete and compatible family of repositories for the selected provider. This allows the application to use an in-memory, SQL, or GraphQL repository family without changing the domain model or application services.

## SOLID Principles

The application applies the SOLID principles throughout its design:

- **Single Responsibility Principle:** Domain objects, application services, GraphQL controllers, security components, and persistence adapters each have focused responsibilities.
- **Open/Closed Principle:** New repository implementations can be added without modifying the application services that depend on them.
- **Liskov Substitution Principle:** Each repository implementation can replace another implementation through the same interface.
- **Interface Segregation Principle:** Repository ports are focused on the operations required by each part of the domain.
- **Dependency Inversion Principle:** Application services depend on repository abstractions rather than concrete databases or external APIs.

## GRASP Principles

The project also applies several GRASP principles:

- **Controller:** GraphQL controllers receive system requests and delegate work to application services.
- **Information Expert:** Domain objects own the data and rules they are best suited to manage.
- **Creator:** Objects and services create related objects when they contain the information required for construction.
- **Low Coupling:** Layers communicate through interfaces rather than concrete implementations.
- **High Cohesion:** Each class is responsible for a closely related set of tasks.
- **Polymorphism:** Multiple repository adapters respond to the same repository operations.
- **Protected Variations:** Repository ports protect the application from changes in database technology and remote service contracts.

## Application Features

The current application includes role-based authentication and separate dashboards for administrators, technicians, and users.

### User Features

Users can:

- View bikes available for use
- Report maintenance concerns
- Review maintenance issues submitted by their own accounts

### Administrator Features

Administrators can:

- Create and update bikes
- Organize the fleet by condition
- Review maintenance issues
- Create work orders
- Assign technicians
- View active and completed work orders

### Technician Features

Technicians can:

- View work orders assigned to them
- Start assigned work
- Close completed work orders
- Review their work-order history

## Architectural Goal

The Bike Maintenance Adapter System demonstrates how design patterns and responsibility-driven design principles can be used to build a flexible application whose business logic remains stable even when its interface, persistence technology, or external integrations change.

The project’s main objective is to show that well-designed software can support multiple data providers and evolving requirements without forcing widespread changes across the system.
