# api-server — your local API playground

A complete REST API you run on **your own machine**, built with **Spring Boot 3.5.16** (Java 17 — matches this repo's ms-17 JDK), **Spring Data JPA**, and an **H2 in-memory database**. It contains:

- A **Task CRUD API** — create/read/update/delete tasks, with validation and clean JSON errors
- **Your own rate limiter** (`com.company.ratelimite`, copied verbatim from this repo's LLD work) wired in as a real servlet filter: exceed the limit and you get **HTTP 429**
- **Local tests** — API tests with MockMvc + plain JUnit tests for your rate-limiter logic
- Docs: **RUN-LOCAL.md** (run it, step by step) · **HOW-TO-ADD-AN-API.md** (build your own endpoints) · **LEARNING-NOTES.md** (every concept from zero)

## Quickstart

**IntelliJ (recommended):** File → Open → select the LLDHLD folder → wait for Maven import → open `ApiServerApplication` → click the green ▶.

**Terminal:** `cd api-server` then `.\mvnw.cmd spring-boot:run`

Then open **http://localhost:8080/api/tasks** — details and troubleshooting in RUN-LOCAL.md.

## Endpoints

| Method | URL | Does | Returns |
|---|---|---|---|
| GET | `/api/ping` | health check / rate-limit demo | 200 `{"message":"pong",...}` |
| GET | `/api/tasks` | list all tasks | 200 + JSON array |
| GET | `/api/tasks?completed=true` | filter by status | 200 + JSON array |
| GET | `/api/tasks/{id}` | one task | 200, or 404 JSON error |
| POST | `/api/tasks` | create (JSON body) | 201 + Location header |
| PUT | `/api/tasks/{id}` | update | 200, or 404 |
| DELETE | `/api/tasks/{id}` | delete | 204, or 404 |
| GET | `/h2-console` | database web UI | JDBC URL `jdbc:h2:mem:taskdb`, user `sa`, empty password |

Any `/api/*` request may also answer **429 Too Many Requests** — that's your token bucket (capacity 5, refill 1/s; tune in `application.properties`).

## Layout

```

├── pom.xml / mvnw.cmd / .mvn/          build definition + self-installing Maven
├── requests.http                        click-to-run requests (IntelliJ / VS Code REST Client)
├── RUN-LOCAL.md · HOW-TO-ADD-AN-API.md · LEARNING-NOTES.md
└── src/
    ├── main/java/com/company/
    │   ├── apiserver/
    │   │   ├── ApiServerApplication.java     entry point (green ▶ here)
    │   │   ├── task/                         the CRUD API: entity, repository,
    │   │   │                                 service, controller, error handling
    │   │   └── ratelimit/
    │   │       ├── RateLimitConfig.java      YOUR classes -> Spring beans
    │   │       ├── RateLimitFilter.java      the "real 429 filter" your Javadoc promised
    │   │       └── PingController.java       /api/ping
    │   └── ratelimite/                       YOUR LLD classes, byte-for-byte unchanged
    ├── main/resources/
    │   ├── application.properties            port, DB, H2 console, rate-limit settings
    │   └── data.sql                          3 seed rows at startup
    └── test/java/com/company/
        ├── apiserver/TaskApiTests.java           API tests (MockMvc)
        ├── apiserver/RateLimitFilterTest.java    proves the 429 behaviour
        └── ratelimite/RateLimiterStrategyTests.java  plain JUnit on your logic
```

This project is standalone on purpose: the root LLDHLD pom and your pattern/DSA code are untouched, so this server always builds even while you experiment elsewhere in the repo.
