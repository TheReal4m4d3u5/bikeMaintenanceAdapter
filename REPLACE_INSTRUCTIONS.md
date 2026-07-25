# Replacement Files Instructions

This bundle contains only the files that are new or changed for the Adapter + Abstract Factory design.

## Replace the files

1. Extract this ZIP.
2. Copy everything inside this folder into the root of your existing `bikeMaintenanceAdapter` project.
3. Choose **Replace the files in the destination** when Windows asks.

The paths in this bundle already match the project structure. For example:

```text
src/main/java/com/avery/bikemaintenance/adapter/outbound/graphql/
src/main/java/com/avery/bikemaintenance/adapter/outbound/sql/
frontend/src/
remote-graphql-provider/
```

## Delete obsolete folders

After copying the replacement files, delete these old folders from the project:

```text
src/main/java/com/avery/bikemaintenance/adapter/outbound/postgresql
frontend/src/services
```

The old `postgresql` package used JPA entities. The updated assignment version uses the `sql` package with `JdbcTemplate`.

The `frontend/src/services` folder is obsolete because the frontend now uses the modules under `frontend/src/api`.

You may also delete the generated folder below; Vite recreates it when needed:

```text
frontend/dist
```

## Important Maven dependency

The supplied `pom.xml` uses:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
</dependency>
```

Do not add `spring-boot-starter-data-jpa`. The replacement SQL adapters use `JdbcTemplate`, not `@Entity` classes.

## Refresh Eclipse

After copying and deleting the obsolete folders:

```text
Right-click project -> Refresh
Right-click project -> Maven -> Update Project
Project -> Clean -> Clean selected project
```

If Eclipse still shows stale errors, close and reopen the project once.
