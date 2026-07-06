# Worked Example: Food-Delivery App on NoSQL (DynamoDB + Redis)

> Companion to `nosql-when-and-how.md`. We design the data layer for a Swiggy/Zomato-style app with live order tracking, following the NoSQL discipline strictly: **access patterns first, then keys.**

---

## 0. Scope & scale assumptions

- 5M customers, 200k restaurants, 300k drivers (100k online at peak).
- Peak: ~2,000 orders/minute placed; each order goes through ~8 status changes.
- Live tracking: each online driver sends a GPS ping every 3 seconds.
- Latency target: p99 < 20ms for all app-facing reads.

First senior move: **notice the workload splits in two.**

1. **Business data** — customers, restaurants, menus, orders. Durable, entity-shaped, known access patterns → **DynamoDB**.
2. **Ephemeral real-time data** — driver GPS pings, "who's near this restaurant." Massive write rate, worthless after seconds, loss-tolerant → **Redis**. (Section 5 has the cost math proving pings must NOT go in DynamoDB.)

---

## 1. Step one, always: the access-pattern list

This list IS the design. Everything after is mechanical.

| # | Access pattern | Frequency | Store |
|---|---|---|---|
| AP1 | Get customer profile by customerId | High | DynamoDB |
| AP2 | Get full menu for a restaurant | Very high | DynamoDB (+cache) |
| AP3 | Get a customer's order history, newest first, paginated | Medium | DynamoDB |
| AP4 | Get one order + its line items by orderId | High | DynamoDB |
| AP5 | Restaurant dashboard: live queue of ACTIVE orders for a restaurant | High | DynamoDB |
| AP6 | Get driver's currently assigned order | High | DynamoDB |
| AP7 | Customer's active order screen: status + driver location, updates every few seconds | Very high | DynamoDB (status) + Redis (location) |
| AP8 | Dispatch: find available drivers within 3km of restaurant | High at peak | Redis GEO |
| AP9 | Update order status through its lifecycle (PLACED→ACCEPTED→PREPARING→PICKED_UP→DELIVERED) | Very high | DynamoDB |
| AP10 | Search restaurants by area/cuisine/dish text | High | **Not DynamoDB** → Elasticsearch via CDC |
| AP11 | Analytics ("avg delivery time by zone") | Batch | **Not DynamoDB** → warehouse via CDC |

AP10/AP11 are listed to *exclude* them — fuzzy search and ad-hoc aggregation are exactly what key-value stores can't do. Recognizing what does NOT belong in your NoSQL store is as important as modeling what does.

---

## 2. DynamoDB single-table design

One table, generic `PK`/`SK`, entity type encoded in the key:

| Entity | PK | SK | Notes |
|---|---|---|---|
| Customer profile | `CUST#c123` | `PROFILE` | AP1: `GetItem` |
| Customer address | `CUST#c123` | `ADDR#home` | bounded set → same partition |
| Restaurant profile | `REST#r77` | `PROFILE` | |
| Menu item | `REST#r77` | `MENU#starters#m42` | AP2: one `Query(PK=REST#r77, SK begins_with MENU#)` returns the whole menu, grouped by category because SKs sort |
| Order (header + status) | `CUST#c123` | `ORDER#2026-07-06T19:42:11Z#o901` | AP3: `Query(PK=CUST#c123, SK begins_with ORDER#, ScanIndexForward=false, Limit=20)` — newest first because the timestamp is IN the sort key |
| Order line item | `ORDER#o901` | `ITEM#1` | separate partition: unbounded-ish, and kitchen reads items without customer context |
| Driver profile | `DRIVER#d55` | `PROFILE` | `activeOrderId` attribute → AP6 is a `GetItem` |

### GSI-1 — order by id (AP4)

Order header's PK is the customer, but support/driver/restaurant flows look up by `orderId`. Add to each order item: `GSI1PK = ORDER#o901`, `GSI1SK = HEADER`. Line items already have PK `ORDER#o901`. So AP4 = one query on GSI1 + one on the main table (or duplicate the header into the `ORDER#o901` partition and make it a single query — a legitimate denormalization; pick one and document it).

### GSI-2 — the restaurant live queue (AP5), using a **sparse index**

New concept: a **sparse GSI**. DynamoDB only puts an item into a GSI **if the item has the GSI's key attributes**. So:

- When an order is created: set `GSI2PK = REST#r77`, `GSI2SK = PLACED#2026-07-06T19:42:11Z`.
- On every status change: update `GSI2SK` to `<newStatus>#<time>`.
- When the order reaches DELIVERED/CANCELLED: **remove** the `GSI2PK`/`GSI2SK` attributes → the item silently drops out of the index.

Result: GSI-2 contains *only active orders*, naturally grouped by restaurant, sorted by status+time. The restaurant dashboard is `Query(GSI2PK=REST#r77)` — small, fast, and the index stays tiny no matter how many billions of historical orders exist. Sparse indexes are the idiomatic way to model "live subset" queries.

Hot-partition check: a huge restaurant at lunch peak does maybe a few hundred active orders — far below any partition limit. Fine. (A flash-sale ghost kitchen doing 10k concurrent orders would need write-sharding the GSI2PK — note it as a known limit.)

---

## 3. Writes: invariants without transactions

### 3.1 Idempotent order placement

New concept: an **idempotency key**. Mobile networks retry; without protection, one tap can create two orders (and two charges). The client generates a unique key per checkout attempt and sends it with the request. Server-side:

```
PutItem(order)  WITH ConditionExpression: attribute_not_exists(PK)
   where the order id o901 is derived from the idempotency key
```

Retry arrives → same derived id → conditional `PutItem` fails cleanly → return the existing order. One item, one atomic write, no transaction needed. (Same trick in Redis for cheap pre-checks: `SET idem:{key} 1 NX EX 600`.)

### 3.2 Driver assignment — the race condition

Two dispatch workers pick the same free driver for two different orders. Naive read-then-write ("read driver, see no activeOrder, write assignment") is a classic TOCTOU race — both read "free," both write. Fix with a conditional write as an **atomic claim**:

```
UpdateItem(PK=DRIVER#d55, SK=PROFILE)
  SET activeOrderId = :o901
  ConditionExpression: attribute_not_exists(activeOrderId)
```

Exactly one dispatcher wins; the loser gets `ConditionalCheckFailedException` and tries the next candidate. This is the NoSQL replacement for `SELECT ... FOR UPDATE`.

If the order must simultaneously record the driver (two items updated atomically), this is one of the rare justified uses of `TransactWriteItems`: claim driver + set `order.driverId`, both conditional, all-or-nothing.

### 3.3 Status transitions as a guarded state machine

Never blind-write status. Enforce legal transitions in the condition:

```
UpdateItem(order)
  SET #status = :PICKED_UP, GSI2SK = :"PICKED_UP#<now>"
  ConditionExpression: #status = :PREPARING
```

An out-of-order or duplicate webhook can't corrupt the lifecycle — the DB rejects it. Cheap, per-item, no coordination.

---

## 4. The real-time layer: Redis

### 4.1 Driver location pings (AP7 write side)

Every ping: `SET loc:driver:d55 "12.9716,77.5946,<ts>" EX 15`. Latest value only — history is worthless for tracking. The 15s TTL doubles as a liveness signal: key missing = driver offline/stale.

Customer's tracking screen: app talks to a gateway over a **WebSocket** (a persistent two-way connection, unlike HTTP request/response — lets the server push without the client polling). The gateway reads `loc:driver:d55` every 2–3s for orders it's serving, pushes to subscribed apps. At this read pattern, one Redis lookup serves thousands of watchers cheaply. (Alternative: Redis **pub/sub** — publishers `PUBLISH` to a channel, subscribers receive instantly with no storage — good when the gateway should be push-driven end-to-end.)

### 4.2 Nearby-driver matching (AP8) — Redis GEO

New concept: Redis **GEO commands** — a sorted set where the score encodes lat/lng (via geohashing: interleaving latitude/longitude bits so nearby points get nearby scores). You get radius queries out of a data structure that's still just a sorted set:

```
GEOADD drivers:online 77.5946 12.9716 d55          # on each ping, if driver is free
GEOSEARCH drivers:online FROMLONLAT 77.60 12.97 BYRADIUS 3 km ASC COUNT 20
ZREM drivers:online d55                             # on assignment / going offline
```

Dispatch = `GEOSEARCH` → try atomic claim (3.2) on candidates in order. Shard the key by city (`drivers:online:{city}`) so no single sorted set becomes a mega-key and traffic spreads across a Redis Cluster.

### 4.3 Menu cache

`GET menu:r77` → miss → DynamoDB query (AP2) → `SET menu:r77 <json> EX 300`. Invalidate with `DEL menu:r77` when the restaurant edits the menu. Jitter the TTL (±30s) so a whole city's menus don't expire in the same second (stampede protection).

---

## 5. The cost math that justifies the split

Why not just write GPS pings to DynamoDB and keep one database?

- 100k online drivers × 1 ping / 3s = **~33,000 writes/sec, sustained**.
- DynamoDB: 1 WCU per write ≈ 33k WCU. On-demand pricing ~$1.25 per million writes → 33k/s ≈ 2.85 **billion** writes/day ≈ **$3,500/day ≈ $107k/month** — for data that's stale in 15 seconds and never queried historically.
- Redis: 33k SETs/sec is a light load for one node; 100k keys × ~100 bytes ≈ 10MB of RAM. Effectively rounding error.

Same logic in reverse: orders (~2k/min ≈ 33 writes/sec) are trivial for DynamoDB and *need* its durability. **Match the store to the data's value-per-byte and lifespan** — this one slide is the whole polyglot-persistence argument.

---

## 6. Fan-out: DynamoDB Streams

Order status changes must trigger: push notification to customer, restaurant dashboard refresh, driver app update, analytics event. Do NOT do these synchronously in the write path (slow, partial-failure hell). Instead:

```
Order status write → DynamoDB Stream (ordered change log) → Lambda consumers:
  ├─ push notification service
  ├─ WebSocket gateway (publish to order channel)
  ├─ Kafka → warehouse (AP11 analytics)
  └─ Elasticsearch indexer (AP10 restaurant search, from menu/restaurant changes)
```

The write path stays one conditional `UpdateItem`; everything else is eventually consistent within ~100ms–1s, which is exactly acceptable for notifications, dashboards, search, and analytics.

---

## 7. Design-review checklist applied to this system

- ✅ Every AP resolves to `GetItem` or a single-partition `Query` — no Scans anywhere.
- ✅ Partition keys are high-cardinality (`CUST#`, `ORDER#`, `DRIVER#`); GSI2 heat bounded by per-restaurant active orders.
- ✅ Unbounded data (order history) grows as *items in a partition keyed by customer*, not arrays in one item; pagination via `Limit` + `LastEvaluatedKey`.
- ✅ Invariants are per-item conditional writes; the single transaction (driver claim + order update) is rationed and justified.
- ✅ Ephemeral high-volume data kept out of the durable store (cost math, Section 5).
- ✅ Search & analytics offloaded via streams — the OLTP table never serves a `GROUP BY`.
- ⚠️ Known limits, documented: flash-sale restaurant → GSI2 write-sharding; payments themselves live in a relational ledger (money = multi-row invariants = Postgres), referenced by `paymentId`.

---

## 8. Exercise (do this before peeking)

Add **customer ratings & reviews**: (a) show a restaurant's rating summary on every menu load, (b) list a restaurant's reviews newest-first paginated, (c) a customer can edit their own review of a restaurant.

Design the items, keys, and any GSI. Constraints: menu load must not aggregate anything at read time; one customer = max one review per restaurant.

<details><summary>One solution</summary>

- Review item: `PK=REST#r77`, `SK=REVIEW#<ts>#<custId>` → (b) is a descending Query. Edit needs lookup by customer: add `GSI3PK=CUST#c123`, `GSI3SK=REVIEW#REST#r77`. Enforce one-per-customer with a second item `PK=REST#r77`, `SK=REVIEWBY#c123` written in a transaction with `attribute_not_exists` (or make REVIEWBY#c123 the SK itself and drop time-ordering, using a GSI for the newest-first listing instead — trade-off to discuss).
- Summary: **computed pattern** — attributes `ratingSum`, `ratingCount` on `REST#r77 / PROFILE`, updated by an atomic `ADD` (or by the stream consumer) on review write/edit. Menu load reads it for free; never aggregate reviews at read time.
</details>
