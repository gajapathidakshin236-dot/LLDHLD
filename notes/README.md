# LLD Master Notes — Complete Study Package

Detailed, ground-up notes from the full LLD prep: foundations (including HLD vs
LLD and ADRs), 6 complete systems, data-structure internals, and the concurrency
thread that ran through everything. Every file includes worked traces, the
mistakes I actually made (flagged), and interview one-liners.

> **Merged set (2026-07-06):** this folder combines two note tracks —
> the curriculum notes that were already in this repo (HLD/LLD/ADR foundations
> + the Factory Method deep dive) and the detailed session notes package.
> Nothing was dropped: the old `01-foundations.md` is now Part I of the new
> `01-foundations.md`, and the old `10-factory-method.md` lives in full inside
> `02-patterns-creational.md` §B.
>
> Related files elsewhere in the repo: `00-roadmap.md` (the full 100-topic
> curriculum plan) and `../LLD_INDEX.md` (catalog of the runnable LLD code
> with paths and pattern usage).

---

## THE MAP (read in this order the first time)

```
00-roadmap.md                  The full LLD+HLD curriculum plan (phases A-G,
                               100 planned topics). The files below are the
                               ones actually written so far.
01-foundations.md              PART I: what software design is, HLD vs LLD,
                               ADRs (anatomy, statuses, worked rate-limiter
                               example). PART II: Composition vs Inheritance,
                               Coupling/Cohesion, Law of Demeter, SOLID map,
                               has-a/is-a rules
02-patterns-creational.md      Singleton (5 levels + production critique),
                               Factory family (+ the counting test), the FULL
                               Factory Method deep dive (GoF roles, JDK
                               examples, edge cases, exercises), Builder
03-patterns-structural-behavioral.md
                               Decorator, Adapter, Observer, Chain of
                               Responsibility, Strategy, State, Template Method,
                               + rapid-fire on the remaining GoF patterns
04-elevator.md                 5-step framework, SCAN/LOOK, TreeSet ceiling/floor,
                               dispatch Strategy, starvation fallback
05-parking-lot.md              Lot->Floor->Spot composition, 3 strategies,
                               fee-vs-payment SRP, why NOT a Singleton
06-rate-limiter.md             Token bucket (the capacity-vs-tokens fix),
                               sliding window log, per-client locking,
                               computeIfAbsent vs getOrDefault
07-kafka-pubsub.md             partitions vs offsets (2D coordinates), the
                               per-(group,partition) offset store, consumer
                               group rules, Broker.poll walkthrough
08-food-delivery.md            MenuItem vs OrderItem (catalog vs transaction),
                               the GUARDED STATE MACHINE (enum + transition map
                               vs full State pattern)
09-stock-trading.md            7 layers from "what is a share" to a matching
                               engine; order book = two heaps; price-time
                               priority; partial fills
10-data-structures.md          TreeSet/red-black tree, Trie, Heap internals,
                               the Top-K min-heap trick
11-concurrency.md              threads from zero, every race we hit, the fix
                               ladder: synchronized -> granularity -> atomics ->
                               concurrent collections -> distributed (Redis)
```

Runnable code for every system lives separately (ElevatorSystem.java,
ParkingLotSystem.java, RateLimiterSystem.java, KafkaSystem.java,
FoodDeliverySystem.java, StockTradingSystem.java — all verified on JDK 21).
See `../LLD_INDEX.md` for the in-repo code catalog
(`src/main/java/com/company/...`, `untitled/src/LLD/ELEVATOR/`, etc.).

---

## THE 5-STEP LLD FRAMEWORK (walk it OUT LOUD in every interview)

```
1. REQUIREMENTS    plain English, 5-7 bullets, agree scope with interviewer
2. ENTITIES        the NOUNS -> classes / enums / interfaces
3. RELATIONSHIPS   has-a / is-a between them (multiplicity where it matters)
4. KEY METHODS     the important verbs per class — signatures + one-liners
5. PATTERNS        name where each fits NATURALLY (never force one)
```
Jumping straight to code is the #1 junior tell. Steps 1-3 take five minutes and
earn most of the design credit.

### Relationship rules (free points once locked)
```
Holds it as a field           -> has-a   (composition ◆ owned/dies-with;
                                          aggregation ◇ swappable/injected)
implements / extends          -> is-a    (EVERY concrete strategy is-a its interface)
Used only in a method         -> uses-a  (dependency)
Enum value on a class         -> has-a   (a label you HOLD; enums can't be extended)
```

---

## MY RECURRING MISTAKES (re-read this list before any interview)

1. **Naming fields/behaviors instead of the entity noun.** "int item, name" is
   not a class — `MenuItem` is. "Finds the spot" is not an entity —
   `SpotAllocationStrategy` is. Name the NOUN; fields and verbs come later.
2. **implements -> is-a. Every time.** I answered "has-a" for
   NearestAgentStrategy -> AgentAssignmentStrategy while getting the identical
   shape right one line later. A concrete strategy IS one way of doing the
   thing; it doesn't HOLD one.
3. **Token bucket: capacity is the CONTAINER (fixed); tokens are the WATER
   (changes).** Refill = `tokens = min(capacity, tokens + elapsed*rate)` — add
   to current, cap at capacity. Never grow capacity.
4. **Heap offer() adds at the BOTTOM and bubbles UP** (not at the top). poll()
   takes the top, moves the last element up, bubbles DOWN. The travel = log n.
5. **Trie: "card" after "car" creates 1 node** (the d). Shared prefix reused;
   a longer word's EXTRA letters still need new nodes.
6. **Answer relationship questions with the WORD, not a behavior description.**
   "has partitioner logic to set messages" is not a relationship — "is-a" is.
7. **A vehicle has ONE VehicleType** — "has-many" was wrong multiplicity.
   SpotType on a spot is has-a, never is-a.
8. **Bill Pugh has NO null check** — class loading IS the lazy+thread-safe
   mechanism. Don't graft Level-2 synchronized style into it.
9. **Decorator implements the INTERFACE and wraps it** — extending a concrete
   class hard-wires it to one type and kills stacking.
10. **calculateFee must be PURE** — computing a fee and collecting payment are
    two responsibilities (SRP). calculate/get = no side effects; pay/send = do
    things.
11. **counter++ is not atomic** (read-add-write) -> AtomicInteger/AtomicLong
    getAndIncrement for shared IDs.
12. **State machines: one hop at a time** — no skipping (PLACED->DELIVERED
    blocked) and terminal states have empty exits (no un-delivering).
13. **Elevator move(): the car PASSES THROUGH non-target floors** — the TreeSet
    holds only requested floors; currentFloor is just position.
14. **Kafka: offset lives per (group, partition) in the broker** — never a
    single number inside the partition (two groups would clobber each other).
    And partition vs offset are two independent coordinates (shelf vs book).

---

## INTERVIEW NARRATIVES — all six systems (~30 seconds each)

**Elevator:** "Multiple elevators with a swappable dispatch Strategy. Hall vs
car calls as a Request hierarchy. Each car runs SCAN/LOOK over a TreeSet of
targets — ceiling/floor give the nearest pending floor above/below in O(log n);
direction flips only when nothing remains that way. Dispatch prefers idle or
same-direction on-the-way cars, nearest first, with a nearest-available
fallback to prevent starvation. Per-elevator locks; extensible to banks."

**Parking Lot:** "Lot->Floor->Spot composition. Three strategies: allocation,
fee, payment — fee calculation stays PURE and separate from payment (SRP), so
we can preview a bill without charging. Spot-fit maps vehicle size to spot
size. Not a Singleton — one injected instance, per-spot locking, state to a DB
when distributed."

**Rate Limiter:** "Strategy over the algorithms; each strategy owns its
per-client state map since state shape is algorithm-specific. Token bucket does
lazy refill on read — min(capacity, tokens + elapsed*rate) — the fixed capacity
is what allows a burst up to capacity. Per-client locks so clients never
contend. Sliding-window log fixes fixed-window's boundary burst; counter
variant trades accuracy for O(1) memory. Distributed: Redis with atomic Lua."

**Kafka pub-sub:** "Topics split into partitions; a Strategy partitioner routes
by key hash so same-key messages stay ordered in one partition. Each partition
is owned by exactly one consumer per group — partition count caps parallelism.
The broker stores offsets per (group, partition), so independent groups read
the same data without interfering; poll reads at the bookmark and advances only
on a successful read."

**Food Delivery:** "Restaurant->Menu->MenuItem and Order->OrderItem — catalog
vs transaction line; OrderItem carries quantity. The lifecycle is a guarded
state machine: an enum whose allowedNext sets encode the graph — one hop at a
time, terminal states sealed, cancel only early. Agent assignment and delivery
fee are Strategies; status changes are a natural Observer hook."

**Stock Trading:** "The order book is two heaps — bids max-heap, asks min-heap
— with (price, then arrival-time) comparators: price-time priority encoded. The
matching engine peeks the best opposite order; while prices cross it trades
min(incoming, resting) at the resting price, popping filled orders and resting
leftovers. Market orders always cross, never rest. Intake serialized — single
writer per symbol — so matching is atomic."

---

## PATTERN SELECTION CHEAT SHEET

```
"Different ways to do X" (dispatch, pricing, partitioning)  -> STRATEGY
Creation with if-else on a type                             -> FACTORY (count
   product types: 1 -> Simple/Factory Method; a matched family -> Abstract Factory)
Complex object, many optional params, want immutability     -> BUILDER
Add behavior in stackable layers, same interface            -> DECORATOR
Incompatible 3rd-party interface                            -> ADAPTER
One change must notify many                                 -> OBSERVER
Request through a pipeline of handlers                      -> CHAIN OF RESP.
Lifecycle with legal/illegal transitions                    -> STATE
   (enum + transition map if states only gate transitions;
    class-per-state when behavior differs richly)
Fixed algorithm skeleton, varying steps                     -> TEMPLATE METHOD
"Exactly one instance"                                      -> prefer DI-managed
   single instance; Bill Pugh/enum if a true Singleton is demanded
```

## CONCURRENCY DECISION LADDER (from 11-concurrency.md)
```
1. Avoid sharing (immutable / per-request / actor+queue)
2. One variable                 -> Atomic classes
3. Map of independent things    -> ConcurrentHashMap + computeIfAbsent
4. Fields that move together    -> lock at the FINEST correct granularity
5. Read-heavy listener list     -> CopyOnWriteArrayList
6. Multiple servers             -> state to Redis/DB, server-side atomic ops
```

---

## HOW TO STUDY THIS (honest advice to myself)

1. **Don't re-read passively.** Pick a system, open a BLANK file, rebuild the
   core method from memory (Broker.poll, the matching loop, transitionTo, the
   token-bucket refill). Then diff against the real code. That gap is the exam.
2. **Before an interview:** re-read ONLY the Recurring Mistakes list + the six
   narratives above (10 minutes).
3. **The night before:** skim the system most likely for the company (payments
   company -> rate limiter + stock trading; logistics -> food delivery +
   parking).
