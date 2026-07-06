# NoSQL — When to Use It, How to Use It (Senior-Level Notes)

> Assumes: solid SQL (joins, indexes, transactions, normalization) and distributed-systems basics (replication, sharding, CAP).

---

## 1. Why NoSQL exists — the real reason

The popular story is "SQL doesn't scale." That's wrong, and believing it leads to bad decisions. Postgres handles tens of thousands of transactions/sec on one machine. Instagram ran on sharded Postgres. SQL scales fine — **until you need to shard, and then two specific features break**:

**Joins break.** A join between two tables works because both tables sit on the same machine and the engine can merge rows locally. Shard `users` across 50 machines and `orders` across 50 machines, and `users JOIN orders` now requires shuffling rows across the network between machines. Cross-shard joins are so slow and unpredictable that sharded-SQL shops ban them and do joins in application code.

**ACID transactions break.** A transaction touching rows on two different shards needs two-phase commit (2PC): a coordinator asks all shards to "prepare," then tells all to "commit." 2PC blocks while waiting for the slowest participant and stalls if the coordinator dies mid-protocol. It works, but it's slow and fragile — which is why cross-shard transactions are also avoided.

So the honest statement is:

> **Relational databases scale vertically very well and horizontally poorly, because their two core value propositions — joins and multi-row ACID transactions — assume all data lives on one machine.**

NoSQL databases (Dynamo/Cassandra/Mongo generation, mid-2000s, born from the Amazon Dynamo and Google Bigtable papers) made a trade: **give up joins and general-purpose transactions from day one, and in exchange get a data model that shards cleanly across hundreds of machines with predictable performance.**

That's the whole thing. Everything else — schema flexibility, JSON documents, "web scale" — is secondary. The core product is *horizontal scalability with predictable latency, bought by removing the features that don't shard.*

### The consequence you must internalize

In SQL, you normalize data and the database can answer **any** question via joins — you design the schema first and write queries later.

In NoSQL, there are no joins, so the database can only answer questions you **pre-planned for**. You must know your queries *before* you design the schema. This inversion (Section 4) is the single biggest mindset shift, and getting it wrong is the #1 cause of failed NoSQL projects.

---

## 2. The fundamental trade (one table to remember)

| | Relational (Postgres/MySQL) | NoSQL (Dynamo/Cassandra/Mongo) |
|---|---|---|
| Query flexibility | Any query, decided later (joins, ad-hoc WHERE, aggregates) | Only pre-planned queries; new query = schema redesign or backfill |
| Scaling | Vertical first; horizontal is painful (manual sharding) | Horizontal by design; add nodes |
| Latency at scale | Degrades as data/joins grow; needs tuning | Flat and predictable (single-digit ms at any size, if modeled right) |
| Transactions | Full ACID, multi-row, multi-table | Usually single-item/single-partition atomicity; limited multi-item support |
| Consistency | Strong by default | Often eventual by default, tunable |
| Schema | Enforced by DB | Enforced by your application code (the schema still exists — it just lives in your code) |
| Integrity (FKs, constraints) | DB enforces | You enforce, or accept drift |

**"Schemaless" is a lie you tell the database, not reality.** Your application still expects `user.email` to exist and be a string. NoSQL just moves schema enforcement from the DB to your code — which is a *cost* (every reader must handle every historical shape of the data ever written), occasionally a benefit (heterogeneous items, fast iteration).

---

## 3. The four families — and what each is actually for

### 3.1 Key-Value stores — Redis, DynamoDB (in its simplest use), Memcached

The data model: a giant distributed hash map. `GET(key) → value`, `PUT(key, value)`. The DB does not understand the value's contents.

- **Strength:** the simplest possible model = the fastest and most scalable. Sub-millisecond (Redis, in-memory) to single-digit ms (DynamoDB, on disk).
- **Weakness:** you can only look up by exact key. No "find all users where city = X".
- **Use for:** caching, session storage, user profiles fetched by ID, feature flags, rate-limit counters, shopping carts, leaderboards (Redis sorted sets).

### 3.2 Document stores — MongoDB, DynamoDB (typical use), Couchbase, Firestore

Key-value where the value is a structured document (JSON/BSON) the DB *can* see inside — so you can query and index on fields (`db.orders.find({status: "shipped"})`).

- **Core idea:** store an entity and its children **together as one document** (order + its line items + shipping address in one blob) instead of normalized across 4 tables. One read fetches what SQL needs a 4-way join for. "Data that is accessed together is stored together."
- **Strength:** natural fit for entity-centric access ("get everything about order 123"), flexible per-item shape, good secondary indexes (Mongo).
- **Weakness:** bad at relationships *between* documents (no real joins — Mongo's `$lookup` is a weak, unsharded-friendly join), bad at ad-hoc cross-entity analytics.
- **Use for:** product catalogs, user profiles, content/CMS, orders, event payloads — anything where the aggregate (entity + children) is the unit of access.

### 3.3 Wide-column stores — Cassandra, ScyllaDB, HBase, Bigtable

Think: a two-level map. `(partition key) → (clustering key, sorted) → columns`. Physically: all rows of a partition live contiguously on one replica set, sorted by clustering key — so "give me the last 100 events for device X" is one sequential disk read on one node.

- **Strength:** enormous, predictable **write** throughput (log-structured storage: writes are appends), linear scaling to hundreds of nodes, great for time-ordered data within a key. No single leader — every node accepts writes (multi-datacenter friendly).
- **Weakness:** the most rigid query model of all (you effectively query only by partition key + clustering-key ranges); eventual consistency by default; operationally heavy (self-hosted).
- **Use for:** time-series and telemetry (IoT, metrics), event logs, chat message history, activity feeds, anything write-heavy and append-mostly at massive scale. Discord stores trillions of messages on this model (`partition = channel`, `clustering = message time`).

### 3.4 Graph databases — Neo4j, Amazon Neptune

Nodes + edges as first-class citizens; traversals ("friends of friends of friends") are pointer-hops, not joins.

- **Why:** in SQL, each relationship "hop" is a self-join; N hops = N joins, cost explodes. In a graph DB, traversal cost is proportional to the subgraph you touch, not total data size.
- **Use for:** fraud-ring detection, recommendation ("people who bought X also…"), knowledge graphs, access-control hierarchies, network topology.
- **Don't use for:** general storage. Graph DBs are specialist tools — use only when multi-hop relationship traversal *is* the workload.

### 3.5 Honorable mentions (specialist, often paired with a main DB)

- **Search engines (Elasticsearch/OpenSearch):** inverted indexes for full-text search, fuzzy matching, faceting. Almost never the system of record — fed from the main DB via CDC (Section 12).
- **Time-series DBs (Timescale, InfluxDB):** compression + downsampling + time-window queries.
- **Vector DBs (Pinecone, pgvector):** similarity search over embeddings for AI/RAG workloads.

---

## 4. The modeling inversion — access patterns first

This is the section to master. Everything else is trivia by comparison.

### SQL workflow
1. Model the entities and relationships (ER diagram).
2. Normalize (3NF) — every fact stored once.
3. Ship. Write whatever queries you need, whenever; add indexes as needed.

### NoSQL workflow
1. **List every query the app will run** ("get user by id", "get 20 most recent orders for a user", "get all orders for a product in a date range"). Written down. Reviewed. This is the actual design artifact.
2. Design keys/documents so **each query is answered by a single key lookup or a single contiguous range read**.
3. **Denormalize freely** — copy data wherever a query needs it. A product's name may live in the product item, in every order line that bought it, and in a category listing item. Storage is cheap; cross-machine joins are what's expensive.
4. Ship. **A new query pattern later is a real project**: new index or table + backfill of existing data — not just a new SELECT.

### The price of denormalization: you own the updates

Product name copied into 50,000 order items? When the name changes you either update all copies (async, via streams/queues — never synchronously in the request path), or you decide stale copies are *correct* (an order should show the name at time of purchase — often denormalization is accidentally the right semantics). Either way: **in SQL the DB keeps data consistent; in NoSQL your application code does.** That engineering cost is the tax you pay for scale, and it's why you don't pay it unless you need to.

### Litmus test

If you can't write down your top ~10 access patterns with confidence, **you are not ready to choose NoSQL** — pick Postgres, keep flexibility, and migrate the hot paths later if needed. Uncertainty about queries is an argument *for* SQL, full stop.

---

## 5. Consistency in practice (beyond CAP)

You know CAP; here's how it shows up day-to-day.

**Tunable consistency, not a binary.** Quorum systems (Cassandra, Dynamo-style) let you pick per-request: with N=3 replicas, write at W and read at R; if `R + W > N`, reads see the latest write ("strong-ish"); if not, reads may be stale but faster/more available. Common production settings: `W=QUORUM(2), R=QUORUM(2)` for correctness-sensitive data; `W=1, R=1` for fire-and-forget telemetry. DynamoDB exposes the same idea as a boolean: eventually-consistent reads (default, half price) vs strongly-consistent reads.

**Read-your-own-writes is the bug you'll actually hit.** User edits profile → next page load hits a stale replica → their edit "disappeared" → support ticket. Fixes: strong read on the pages right after a write; or session stickiness to the written replica; or serve the just-written value from cache/client state.

**Conflicts.** Leaderless systems accept concurrent writes to the same key on different nodes. Resolution is usually **last-write-wins by timestamp** (Cassandra) — which means with concurrent updates, *one silently loses*. Design around it: model data so concurrent writers touch different items, or use atomic ops (counters, DynamoDB conditional writes) instead of read-modify-write.

**Transactions, realistically.**
- DynamoDB: `TransactWriteItems` — ACID across up to 100 items, at ~2x cost and lower throughput. Use for the rare invariant (e.g., unique-email enforcement via a second item), not as a habit.
- MongoDB: multi-document ACID transactions exist (4.0+), but if you need them *often*, your document design is wrong (should have embedded) or you need a relational DB.
- Cassandra: lightweight transactions (Paxos-based compare-and-set) — expensive; use sparingly.
- Rule of thumb: **NoSQL gives you atomicity per item/partition cheaply; anything wider is expensive and rationed.** Model so invariants live inside one item.

---

## 6. The decision framework — when to use, when to run away

### Reach for NoSQL when several of these are true

1. **Scale is real, not aspirational** — you can point at numbers (>~10k writes/sec sustained, TBs growing fast, or a hard requirement for horizontal scale-out / multi-region active-active).
2. **Access patterns are known and few** — a handful of by-key and by-range lookups, not ad-hoc questions.
3. **Latency must be flat** — you need p99 single-digit ms *regardless of data size* (SQL latency degrades as tables and joins grow; DynamoDB's is the same at 1GB and 10TB).
4. **Data is naturally aggregate-shaped or append-only** — self-contained entities (documents) or time-ordered streams (wide-column), not a dense web of many-to-many relationships.
5. **You can live with per-item atomicity** and app-enforced integrity.
6. **Ops model fits** — serverless/managed (DynamoDB: zero ops, pay-per-request) is itself a legitimate reason teams pick NoSQL even at modest scale.

### Run away (use Postgres) when

1. **Queries are unknown or evolving** — early-stage product, exploratory domain. Flexibility is worth more than scale you don't have.
2. **Many-to-many relationships dominate and are queried from many directions** — ERP, banking ledgers, marketplace with rich cross-entity queries. That web of relationships is what "relational" is *for*.
3. **You need real multi-row invariants** — money movement, inventory reservation across items, anything where "partially applied" = corruption.
4. **Reporting/analytics run on the same store** — `GROUP BY`, ad-hoc filters, BI tools. NoSQL is terrible at this; you'd end up bolting on a warehouse anyway.
5. **Your scale fits one big Postgres box** (most companies, forever). A single modern instance + read replicas + a Redis cache in front covers an enormous range. **Boring default: Postgres until it demonstrably hurts.**

### Choosing within NoSQL

| Workload | Pick |
|---|---|
| Cache, session, counters, leaderboards | Redis |
| Entity-centric CRUD at scale, known access patterns, want zero ops | DynamoDB |
| Entity-centric, richer secondary queries, flexible documents | MongoDB |
| Massive write-heavy time-ordered data, multi-DC active-active | Cassandra/Scylla |
| Full-text / fuzzy search | Elasticsearch (as a sidecar, not system of record) |
| Multi-hop relationship traversal | Neo4j/Neptune |

### The senior-level answer shape (interviews and design docs)

Never "NoSQL because scale." Instead: *"Access patterns are X, Y, Z; write rate is N/sec growing to M; we need p99 < 10ms; no cross-entity transactions needed; therefore DynamoDB with this key design. Reporting goes to the warehouse via CDC. If access patterns were still fluid, I'd stay on Postgres."* — Named patterns, named numbers, named trade-offs, named escape hatch.

---

## 7. DynamoDB deep dive — the worked example

DynamoDB is the purest expression of NoSQL discipline, so learning it teaches the mental model. Core concepts:

- **Partition key (PK):** hashed to decide which storage partition holds the item. Every read must supply it (except expensive full-table `Scan`s — never in a request path).
- **Sort key (SK):** optional; items sharing a PK are stored **sorted by SK**, enabling range queries within the partition (`begins_with`, `between`).
- **Item = document** (up to 400KB). **Atomicity: single item** free; `TransactWriteItems` for up to 100 items at 2x cost.
- **Pricing:** per-request (RCU/WCU) — you pay per operation, not per CPU-hour. Zero servers to manage.

### Single-table design

Instead of one table per entity, put **all entity types in one table** with generic `PK`/`SK` columns, encoding type into the key. Why: DynamoDB has no joins, so "get a customer AND their orders" from two tables = two round trips. Put them in the same partition and it's **one query**.

E-commerce access patterns → key design:

| Entity | PK | SK |
|---|---|---|
| Customer | `CUST#123` | `PROFILE` |
| Order | `CUST#123` | `ORDER#2026-07-01#o-789` |
| Order line item | `ORDER#o-789` | `ITEM#1` |
| Product | `PROD#p-456` | `DETAILS` |

Now:
- "Get customer 123's profile" → `GetItem(PK=CUST#123, SK=PROFILE)`
- "Get customer 123's orders, newest first" → `Query(PK=CUST#123, SK begins_with ORDER#, ScanIndexForward=false, Limit=20)` — works because the date is *embedded in the sort key*, so items are physically stored in date order. **Sort keys are where the modeling craft lives.**
- "Profile + recent orders together" → same `Query`, one round trip, returns heterogeneous items your code fans out by SK prefix.

### Global Secondary Indexes (GSIs)

Need "get order by order-id" but PK is customer? A **GSI** is an automatically-maintained *copy* of the table with different keys (e.g., GSI PK = `orderId`). Costs: extra writes (every write fans out to each GSI), extra storage, and **GSIs are eventually consistent** (typically ms behind). Budget ~5 GSIs of headroom; each new access pattern consumes one.

### Hot partitions — the classic production failure

Throughput is per-partition (~1,000 WCU / 3,000 RCU each). If keys are unbalanced, one partition saturates while the rest idle, and you get throttling *while paying for idle capacity*.

Classic mistake: `PK = date` for an events table → **all of today's writes hit one partition**. The table scales to 100 nodes; your write load hits 1. Fixes: choose high-cardinality, evenly-accessed PKs (`deviceId`, `userId`); for celebrity keys, add a random suffix (`PROD#p-456#7`, spread across N sub-partitions, fan-in on read) — "write sharding."

**Rule: your partition key distribution is your scalability.** Uniform keys = linear scale. Skewed keys = an expensive single machine.

### Other Dynamo notes worth knowing

- `ConditionExpression` (conditional writes) = optimistic concurrency without transactions: `UpdateItem ... SET stock = stock - 1 CONDITION stock > 0` — atomic, no read-modify-write race.
- **DynamoDB Streams** = ordered change feed of every write → drives Lambda consumers for denormalization fan-out, cache invalidation, search indexing (Section 12).
- **TTL** attribute auto-expires items free of charge (sessions, tokens).
- 400KB item limit shapes design: unbounded lists (e.g., ever-growing order history) must be separate items in the partition, not an array inside one item.

---

## 8. MongoDB — modeling patterns that matter

Mongo is the "gateway" NoSQL: rich query language, real secondary indexes, aggregation pipeline — the least mental distance from SQL. The craft is the **embed vs reference** decision.

### Embed vs reference

**Embed** (child documents inside the parent) when: children are always fetched with the parent, the list is bounded, and children aren't queried independently.

```js
// Order with embedded line items — 1 read, atomic updates (single-doc ACID is free)
{ _id: "o-789", customerId: "c-123", status: "shipped",
  items: [ { productId: "p-1", name: "Keyboard", qty: 1, price: 49.99 } ],
  shippingAddress: { ... } }
```

**Reference** (store an id, fetch separately) when: the child is shared across parents (product referenced by many orders), the list is unbounded (a user's ever-growing activity), or children need independent querying.

Named patterns to know by name:
- **Bounded-embed / outlier pattern:** embed the common case (first ~50 comments), overflow to separate docs for the rare huge parent.
- **Bucket pattern:** for time-series, one doc per `(sensor, hour)` holding an array of readings — turns 3,600 tiny docs into 1, massively cutting index size.
- **Computed pattern:** store `ratingAvg`/`ratingCount` on the product doc, updated on write, instead of aggregating 10k reviews per page view.
- **Schema-versioning pattern:** put `schemaVersion: 3` in docs; readers upgrade lazily. This is how "schemaless" stays sane over years.

### Mongo pitfalls

- Unbounded array growth → 16MB doc limit, doc relocations, degraded performance. Any array that grows with user activity must be referenced or bucketed.
- `$lookup` (Mongo's join) is a crutch: fine for occasional admin queries, wrong if hot paths need it (means your embedding boundaries are wrong).
- Indexes work like SQL indexes (B-trees, compound, covered) — the ESR rule for compound indexes: **E**quality fields first, then **S**ort, then **R**ange.
- Default write concern history matters: use `w: "majority"` for anything you can't lose.

---

## 9. Cassandra — query-first, or you will suffer

Model: `PRIMARY KEY ((partition_key), clustering_columns...)`. All rows of a partition are contiguous on the replicas, sorted by clustering columns. **One table per query** is normal and correct — writes are so cheap you write the same data to 3 tables shaped for 3 queries.

```sql
-- Query: "messages in a channel, newest first, paginated"
CREATE TABLE messages_by_channel (
  channel_id uuid, bucket text,            -- bucket = month, prevents unbounded partitions
  message_id timeuuid, author_id uuid, body text,
  PRIMARY KEY ((channel_id, bucket), message_id)
) WITH CLUSTERING ORDER BY (message_id DESC);
```

Rules of thumb:
- **Partition size:** keep under ~100MB / ~100k rows. Unbounded partitions (all messages of a big channel forever) are the Cassandra hot-partition equivalent — hence the time `bucket` in the key.
- **No WHERE on non-key columns** without paying: `ALLOW FILTERING` = full cluster scan = never in production paths. Secondary indexes in Cassandra are weak (node-local); don't lean on them — make another table instead.
- **Tombstones:** deletes write markers that linger until compaction; delete-heavy workloads (queues!) rot read performance. **Never build a queue on Cassandra.**
- Writes are append-only (LSM-tree storage) → spectacular write throughput; reads may touch multiple SSTables → reads are the thing you tune.
- Leaderless replication: every node accepts writes; consistency per-query (`QUORUM` etc.); LWW conflict resolution — see Section 5.

---

## 10. Redis — the patterns

Redis is an in-memory data-structure server (sub-ms ops) — usually a *companion* to your main DB, sometimes primary store for ephemeral data.

- **Cache-aside** (dominant pattern): read → miss → fetch from DB → `SET key val EX 300`. Invalidate by TTL + explicit `DEL` on writes. Accept brief staleness.
- **Session store:** `SETEX session:{id} 1800 {json}` — beats DB (fast) and beats in-process memory (survives deploys, shared across instances).
- **Rate limiter:** `INCR rate:{user}:{minute}` + `EXPIRE` — atomic, no race.
- **Leaderboard:** sorted sets — `ZADD scores 4200 user:9`, `ZREVRANGE scores 0 9` for top-10 in O(log n).
- **Distributed lock:** `SET lock:{res} {token} NX EX 10` — fine for efficiency locks; do *not* build correctness-critical locking on single-node Redis (see Redlock debate — use a consensus store like ZooKeeper/etcd for that).
- Pitfalls: memory is the budget (eviction policies matter: `allkeys-lru` for cache, `noeviction` for data you can't lose); Redis is single-threaded per shard — one slow command (`KEYS *`, huge `SMEMBERS`) blocks everything; persistence (RDB/AOF) exists but treat durability as best-effort.
- **Cache stampede:** popular key expires → 10k requests hit the DB simultaneously. Fixes: jittered TTLs, request coalescing (one flight refreshes, rest wait), or soft-TTL background refresh.

---

## 11. Senior-level operational concerns

**Migrations & backfills.** "New access pattern" in NoSQL = new index/table + **backfilling billions of existing items** via a throttled scan job that must survive restarts, coexist with live traffic, and be verified. Plan days-to-weeks, not a `CREATE INDEX`. This is the hidden cost of access-pattern-first design — budget for it when choosing NoSQL.

**Dual-write is a trap.** Migrating stores by writing to both from the app = drift (one write succeeds, the other fails). Correct pattern: write to A, stream changes A→B (CDC), backfill B, verify, cut reads over, then writes.

**Capacity & cost model.** DynamoDB charges per request → costs scale with traffic and are *predictable per operation* but can shock at high volume (item size matters: 1 WCU per 1KB — a 10KB item costs 10x to write; GSIs multiply again). Cassandra/self-hosted charges you in engineers. Redis charges in RAM. Know which currency you're paying in.

**Monitoring that matters:** p99 latency (not averages), throttle/timeout rates, partition heat maps (hot-key detection), replication lag (staleness), Cassandra compaction backlog & tombstone warnings, Redis memory fragmentation & evictions.

**Backup/restore:** point-in-time recovery exists on managed offerings (DynamoDB PITR, Atlas) — but restoring 10TB takes hours; know your RTO. Also: NoSQL's app-enforced integrity means *logical* corruption (bad deploy writes garbage) isn't caught by the DB — PITR is your only undo.

**Security defaults:** the "open MongoDB/Elasticsearch on the internet" breach genre is real — these systems historically shipped with no auth. Always: auth on, TLS on, private network only.

---

## 12. Polyglot persistence + CDC — how real systems combine stores

No single database wins at everything, so mature systems use **several, each for what it's best at**, kept in sync by **Change Data Capture (CDC)**: one store is the *system of record*; its ordered change stream (DynamoDB Streams, Mongo change streams, Debezium reading Postgres/MySQL WAL) feeds the others asynchronously.

Typical e-commerce shape:

```
Postgres (orders, payments — transactions & integrity, system of record)
   │ CDC (Debezium → Kafka)
   ├─→ Elasticsearch   (product search)
   ├─→ Redis           (cache, sessions, rate limits)
   ├─→ DynamoDB        (user-facing read views: "my orders", flat-latency)
   └─→ Warehouse       (Snowflake/BigQuery — analytics, so OLAP never touches OLTP)
```

Everything downstream is eventually consistent (ms–s lag) — acceptable for search/cache/analytics, and the async boundary is what lets each store scale independently. This pattern also dissolves most "SQL vs NoSQL" arguments: **it's rarely either/or; it's system-of-record + specialized read views.**

---

## 13. Cheat sheet

**Decision, in one paragraph:** Default to Postgres. Move a workload to NoSQL when you can name (a) its access patterns exhaustively, (b) real scale/latency numbers Postgres can't meet economically, and (c) how you'll live without joins and multi-row transactions for that workload. Pick the family by data shape: by-key lookups → key-value; entity aggregates → document; write-heavy time-ordered → wide-column; relationship traversal → graph; text search → search engine. Keep the system of record wherever integrity matters most, fan out read views via CDC.

**Red flags in a design review (memorize):**

- "We chose Mongo for flexibility" with no access-pattern list → schema chaos in 18 months.
- Low-cardinality or time-based partition keys (`date`, `status`) → hot partitions.
- `Scan`/`ALLOW FILTERING`/`KEYS *` anywhere near a request path.
- Unbounded growth inside one document or one partition (arrays, celebrity keys, giant channels).
- Cross-entity invariants ("reserve inventory + create order atomically") on a store with per-item atomicity.
- Queue built on Cassandra (tombstones) or correctness-critical locks on Redis.
- Analytics/BI pointed at the NoSQL store instead of a warehouse.
- Dual-writes instead of CDC for keeping two stores in sync.
- Synchronous fan-out of denormalized copies in the request path (should be async via streams).

**Numbers worth carrying around (rough, 2026):** single Postgres box: ~tens of thousands of simple TPS, TB-scale comfortable. DynamoDB: ~1k WCU / 3k RCU per partition, 400KB item cap, single-digit-ms at any scale. Cassandra partition: keep <100MB. Mongo doc cap: 16MB. Redis: sub-ms, RAM-bound, single-threaded per shard.

