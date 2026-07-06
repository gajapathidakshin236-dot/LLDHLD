# 11 — Concurrency Lessons (the thread running through EVERY system)

Concurrency issues appeared in all six LLDs. This file builds the topic from
zero, then maps every fix we used back to the system it came from.

---

## GROUND FLOOR: what is a thread? (no assumed knowledge)

### First: what is a program vs a process?
- A **program** = the code sitting on disk (your compiled `.class` files).
- A **process** = that program RUNNING — loaded into memory, executing. Open
  Chrome twice = two processes of one program.

### What is a thread?
A **thread** = one independent lane of execution INSIDE a process. A process
starts with one thread (the `main` thread), but can create more — and all
threads in a process **share the same memory** (the same objects, the same
fields).

Analogy: a kitchen (the process) with several cooks (threads). All cooks share
the same fridge and counters (memory/objects). More cooks = more done in
parallel — but two cooks grabbing the same pan at once = trouble.

### Why do threads exist? (the problem they solve)
One lane of execution can only do one thing at a time. A web server handling
requests one-by-one would make user #2 wait for user #1. Threads let the server
handle many requests SIMULTANEOUSLY — one thread per request. Speed and
responsiveness. Every backend you'll build (Spring apps included) is
multi-threaded: each incoming HTTP request runs on its own thread, and they all
touch the same shared objects.

That sharing is the power AND the danger. Which brings us to:

---

## THE CORE PROBLEM: the race condition

### Definition
A **race condition** = a bug where the RESULT depends on the unpredictable
TIMING of threads — two threads interleave their steps in an order you didn't
intend, and shared data gets corrupted.

### Why it happens: operations that LOOK atomic aren't
**Atomic** = happens as ONE indivisible step; nothing can interleave in the
middle. Most code is NOT atomic. The classic example — `counter++` — is
actually THREE steps at the machine level:
```
1. READ  counter        (say it's 5)
2. ADD   1              (6)
3. WRITE 6 back
```
Two threads creating Orders at the same instant:
```
Thread A: reads 5 ─┐
Thread B: reads 5 ─┤  both read BEFORE either writes
Thread A: writes 6 │
Thread B: writes 6 ┘   -> both orders got id 5 (DUPLICATE), counter lost a step
```
The interleaving in the middle of a "single" statement is the race.

### Every race we hit in the LLDs (same disease, different clothes)
| System | The race | The shared data |
|---|---|---|
| Parking Lot | two threads find the SAME free spot; both `occupy()` -> two cars, one spot | the spot's occupied flag; find+occupy not atomic |
| Elevator | strategy READS currentFloor/state while move() WRITES them; TreeSet corrupted by concurrent adds | targetFloors, currentFloor, state |
| Rate Limiter | two requests for the SAME client both read tokens=1, both consume -> over-limit | the client's TokenBucket |
| Order IDs | `counter++` duplicates ids under concurrent creation | the static counter |
| Kafka toy | two threads appending to a partition's ArrayList | the log list |
| Stock Trading | two submits mutating the book mid-match -> corrupted heaps | the bid/ask heaps |

Pattern to internalize: **race = SHARED data + at least one WRITER + a
multi-step operation that can be interleaved.** Remove any of the three and the
race dies. All our fixes do exactly that.

---

## FIX #1: `synchronized` — the mutual-exclusion lock

### What a lock is
A **lock** (mutex) = a token only ONE thread can hold at a time. To enter a
protected section, a thread must acquire the lock; others BLOCK (wait) until
it's released. This turns a multi-step operation back into an effectively
atomic one — no interleaving inside.

### The two forms (they matter!)
```java
public synchronized void park() { ... }        // form 1: locks on `this` (the object)
public static synchronized X get() { ... }     // static method: locks on the CLASS object
public void park() {
    synchronized (someObject) { ... }          // form 2: locks on ANY object you choose
}
```
Form 2 is what enables fine-grained locking (next section) — YOU pick which
object is the lock, so you pick the GRANULARITY.

### Where we used method-level synchronized (and why it was OK there)
- `ParkingLotController.parkVehicle/exitVehicle` — makes find-spot+occupy one
  atomic unit. Trade-off acknowledged: one car enters at a time.
- `Exchange.submit` (stock trading) — order intake SERIALIZED so the book can't
  be corrupted mid-match. This one is actually CORRECT design, not a shortcut:
  real exchanges deliberately run ONE matching thread per symbol
  ("single-writer discipline") because matching must be atomic per order.
- `Partition.append/read` (Kafka toy) — guard the shared ArrayList.

---

## FIX #2: LOCK GRANULARITY — the senior-level idea

### The question every design must answer: WHAT do you lock?
- **Coarse lock** (one global lock): simple, correct — but EVERYTHING
  serializes. One car entering the lot blocks all other cars. One client's
  rate-limit check blocks every other client. Throughput dies.
- **Fine-grained lock** (one lock per independent resource): only operations on
  the SAME resource serialize; independent resources proceed in parallel.

### The rule we derived (appeared 3+ times)
> **Lock at the boundary of correctness — the smallest unit whose steps must be
> atomic — and no wider.**

| System | Coarse (bad) | Fine-grained (what we did) |
|---|---|---|
| Elevator | lock the Controller | lock **per Elevator** — cars move in parallel |
| Rate Limiter | synchronized allowRequest (whole strategy) | `synchronized (bucket)` — **per client**; user_123 and user_456 never contend |
| Parking Lot | lock the whole lot | per-SPOT locks / AtomicBoolean on occupied — different spots park in parallel |
| Sliding window | lock the strategy | `synchronized (log)` — per client's deque |

The interview phrasing (rate-limiter version, adapt per system):
> "I lock the per-client bucket, not the whole limiter, so different clients
> don't contend. Only concurrent requests for the SAME client serialize — which
> is exactly the correctness boundary."

---

## FIX #3: ATOMIC CLASSES — lock-free single-variable safety

For a SINGLE shared variable (a counter), a lock is overkill. Java's
`java.util.concurrent.atomic` classes make read-modify-write ONE indivisible
hardware operation:
```java
private static final AtomicInteger counter = new AtomicInteger(1);
this.id = counter.getAndIncrement();   // atomic "return current, then bump"
// (AtomicLong used in Stock Trading for order ids — same idea, bigger range)
```
`getAndIncrement()` = the thread-safe `counter++` (post-increment semantics:
first caller still gets 1). No lock, no blocking — faster under contention.

Rule of thumb:
- ONE variable to protect -> Atomic class.
- MULTIPLE fields that must change together (tokens AND lastRefillTime; find
  AND occupy) -> a lock around the whole unit. Atomics can't make two separate
  writes jointly atomic.
That's why TokenBucket used `synchronized(bucket)` (two fields move together)
but order IDs used AtomicLong (one variable).

---

## FIX #4: CONCURRENT COLLECTIONS

### ConcurrentHashMap (used in Rate Limiter + Kafka broker)
A HashMap is NOT thread-safe — concurrent writes can corrupt it. ConcurrentHashMap
is a Map built for concurrent access: reads don't block, writes lock only small
internal segments, and — crucially — it gives you ATOMIC compound operations:

**`computeIfAbsent(key, k -> new Thing())`** = "get it, or if missing,
create-AND-STORE it" as ONE atomic step. The naive version has a race:
```java
Thing t = map.get(key);
if (t == null) { t = new Thing(); map.put(key, t); }   // two threads can BOTH
                                                        // see null -> two Things,
                                                        // one gets lost
```
computeIfAbsent closes that gap. We used it for: per-client TokenBucket
creation, per-client sliding-window deques, per-group offset maps in the
broker.

**`computeIfAbsent` vs `getOrDefault`** — the distinction that confused me:
| | key missing -> | STORES it? | use when |
|---|---|---|---|
| computeIfAbsent | build via lambda | **YES** | you'll WRITE into the result later (it must be the real, connected object) |
| getOrDefault | return the default | **NO** | you only need a VALUE to read; the real write happens separately, only if warranted |
In Broker.poll: the outer map uses computeIfAbsent (we later `put` into that
inner map — it must be stored); the offset uses getOrDefault(partition, 0) (0
is just the reading default; we store the advance only after a successful read).

### CopyOnWriteArrayList (used in Observer)
A List where every WRITE copies the whole underlying array; READS are lock-free
on the current snapshot. Perfect for "many reads, rare writes" — exactly an
observer/listener list (notifications happen constantly; subscribe/unsubscribe
rarely). Fixes the ConcurrentModificationException you'd get iterating a plain
ArrayList while someone unsubscribes.

---

## FIX #5 (mentioned, not built): the actor model
Instead of sharing + locking, give each resource its OWN thread consuming a
queue of commands (one thread per elevator; one matching thread per symbol).
No shared mutable state -> no locks. This is what "single-writer discipline"
formalizes; real exchanges and many high-performance systems work this way.

---

## THE `volatile` KEYWORD (from Singleton DCL)
`volatile` on a field forbids the JVM from REORDERING writes around it and
guarantees visibility across threads. Needed in double-checked locking because
`new Logger()` is really allocate -> initialize -> assign, and the JVM may do
allocate -> assign -> initialize; another thread could then see a non-null but
HALF-BUILT object. `volatile` prevents that. (It does NOT make `count++`
atomic — visibility, not atomicity. Different tools for different problems.)

---

## DISTRIBUTED = the locks stop working
Everything above protects ONE JVM. Behind a load balancer, each server has its
own memory: two servers can hand out the same parking spot / rate-limit budget
/ singleton "instance" — the JVM locks can't see each other. The fix is always
the same shape: **move the shared state OUT of JVM memory** into a shared store
(Redis, a DB) with ITS OWN atomic operations (Redis Lua scripts for token
bucket; SELECT ... FOR UPDATE; sorted sets for sliding windows). Every
"how do you scale this?" follow-up lands here.

---

## THE DECISION LADDER (what to reach for, in order)
```
1. Can I avoid sharing at all? (immutable objects, per-request data, actor/queue)
2. One variable?               -> Atomic class (AtomicInteger/Long/Boolean)
3. A map of independent things? -> ConcurrentHashMap + computeIfAbsent
4. Multiple fields moving together? -> a lock — at the FINEST correct granularity
   (per-spot / per-bucket / per-elevator, never global unless intake must serialize)
5. Read-heavy listener list?   -> CopyOnWriteArrayList
6. Multi-server?               -> state to Redis/DB with atomic server-side ops
```

## One-liners
> "counter++ is three steps — read, add, write — so it races; AtomicInteger's
> getAndIncrement makes it one indivisible operation."
> "Lock at the correctness boundary: per-client, per-spot, per-elevator —
> independent resources must not contend."
> "computeIfAbsent is the atomic get-or-create; the naive null-check-then-put
> has a two-thread race."
> "JVM locks don't cross servers — distributed means shared state moves to
> Redis/DB with server-side atomic operations."
