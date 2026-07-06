# 06 — LLD: Rate Limiter (full walkthrough)

Core infrastructure at Razorpay/PhonePe-type companies. Teaches: per-client
state, the token-bucket refill math (my biggest conceptual fix of the whole
prep), lazy computation, fine-grained locking, and get-or-create map patterns.

---

## Step 1 — Requirements
```
1. Rate-limit BY a client identifier (userId / IP / API key) — the KEY we
   bucket requests under. (NOT "the load balancer" — though in prod the limiter
   often LIVES at the gateway, the design question is what key we count by.)
2. Allow if under the limit; reject (HTTP 429) if over
3. Configurable: N requests per window, per client
4. Multiple SWAPPABLE algorithms (Token Bucket, Leaky Bucket, Fixed Window,
   Sliding Window) -> Strategy
5. PER-CLIENT STATE: each client has its own counter/bucket
   -> somewhere there's a Map<clientId, state>
6. Thread-safe: concurrent requests, even for the SAME client
```
Scope: single-machine, in-memory. Distributed version = same design with the
per-client Map moved to Redis (atomic Lua for token bucket; sorted set
ZADD/ZREMRANGEBYSCORE for sliding window).

## Step 2 — Entities (and the KEY insight about state)
```
RateLimiterController                the entry point; holds ONE strategy, delegates
RateLimitingStrategy (interface)     boolean allowRequest(String clientId)
  TokenBucketStrategy
  SlidingWindowLogStrategy
  (LeakyBucket / FixedWindow — same shape)
TokenBucket                          per-client state FOR TOKEN BUCKET ONLY
```
**THE INSIGHT: there is NO universal ClientState class.** Each algorithm's
state has a different SHAPE:
| Strategy | one client's state |
|---|---|
| Token Bucket | { tokens, lastRefillTime } |
| Leaky Bucket | { waterLevel, lastLeakTime } |
| Fixed Window | { windowStart, count } |
| Sliding Window Log | Deque<Long> timestamps |
So **each strategy OWNS its own `Map<clientId, itsStateType>`** internally. In
an interview, when asked "where does state live": "inside each strategy,
because the representation is algorithm-specific."

---

## TOKEN BUCKET — the algorithm + THE FIX

### The mental model (burn this in — I got it wrong first)
> **The bucket is the CONTAINER (capacity = its size, FIXED FOREVER). Tokens
> are the WATER (the level CHANGES — time pours water in, a request scoops one
> out). The bucket NEVER resizes.**

MY MISTAKE: `capacity = capacity + elapsed*rate` — I was GROWING THE CONTAINER
over time. Time adds TOKENS, never capacity. Capacity is the ceiling you cap
tokens against. Once that clicked, the whole algorithm is two lines.

### The refill formula (say it aloud: "add to current, then cap at capacity")
```
tokens = min(capacity, tokens + elapsedSeconds * refillRatePerSecond)
              ▲                 ▲
        never exceed      ADD to what's there (don't replace!)
```
Two slips this formula prevents:
1. Forgetting the EXISTING tokens (it's current + added, not just added).
2. Forgetting the cap: wait 100 years, the bucket still holds only `capacity`.
   That cap is WHY token bucket allows a burst UP TO capacity but never more —
   its signature feature.

Worked: capacity 10, holds 4, rate 2/s, 6s pass:
tokensToAdd = 12; new = min(10, 4+12) = **10** (overflow lost). Request needs 1
-> allowed, 9 left.

### Lazy refill (no background thread!)
We do NOT run a timer topping up buckets. We compute the refill ON READ, from
`lastRefillTime`. Between requests the bucket "virtually" fills; the math
materializes it when someone asks. Cheaper, simpler, exact.

### The state class — structure encodes the lesson
```java
class TokenBucket {
    private final double capacity;              // FIXED: final, getter only, NO setter
    private final long refillRatePerSecond;     // FIXED
    private double tokens;                      // MUTABLE (double! fractional refills
    private long lastRefillTime;                //  — long would truncate 0.5/s to 0)

    TokenBucket(double capacity, long rate) {
        this.capacity = capacity; this.refillRatePerSecond = rate;
        this.tokens = capacity;                          // START FULL (else first
        this.lastRefillTime = System.currentTimeMillis();// request wrongly rejected)
    }
    // fixed fields: getters only. mutable: getters+setters.
}
```
Making fixed fields `final` with no setters means the COMPILER enforces
"capacity never changes" — the structure teaches the concept.

### allowRequest — full logic
```java
boolean allowRequest(String clientId) {
    TokenBucket bucket = buckets.computeIfAbsent(clientId,
            k -> new TokenBucket(capacity, refillRate));       // get-or-create, atomic

    synchronized (bucket) {                                    // lock THIS CLIENT ONLY
        long now = System.currentTimeMillis();
        double elapsedSeconds = (now - bucket.getLastRefillTime()) / 1000.0;  // .0 keeps fractions

        double refilled = bucket.getTokens() + elapsedSeconds * bucket.getRefillRatePerSecond();
        bucket.setTokens(Math.min(bucket.getCapacity(), refilled));   // add + cap
        bucket.setLastRefillTime(now);

        if (bucket.getTokens() >= 1) {
            bucket.setTokens(bucket.getTokens() - 1);          // consume one
            return true;
        }
        return false;                                          // dry -> 429
    }
}
```

### Lock granularity (the senior answer)
`synchronized (bucket)` — the PER-CLIENT object — NOT `synchronized` on the
method. Method-level = one global lock = user_123 and user_456 block each other
despite touching separate buckets. Per-bucket = only same-client requests
serialize (which IS the correctness boundary). Same lesson as per-elevator
locks and per-spot locks. **Fine-grained > coarse.**

### `computeIfAbsent` vs `getOrDefault` (the two-line pattern I struggled with)
```java
Map<Integer,Integer> groupOffsets = offsets.computeIfAbsent(g, k -> new ConcurrentHashMap<>());
int cur = groupOffsets.getOrDefault(p, 0);
```
Both are shorthand for `if (x == null)` checks. The CRITICAL difference:
| | key missing -> | STORES the result? |
|---|---|---|
| computeIfAbsent | run lambda, create | **YES** — written into the map |
| getOrDefault | return the default | **NO** — map untouched |
Why it matters: we later WRITE into the inner map — it must be the one actually
connected to the outer map (computeIfAbsent). The default 0 is only a value to
READ with — the real write happens later, only if warranted (getOrDefault).
Using getOrDefault for the outer map = writing advances into a throwaway map =
state lost forever. Also: computeIfAbsent on ConcurrentHashMap is ATOMIC — the
naive null-check-then-put has a race (two threads both create).
Everyday analogy: Netflix "Continue Watching" — per-profile, per-show bookmark.
computeIfAbsent = "find or start your profile's bookmark book";
getOrDefault(show, 0:00) = "never watched? start at the beginning."

---

## SLIDING WINDOW LOG — the second algorithm

### The idea
Per client, keep a LOG (Deque) of exact request timestamps. On each request:
```
1. Evict timestamps older than (now - windowSize)  <- they "slid out"
2. if log.size() < limit -> record now, ALLOW
3. else -> REJECT
```
```java
Deque<Long> log = requestLogs.computeIfAbsent(clientId, k -> new ArrayDeque<>());
long windowStart = now - windowSizeMs;
synchronized (log) {
    while (!log.isEmpty() && log.peekFirst() <= windowStart) log.pollFirst();
    if (log.size() < maxRequests) { log.addLast(now); return true; }
    return false;
}
```
Deque: oldest at the FRONT (evict there), newest at the BACK. FIFO shape.

### Why "sliding" beats Fixed Window
Fixed window (reset counter each clock minute) has the **boundary burst** bug:
send `limit` requests at 10:00:59 and `limit` more at 10:01:00 -> DOUBLE the
rate across the boundary. Sliding window measures "the last N ms from NOW" —
no boundary to exploit. Most ACCURATE algorithm.

### The trade-off + the counter variant
Log stores one timestamp per request -> O(limit) memory per client. For huge
limits: **Sliding Window Counter** — keep just two counters (current + previous
window) weighted by position in the window. O(1) memory, slightly approximate.
Know both; say the trade-off.

---

## Controller + runtime swap
```java
class RateLimiterController {
    private RateLimitingStrategy strategy;                    // non-final
    void setStrategy(RateLimitingStrategy s) { ... }          // hot-swap (peak hours)
    boolean isAllowed(String id) { return strategy.allowRequest(id); }
}
```
Design choice to state: one strategy for all here; per-endpoint/per-tier limits
-> hold Map<route, Strategy> and pick per request.

## Interview narrative
> "Strategy over the algorithms; each strategy owns its per-client state map
> because state shape is algorithm-specific. Token bucket does lazy refill on
> read — tokens = min(capacity, tokens + elapsed*rate) — capacity is a fixed
> ceiling, which is what gives burst-up-to-capacity. Per-client locks so
> different clients never contend. Sliding-window log fixes fixed-window's
> boundary burst at O(limit) memory; the counter variant trades accuracy for
> O(1). Distributed: state moves to Redis — Lua for atomic bucket ops, sorted
> sets for the window."
