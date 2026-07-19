# Interview cheatsheet — booking-service

The 12 questions this project answers, with the shortest correct answer.

## Request lifecycle

**Q: Walk through a request from HTTP wire to database.**
1. Tomcat NIO connector accepts the socket → hands the servlet request to `DispatcherServlet`.
2. Filter chain runs. `TenantFilter` reads `X-Tenant-Id` and pins it on a `ThreadLocal` (cleared in `finally`).
3. `HandlerMapping` finds the `@RequestMapping` method. `HandlerAdapter` resolves arguments — `PageableHandlerMethodArgumentResolver` builds `Pageable` from `page/size/sort`, `@RequestBody` triggers Jackson → validation runs.
4. Controller calls service. Service opens a transaction (`@Transactional`), calls repository. Spring Data generates SQL, Hibernate binds params, JDBC executes.
5. Result climbs back → DTO mapping → Jackson serializes → response written.

## Beans and DI

**Q: How does Spring instantiate `BookingService`?**
Component scan (`@SpringBootApplication` implies `@ComponentScan("com.company.booking")`) finds `@Service`. Spring inspects the constructor, resolves each parameter type from the context, creates the bean once as a singleton. `@RequiredArgsConstructor` (Lombok) generates the constructor for injection.

## Transactions

**Q: Why isn't `createBooking` one big `@Transactional` method?**
The payment call is an external side effect. Wrapping it in the tx means a DB commit failure would roll back the DB row but the money is already gone. Split it: short tx to insert PENDING → payment call outside tx → short tx to mark CONFIRMED/FAILED.

**Q: `@Transactional(readOnly = true)` — what does it actually do?**
Hibernate sets `FlushMode.MANUAL` and skips dirty-check snapshots (less CPU + memory on large result sets), and the JDBC connection is flagged read-only. A routing DataSource sees the flag and can send the query to a read replica.

## Cache

**Q: Why does `@Cacheable` need `@EnableCaching`?**
`@Cacheable` is a hint. `@EnableCaching` activates the AOP proxy that intercepts calls to hinted methods. Without it there's no proxy, so the annotation does nothing.

**Q: What's the classic `@Cacheable` bug?**
Self-invocation. Calling `this.getShow(id)` from another method in the same class bypasses the proxy — the cache is never consulted. Always call across beans.

**Q: Why is the cache key `(tenantId, showId)` and not just `showId`?**
Multi-tenant correctness. Tenant A caching show 5 must NOT be visible when tenant B asks for show 5.

## Pagination

**Q: `Page<T>` vs `Slice<T>`?**
`Page` runs 2 queries — the data query and a `SELECT COUNT(*)`. Use only when a human reads the total. `Slice` fetches `size + 1` rows; if the extra one came back, `hasNext()` is true. One query. Use for infinite scroll.

**Q: Why is page 5000 slow?**
`OFFSET` isn't a seek. The DB sorts and produces the first 100 020 rows, discards 100 000, returns 20. O(offset). Keyset fixes it: `WHERE (created_at, id) < (:ts, :id)` seeks via the index in O(log n) and reads 20 forward. Flat cost.

**Q: Beyond speed — what else does `OFFSET` break?**
Drift. Inserts above the current page shift every row down; row 20 appears again as row 21 on the next page. Or a delete makes one silently vanish. Keyset anchors to a real row's key, not a position — drift-free.

**Q: Why is the cursor composite?**
`created_at` isn't unique. Two rows in the same millisecond collide on the boundary — either skipped or duplicated. Compound with `id` (unique) to break the tie: `(created_at, id)`.

**Q: Biggest pagination footgun?**
No `max-page-size`. `?size=999999999` loads the table into your heap. Spring silently clamps to the max — no error, just a capped page. Set it.

## Multi-tenancy

**Q: Where does `tenantId` come from and why isn't it in the URL?**
From a signed source (JWT claim in real life, header in this demo), placed on a `ThreadLocal` by a filter. In-URL tenant is easy to forge; header-plus-auth is server-side truth. Every repo query filters on it so no query can "forget" the tenant.

## Concurrency

**Q: Two users try to book seat A1 at the same instant. What happens?**
Both threads pass the `findByShowIdAndSeatNo` check (nothing there yet). Both call `save`. The DB `UNIQUE (show_id, seat_no)` fires on the second insert → `DataIntegrityViolationException` → service maps it to `SeatUnavailableException` → controller returns 409. The DB is the source of truth; the app-level check is best-effort.

## Errors

**Q: How do you keep error responses consistent?**
`@RestControllerAdvice` + `@ExceptionHandler` methods per exception type. Each returns a shared `ErrorResponse` record. Controllers never `try/catch` — they let exceptions bubble.

## Config

**Q: Why is `open-in-view: false` set?**
The default (`true`) keeps the Hibernate session open until the response is fully written, which lets you lazy-load associations in the view layer — and quietly triggers N+1 queries per request. Turning it off forces you to declare fetching explicitly in the query (`join fetch`) or the service.

## Filter vs Interceptor vs Aspect

**Q: When do you use each?**
Filter = servlet-container level, before Spring MVC; sees every request including ones that never reach a controller (correlation id, tenant extraction, auth). Interceptor = Spring MVC level; knows the exact `HandlerMethod`, path-scopable in `WebMvcConfigurer` (rate limiting, API logging). Aspect = bean-proxy level; wraps any bean method, web or not (timing, custom transactions).

**Q: Order of execution?**
Filters (by `@Order`) → DispatcherServlet → interceptors `preHandle` (registration order) → argument resolvers → controller → `postHandle` → `afterCompletion` (reverse order) → filters unwind. `postHandle` is skipped when the handler throws; `afterCompletion` always runs.

**Q: Why doesn't my interceptor fire?**
`@Component` alone doesn't register it. It must be added in a `WebMvcConfigurer.addInterceptors()` — see `WebConfig`.

## Self-invocation (the #1 proxy trap)

**Q: Why did `@Transactional` silently not work?**
`this.method()` calls skip the Spring proxy — no transaction starts, no cache is consulted, no async dispatch happens. This project hit it for real: `markStatus()` relied on dirty-checking to flush a status change; called via `this.` there was no tx, no commit, no flush. Fix: move the annotated methods to a separate bean (`BookingTxSteps`) so calls cross a proxy boundary.

## Events + async

**Q: `@EventListener` vs `@TransactionalEventListener`?**
Plain `@EventListener` fires immediately at `publishEvent()` — inside the transaction. Roll back and you've already sent the email. `@TransactionalEventListener(phase = AFTER_COMMIT)` defers until the commit succeeds; on rollback the listener never runs.

**Q: What does `@Async` need to actually be async?**
`@EnableAsync` (activates the proxy) + a defined executor. Defaults are dangerous: without config you get unbounded thread creation. Use a bounded `ThreadPoolTaskExecutor` with `CallerRunsPolicy` so overflow slows the producer instead of dropping work or OOMing. And it's proxy-based — self-invocation runs it synchronously.

**Q: Why does the event carry `tenantId` instead of reading `TenantContext`?**
The async listener runs on a different thread. ThreadLocals don't cross threads — pass data in the event payload.

## Validation — the two different exceptions

**Q: `@Valid` on a body vs `@Min` on a request param — same failure path?**
No, and this trips people constantly. `@Valid @RequestBody` failures throw `MethodArgumentNotValidException` (has `BindingResult` with per-field errors). `@Min/@Max` on `@RequestParam` (class annotated `@Validated`) throws `jakarta.validation.ConstraintViolationException`. Two different handlers needed, or one path silently becomes a 500.

## Optimistic locking

**Q: Two admins edit the same show simultaneously — what stops the lost update?**
`@Version` on the entity. Hibernate appends `WHERE version = <value read at load>` to the UPDATE and increments the column. The slower writer matches 0 rows → `ObjectOptimisticLockingFailureException` → mapped to 409; client refetches and retries. "Optimistic" = no upfront locks (vs pessimistic `SELECT FOR UPDATE`), so reads stay cheap and conflicts are detected only at write time. Choose optimistic when conflicts are rare; pessimistic when they're common enough that retries would be more expensive.

## Cache keys

**Q: What's wrong with `key = "T(Objects).hash(#a, #b)"`?**
Hashes collide. Two different key pairs mapping to one int means one caller gets the other's cached data — in a multi-tenant cache that's a cross-tenant leak. Cache keys must be injective: concatenate the raw values (`"42:7"`), don't hash them.

## Rate limiting

**Q: How is the 429 produced without the exception handler?**
The interceptor's `preHandle` returns `false` and writes the JSON body itself. `@RestControllerAdvice` only wraps handler execution — a request stopped in `preHandle` never had a handler.

**Q: Weakness of fixed-window limiting?**
Boundary burst: 120 requests at 11:59:59 + 120 at 12:00:00 = 240 in two seconds. Sliding window or token bucket smooths it. Also per-JVM counters don't work behind a load balancer — production uses Redis (Bucket4j).

---
Read `README.md` for the end-to-end diagram, curl examples, and file-by-file map. This file is answers-only for revising the day before.
