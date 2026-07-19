# Streams & Collections — Practice Notes

Practical, interview-focused notes. Each lesson has: **the mental model → the internals → the functions → interview traps → practice**. Runnable code lives alongside these notes.

---

## LESSON 1 — Foundations + Stream Internals

### Part A — The Collections map (know *when to use what*)

A **collection** = an object that holds a group of other objects ("elements"). Java gives you a family of them under `java.util`. There are **two separate roots**, and mixing them up is a classic interview slip:

- `Collection<E>` — root of **List, Set, Queue/Deque**. Holds single elements.
- `Map<K,V>` — a **separate** hierarchy. Holds key→value pairs. `Map` is *not* a `Collection`.

Why two roots? A `Map` isn't "a bunch of elements" — it's an association from keys to values. Its operations (`put`, `get`, `containsKey`) don't fit the single-element `Collection` API (`add`, `contains`), so it lives on its own.

#### The three element-collections

**List** — ordered (has an index), duplicates allowed.

| Impl | Backed by | Get by index | Insert/remove at end | Insert/remove middle | Use when |
|---|---|---|---|---|---|
| `ArrayList` | resizable array | O(1) | O(1) amortized | O(n) (shifts) | default choice; lots of random access |
| `LinkedList` | doubly-linked nodes | O(n) | O(1) | O(1) *if you hold the node* | you constantly add/remove at the ends (rare in practice) |

> Interview trap: people say "LinkedList is faster for inserts." Only if you already have the node reference. Inserting "in the middle" still costs O(n) to *find* the spot. In real code `ArrayList` wins almost always because arrays are cache-friendly.

**Set** — no duplicates. Same API, differ only by **iteration order + cost**:

| Impl | Order | Lookup/add/remove | Use when |
|---|---|---|---|
| `HashSet` | none (hash order, unpredictable) | O(1) average | you just need uniqueness, fastest |
| `LinkedHashSet` | insertion order | O(1) average | uniqueness **and** predictable order |
| `TreeSet` | sorted (natural or `Comparator`) | O(log n) | you need elements kept sorted |

**Queue / Deque** — process from ends:

| Impl | What it is | Use when |
|---|---|---|
| `ArrayDeque` | resizable array, double-ended | fast stack **or** queue (prefer over `Stack`/`LinkedList`) |
| `PriorityQueue` | binary heap | always pull the min/max next (Dijkstra, top-K) |

> `Stack` and `Vector` are legacy (synchronized, slow). Use `ArrayDeque` for a stack. Interviewers like when you know this.

#### Map implementations

| Impl | Order | get/put | Use when |
|---|---|---|---|
| `HashMap` | none | O(1) average | default key→value store |
| `LinkedHashMap` | insertion (or access) order | O(1) average | predictable order; **access-order = LRU cache** |
| `TreeMap` | sorted by key | O(log n) | need keys sorted / range queries (`headMap`, `ceilingKey`) |

> Run `L01_Foundations.java` sections 5–6 to *see* the ordering differences. Note: with a tiny `HashSet`/`HashMap`, the print order can *coincidentally* look like insertion order — that's luck of the hash buckets, not a guarantee. Never rely on `HashMap`/`HashSet` order.

---

### Part B — What a Stream actually is (the internals interviewers probe)

**A Stream is NOT a data structure.** It stores no elements. It's a **pipeline** — a description of computation to run over a *source* (a collection, array, or generator). Think "a query you run over data," not "a container of data."

#### Anatomy of every stream pipeline

```
   SOURCE            INTERMEDIATE ops (0+)          TERMINAL op (exactly 1)
   list.stream()  →  .filter(...).map(...)      →   .collect(...) / .forEach(...) / .count()
   (produces         (lazy, each returns a           (eager — triggers the whole
    a Stream)         NEW Stream, do nothing yet)     pipeline, produces a result)
```

Three facts that follow, each proven by a section in `L01_Foundations.java`:

1. **Laziness (§1).** Intermediate ops (`filter`, `map`, `peek`, `sorted`, `distinct`, `limit`…) build up the recipe and run *nothing*. The work starts only when a **terminal** op is attached. Why it exists: it lets Java **fuse** all the steps into one pass and skip work it doesn't need.

2. **Terminal drives execution (§2).** The terminal op "pulls" elements through the chain. No terminal = no work ever happens.

3. **Vertical, element-by-element processing (§3).** Java does **not** run `filter` over the whole list, then `map` over the whole list. Each element flows through the *entire* chain before the next element starts. This is why **short-circuiting** works: `limit(3)`, `findFirst()`, `anyMatch()` can stop the source early — §3 runs on an *infinite* stream and still terminates.

4. **Single-use (§4).** A stream can be traversed **once**. Touch it a second time → `IllegalStateException: stream has already been operated upon or closed`. Need it twice? Re-create it from the source.

#### The engine underneath: Spliterator

Every stream is driven by a **`Spliterator`** ("splittable iterator"). It does two jobs:
- **Iterate** — hand elements to the pipeline one at a time (`tryAdvance`).
- **Split** — for parallel streams, cut the data in half (`trySplit`) so threads share the work.

It also carries **characteristics** — flags like `SIZED`, `ORDERED`, `SORTED`, `DISTINCT`. The stream uses them to optimize. Example: `stream.count()` on a `SIZED` source with no filter can return the size directly without walking elements. `distinct()` on an already-`DISTINCT` source is a no-op. This is *why* the same code can be faster on an `ArrayList` (SIZED, ORDERED) than on a `HashSet`.

#### How to create a stream (sources)
- From a collection: `list.stream()`, `list.parallelStream()`
- From values: `Stream.of(1,2,3)`
- From an array: `Arrays.stream(arr)`
- Primitive streams (avoid boxing): `IntStream.range(0,10)`, `IntStream.rangeClosed(1,10)`
- Infinite (needs `limit`): `Stream.iterate(1, n->n+1)`, `Stream.generate(Math::random)`

> **Primitive streams** (`IntStream`, `LongStream`, `DoubleStream`) exist to avoid boxing every `int` into an `Integer` object. They add numeric terminals: `sum()`, `average()`, `min()`, `max()`, `summaryStatistics()`. Convert with `mapToInt(...)` / `boxed()`.

---

### Interview quick-answers (Lesson 1)

- **"Is a stream stored in memory?"** No. It holds no data; it processes a source on demand.
- **"Difference between intermediate and terminal ops?"** Intermediate = lazy, returns a Stream, builds the recipe. Terminal = eager, returns a result/side-effect, triggers execution. A pipeline needs exactly one terminal.
- **"Can you reuse a stream?"** No — single-use. Re-derive from the source.
- **"Collection vs Stream?"** Collection = *stores* data, you manage it (add/remove). Stream = *computes over* data, you describe transformations. Collection is about storage; Stream is about computation.
- **"Why is `Map` not a `Collection`?"** It models key→value associations, not a group of single elements, so it has a different API and its own hierarchy.
- **"ArrayList vs LinkedList?"** ArrayList (array, O(1) index, cache-friendly) is the default. LinkedList only wins for O(1) end insertion when you hold the node — rare.

---

### Practice (Lesson 1)
Open `P01_Practice.java`, fill in the 6 methods (EASY, streams only), run it, turn all 6 green.

Run command (in the sandbox): `bash /tmp/prun.sh P01_Practice`
On your own machine (from repo root): `javac -d out src/main/java/com/company/streams/*.java && java -cp out com.company.streams.P01_Practice`

---

## LESSON 4 — Collectors deep dive: grouping & toMap over objects

You already met `collect(Collectors.toList())`. `Collectors` is a **toolbox of recipes** for the
terminal `collect(...)` op. The three you must own for interviews are `toMap`, `groupingBy`, and
`partitioningBy` — plus the **downstream** collectors that plug into them.

### 4.1 `toMap` — turn a stream into a key->value map

```java
Map<String,Integer> nameToAge =
    people.stream().collect(Collectors.toMap(Person::getName, Person::getAge));
```

Anatomy: `toMap(keyFn, valueFn)`. For each element it computes a key and a value and puts them in a map.

**The #1 trap:** if two elements produce the **same key**, `toMap` throws
`IllegalStateException: Duplicate key`. It refuses to guess which value wins. You must supply a
**merge function** — a `BinaryOperator` that combines the two colliding values:

```java
// key = city, value = a name; on collision keep the FIRST one seen
toMap(Person::getCity, Person::getName, (first, second) -> first)
// or keep the higher number:
toMap(Person::getCity, Person::getAge, (a, b) -> Math.max(a, b))
```

A **4th argument** picks the map type (a supplier), e.g. sorted keys:
```java
toMap(Person::getName, Person::getAge, (a,b)->a, TreeMap::new)
```

Use `toMap` when each key maps to **one** value. If a key maps to **many** values, you want grouping.

### 4.2 `groupingBy` — bucket elements by a key

```java
Map<String,List<Person>> byCity =
    people.stream().collect(Collectors.groupingBy(Person::getCity));
```

Anatomy: `groupingBy(classifier)` runs the classifier on each element to get its **bucket key**, and
collects all elements with that key into a `List` (the default). Unlike `toMap`, collisions are the
whole point — same key just means "same bucket."

The powerful form takes a **downstream collector** — "for each bucket, collect its elements *this* way":

```java
groupingBy(classifier, downstreamCollector)
```

So `groupingBy` decides the *buckets*; the downstream decides *what each bucket becomes*.

### 4.3 The downstream collector catalog (memorize these)

| Downstream | Bucket becomes | Example |
|---|---|---|
| `counting()` | `Long` count | employees per dept |
| `summingInt(fn)` | `Integer` sum | total salary per dept |
| `averagingDouble(fn)` | `Double` average | avg salary per dept |
| `mapping(fn, toList())` | list of a transformed field | names per dept |
| `mapping(fn, toSet())` | set (dedup) | distinct titles per dept |
| `maxBy(cmp)` / `minBy(cmp)` | `Optional<T>` | top earner per dept |
| `joining(", ")` | `String` | "Ann, Bob" per dept |
| `summarizingInt(fn)` | count+sum+min+max+avg in one pass | salary stats per dept |
| `filtering(pred, down)` (Java 9+) | keep only matching, then collect | high earners per dept |
| `flatMapping(fn->stream, down)` (Java 9+) | flatten a collection field | all skills per dept |
| `reducing(id, fn, op)` | custom fold | same as summing, generalized |
| `collectingAndThen(down, finisher)` | collect, then transform the result | maxBy then `.get().getName()` |

Two rules that trip people up:
- `maxBy`/`minBy` return an **`Optional`** (a bucket could be empty in theory), so you usually wrap
  them in `collectingAndThen(maxBy(cmp), opt -> opt.get().getName())` to unwrap.
- Downstreams **nest**: `groupingBy(a, groupingBy(b, counting()))` gives a two-level
  `Map<A, Map<B, Long>>`. Anywhere a "downstream" is expected, another collector can go — including
  another `groupingBy`.

### 4.4 Multi-level grouping

```java
// dept -> (title -> list of names)
groupingBy(Emp::getDept,
    groupingBy(Emp::getTitle,
        mapping(Emp::getName, toList())))
```

Read it outside-in: bucket by dept; inside each dept bucket by title; inside each title bucket collect names.

### 4.5 `partitioningBy` — the special 2-bucket grouping

```java
Map<Boolean,List<Person>> parts =
    people.stream().collect(Collectors.partitioningBy(p -> p.getAge() >= 30));
```

It's `groupingBy` with a **boolean** classifier, but with one guarantee `groupingBy` doesn't give:
the result **always has both keys** `true` and `false` (even if one bucket is empty). It also takes a
downstream: `partitioningBy(pred, counting())` -> `Map<Boolean,Long>`.

### 4.6 Choosing the tool (interview reflex)
- One value per key -> **`toMap`** (remember the merge function for collisions).
- Many values per key -> **`groupingBy`** (+ a downstream to shape each bucket).
- Exactly two buckets by a yes/no test -> **`partitioningBy`**.

### Practice (Lesson 4)
`P11_Grouping.java` — ~30 questions, easy -> complex, all over `Emp` objects. Auto-graded against a
correct reference (`SolutionsP11.java`). Run `SolutionsP11` to see worked answers *after* you try.
