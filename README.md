# LLDHLD (Maven + Design Patterns)

## Structure

- `pom.xml`
- `src/main/java/com/company/abstractfactory/*`
- `src/main/java/com/company/builder/*`
- `src/main/java/com/company/prototype/*`
- `src/main/java/com/company/singleton/*`

## Entry point

- `com.company.abstractfactory.Main`
- `com.company.builder.Main`
- `com.company.prototype.Main`
- `com.company.singleton.Main`

## Run (requires JDK 17+; Maven NOT required - wrapper included)

```bash
.\mvnw.cmd -q -DskipTests package     # Windows
./mvnw -q -DskipTests package         # Linux/Mac
```

If you want to run the `main` method from Maven, you can use your IDE (recommended) or add the `exec-maven-plugin`.

## api-server - runnable REST API on localhost

`api-server/` is a standalone Spring Boot project (own `pom.xml`, independent of this root one): a Task CRUD API + H2 in-memory database + **the `com.company.ratelimite` token bucket from this repo running as a real HTTP 429 filter**, plus tests.

```powershell
cd api-server
.\mvnw.cmd spring-boot:run    # then open http://localhost:8080/api/tasks
.\mvnw.cmd test               # run the local tests
```

Start with `api-server/RUN-LOCAL.md`. To build your own endpoints: `api-server/HOW-TO-ADD-AN-API.md`.
