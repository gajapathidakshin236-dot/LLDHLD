# DSA Practice — Approach Notes (5 problems)

Read the approach, then implement the stub in `DSAPractice.java`. Grade with the harness
(`DSAPractice`); worked answers live in `DSASolutions.java`.

---

## P1 — Linked list backed by arrays

**Why it exists.** A normal linked list allocates a separate object per node (pointer chasing,
GC pressure, poor cache locality). An **array-backed** list stores nodes in flat arrays instead,
which is cache-friendly and lets you pool/reuse memory. It's the classic way to build a linked
list in languages without pointers, and how pooled allocators work.

**Model.** Two parallel arrays:
- `val[i]`  = the data in node `i`
- `next[i]` = the **index** of the next node (`-1` means "end")

Plus `int head` (index of first node, `-1` if empty), a `size`, a capacity, and a **free-list**
(`freeHead`) — a chain of slots freed by `removeAt`, so new nodes reuse them in O(1) instead of
leaking space. `newNode` pops a slot from the free-list if available, else bumps a high-water mark
(`used`) and grows (doubles) the arrays when full.

**Complexity.** `addFirst` O(1); `addLast` O(n) (walk to the tail — add a `tail` index to make it
O(1)); `get`/`removeAt` O(n) (must walk `index` links). Space O(capacity).

**Gotchas.** Recycle removed slots via the free-list; `removeAt(0)` is a special case (move `head`);
growing must copy both arrays.

---

## P2 — Rotten oranges (multi-source BFS)

**Prerequisite — BFS.** Breadth-First Search explores a graph/grid **level by level** using a
**queue** (FIFO): you dequeue a cell, enqueue its unvisited neighbours, repeat. Because it expands
in rings, the first time BFS reaches a cell is via a shortest path — which is why it measures "time"
or "distance".

**Multi-source BFS.** Here, *every* rotten orange spreads at the same time, so you **seed the queue
with all rotten cells at once** (not one). Then process the queue in **layers**: capture
`queueSize` at the start of a minute, process exactly that many cells, and only *after* the layer
do you increment `minutes`. Each layer = one minute of spreading.

**Algorithm.** Count `fresh` while seeding. If `fresh==0` at the start, return 0. BFS layer by
layer; every time a fresh neighbour rots, decrement `fresh` and enqueue it. At the end, return
`minutes` if `fresh==0`, else `-1` (some orange was unreachable).

**Complexity.** O(R×C) — each cell enters the queue at most once.

**Gotchas.** Return 0 when there's no fresh fruit (don't over-count). Only bump `minutes` on layers
that actually rotted something, or you'll be off by one. Mutating the grid to mark "rotten" doubles
as your visited-set.

---

## P3 — First non-repeating character

**Approach — two passes.** Pass 1: count each character's occurrences (an `int[128]`/`int[256]`
for ASCII, or a `HashMap<Character,Integer>` for Unicode). Pass 2: walk the **original string** and
return the first char whose count is 1. Return a sentinel (`'#'`) if none.

**Why scan the string, not the map.** "First" means first *in input order*. A plain `HashMap`
loses order, so you re-scan the string. (Alternatively, a `LinkedHashMap` preserves insertion order
and lets you scan the map instead.)

**Complexity.** O(n) time, O(1) space for a fixed alphabet (O(k) for k distinct chars).

**Gotcha.** Don't return based on the map's iteration order — order comes from the string.

---

## P4 — Insert k commas to maximise the number

**The key insight.** `k` commas create `k+1` non-empty parts. You want the **single largest part**
to be as big as possible. A number is bigger mainly by having **more digits**, so make one part as
long as possible. Since the other `k` parts need at least 1 char each, the longest any part can be
is `L = n - k`, and the remaining `k` chars become single-digit parts (always a valid split).

So the answer = the **maximum contiguous substring of length `L = n - k`**. Because all candidate
windows have the *same* length, comparing them numerically is identical to comparing them as
strings (`compareTo`) — leading zeros just make a window smaller, which is handled automatically.

**Worked:** `s="857", k=1` → `L=2`, windows `"85","57"` → `"85"`. `s="1000", k=1` → `L=3`,
windows `"100","000"` → `"100"`. `s="999", k=2` → `L=1` → `"9"`.

**Complexity.** O(n·L) naive (each substring compare), fine for interview sizes; O(n) achievable.

**Gotcha.** Same-length digit strings: lexicographic order == numeric order, so `String.compareTo`
is safe. Return the substring (avoids overflow for huge inputs).

---

## P5 — Maximum sum of a window of size k (sliding window)

**Prerequisite — sliding window.** Instead of recomputing each window's sum from scratch
(O(n·k)), keep a running sum and **slide**: when the window moves one step right, add the entering
element and subtract the leaving one — `sum += a[i] - a[i-k]`. That turns it into a single pass.

**Algorithm.** Sum the first `k` elements (window 0), set `max` to it, then slide from `i=k`:
`sum += a[i] - a[i-k]; max = Math.max(max, sum)`.

**Complexity.** O(n) time, O(1) space.

**Gotcha.** Initialise `max` to the **first window's sum**, not 0 — otherwise all-negative arrays
return a wrong 0. Assumes `k <= n`.
