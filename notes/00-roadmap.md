# LLD + HLD Learning Roadmap

> Goal: take you from "I've never heard of an ADR" to designing both the **inside** of a service (LLD) and the **outside-of-the-box system** (HLD) with confidence, using Java as the working language.
> Rule: **no assumed knowledge**. If a term shows up that hasn't been taught yet, it gets a definition + the problem it solves + prerequisites, before we use it.

---

## STATUS (updated 2026-07-06 — after the notes merge)

The numbering in this roadmap is the **planned curriculum** (100 topics). The
files actually written in `notes/` follow their own numbering. Mapping so far:

| Actual file | Covers roadmap items |
|---|---|
| `01-foundations.md` | 01 (HLD vs LLD, ADR) + 03 (coupling/cohesion) + parts of 04 (SOLID map) + 06 (composition vs inheritance) + Law of Demeter |
| `02-patterns-creational.md` | 09 (singleton), 10 (factory method — full deep dive, was its own file), 11 (abstract factory overview), 12 (builder), + simple factory |
| `03-patterns-structural-behavioral.md` | 14 (adapter), 17 (decorator), 21 (chain of resp.), 26 (observer), 27 (state), 28 (strategy), 29 (template method) + rapid-fire on the rest of GoF |
| `04-elevator.md` | 40 (case: elevator) |
| `05-parking-lot.md` | 39 (case: parking lot) |
| `06-rate-limiter.md` | 50 (case: rate limiter) + overlaps 68 |
| `07-kafka-pubsub.md` | LLD slice of 60 (message queues) |
| `08-food-delivery.md` | case study (state machine, strategies, observer) |
| `09-stock-trading.md` | case study (matching engine; LLD slice of 95) |
| `10-data-structures.md` | supporting internals: TreeSet/red-black, Trie, Heap |
| `11-concurrency.md` | supporting: threads, races, the fix ladder |
| `12-patterns-structural-2.md` | 13 (prototype), 14-20 completed: proxy, facade, composite, bridge, flyweight — with runnable code per pattern |
| `13-patterns-behavioral-2.md` | 22-26, 30-32 completed: command, iterator, mediator, memento, visitor, interpreter, null object — with runnable code per pattern |

**All 23 GoF patterns now have notes; runnable code lives in
`src/main/java/com/company/patterns/*` and `com/company/{factorymethod,
abstractfactory,builder,prototype,singleton}`.**

Still to be written: non-GoF patterns (33-38: repository/DAO, unit of work,
MVC/MVP/MVVM, CQRS, resilience, saga), remaining LLD case studies (41-52),
and all of Phase E/F/G (HLD).

---

## 0. How to read this roadmap

Each numbered phase below maps to one note file in `notes/`. We will fill them in **in order**, but only as far ahead of you as is useful — we are NOT going to dump every file at once. You self-assess what you know, we skip what you've nailed, we deep-dive what you haven't.

For every topic, each note follows the same shape:

1. **What is it?** Plain-English definition.
2. **Why does it exist?** What problem in the real world made someone invent this.
3. **Prerequisites.** What you need to know first (and a link back if we haven't taught it yet).
4. **Mental model.** A picture or analogy.
5. **Practical example in Java.** Real code you can compile.
6. **Edge cases & gotchas.** Where people get burned in production.
7. **When NOT to use it.** Anti-patterns and over-engineering signs.
8. **Exercises.** Tiny problems for you to solve.

---

## Phase A — Absolute foundations (zoom levels and decision-making)

| #   | Note                                            | What it covers                                                                   |
| --- | ----------------------------------------------- | -------------------------------------------------------------------------------- |
| 01  | `01-foundations.md`                             | What is software design. HLD vs LLD: which question each answers. What an ADR is and why it exists. Worked example. |

---

## Phase B — LLD foundations (the building blocks of "inside-a-service" design)

| #   | Note                              | What it covers                                                                   |
| --- | --------------------------------- | -------------------------------------------------------------------------------- |
| 02  | `02-oop-fundamentals.md`          | Class, object, field, method, constructor, encapsulation, inheritance, polymorphism, abstraction. Java syntax for each. Why each exists. |
| 03  | `03-coupling-cohesion.md`         | Coupling (how stuck-together two pieces of code are), cohesion (how focused one piece is). Why every design principle is a trick to lower coupling and raise cohesion. |
| 04  | `04-solid-principles.md`          | S (Single Responsibility), O (Open/Closed), L (Liskov Substitution), I (Interface Segregation), D (Dependency Inversion). Each principle with a Java before/after refactor. |
| 05  | `05-dry-kiss-yagni.md`            | The other three slogans engineers throw around: Don't Repeat Yourself, Keep It Simple, You Aren't Gonna Need It. When each is bad advice. |
| 06  | `06-composition-vs-inheritance.md`| "Favor composition over inheritance" — why; the diamond problem; delegation; mixin patterns in Java via interfaces with default methods. |
| 07  | `07-uml-quickstart.md`            | Class diagrams (boxes, arrows, multiplicity), sequence diagrams (lifelines, messages). Enough UML to communicate, not enough to drown in. |
| 08  | `08-dependency-injection.md`      | What "dependency injection" actually means. Manual DI, constructor DI, the DI-container idea (Spring, Guice). Why testability is the real motivator. |

---

## Phase C — Design patterns (the GoF 23 + the ones you'll meet at work)

We're going to do every pattern with the same template: motivation → bad code → refactor with pattern → Java code in `src/main/java/com/company/<pattern>/` → when NOT to use it.

### Creational — "how do I create the right object?"

| #   | Note                       | Status                                                                                              |
| --- | -------------------------- | --------------------------------------------------------------------------------------------------- |
| 09  | `09-singleton.md`          | You already have `src/main/java/com/company/singleton/` — we'll review + add thread-safety variants (double-checked locking, enum singleton, holder idiom). |
| 10  | `10-factory-method.md`     | Not yet in repo — to be added.                                                                      |
| 11  | `11-abstract-factory.md`   | You already have `src/main/java/com/company/abstractfactory/` — we'll review + extend with a third theme. |
| 12  | `12-builder.md`            | You already have `src/main/java/com/company/builder/` — we'll review + add Lombok-free fluent variants and Joshua Bloch's "telescoping constructor" motivation. |
| 13  | `13-prototype.md`          | You already have `src/main/java/com/company/prototype/` — we'll review + clarify deep vs shallow clone with `Cloneable` pitfalls. |

### Structural — "how do I assemble objects into bigger structures?"

| #   | Note                       |
| --- | -------------------------- |
| 14  | `14-adapter.md`            |
| 15  | `15-bridge.md`             |
| 16  | `16-composite.md`          |
| 17  | `17-decorator.md`          |
| 18  | `18-facade.md`             |
| 19  | `19-flyweight.md`          |
| 20  | `20-proxy.md`              |

### Behavioral — "how do objects coordinate and divide work at runtime?"

| #   | Note                              |
| --- | --------------------------------- |
| 21  | `21-chain-of-responsibility.md`   |
| 22  | `22-command.md`                   |
| 23  | `23-iterator.md`                  |
| 24  | `24-mediator.md`                  |
| 25  | `25-memento.md`                   |
| 26  | `26-observer.md`                  |
| 27  | `27-state.md`                     |
| 28  | `28-strategy.md`                  |
| 29  | `29-template-method.md`           |
| 30  | `30-visitor.md`                   |
| 31  | `31-interpreter.md`               |
| 32  | `32-null-object.md`               |

### Non-GoF, you-will-actually-see-these patterns

| #   | Note                          | What it covers                                                                  |
| --- | ----------------------------- | ------------------------------------------------------------------------------- |
| 33  | `33-repository-and-dao.md`    | Repository pattern, DAO, DTO. Where they overlap, where they don't.             |
| 34  | `34-unit-of-work.md`          | Treating a transaction as one object.                                           |
| 35  | `35-mvc-mvp-mvvm.md`          | The presentation triplets, explained without religion.                          |
| 36  | `36-cqrs-event-sourcing.md`   | Command-Query Responsibility Segregation; storing changes instead of state.     |
| 37  | `37-resilience-patterns.md`   | Circuit breaker, bulkhead, retry with exponential backoff + jitter, timeout, fallback. |
| 38  | `38-saga.md`                  | Long-running distributed transactions via compensating actions.                 |

---

## Phase D — LLD case studies (object-orient a real problem end to end)

For each: requirements → entities → class diagram → Java code in its own package → tests.

| #   | Note                                |
| --- | ----------------------------------- |
| 39  | `39-case-parking-lot.md`            |
| 40  | `40-case-elevator.md`               |
| 41  | `41-case-library-mgmt.md`           |
| 42  | `42-case-chess.md`                  |
| 43  | `43-case-vending-machine.md`        |
| 44  | `44-case-atm.md`                    |
| 45  | `45-case-movie-booking.md`          |
| 46  | `46-case-splitwise.md`              |
| 47  | `47-case-snake-and-ladder.md`       |
| 48  | `48-case-lru-cache.md`              |
| 49  | `49-case-logger.md`                 |
| 50  | `50-case-rate-limiter.md`           | You already have `RateLimier/rate-limiter/` in progress — we'll formalize it. |
| 51  | `51-case-file-system.md`            |
| 52  | `52-case-tic-tac-toe.md`            |

---

## Phase E — HLD foundations (the building blocks of "between services" design)

| #   | Note                              | What it covers                                                                       |
| --- | --------------------------------- | ------------------------------------------------------------------------------------ |
| 53  | `53-networking-basics.md`         | IP addresses, ports, TCP vs UDP, DNS, what a packet is. The OSI model just deep enough to be useful. |
| 54  | `54-http-and-rest.md`             | HTTP request/response anatomy, methods, status codes, headers, cookies, idempotency, REST conventions. |
| 55  | `55-other-api-styles.md`          | When NOT to use REST — GraphQL, gRPC, WebSockets, Server-Sent Events. Trade-offs.    |
| 56  | `56-databases-sql.md`             | Tables, normalization, transactions, ACID, isolation levels (the four anomalies), indexes (B-tree, hash), execution plans. |
| 57  | `57-databases-nosql.md`           | Document, key-value, wide-column, graph. BASE vs ACID. When each shape wins.         |
| 58  | `58-caching.md`                   | Browser, CDN, application, distributed (Redis/Memcached). Cache aside, write-through, write-back, write-around. TTLs, invalidation, stampedes. |
| 59  | `59-load-balancing.md`            | L4 vs L7, round-robin, least-connections, consistent hashing, sticky sessions. Health checks. |
| 60  | `60-message-queues.md`            | Point-to-point vs pub/sub. Kafka, RabbitMQ, SQS, SNS. Delivery semantics (at-most-once, at-least-once, exactly-once). |
| 61  | `61-cdn.md`                       | What edge caching gives you, what it doesn't.                                        |
| 62  | `62-scalability.md`               | Vertical vs horizontal, stateless services, sharding, partitioning, replication.    |
| 63  | `63-replication.md`               | Leader-follower, multi-leader, leaderless. Sync vs async. Failover.                  |
| 64  | `64-consistency-models.md`        | Strong, eventual, causal, read-your-writes, monotonic-read. With examples.           |
| 65  | `65-cap-and-pacelc.md`            | CAP theorem, why "Consistency" in CAP is not the same as ACID's C, PACELC's PA/EL extension. |
| 66  | `66-monolith-vs-microservices.md` | The real trade-off (deployability vs distributed-system tax). When microservices are wrong. |
| 67  | `67-distributed-systems-core.md`  | Consensus (Raft, Paxos), leader election, clocks (logical, vector, hybrid), gossip. |
| 68  | `68-rate-limiting.md`             | Token bucket, leaky bucket, fixed window, sliding window. Where this overlaps with your `RateLimier/` LLD case study. |
| 69  | `69-api-gateway-service-mesh.md`  | Edge concerns (auth, routing, throttling) vs in-mesh concerns (mTLS, retries).      |
| 70  | `70-observability.md`             | Logs vs metrics vs traces. The three you actually need: error rate, latency, saturation. |
| 71  | `71-security-basics.md`           | AuthN vs AuthZ, OAuth2 + OIDC, JWTs, TLS, OWASP Top 10 in one page.                 |

---

## Phase F — HLD case studies (whiteboard-style system designs)

For each: clarifying questions → back-of-envelope numbers → high-level diagram → component-by-component design → trade-offs → "what would scale-2 look like".

| #   | Note                                       |
| --- | ------------------------------------------ |
| 72  | `72-design-url-shortener.md`               |
| 73  | `73-design-pastebin.md`                    |
| 74  | `74-design-twitter-feed.md`                |
| 75  | `75-design-instagram.md`                   |
| 76  | `76-design-whatsapp.md`                    |
| 77  | `77-design-youtube.md`                     |
| 78  | `78-design-netflix.md`                     |
| 79  | `79-design-uber.md`                        |
| 80  | `80-design-notification-system.md`         |
| 81  | `81-design-search-autocomplete.md`         |
| 82  | `82-design-google-search.md`               |
| 83  | `83-design-distributed-cache.md`           |
| 84  | `84-design-rate-limiter-distributed.md`    |
| 85  | `85-design-web-crawler.md`                 |
| 86  | `86-design-dropbox.md`                     |
| 87  | `87-design-amazon-product.md`              |
| 88  | `88-design-payment-system.md`              |
| 89  | `89-design-booking-system.md`              |
| 90  | `90-design-ad-serving.md`                  |
| 91  | `91-design-recommendation-system.md`       |
| 92  | `92-design-job-scheduler.md`               |
| 93  | `93-design-log-aggregator.md`              |
| 94  | `94-design-typeahead.md`                   |
| 95  | `95-design-stock-exchange.md`              |

---

## Phase G — Interview drill + lead-engineer track

| #   | Note                                  | What it covers                                                                  |
| --- | ------------------------------------- | ------------------------------------------------------------------------------- |
| 96  | `96-interview-framework-LLD.md`       | The 7-step LLD interview frame: clarify → actors → use cases → entities → class diagram → API → code skeleton. |
| 97  | `97-interview-framework-HLD.md`       | The 6-step HLD frame: clarify → scope → estimate (QPS/storage) → high-level → deep-dive component → bottlenecks/follow-ups. |
| 98  | `98-back-of-envelope.md`              | Numbers every engineer should memorize: latency table, "billions per day → QPS", storage sizing.|
| 99  | `99-trade-off-vocabulary.md`          | The vocabulary that makes you sound senior: idempotency, fanout, hot-key, thundering herd, head-of-line blocking, write amplification, read amplification, tail latency. |
| 100 | `100-adr-library.md`                  | A growing collection of real ADRs we write together as we design things. Closes the loop with Phase A. |

---

## What you should do next

Open `01-foundations.md`. After that, answer the self-assessment question I'm about to ask you in chat so we can skip what you already know and start deep-diving from your first real gap.
