# booking-service

Reference Spring Boot pipeline for interview prep. Ships the entire flow from HTTP request to database and back, with two collaborating services, cache, tenant context, both offset and keyset pagination, and a clean error contract.

## What this shows in one diagram

```
HTTP request
   │
   ▼
TenantFilter                 (reads X-Tenant-Id → ThreadLocal)
   │
   ▼
DispatcherServlet → HandlerMapping
   │
   ▼
BookingController / ShowController
   │  @Valid on request body
   │  Pageable auto-populated by PageableHandlerMethodArgumentResolver
   ▼
BookingService  ─── uses ───►  ShowService     (Caffeine cache: shows)
   │                           PaymentService  (external side effect, NOT in tx)
   │  @Transactional boundaries
   ▼
BookingRepository / ShowRepository (Spring Data JPA)
   │
   ▼
Hibernate → JDBC → H2 (in-memory)
   │
   ▼
Result maps back through DTOs (BookingResponse / ShowResponse / PagedResponse / CursorPage)
   │
   ▼
Jackson serializes → HTTP response
```

Any exception on the way back gets caught by `GlobalExceptionHandler` and turned into a consistent `ErrorResponse` JSON.

## The full cross-cutting stack (filter vs interceptor vs aspect)

The exact order a request passes through, and which tool owns which job:

```
                    ┌─ SERVLET CONTAINER (Tomcat) ─────────────────────────┐
 request ──────────►│ CorrelationIdFilter   (@Order HIGHEST — MDC cid)     │
                    │ TenantFilter          (X-Tenant-Id → ThreadLocal)    │
                    └──────────────────────┬────────────────────────────────┘
                                           ▼
                    ┌─ SPRING MVC (DispatcherServlet) ─────────────────────┐
                    │ HandlerMapping        (which controller method?)      │
                    │ RateLimitInterceptor.preHandle   (429 if over limit) │
                    │ LoggingInterceptor.preHandle     (knows HandlerMethod)│
                    │ ArgumentResolvers     (Pageable, @Valid @RequestBody) │
                    └──────────────────────┬────────────────────────────────┘
                                           ▼
                    ┌─ BEAN PROXIES (AOP layer) ───────────────────────────┐
                    │ TimingAspect @Around  (per service-method timing)     │
                    │ @Transactional proxy  (BookingTxSteps)                │
                    │ @Cacheable proxy      (ShowService)                   │
                    └──────────────────────┬────────────────────────────────┘
                                           ▼
                              controller → services → repository → H2
                                           │
                    response unwinds in REVERSE: postHandle → afterCompletion
                    → filters. @Async listeners fire AFTER_COMMIT on their own pool.
```

Rule of thumb for "where does X go?":

| Concern | Tool | Why |
|---|---|---|
| Must see EVERY request incl. 404s/static | **Filter** | runs before Spring MVC exists |
| Needs to know WHICH controller method | **Interceptor** | gets the `HandlerMethod` |
| API-scoped policy (rate limit, API logging) | **Interceptor** | path-pattern scoping in `WebConfig` |
| Applies to service/repo methods, not just web | **Aspect** | proxy wraps any bean |
| React to domain changes without coupling | **Event + listener** | `@TransactionalEventListener(AFTER_COMMIT)` |
| Don't block the response for it | **`@Async`** | own thread pool, bounded queue |

## Life of a request — POST /api/v1/bookings, every step in order

This is the flow to narrate in the interview. File names in brackets.

1. **TCP accept** — Tomcat's NIO connector accepts the socket, parses HTTP, and hands a `HttpServletRequest` to the filter chain on a worker thread from Tomcat's pool.
2. **`CorrelationIdFilter`** — first filter (`@Order(HIGHEST_PRECEDENCE)`). Reads `X-Correlation-Id` or mints one, puts it in the **MDC** so every subsequent log line of this request prints `[cid=...]`. Echoes it back as a response header.
3. **`TenantFilter`** — reads `X-Tenant-Id`, parses it, stores it in `TenantContext` (a ThreadLocal). Missing/invalid header → request rejected with 400 right here; controller never runs. The `finally { clear() }` guarantees no tenant leaks to the next request on this pooled thread.
4. **`DispatcherServlet`** — Spring MVC's front controller. Asks its `HandlerMapping`s "who handles POST /api/v1/bookings?" → finds `BookingController#create`.
5. **`RateLimitInterceptor.preHandle`** — increments this tenant's counter for the current minute window. Over 120/min → writes a 429 JSON body itself and returns `false`, which stops the pipeline dead.
6. **`LoggingInterceptor.preHandle`** — records start time, logs which handler method will run (only interceptors know this — filters don't).
7. **Argument resolution** — for `@RequestBody`: Jackson deserializes JSON → `CreateBookingRequest` record. Malformed JSON → `HttpMessageNotReadableException` → 400. Then `@Valid` runs Bean Validation — a `@NotBlank` failure → `MethodArgumentNotValidException` → 400 with `fieldErrors`.
8. **`BookingController.create`** — thin: pulls tenant from `TenantContext`, delegates to `BookingService.createBooking`. Controllers route and translate; services decide.
9. **`TimingAspect`** — the call to the service passes through its AOP proxy; the aspect starts a timer around the method.
10. **`ShowService.getShow`** (via its `@Cacheable` proxy) — key `"1:5"` (tenant:show). Cache hit → method body never executes, no SQL. Miss → repository query, result cached. Wrong show / wrong tenant → `BookingNotFoundException` → 404.
11. **`BookingTxSteps.reservePending`** (via its `@Transactional` proxy — cross-bean, so the proxy actually applies) — **tx #1 opens**: best-effort seat check, INSERT with status PENDING, flush. A racing duplicate hits `UNIQUE(show_id, seat_no)` → `DataIntegrityViolationException` → rethrown as `SeatUnavailableException` → 409. **Commit.** Short transaction = locks held for milliseconds.
12. **`PaymentService.charge`** — **outside any transaction** (external money movement can't be rolled back). Deterministic in this demo: userId ending in 0 declines.
13. **`BookingTxSteps.markStatus`** — **tx #2 opens**: load booking, `setStatus(CONFIRMED/FAILED)`. If CONFIRMED, `publishEvent(BookingConfirmedEvent)` — but the listener is `AFTER_COMMIT`, so nothing fires yet. **Commit** — dirty-checking flushes the status UPDATE; only now does the event dispatch.
14. **`NotificationService.onBookingConfirmed`** — `@Async("notificationExecutor")`: runs on the notification thread pool, not the request thread. The HTTP response doesn't wait for it. It reads tenant from the **event payload** because ThreadLocals don't cross threads.
15. **Response path** — entity → `BookingResponse` DTO → Jackson serializes → 201 with `Location` header.
16. **`LoggingInterceptor.afterCompletion`** — always runs (even on exceptions): logs status + duration.
17. **Filters unwind** — `TenantFilter` clears the ThreadLocal, `CorrelationIdFilter` clears the MDC. Thread returns to Tomcat's pool, clean.

Any exception thrown between steps 7–15 short-circuits to `GlobalExceptionHandler`, which maps it to the right status code with a consistent `ErrorResponse` body.

## Life of a request — GET /api/v1/bookings/feed (keyset)

1. Steps 1–6 identical (filters → interceptors).
2. `cursor` param: absent on page 1; otherwise `Cursor.decode` unwraps base64 → `(createdAt, id)`. Garbage → `IllegalArgumentException` → 400.
3. `size` param: `@Min(1) @Max(100)` — violation → `ConstraintViolationException` → 400 (different exception than body validation — see cheatsheet).
4. Repository runs the keyset query: `WHERE tenant_id = ? AND (created_at, id) < (?, ?) ORDER BY created_at DESC, id DESC LIMIT size+1`. The composite index `idx_booking_feed` satisfies both WHERE and ORDER BY — an index seek, not a scan. **Page 5000 costs the same as page 1.**
5. Service checks: got `size+1` rows? → `hasMore=true`, drop the probe row, encode the last row's `(createdAt, id)` as the `nextCursor`.
6. Response: `content + nextCursor + hasMore`. No totals, no page numbers — that's the keyset trade, made visible in the API shape.

## Run it

```powershell
cd C:\Users\Admin\Documents\GitHub\LLDHLD\booking-service
mvn spring-boot:run
```

Then:

- App:      http://localhost:8080
- H2 UI:    http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:bookings`, user `sa`, no password)
- Health:   http://localhost:8080/actuator/health
- Caches:   http://localhost:8080/actuator/caches

## Try every endpoint (PowerShell curl)

**Every request needs `X-Tenant-Id`.**

```powershell
# 1. List shows (first call = DB hit, subsequent = cache hit — watch the log)
curl -H "X-Tenant-Id: 1" http://localhost:8080/api/v1/shows

# 2. Get one show — cached by (tenantId, showId)
curl -H "X-Tenant-Id: 1" http://localhost:8080/api/v1/shows/1
curl -H "X-Tenant-Id: 1" http://localhost:8080/api/v1/shows/1   # cache hit

# 3. Update title — evicts the cache entry
curl -X PATCH -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/shows/1/title?title=Inception+Remastered"
curl -H "X-Tenant-Id: 1" http://localhost:8080/api/v1/shows/1   # DB hit again

# 4. Offset pagination (admin table view)
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings?page=0&size=10&sort=createdAt,desc"
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings?page=1&size=10"

# 5. Keyset feed — first page has no cursor
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/feed?size=10"
# copy `nextCursor` from the response, then:
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/feed?size=10&cursor=<paste>"

# 6. Create a booking — full pipeline
curl -X POST -H "X-Tenant-Id: 1" -H "Content-Type: application/json" `
     -d '{"showId":1,"userId":9999,"seatNo":"Z1"}' `
     http://localhost:8080/api/v1/bookings

# 7. Try to double-book the same seat — 409 Conflict from the UNIQUE constraint
curl -X POST -H "X-Tenant-Id: 1" -H "Content-Type: application/json" `
     -d '{"showId":1,"userId":8888,"seatNo":"A1"}' `
     http://localhost:8080/api/v1/bookings

# 8. Validation failure — 400 with fieldErrors
curl -X POST -H "X-Tenant-Id: 1" -H "Content-Type: application/json" `
     -d '{"showId":1,"userId":8888}' `
     http://localhost:8080/api/v1/bookings

# 9. Missing tenant header — 400 from the filter
curl http://localhost:8080/api/v1/bookings

# 10. Slice pagination — hasNext but NO totals (one query, no COUNT)
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/by-status?status=CONFIRMED&size=10"

# 11. Search with optional filter — custom @Query + explicit countQuery
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/search?showId=1&size=10"
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/search?size=10"   # no filter

# 12. Payment decline branch — userId ending in 0 is deterministically declined
curl -X POST -H "X-Tenant-Id: 1" -H "Content-Type: application/json" `
     -d '{"showId":1,"userId":9010,"seatNo":"Z50"}' `
     http://localhost:8080/api/v1/bookings          # -> status FAILED

# 13. Error contract tour — each returns a clean 400, not a 500
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings?sort=notAField"        # bad sort
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/feed?size=0"           # @Min violated
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/bookings/by-status?status=NOPE" # bad enum
curl -H "X-Tenant-Id: 1" "http://localhost:8080/api/v1/shows/abc"                      # not a number

# 14. Tenant isolation — tenant 2 asking for tenant 1's show gets 404
curl -H "X-Tenant-Id: 2" http://localhost:8080/api/v1/shows/1
```

## Files, and what to say about each in an interview

| File | Why it exists |
|---|---|
| `BookingServiceApplication.java` | `@SpringBootApplication` + `@EnableCaching`. Without the second one, `@Cacheable` is silently ignored. |
| `TenantFilter` + `TenantContext` | Multi-tenant boundary. Filter reads header, sets ThreadLocal, `finally` clears it — critical because Tomcat pools threads. |
| `Show`, `Booking` entities | Money in `long cents`, timestamps in `Instant`, `@UniqueConstraint(show_id, seat_no)` prevents double-book at the DB level. |
| `BookingRepository` | All three pagination flavours: `Page` (2 queries), `Slice` (1 query, size+1), keyset (custom `@Query`, tuple expanded). |
| `ShowService` | `@Cacheable("shows", key=hash(tenantId, showId))`. Key is composite because two tenants asking for the same id must NOT share cache. |
| `PaymentService` | Mocked external call. Deliberately not `@Transactional` — side effects don't roll back. |
| `BookingService` | Two-phase write: PENDING in tx → call payment out of tx → CONFIRMED/FAILED in fresh tx. |
| `BookingController` | `Pageable` auto-populated; keyset endpoint deliberately has no `page` param. |
| `PagedResponse`, `CursorPage` | Never leak Spring's `PageImpl` — its shape is a Spring Data implementation detail. |
| `Cursor` | Composite `(createdAt, id)` — `created_at` alone isn't unique, ties get skipped/duplicated. |
| `GlobalExceptionHandler` | Consistent error shape. Validation → 400, not-found → 404, seat clash → 409. Never leak stack traces. |
| `CacheConfig` | Caffeine with `recordStats()` so `/actuator/caches` shows hit ratio. |
| `schema.sql` | Composite index `(tenant_id, created_at DESC, id DESC)` — exact leftmost-prefix match for the keyset ORDER BY. |
| `CorrelationIdFilter` | MDC `cid` on every log line of a request. Filter (not interceptor) because it must cover requests that never reach a controller. |
| `LoggingInterceptor` | `preHandle`/`afterCompletion` with the actual `HandlerMethod` + timing. `afterCompletion` always runs; `postHandle` is skipped on exceptions. |
| `RateLimitInterceptor` | Fixed-window per-tenant limit, returns 429 by short-circuiting `preHandle` (return false = controller never runs). |
| `WebConfig` | The registration step people forget — `@Component` alone does NOT activate an interceptor. Also CORS. |
| `TimingAspect` | `@Around` on all service methods. Same proxy machinery as `@Transactional` — and same self-invocation blind spot. |
| `BookingTxSteps` | THE self-invocation fix: `@Transactional` methods moved to their own bean so calls cross a proxy. Read its Javadoc — top interview trap. |
| `BookingConfirmedEvent` + `NotificationService` | `@TransactionalEventListener(AFTER_COMMIT)` + `@Async` — notification only after a real commit, off the request thread. |
| `AsyncConfig` | Bounded `ThreadPoolTaskExecutor` + `CallerRunsPolicy` — never use the default unbounded executor. |

## What's deliberately NOT here (and what you'd add for real)

- **Flyway/Liquibase migrations** — `schema.sql` is fine for the demo, never for prod
- **Spring Security** — tenant would come from a JWT claim, not a header
- **OpenAPI/Swagger** — add `springdoc-openapi-starter-webmvc-ui` and you get `/swagger-ui`
- **Resilience4j** on `PaymentService` — timeout, retry, circuit breaker
- **Distributed cache (Redis)** — Caffeine is per-JVM
- **Testcontainers Postgres** — H2 is fine for logic, real DB tests need Postgres because JPQL/SQL behaviour diverges
- **Metrics** — Micrometer + Prometheus; already wired up via `spring-boot-starter-actuator`

## Bugs the audit caught — study these, they ARE the lessons

Each of these shipped in the first version of this project and was found on re-review. They're the exact class of bug interviewers probe for.

| # | Bug | Symptom | Root cause → fix | File |
|---|---|---|---|---|
| 1 | `AUTO_INCREMENT` in DDL with `MODE=PostgreSQL` | App crashes on startup: syntax error in schema.sql | MySQL-only syntax in a Postgres-mode DB → standard `GENERATED BY DEFAULT AS IDENTITY` | `schema.sql` |
| 2 | Cache key built from `Objects.hash(tenantId, showId)` | Rare, unreproducible wrong-data responses | Hashes collide → two different (tenant, show) pairs share one cache slot → **cross-tenant data leak**. Keys must be injective → `"tenant:show"` string | `ShowService` |
| 3 | `@Transactional` methods called via `this.` | Status stuck at PENDING, no error anywhere | Self-invocation bypasses the proxy → no transaction → dirty-check flush never happens → extract to `BookingTxSteps` bean | `BookingService` |
| 4 | Param/type/sort/JSON errors hit the 500 catch-all | Client mistakes reported as server bugs | Missing handlers for `ConstraintViolationException`, `MethodArgumentTypeMismatchException`, `HttpMessageNotReadableException`, `PropertyReferenceException` → all mapped to 400 | `GlobalExceptionHandler` |
| 5 | Random 10% payment failure | Flaky test: create-booking asserted "CONFIRMED or FAILED" | Nondeterminism inside a test boundary → deterministic rule (userId ending in 0 declines) → exact assertions | `PaymentService` |
| 6 | Lost updates on concurrent show edits | Second editor silently overwrites the first | No concurrency control → `@Version` optimistic locking → stale writer gets 409 | `Show`, `schema.sql` |

The meta-lesson: **all six bugs were invisible in a happy-path demo.** They only appear under a different DB mode, a hash collision, a concurrent write, or a malformed request. Reviewing code means hunting for the conditions under which it breaks, not confirming the conditions under which it works.

## Troubleshooting

| Symptom | Likely cause |
|---|---|
| `Failed to auto-configure a DataSource` | H2 dependency missing — check `pom.xml` |
| `Cache 'shows' does not exist` | `@EnableCaching` not on the main class |
| `PropertyReferenceException: no property 'foo'` | Client sent `?sort=foo` for a non-existent property |
| Every call is a DB hit despite `@Cacheable` | You're calling `this.method()` — self-invocation bypasses the proxy |
| Tenant leaks between requests | `finally { TenantContext.clear() }` missing in the filter |
| Deep `OFFSET` is slow | Switch to keyset — you cannot fix `OFFSET` cost, only avoid it |
