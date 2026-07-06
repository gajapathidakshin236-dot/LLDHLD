# 10 — Data Structures that power the LLDs (TreeSet, Trie, Heap)

These three appeared INSIDE the designs — knowing their internals is what let
the designs work.

---

## TREESET (used in: Elevator target floors)

### The Set family (difference = ordering)
| Type | Order kept | Speed | Backed by |
|---|---|---|---|
| HashSet | none | O(1) | hash table |
| LinkedHashSet | insertion order | O(1) | hash table + linked list |
| **TreeSet** | **SORTED** | O(log n) | **red-black tree** |
`add 5,1,3` -> TreeSet stores {1,3,5}. Auto-sorted is its superpower.

### The four "nearest" methods (number-line picture)
```
set = {2,5,9}        --●-----●-----●-->
ceiling(x) = smallest element >= x     ceiling(4)=5  ceiling(5)=5  ceiling(10)=null
floor(x)   = largest  element <= x     floor(4)=2    floor(5)=5    floor(1)=null
higher(x)/lower(x) = strict versions (> / <)
```
Hooks: ceiling is ABOVE you in a room; floor is BELOW. x itself counts ("at
or"). `null` = nothing that direction — the elevator's flip signal.

### Red-black tree (why it's O(log n) — the level you need)
- BST rule: left subtree smaller, right larger -> search halves each step.
- Plain BST FLAW: insert sorted data (1,2,3,4,5) -> chains into a linked list
  -> O(n). Shape depends on insertion order.
- **Self-balancing** trees auto-fix shape. Red-black = nodes colored RED/BLACK
  under rules (root black; no two consecutive reds; equal black-count on every
  root-to-leaf path). Violations trigger recolor/rotate in O(log n), keeping
  height ~log n GUARANTEED regardless of insertion order.
- You don't memorize rotations. The sentence:
> "TreeSet/TreeMap are backed by red-black trees — self-balancing BSTs — so
> add/contains/ceiling/floor are O(log n) no matter the insertion order. Same
> structure as C++ std::set."

---

## TRIE (autocomplete / typeahead; shows up inside search features)

### The problem
Type "ca" -> instantly show cat/car/card out of millions of words. Naive scan
= O(N x L) per keystroke — far too slow.

### The structure
A tree where each EDGE is a letter; each path from the root spells a prefix.
Words sharing a beginning SHARE the path (stored once):
```
        (root)
        /    \
       c      d
       |      |
       a      o
      / \     |
     t•  r•   g•        • = isWord (a complete word ends here)
         |
         d•
paths: cat, car, card, dog — c->a stored ONCE for all three ca-words
```

### TrieNode
```java
class TrieNode {
    Map<Character, TrieNode> children;   // "next letter -> next node"
    boolean isWord;                       // needed because a node can be the END
                                          // of one word (car) AND the MIDDLE of
                                          // another (card). Without it you can't
                                          // tell 'car' is a word but 'ca' isn't.
    List<Suggestion> topSuggestions;      // cached top-K for this prefix (below)
}
```

### insert(word) — O(L)
Walk letter by letter; child missing -> create; exists -> reuse; mark last node
isWord.
GOTCHA I HIT: inserting "card" after "car" creates **1 new node** (the 'd'),
not 0. Shared prefix (c,a,r) reused; the EXTRA letters of a longer word still
need new nodes.

### search(prefix) — O(P) to the node
Walk to the prefix's node (fail -> null = no matches). Its SUBTREE = all
matching words; DFS collecting isWord paths turns it into the list. Cost is
prefix length + relevant subtree only — independent of total word count. That's
the whole win.

### topSuggestions caching (the read-optimization)
Recomputing top-K per keystroke is wasteful. During INSERT, update a cached
top-K list at EVERY node along the path. Then search("ca") = walk to the node,
return the ready list — no DFS, no heap at query time. Pay at write, save at
read (typeahead is read-heavy -> correct trade).

---

## HEAP / PRIORITYQUEUE (used in: Trie top-K, and it IS the order book)

### What it is
PriorityQueue is backed by a **binary heap** — a tree kept "heap-ordered":
**min-heap rule: every parent <= its children** -> the MINIMUM is always on top
(O(1) peek). NOT fully sorted — just enough to always know the min; that
looseness is why ops are O(log n) not O(n log n).

### Stored as an ARRAY (the internals detail)
No node objects. For index i: left child 2i+1, right child 2i+2, parent
(i-1)/2. The tree is conceptual; physically a compact array.

### The two ops — GOTCHA I HAD
- **offer(x): add at the BOTTOM (end of array), then BUBBLE UP** — swap with
  parent while smaller; stop when parent <= x or at root. x reaches the top
  ONLY if it's the new min. (I said "adds at the top" — wrong. Dropping on top
  would break the min-on-top rule instantly; add safely at the bottom and let
  it RISE.)
- **poll(): take the TOP, move the LAST element to the top, BUBBLE DOWN** —
  swap with the smaller child until order restored.
Both O(log n): the element travels the tree HEIGHT = log n. That travel IS the
complexity.
```
offer(2) into {1,3,5,8}:  add 2 under 3 -> 2<3 swap -> 2<1? no, stop.
Final {1,2,5,8,3}; min (1) untouched on top.
```

### Java syntax
```java
new PriorityQueue<Integer>();                                  // min-heap default
new PriorityQueue<>(Collections.reverseOrder());               // max-heap
new PriorityQueue<Suggestion>((a,b) -> a.frequency - b.frequency);  // custom
// Order book comparators = (price, then time):
Comparator.comparingDouble((Order o) -> o.price).reversed()    // bids: price DESC
          .thenComparingLong(o -> o.timestamp);                // ties: earlier first
```

### THE TOP-K TRICK (counterintuitive — memorize)
> **Top K LARGEST -> a MIN-heap of size K.**
Why a MIN-heap for the LARGEST? The smallest of your current top-K sits on top
— the "weakest survivor," instantly evictable when something bigger arrives.
```java
for (Suggestion s : all) {
    heap.offer(s);
    if (heap.size() > k) heap.poll();   // over capacity -> drop the smallest
}   // heap now holds the k largest.  O(N log K)  — beats sorting O(N log N)
```
Trace k=2 over [9000,8500,3000,500]: 3000 and 500 get evicted as they enter;
{9000,8500} survive. Mirror rule: top-K SMALLEST -> MAX-heap of size K.

### The heap<->order-book bridge (why we learned it first)
"Best price instantly on top, ties by earliest time" is EXACTLY a heap with a
(price,time) comparator. Bids = max-heap, asks = min-heap. And because heap
order ignores QUANTITY, you can peek the head and shrink its quantity in place
(partial fill) without re-heapifying; poll only at 0.

### One-liners
> "PriorityQueue = binary heap in an array; parent<=children keeps the min at
> the top: peek O(1), offer/poll O(log n) via bubble-up/bubble-down."
> "Top-K largest: size-K min-heap, evict the smallest survivor. O(N log K)."
