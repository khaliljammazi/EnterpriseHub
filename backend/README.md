# EnterpriseHub backend

Spring Boot backend using Java 21, Maven, PostgreSQL, JPA/Hibernate, and Flyway.

## Local setup

1. Start PostgreSQL from the repository root: `docker compose up -d postgres`.
2. Configure IntelliJ to use a complete Java 21 JDK.
3. Run `BackendApplication` or execute `./mvnw spring-boot:run`.
4. Verify `http://localhost:8080/actuator/health`.
5. Create a company with `POST /api/companies` and JSON `{ "name": "EnterpriseHub" }`.

## Tests

Run `./mvnw test`. Tests must pass before a pull request is merged.
