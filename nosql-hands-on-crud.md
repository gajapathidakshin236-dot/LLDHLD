# NoSQL Hands-On: CRUD the Way You Learned SQL

> Every command shown next to the SQL you already know. All of it is runnable on your machine.

---

## 0. Setup — one Docker command per database

New concept if needed: **Docker** runs an app in an isolated container so you don't install anything permanently. Install Docker Desktop, then:

```bash
docker run -d --name mongo -p 27017:27017 mongo:7        # MongoDB
docker run -d --name redis -p 6379:6379 redis:7          # Redis
docker run -d --name cassandra -p 9042:9042 cassandra:5  # Cassandra (takes ~1 min to boot)
docker run -d --name dynamo -p 8000:8000 amazon/dynamodb-local   # DynamoDB Local
```

Open a shell into each:

```bash
docker exec -it mongo mongosh          # MongoDB shell
docker exec -it redis redis-cli        # Redis shell
docker exec -it cassandra cqlsh        # Cassandra shell (CQL)
# DynamoDB: use aws cli with --endpoint-url http://localhost:8000 (needs aws cli; any fake credentials work)
aws configure set aws_access_key_id fake && aws configure set aws_secret_access_key fake && aws configure set region us-east-1
```

---

## 1. MongoDB — the closest to SQL

### Vocabulary mapping

| SQL | MongoDB |
|---|---|
| database | database |
| table | **collection** |
| row | **document** (JSON) |
| column | **field** |
| primary key | `_id` (auto-generated ObjectId if you don't set one) |
| index | index (same B-trees!) |
| JOIN | embed the data, or `$lookup` (rare) |
| WHERE / GROUP BY / HAVING | `find()` filters / aggregation pipeline |

### CREATE TABLE → nothing (and that's important)

```sql
-- SQL
CREATE DATABASE shop;
CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT, email TEXT UNIQUE, age INT, city TEXT);
```

```js
// MongoDB — collections are created on first insert. No schema declaration.
use shop                      // switches DB (creates lazily)
// That's it. The "schema" will live in your application code.
// Optional: you CAN enforce structure with a JSON-schema validator — teams at scale often do:
db.createCollection("users", { validator: { $jsonSchema: {
  required: ["name", "email"], properties: { age: { bsonType: "int", minimum: 0 } } } } })
```

### INSERT

```sql
INSERT INTO users (name, email, age, city) VALUES ('Dakshin', 'd@x.com', 24, 'Chennai');
```

```js
db.users.insertOne({ name: "Dakshin", email: "d@x.com", age: 24, city: "Chennai" })

db.users.insertMany([
  { name: "Asha",  email: "a@x.com", age: 31, city: "Bengaluru", premium: true },
  { name: "Ravi",  email: "r@x.com", age: 28, city: "Chennai",
    addresses: [ { label: "home", pin: "600001" } ] }   // nested data — no second table needed
])
// Note: Asha has a field Ravi doesn't. Legal. This is the flexibility — and the danger.
```

### SELECT

```sql
SELECT * FROM users;
SELECT name, email FROM users WHERE city = 'Chennai';
SELECT * FROM users WHERE age > 25 AND city = 'Chennai';
SELECT * FROM users WHERE city IN ('Chennai','Bengaluru') ORDER BY age DESC LIMIT 10 OFFSET 20;
SELECT * FROM users WHERE name LIKE 'Da%';
SELECT COUNT(*) FROM users WHERE city = 'Chennai';
```

```js
db.users.find()
db.users.find({ city: "Chennai" }, { name: 1, email: 1, _id: 0 })   // 2nd arg = projection (column list)
db.users.find({ age: { $gt: 25 }, city: "Chennai" })                // AND = same object
db.users.find({ city: { $in: ["Chennai", "Bengaluru"] } }).sort({ age: -1 }).skip(20).limit(10)
db.users.find({ name: { $regex: "^Da" } })
db.users.countDocuments({ city: "Chennai" })

// Operators you'll use daily: $gt $gte $lt $lte $ne $in $nin $and $or $exists $regex
db.users.find({ $or: [ { age: { $lt: 25 } }, { premium: true } ] })
db.users.find({ "addresses.pin": "600001" })   // query INSIDE nested docs with dot-notation
```

### UPDATE

```sql
UPDATE users SET age = 25 WHERE email = 'd@x.com';
UPDATE users SET login_count = login_count + 1 WHERE email = 'd@x.com';
```

```js
db.users.updateOne({ email: "d@x.com" }, { $set: { age: 25 } })
db.users.updateOne({ email: "d@x.com" }, { $inc: { loginCount: 1 } })   // atomic increment
db.users.updateOne({ email: "r@x.com" },
  { $push: { addresses: { label: "office", pin: "600042" } } })          // append to array — no SQL equivalent!
db.users.updateMany({ city: "Chennai" }, { $set: { zone: "south" } })
db.users.updateOne({ email: "new@x.com" }, { $set: { name: "New" } }, { upsert: true })  // INSERT ... ON CONFLICT UPDATE

// ⚠️ Classic beginner bug: updateOne({..}, { age: 25 }) WITHOUT $set REPLACES the whole document.
```

### DELETE

```sql
DELETE FROM users WHERE email = 'd@x.com';
```

```js
db.users.deleteOne({ email: "d@x.com" })
db.users.deleteMany({ city: "Chennai" })
db.users.drop()                                 // DROP TABLE
```

### CREATE INDEX (works just like SQL)

```sql
CREATE INDEX idx_city ON users(city);
CREATE UNIQUE INDEX idx_email ON users(email);
CREATE INDEX idx_city_age ON users(city, age DESC);
EXPLAIN SELECT * FROM users WHERE city='Chennai';
```

```js
db.users.createIndex({ city: 1 })
db.users.createIndex({ email: 1 }, { unique: true })    // yes, Mongo CAN enforce uniqueness
db.users.createIndex({ city: 1, age: -1 })              // compound; ESR rule: Equality, Sort, Range
db.users.find({ city: "Chennai" }).explain("executionStats")
// In the output: "IXSCAN" = index used (good), "COLLSCAN" = full table scan (bad, same as Seq Scan)
```

### GROUP BY → aggregation pipeline

A pipeline = stages the documents flow through, each stage like a SQL clause:

```sql
SELECT city, COUNT(*) AS n, AVG(age) AS avg_age
FROM users WHERE age > 20
GROUP BY city HAVING COUNT(*) > 5
ORDER BY n DESC;
```

```js
db.users.aggregate([
  { $match: { age: { $gt: 20 } } },                                  // WHERE
  { $group: { _id: "$city", n: { $sum: 1 }, avgAge: { $avg: "$age" } } },  // GROUP BY
  { $match: { n: { $gt: 5 } } },                                     // HAVING (a second $match!)
  { $sort: { n: -1 } }                                               // ORDER BY
])
```

### JOIN → `$lookup` (know it exists, avoid needing it)

```sql
SELECT u.name, o.total FROM users u JOIN orders o ON o.user_id = u._id;
```

```js
db.users.aggregate([
  { $lookup: { from: "orders", localField: "_id", foreignField: "userId", as: "orders" } }
])
// Works, but if a hot path needs this, the design is wrong — the orders (or a summary)
// should have been embedded or denormalized. $lookup is for admin/reporting queries.
```

---

## 2. Redis — commands, not queries

No tables, no WHERE. You address values by key and pick the **data structure** per use case.

```bash
# STRINGS — the workhorse                            SQL-ish equivalent
SET user:1:name "Dakshin"                            # UPDATE ... SET
GET user:1:name                                      # SELECT ... WHERE pk = 1
SET session:abc '{"uid":1}' EX 1800                  # ...with 30-min self-destruct (TTL!)
TTL session:abc                                      # seconds left; -2 = gone
INCR page:views                                      # UPDATE views = views + 1 — atomic
SET lock:job42 "worker7" NX EX 10                    # write ONLY IF NOT EXISTS (NX) = atomic claim

# HASHES — an object with fields (like one row)
HSET user:1 name "Dakshin" age 24 city "Chennai"     # INSERT one row
HGET user:1 name                                     # SELECT name
HGETALL user:1                                       # SELECT *
HINCRBY user:1 age 1                                 # atomic per-field increment

# LISTS — ordered, push/pop at both ends
LPUSH queue:emails '{"to":"a@x.com"}'                # producer enqueues
RPOP queue:emails                                    # consumer dequeues (FIFO with LPUSH+RPOP)
LRANGE recent:searches:u1 0 9                        # last 10 items

# SETS — unique members
SADD order:901:tags "veg" "spicy"
SISMEMBER order:901:tags "veg"                       # membership test O(1)
SINTER skills:python skills:sql                      # INTERSECT — who has both

# SORTED SETS — members ranked by score (the leaderboard structure)
ZADD leaderboard 4200 "user:9" 3100 "user:2"
ZINCRBY leaderboard 50 "user:2"                      # add points atomically
ZREVRANGE leaderboard 0 9 WITHSCORES                 # top 10 — ORDER BY score DESC LIMIT 10
ZREVRANK leaderboard "user:9"                        # my rank

# Housekeeping
DEL user:1                                            # DELETE
EXPIRE user:1 60                                      # add TTL to existing key
SCAN 0 MATCH "user:*" COUNT 100                       # iterate keys safely — NEVER use KEYS * in prod
```

The mental shift: in SQL you ask questions; in Redis you **precompute the answer into the right structure at write time** (the leaderboard IS the sorted set), then reads are O(1)/O(log n) lookups.

---

## 3. DynamoDB — CRUD via AWS CLI (against local Docker)

Add `--endpoint-url http://localhost:8000` to every command (omit it against real AWS).

### CREATE TABLE (the only schema Dynamo has: the keys)

```bash
aws dynamodb create-table --table-name app \
  --attribute-definitions AttributeName=PK,AttributeType=S AttributeName=SK,AttributeType=S \
  --key-schema AttributeName=PK,KeyType=HASH AttributeName=SK,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --endpoint-url http://localhost:8000
# Note what's DECLARED: only PK and SK. All other attributes are free-form per item.
```

### INSERT → put-item

```bash
aws dynamodb put-item --table-name app --item '{
  "PK": {"S": "CUST#c1"}, "SK": {"S": "PROFILE"},
  "name": {"S": "Dakshin"}, "city": {"S": "Chennai"}, "age": {"N": "24"}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name app --item '{
  "PK": {"S": "CUST#c1"}, "SK": {"S": "ORDER#2026-07-06T10:00:00Z#o1"},
  "total": {"N": "499"}, "status": {"S": "PLACED"}
}' --endpoint-url http://localhost:8000
# The {"S": ...}/{"N": ...} wrappers are Dynamo's type system. SDKs (boto3 etc.) hide this noise.
```

### SELECT by full key → get-item

```bash
# SQL: SELECT * FROM app WHERE pk='CUST#c1' AND sk='PROFILE'
aws dynamodb get-item --table-name app \
  --key '{"PK": {"S": "CUST#c1"}, "SK": {"S": "PROFILE"}}' \
  --endpoint-url http://localhost:8000
```

### SELECT a range → query (the workhorse)

```bash
# SQL: SELECT * FROM app WHERE pk='CUST#c1' AND sk LIKE 'ORDER#%' ORDER BY sk DESC LIMIT 20
aws dynamodb query --table-name app \
  --key-condition-expression "PK = :pk AND begins_with(SK, :pfx)" \
  --expression-attribute-values '{":pk": {"S": "CUST#c1"}, ":pfx": {"S": "ORDER#"}}' \
  --no-scan-index-forward --limit 20 \
  --endpoint-url http://localhost:8000
# key-condition-expression may ONLY touch PK and SK. There is no WHERE on other fields
# (there's --filter-expression, but it filters AFTER reading — you still pay for every row read).
```

### UPDATE → update-item (with the two killer features)

```bash
# SQL: UPDATE app SET status='ACCEPTED' WHERE ... AND status='PLACED'  ← condition = state machine
aws dynamodb update-item --table-name app \
  --key '{"PK": {"S": "CUST#c1"}, "SK": {"S": "ORDER#2026-07-06T10:00:00Z#o1"}}' \
  --update-expression "SET #s = :new" \
  --condition-expression "#s = :old" \
  --expression-attribute-names '{"#s": "status"}' \
  --expression-attribute-values '{":new": {"S": "ACCEPTED"}, ":old": {"S": "PLACED"}}' \
  --endpoint-url http://localhost:8000
# Fails with ConditionalCheckFailedException if status wasn't PLACED. Race-proof.

# Atomic counter:  --update-expression "ADD loginCount :one"
```

### DELETE → delete-item / full scan → scan (never in request paths)

```bash
aws dynamodb delete-item --table-name app \
  --key '{"PK": {"S": "CUST#c1"}, "SK": {"S": "PROFILE"}}' --endpoint-url http://localhost:8000

aws dynamodb scan --table-name app --endpoint-url http://localhost:8000   # SELECT * with no WHERE — fine locally, sin in prod
```

---

## 4. Cassandra (CQL) — looks like SQL, behaves like a hash map

```sql
-- Keyspace = database, with replication declared up front
CREATE KEYSPACE shop WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE shop;

-- CREATE TABLE — the PRIMARY KEY layout IS the design
CREATE TABLE orders_by_customer (
  customer_id text,
  order_time  timestamp,
  order_id    text,
  total       decimal,
  status      text,
  PRIMARY KEY ((customer_id), order_time)      -- (partition key), clustering column
) WITH CLUSTERING ORDER BY (order_time DESC);

INSERT INTO orders_by_customer (customer_id, order_time, order_id, total, status)
VALUES ('c1', '2026-07-06 10:00:00', 'o1', 499, 'PLACED');
-- ⚠️ INSERT and UPDATE are the same operation underneath (an "upsert"). Inserting an
-- existing key silently overwrites. No duplicate-key error like SQL.

-- ✅ Queries that work: partition key equality + clustering ranges
SELECT * FROM orders_by_customer WHERE customer_id = 'c1';
SELECT * FROM orders_by_customer WHERE customer_id = 'c1' AND order_time > '2026-07-01';

-- ❌ The query that teaches you Cassandra:
SELECT * FROM orders_by_customer WHERE status = 'PLACED';
-- Error: "Cannot execute this query as it might involve data filtering..."
-- Cassandra REFUSES queries it can't do fast. It's not being difficult — it's telling
-- you 'status' isn't in the key, so answering would mean scanning every node.
-- The fix is NOT "ALLOW FILTERING" (that forces the cluster scan). The fix is a second
-- table shaped for that query — one table per query is normal:
CREATE TABLE orders_by_status (
  status text, order_time timestamp, order_id text, customer_id text,
  PRIMARY KEY ((status), order_time, order_id)
);
-- and your app writes each order to BOTH tables. Writes are cheap; that's the deal.

UPDATE orders_by_customer SET status='DELIVERED' WHERE customer_id='c1' AND order_time='2026-07-06 10:00:00';
DELETE FROM orders_by_customer WHERE customer_id='c1' AND order_time='2026-07-06 10:00:00';  -- writes a tombstone
```

---

## 5. Drills (do these in the shells)

**Mongo:** build a `products` collection: 10 products across 3 categories with nested `specs` and a `tags` array. Then: (1) all products in "electronics" under ₹5,000 sorted by price; (2) products tagged both "sale" and "new" ($all); (3) average price per category with the pipeline; (4) add index to make (1) an IXSCAN and prove it with explain(); (5) atomically decrement stock but only if stock > 0 (hint: filter on stock in the query part of updateOne — the filter IS the condition).

**Redis:** build a working rate limiter: allow 5 requests/minute per user using INCR + EXPIRE (key `rate:{user}:{floor(now/60)}`). Then a "recently viewed products" list capped at 10 per user (LPUSH + LTRIM).

**DynamoDB:** model the review exercise from the food-delivery file (`REST#r77 / REVIEW#...`) in dynamodb-local: put 5 reviews, query them newest-first, then write the one-review-per-customer guard with a condition-expression and prove the second attempt fails.

**Cassandra:** create `messages_by_channel` from the notes (with the month `bucket`), insert 10 messages across 2 channels, fetch the latest 5 of one channel, then try to query by author and read the error — then design (don't build) the second table that would serve it.

---

## 6. The pattern you should have noticed

| Concept | Mongo | Redis | DynamoDB | Cassandra |
|---|---|---|---|---|
| Fast lookup by | `_id` / indexed field | key | PK (+SK range) | partition key (+clustering range) |
| Arbitrary WHERE | ✅ (indexes) | ❌ | ❌ (filter ≠ index) | ❌ (refuses) |
| Atomic ops | field-level ($inc, $set, conditional filter) | command-level (INCR, SET NX) | update-expr + condition-expr | LWT (expensive) |
| "Transaction" scope | 1 document free; multi-doc exists | MULTI/Lua per shard | 1 item free; TransactWrite ≤100 | 1 partition batch |
| Schema lives in | app (+ optional validator) | app | app (keys only declared) | table DDL (but query-shaped) |

Mongo lets you keep the most SQL habits; Cassandra punishes them fastest; Dynamo makes the discipline explicit; Redis was never pretending to be a database in the SQL sense.
