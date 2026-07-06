# 01 — Foundations

> Read this before anything else. Every later note assumes you understand the words here.
>
> This file merges two foundation tracks:
> **Part I** — what software design is, the HLD/LLD zoom levels, and ADRs (the decision-record habit).
> **Part II** — the three OO principles that came BEFORE any pattern (composition vs inheritance, coupling/cohesion, Law of Demeter), the SOLID map, and the relationship vocabulary used in every LLD.

---

# PART I — Software design, HLD vs LLD, and what an ADR is

## 1. What is "software design", actually?

When you write a program longer than a few hundred lines, you keep making **choices** that aren't about the language or the algorithm. They're choices like:

- "Should this be one class or three?"
- "Which class owns this data?"
- "Should these two services talk over HTTP or by pushing messages to a queue?"
- "Should I use one database or two?"
- "What happens if this call fails?"

**Software design = the discipline of making those choices on purpose, in a way that survives change.** Working code is the floor. Design is what stops the code from rotting six months from now when somebody asks for a new feature you didn't plan for.

A bad design ships and then becomes hell to extend. A good design ships and then **absorbs** the new requirement without you having to rewrite half the code.

That's the whole game. Everything else in this curriculum — patterns, principles, ADRs, scalability — is just *techniques* for playing it.

---

## 2. Two zoom levels: HLD and LLD

Software design problems come in two very different sizes. The industry separates them by zoom level.

### 2.1 HLD — High-Level Design

> **HLD = the design of the system as a whole, viewed from above.** What boxes exist, who talks to whom, where data lives, where the load goes, what fails when, and how the whole thing scales when you 10x the users.

You draw it as a **box-and-arrow diagram** at the level of "API gateway → user service → Postgres + Redis → notification queue". You do NOT draw individual classes here. You don't even draw functions. You draw **deployable components** (services, databases, caches, queues, CDNs) and the **network calls between them**.

Questions HLD answers:

- How many servers, of what kind, in which regions?
- SQL or NoSQL? One DB or sharded?
- Where do we put a cache, and what does it cache?
- How do we keep a counter consistent across 50 machines?
- What happens to in-flight orders if the payments service goes down?
- What's our p99 latency budget per hop?

Real-world synonyms you'll hear: **system design**, **architecture**, **macro design**, **infrastructure design**, **systems architecture**.

### 2.2 LLD — Low-Level Design

> **LLD = the design of the code inside one component.** What classes exist, what methods each class has, who calls whom, what each interface promises.

LLD is what most interviewers actually mean when they say "design X object-oriented-style". You draw it as a **UML class diagram** or just by listing classes, fields, and methods.

Questions LLD answers:

- Should `PaymentProcessor` know about `Order`, or only about an `OrderRef`?
- Is `Vehicle` an abstract class or an interface?
- Where does the state machine for "order placed → paid → shipped" live?
- When the rule "VIP customers skip the queue" changes, how many files do I touch?

Real-world synonyms: **object-oriented design**, **OOD**, **detailed design**, **micro design**, **class design**.

### 2.3 How HLD and LLD fit together

Picture a city.

- **HLD** is the city map: where the power plant is, where the highways run, where the airport is. You can answer "how does electricity get from the plant to my house?" without knowing how my microwave works inside.
- **LLD** is the wiring diagram of your microwave. You can answer "what happens when I press +30 seconds?" without knowing where the city's substations are.

You need both. A perfectly designed microwave in a city with no power grid is useless. A perfect power grid full of broken appliances is useless. Real engineering decisions cross levels — e.g. "we'll cache user sessions in Redis" is HLD, but "the session object is immutable and has these five fields" is LLD.

### 2.4 Quick disambiguation table

| Symptom                                                            | Lives in HLD       | Lives in LLD       |
| ------------------------------------------------------------------ | ------------------ | ------------------ |
| "Use Kafka instead of polling the database"                        | yes                | no                 |
| "Split the order entity into `Order` and `OrderLine`"              | no                 | yes                |
| "Add a CDN in front of the static assets"                          | yes                | no                 |
| "Make `RateLimiter` an interface and swap implementations"         | no                 | yes                |
| "Shard by user_id mod 16"                                          | yes                | no                 |
| "Use the Strategy pattern for pricing rules"                       | no                 | yes                |
| "Move pricing logic into its own microservice"                     | yes                | sort of (the boundary affects LLD on both sides) |
| "Pricing engine and tax engine should be different classes"        | no                 | yes                |

If you can't tell which level a question lives in, ask: *"if I changed my mind here, would I change a deploy diagram, or would I change a class diagram?"* Deploy diagram → HLD. Class diagram → LLD.

---

## 3. The decision problem you'll face constantly

Both HLD and LLD work boils down to **picking between options with trade-offs**.

- HLD: "Kafka vs. RabbitMQ vs. SQS for our event bus?"
- LLD: "Inheritance vs. composition for the discount hierarchy?"

In both cases you have:

1. A **context** (what we're building, constraints, NFRs like latency/throughput/team size).
2. Two or more **options**.
3. A **trade-off table** (each option scores well on some dimensions and poorly on others).
4. A **decision** (we pick one, knowing what we're giving up).
5. **Consequences** (what becomes easier, what becomes harder, what we'll have to revisit later).

This recurring shape is so common that engineers gave it a name and a file format: the **Architecture Decision Record**.

---

## 4. ADR — Architecture Decision Record

### 4.1 What is an ADR?

> **An ADR is a short markdown document that records ONE architectural decision: what we decided, why, what options we considered, and what we're giving up.**

It's just a file in your repo. Typically `docs/adr/0042-use-postgres-not-mongo.md`. People keep them in `docs/adr/` numbered sequentially.

### 4.2 Why does it exist? (the problem it solves)

You join a team. You look at the system and think:

> "Why on earth are we using Kafka here? Wouldn't SQS have been simpler?"

You ask around. Nobody remembers. The person who picked Kafka left two years ago. There's no record. So:

- You can't tell if the reason still applies. Maybe the original reason was "we need exactly-once delivery semantics" — but if SQS now supports FIFO + dedup, the decision is stale and you could simplify.
- You also can't tell if it was a *good* reason at all. Was it a strategic call, or just "the guy who left really liked Kafka"?
- You're scared to change it, because you don't know what you'd break.

This is **organizational amnesia.** Every team without ADRs has it. It's the silent killer of clean architecture — nobody dares touch anything, because nobody remembers *why* anything is the way it is.

ADRs fix this with the cheapest possible tool: a short markdown file written **at the moment** of the decision, by the people in the room.

### 4.3 Anatomy of an ADR

A good ADR has six sections.

```markdown
# ADR-0042: Use Kafka for the order-event bus

**Status:** Accepted
**Date:** 2026-06-17
**Deciders:** @dakshin, @backend-team

## Context
We need a way to publish "order placed", "order paid", "order shipped"
events so that several downstream services (search index, analytics,
emails) can react. Today we use direct HTTP calls; this couples
publisher to subscribers and falls over on retry storms.

NFRs:
- ~10,000 events/sec peak, 500/sec average
- Must replay last 7 days of events for new subscribers
- Single-region for now, multi-region in 12 months
- Two-engineer ops team

## Decision
We will use Apache Kafka (self-hosted on the existing k8s cluster).

## Options considered

### Option A — Apache Kafka (chosen)
| Dimension       | Score |
|-----------------|-------|
| Throughput      | very high |
| Replay history  | yes, native |
| Ops complexity  | high |
| Cost            | medium (we already run k8s) |
| Team familiarity| medium |

**Pros**
- Replay built-in: new consumers can rewind 7 days trivially.
- Throughput headroom for 10x growth.

**Cons**
- Operational burden (ZooKeeper/KRaft, partitions, rebalances).
- Two-engineer team will feel it.

### Option B — AWS SQS + SNS
| Dimension       | Score |
|-----------------|-------|
| Throughput      | high enough |
| Replay history  | no (max 14d retention but no replay-from-offset) |
| Ops complexity  | very low |
| Cost            | per-message; ~$X at our volume |
| Team familiarity| high |

**Pros**
- Zero ops.
- Already in our AWS bill.

**Cons**
- No replay-from-offset. We'd need to engineer that on top.
- Multi-region story is messier.

### Option C — RabbitMQ
Eliminated early: similar ops burden to Kafka but worse replay story.

## Trade-off analysis
The deciding factor is **replay**. SQS gives us simpler ops but forces
us to build a homegrown replay store; Kafka gives us replay natively
and we accept higher ops cost. With multi-region coming up in 12
months, we'd be migrating off SQS soon anyway.

## Consequences
- Easier: onboarding new subscribers, debugging by replay.
- Harder: capacity planning, partition design, ops runbook.
- To revisit: if AWS releases a Kafka-style managed offering with
  replay semantics, reopen this ADR.

## Action items
- [ ] Stand up 3-node Kafka cluster in staging
- [ ] Write partition-key guide for publishers
- [ ] Add Kafka section to on-call runbook
```

### 4.4 The five fields you must not skip

If you write nothing else, write these:

- **Context** — why this decision exists. The forces in play.
- **Decision** — one sentence: we are doing X.
- **Options considered** — at least two. "We did X because the alternative was even worse" is a stronger argument than "we did X because we like X".
- **Trade-off** — what we explicitly gave up.
- **Consequences** — what becomes easier, what becomes harder, what triggers a revisit.

### 4.5 When do you write one?

You write an ADR when the decision is:

1. **Hard to reverse.** Picking a database — yes. Picking a tab width — no.
2. **Cross-team or cross-component.** Your event bus is everybody's problem.
3. **Driven by trade-offs.** If there's an obviously correct answer, no ADR needed.
4. **Likely to be questioned later.** Three months from now somebody *will* ask "why this?".

You do NOT write an ADR for "I picked HashMap over TreeMap". That's just code.

### 4.6 ADR statuses

ADRs have a lifecycle:

- **Proposed** — a draft, not yet agreed.
- **Accepted** — the team has agreed; this is the current decision.
- **Deprecated** — no longer the recommended approach but still in use.
- **Superseded by ADR-N** — replaced by a newer decision. You never delete the old ADR; you keep it for the historical record.

The "Superseded" status is the secret weapon. Six months from now when somebody asks "why are we still on Kafka?" you can answer "we used to be; see ADR-0042 superseded by ADR-0091" — and they can read both and see what changed.

---

## 5. How HLD, LLD, and ADRs fit together

```
┌──────────────────────────────────────────────────────────┐
│  Big design decision: "How should we design Service X?"   │
└──────────────────────────────────────────────────────────┘
                │
                ▼
   ┌────────────────────────┐
   │   HLD work             │  ← deploy diagrams, network diagrams,
   │  (boxes and arrows)    │     capacity numbers, NFR commitments
   └────────────────────────┘
                │
                ▼
   ┌────────────────────────┐
   │   LLD work             │  ← class diagrams, interfaces,
   │  (classes and methods) │     responsibilities, patterns
   └────────────────────────┘
                │
                ▼
   ┌────────────────────────┐
   │   ADRs                 │  ← short docs that capture WHY
   │  (one per hard choice) │     each level chose what it chose
   └────────────────────────┘
```

Both levels produce **decisions**, and the decisions worth remembering get captured as ADRs.

---

## 6. Worked LLD vs HLD example: "we need rate limiting"

(You already have a `RateLimier/` folder in this repo. Good, we can use it as the running example. The full rate-limiter LLD is in `06-rate-limiter.md`.)

The request: *"users are slamming our public API. We need to rate-limit them."*

That single request actually creates **two** design tasks, at two zoom levels.

### LLD slice: "what does the rate-limiter look like INSIDE one service?"

You're designing classes:

- A `RateLimiter` **interface** with `boolean tryAcquire(String userId)`.
- Concrete implementations: `TokenBucketLimiter`, `LeakyBucketLimiter`, `FixedWindowLimiter`, `SlidingWindowLimiter`.
- A `RateLimiterFactory` so callers don't hard-code the implementation.
- A `RateLimiterConfig` value object so we don't pass 7 longs to every constructor.

That's all class-level stuff. Nobody outside your service knows or cares.

### HLD slice: "where does rate-limiting live in the system?"

You're answering:

- Do we limit at the **API gateway** (one chokepoint, easy) or in **each service** (more flexible, more places to misconfigure)?
- Do we count requests **per node** (cheap but inaccurate when you scale out) or **globally** via Redis (accurate, but Redis becomes a hot path)?
- What happens if Redis goes down — **fail open** (let everyone through) or **fail closed** (block everyone)?

That's all boxes-and-arrows. The class diagram doesn't care.

### And the ADR

Once you pick — say, "we'll use a sliding-window counter stored in Redis, at the API gateway, failing open on Redis outage" — that's a decision worth remembering. So you write `docs/adr/0001-rate-limiting-strategy.md` with the six sections above.

Three months later when somebody says "wait, why are we letting everyone through when Redis is down??" — the ADR has the answer: *because availability matters more than perfect rate-limiting for our use case, and the worst case is one user getting a free ride for 60 seconds.*

---

## 7. Vocabulary check (you should be able to say what each is)

- **Software design** — the practice of making choices that don't show up in any individual line of code but determine whether the codebase survives change.
- **HLD** — design at the level of services, databases, and network calls.
- **LLD** — design at the level of classes, methods, and responsibilities.
- **NFR** (non-functional requirement) — a requirement that isn't a feature: latency, throughput, availability, security, cost, team size. They're what *force* the trade-offs.
- **ADR** — a short markdown file capturing one architecture decision: context, options, decision, trade-offs, consequences.
- **Trade-off** — every benefit a choice gives you comes with a cost it makes you pay; design is the discipline of paying the costs you're willing to pay.

If any term in this list still feels fuzzy, flag it and deep-dive it before moving on.

---

## 8. Mini-exercises (Part I)

Don't skip these — they're the cheapest way to check that the ideas stuck.

1. Take this statement: *"We'll switch from HashMap to ConcurrentHashMap for the session cache."* Is it HLD or LLD? Does it deserve an ADR? Why / why not?
2. Take this statement: *"We'll add a read-replica for the orders database so reporting queries don't hammer the primary."* HLD or LLD? ADR-worthy? Why / why not?
3. Write a 5-line ADR (just Context + Decision + one Trade-off) for *"we'll write Java services in this repo using Maven, not Gradle."*
4. Open your existing `RateLimier/rate-limiter/` code. Sketch (in plain text in this note, or in a new file) which decisions in there are LLD calls (e.g., "this is an interface, not an abstract class") and which would be HLD calls if you scaled this to a real service.

---

# PART II — The three principles every pattern stands on

These three principles came BEFORE any pattern, because every pattern is just a
disciplined application of them.

---

## 9. Composition vs Inheritance ("favor composition")

### The vocabulary first
- **Inheritance** = "is-a". `class Sparrow extends Bird` — a Sparrow IS a Bird.
  The child gets the parent's methods automatically.
- **Composition** = "has-a". A class HOLDS another object as a field and
  delegates work to it. `class Bird { FlyBehavior flyBehavior; }`

### The problem inheritance creates (the Bird example)

```java
class Bird {
    public void eat() { ... }
    public void fly() { ... }        // assumes ALL birds fly
}
class Penguin extends Bird {
    @Override public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly!");  // BROKEN
    }
}
```

`Penguin penguin = new Penguin(); penguin.fly();` blows up. This violates
**LSP — Liskov Substitution Principle**: a subclass must be usable anywhere its
parent is, without breaking behavior. A child that throws on a parent method is
NOT substitutable.

### Fix level 1 — capability interfaces (Interface Segregation)

Don't put `fly()` on Bird. Model capabilities as separate interfaces:

```java
interface Flyable  { void fly(); }
interface Swimmable { void swim(); }

abstract class Bird { public void eat() { ... } }   // only what ALL birds share

class Sparrow extends Bird implements Flyable { ... }
class Penguin extends Bird implements Swimmable { ... }
class Duck    extends Bird implements Flyable, Swimmable { ... }
class Ostrich extends Bird { }                       // just eats. Fine.
```

No subclass throws "unsupported" -> LSP restored. Code that needs flyers takes
`Flyable`, not `Bird`.

### Fix level 2 — true composition (behavior objects)

Behaviors become swappable OBJECTS plugged into the bird:

```java
interface FlyBehavior { void fly(); }
class CanFly implements FlyBehavior    { public void fly() { ... } }
class CannotFly implements FlyBehavior { public void fly() { System.out.println("can't"); } }

abstract class Bird {
    protected FlyBehavior flyBehavior;          // COMPOSED, not inherited
    public Bird(FlyBehavior fb) { this.flyBehavior = fb; }
    public void performFly() { flyBehavior.fly(); }          // delegate
    public void setFlyBehavior(FlyBehavior fb) { this.flyBehavior = fb; }  // swap at RUNTIME
}
```

This is literally the **Strategy pattern**. Composition's superpower: behavior
is swappable at runtime; inheritance locks it at compile time.

### Interview line
> "Inheritance models 'is-a' but locks behavior at compile time. Composition
> models 'has-a' and lets behavior change at runtime. Prefer composition unless
> there's a true is-a relationship with shared implementation."

---

## 10. Coupling & Cohesion

### Definitions
- **Cohesion** = what a class DOES. High cohesion = the class does ONE thing well.
- **Coupling** = what a class KNOWS about others. Low coupling = it knows as
  little as possible about other classes' internals.

Aim: **HIGH cohesion, LOW coupling.**

### The disease (one class doing everything, hardcoded to everything)

```java
class OrderProcessor {
    public void processOrder(...) {
        // 1. validate   2. open raw JDBC to MySQL with hardcoded credentials
        // 3. send email via hardcoded Gmail SMTP   4. System.out logging
        // 5. inventory logic
    }
}
```

- **Low cohesion**: 5 unrelated jobs in one class (a junk drawer).
- **High coupling**: hardcoded to MySQL, Gmail, System.out. Swap MySQL->Postgres
  and this class breaks.
- **OCP violated**: adding SMS notification = MODIFY this class.

### The cure — split by responsibility + depend on abstractions (DIP)

```java
class OrderProcessor {
    private final OrderValidator validator;
    private final OrderRepository repository;       // interface
    private final NotificationService notifier;     // interface
    private final InventoryService inventory;
    private final Logger logger;                    // interface

    public OrderProcessor(OrderValidator v, OrderRepository r,
                          NotificationService n, InventoryService i, Logger l) {
        // constructor injection (DI)
        ...
    }
    public void processOrder(Order order) {
        validator.validate(order);
        repository.save(order);
        inventory.reduceStock(order);
        notifier.notify(order);
        logger.log(...);
    }
}
```

- Cohesion UP (each class one job), coupling DOWN (depends on interfaces).
- New notification channel = new impl class, `OrderProcessor` untouched (OCP).
- Testable: inject mocks.

### Interview lines
> "Cohesion is about what a class does — one thing. Coupling is about what a
> class knows — as little as possible. High cohesion, low coupling."
> "Depend on abstractions, not concretions, so swapping implementations doesn't
> ripple through the codebase." (DIP — the D in SOLID)

---

## 11. Law of Demeter ("don't talk to strangers" / "tell, don't ask")

### The rule
A method should only talk to: itself, its parameters, objects it creates, its
direct fields. NOT "friends of friends".

### The smell — the train wreck

```java
double money = customer.getWallet().getMoney();       // two dots deep
customer.getWallet().setMoney(money - amount);
```

`PaymentService` was handed a `Customer`; `Wallet` is a stranger. It now knows
Customer's INTERNAL STRUCTURE. If Wallet becomes Account, or getMoney() becomes
getBalance(), or money becomes BigDecimal — PaymentService breaks. Bonus bug:
read-check-write is a race condition under threads.

### The fix — push the operation INTO the object that owns the data

```java
class Wallet {
    private double balance;
    public void debit(double amount) {
        if (amount > balance) throw new InsufficientFundsException(...);
        balance -= amount;
    }
}
class Customer {
    private Wallet wallet;
    public void pay(double amount) { wallet.debit(amount); }   // delegate
}
class PaymentService {
    public void charge(Customer customer, double amount) {
        customer.pay(amount);          // ONE dot. Demeter happy.
    }
}
```

- Wallet owns its rules; logic lives where the data lives.
- No leaking getters. Swap Wallet->Account later: `pay()` signature unchanged.

### Nuance
Fluent APIs (builders, streams) LOOK like train wrecks but aren't — they return
`this`/same type, so no internal structure leaks. Demeter is about reaching INTO
other objects.

### Interview lines
> "Chained calls like a.getB().getC() mean you know too much about someone
> else's structure."
> "Tell, don't ask — push the operation into the object that owns the data."

---

## 12. Quick SOLID map (as encountered in the sessions)

| Letter | Principle | Where we hit it |
|---|---|---|
| S | Single Responsibility | OrderProcessor junk drawer; calculateFee must NOT also pay |
| O | Open/Closed | if-else chains -> Factory/Strategy; add class, don't modify |
| L | Liskov Substitution | Penguin.fly() throwing = violation |
| I | Interface Segregation | Flyable/Swimmable instead of fat Bird |
| D | Dependency Inversion | Controller depends on Strategy INTERFACE, injected |

## 13. Relationship vocabulary (used in every design)

| You hold it as a field | **has-a** (composition ◆ if owned/dies-with; aggregation ◇ if swappable) |
| You implement/extend it | **is-a** |
| You only use it in a method (param/local) | **uses-a** (dependency) |

RULE THAT KEPT TRIPPING: a concrete strategy **implements** its interface ->
ALWAYS **is-a**. (NearestAgentStrategy is-a AgentAssignmentStrategy. Every LLD
had exactly this shape.)

Enum relationships: something HAS an enum value (ParkingSpot has-a SpotType).
Never "is-a" an enum — you can't extend enums.

---

## 14. What's next

Flip back to `00-roadmap.md` for the full curriculum map, or continue to
`02-patterns-creational.md` (Singleton, Factory family — including the full
Factory Method deep dive — and Builder).
