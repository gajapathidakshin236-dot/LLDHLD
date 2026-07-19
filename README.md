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

## API server - runs straight from this project

This project IS now a Spring Boot app (see `pom.xml`). The server lives in `src/main/java/com/company/apiserver/`: a Task CRUD API + H2 in-memory database + **the `com.company.ratelimite` token bucket from this repo running as a real HTTP 429 filter**, plus tests in `src/test/java`. Your pattern/DSA code is untouched and compiles alongside it.

Easiest start: double-click **`run-server.bat`** (finds Java automatically), or:

```powershell
.\mvnw.cmd spring-boot:run    # then open http://localhost:8080/api/tasks
.\mvnw.cmd test               # run the local tests (or run-tests.bat)
```

In IntelliJ: reload Maven (right-click `pom.xml` → Maven → Reload Project), then green ▶ on `ApiServerApplication`. Full steps: `RUN-LOCAL.md`. Build your own endpoints: `HOW-TO-ADD-AN-API.md`. Test drive: `test-api.bat` or `requests.http`.
