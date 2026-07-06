# 07 — LLD: Kafka-style Pub-Sub (full walkthrough)

In-memory message queue with topics, partitions, consumer groups, and offsets.
Teaches: the offset model (per group-partition, NOT per partition), partition
assignment rules, and where real-Kafka differs from the toy.

---

## Step 1 — Requirements (with vocabulary)
```
1. A PRODUCER publishes a MESSAGE (record: key + value) to a TOPIC
2. A topic is split into PARTITIONS (parallelism + per-partition ordering)
3. Partition chosen by a PARTITIONER:
     key present -> hash(key) % numPartitions  (same key -> same partition
                    -> all of user_42's events stay ORDERED together)
     no key      -> round-robin (load spreading)
4. A CONSUMER tracks an OFFSET per partition — its bookmark; resumes at
   committed offset on restart
5. CONSUMER GROUP: each partition read by EXACTLY ONE consumer in the group
6. Ordering guaranteed WITHIN a partition, NOT across partitions (the
   fundamental trade-off: partitions buy parallelism, cost global ordering)
7. Thread-safe (producers + consumers concurrent)
```

## Step 2 — Entities (with my naming slips fixed)
```
Message        key, value, timestamp
Topic          name + its partitions          <- I said "topics list" = the FIELD;
Partition      an ordered append-only LOG        the CLASS is the singular noun
Producer       send(topic, key, value)
Consumer       polls its ASSIGNED partitions
ConsumerGroup  holds consumers; ASSIGNS partitions among them
Broker         central: holds topics, routes publishes, OWNS THE OFFSET STORE
Partitioner (interface, Strategy)
  KeyHashPartitioner / RoundRobinPartitioner
```
(Also: "apache" is not a class 😄 — the component is the **Broker**.)

## Step 3 — Relationships
```
Broker ◆── many Topic          Topic ◆── many Partition      Partition ◆── many Message
Producer ◇── Partitioner (has-a, swappable)
ConsumerGroup ◆── many Consumer          Consumer ── 1..N Partitions (has-a)
KeyHashPartitioner ──▷ Partitioner       is-a (implements — ALWAYS is-a)
```

---

## THE TWO COORDINATES: partition vs offset (the confusion I had)

You locate one message with TWO independent numbers — a 2D coordinate:
```
Topic "orders":
  Partition 0: [m0, m1, m2, m3]      <- offsets 0..3 WITHIN partition 0
  Partition 1: [m0, m1, m2]          <- its OWN offsets 0..2
  Partition 2: [m0 ... m4]
```
- **partitionId** picks WHICH LOG (which row).
- **offset** picks WHERE IN THAT LOG (which column).
- There is **no global offset**. "Offset 3" is meaningless without saying which
  partition — partition 0's offset 3 and partition 2's offset 3 are different
  messages. Library analogy: partition = which shelf; offset = which book on
  that shelf. You always need both.
So `topic.getPartition(partitionId)` takes the partition NUMBER — never an
offset. Offsets only matter after you have a partition: `partition.read(offset)`.

Who picks what:
| | partition | offset |
|---|---|---|
| producing | Partitioner (hash(key)%N) | automatic — append = next index |
| consuming | assigned by the ConsumerGroup | the group's stored bookmark |

In our model, **list index == offset** (`log.get(offset)`), because an
ArrayList gives O(1) random access. Real Kafka's log is BYTES ON DISK — you
can't seek to "message #5" in a byte stream — so Kafka keeps **index files**
mapping offset -> byte position (SPARSE: one entry per ~4KB; binary-search the
index, then a short scan). In-RAM we don't need that; on disk you do.

---

## THE OFFSET MODEL (the heart of this LLD)

**A partition CANNOT store a single offset.** Two groups read the same
partition INDEPENDENTLY:
```
Partition orders-0: [m0..m4]
  analytics-group -> read to offset 4
  billing-group   -> read to offset 1
```
One number would make the groups clobber each other. Therefore:
> **The offset lives per (consumer-group, partition) pair — in the BROKER's
> offset store, not in the partition.**
```java
Map<String, Map<Integer, Integer>> offsets;   // groupId -> (partitionId -> NEXT offset)
// { "analytics": {0->5, 1->3},  "billing": {0->1} }   same data, separate bookmarks
```
Messages live ONCE in the partition (shared); the BOOKMARKS into them are
per-group.

### Broker.poll — read at the bookmark, advance the bookmark
```java
Message poll(String topicName, int partitionId, String groupId) {
    Partition partition = topics.get(topicName).getPartition(partitionId);

    Map<Integer,Integer> groupOffsets =
            offsets.computeIfAbsent(groupId, g -> new ConcurrentHashMap<>()); // get-or-create+STORE
    int cur = groupOffsets.getOrDefault(partitionId, 0);   // never read -> start at 0

    Message m = partition.read(cur);                        // read AT the bookmark
    if (m != null) groupOffsets.put(partitionId, cur + 1);  // advance ONLY if we got one
    return m;                                               // null = caught up; bookmark stays
}
```
Two crucial details:
1. **Advance only on a real read.** If nothing's new, the bookmark must NOT
   move — else you'd skip the next message that arrives.
2. **poll takes partitionId as a PARAMETER.** It does not "figure out" the
   partition from the group — assignment happened UPSTREAM (ConsumerGroup).
   Separation: assignment = who owns what (once, on join/rebalance); poll =
   read-at-offset-and-advance (repeatedly). Mixing them is what tangled me.

Also note current vs committed offset (real Kafka): what you've read vs what
you've durably acknowledged; restart resumes from COMMITTED (stored in the
internal __consumer_offsets topic).

---

## CONSUMER GROUPS & PARTITION ASSIGNMENT

### The rule (both directions)
> Within a group: **one partition -> exactly ONE consumer**, but **one consumer
> -> MANY partitions.** (Dealing cards: every card to exactly one hand; a hand
> may hold several.)

6 partitions:
- 3 consumers -> 2 partitions each. 1 consumer -> all 6. 6 consumers -> 1 each.
- **8 consumers -> 2 sit IDLE.** A partition is never split within a group
  (would break ordering). Therefore:
> **Partition count is the CEILING on consumer parallelism.** That's why topics
> are over-partitioned (30 partitions for 3 consumers today) — scale consumers
> later without re-partitioning.

Contrast: two DIFFERENT groups both fully read the same partition (independent
offsets) — they don't compete. Within ONE group, they split.

Our ConsumerGroup.assign(): round-robin partitions across consumers
(`consumers.get(p % consumers.size())`), reset on each call = a toy rebalance.

### Who decides what (the ZooKeeper question)
TWO different decisions — don't conflate:
1. **Which partition does a MESSAGE go to?** -> the **Partitioner**, on the
   PRODUCER, per message. Pure local hash+modulo. ZooKeeper never involved.
2. **Which CONSUMER owns which partitions?** -> group coordination/REBALANCING.
   History: old Kafka used ZooKeeper for group state/offsets; modern Kafka uses
   a broker-side **Group Coordinator** + __consumer_offsets; **KRaft** removes
   ZooKeeper entirely (Kafka self-manages metadata via Raft).

### "Broker holds all topics" — true in OUR model, FALSE in real Kafka
Real Kafka: a broker is ONE SERVER holding a SUBSET of partitions; partitions
are distributed + replicated across brokers (leader + followers) — that's the
horizontal scaling + fault tolerance. State the simplification up front:
> "Single in-memory broker here; in a cluster, partitions are distributed and
> replicated across brokers — that's the HLD layer I'm scoping out."

---

## What the demo proved (all four concepts)
1. Same key -> same partition, in order (all user_1 orders landed in p1: A,C,F,G)
2. Group split: C1 got [0,2], C2 got [1] — one owner per partition
3. Independent groups: billing re-read everything with its own offsets
4. Offsets persist: analytics' second poll saw ONLY the new messages (G,H)

## Interview narrative
> "Topics split into partitions; a Strategy partitioner routes by key hash so
> same-key messages stay ordered in one partition. Consumer groups assign each
> partition to exactly one consumer — partition count caps parallelism. The
> broker keeps offsets per (group, partition) so independent groups read the
> same data without interfering; poll reads at the bookmark and advances only
> on a successful read. Distributed: partitions replicated across brokers,
> offsets in an internal topic, coordinator-driven rebalancing (KRaft, no
> ZooKeeper)."
