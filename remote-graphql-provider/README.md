# Remote GraphQL Maintenance Provider

This is the separate GraphQL data provider used by the main application’s
outbound GraphQL Adapter family.

## Run

```bash
./mvnw spring-boot:run
```

The provider runs at:

```text
http://localhost:8090/graphql
```

GraphiQL is available at:

```text
http://localhost:8090/graphiql
```

The provider stores maintenance data in its own in-memory maps. It does not
store users or password hashes. The main application keeps identity local.
