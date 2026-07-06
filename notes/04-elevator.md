# 04 — LLD: Elevator System (full walkthrough)

The first complete LLD. Teaches: the 5-step design framework, dispatch as a
Strategy, TreeSet-driven SCAN/LOOK, and fine-grained locking.

---

## THE 5-STEP FRAMEWORK (used for every LLD after this)
```
1. REQUIREMENTS   what should it do? (plain English first, no classes)
2. ENTITIES       the NOUNS -> classes/enums/interfaces
3. RELATIONSHIPS  has-a / is-a between them
4. KEY METHODS    the important verbs per class (signatures, 1-liners)
5. PATTERNS       where each pattern fits naturally (never force one)
```
Interviewers grade you on walking these OUT LOUD. Jumping straight to code is
the #1 junior mistake.

---

## Step 1 — Requirements (with the domain vocabulary)

- **Hall call** = request from OUTSIDE, on a floor: press UP or DOWN. You only
  know the direction, not the destination (real lobbies have just two buttons).
- **Car call** = request from INSIDE the car: press a floor number. ("Car" =
  the cabin, industry term.)
- Multiple elevators; the system must DECIDE WHICH ONE responds (dispatch).
- Elevator states: IDLE, MOVING_UP, MOVING_DOWN, (MAINTENANCE).
- Order within one elevator: serve floors sensibly, not in arrival order.

## Step 2 — Entities
```
Elevator                     the car: floor, state, direction, pending targets
ElevatorController           holds all elevators, routes requests, ticks simulation
Request (ABSTRACT)           base: floor
  HallCall extends Request   + Direction
  CarCall  extends Request   floor only
ElevatorSelectionStrategy    interface — the swappable dispatch algorithm
  DirectionalScanStrategy    the SCAN/LOOK implementation
Direction { UP, DOWN, IDLE }        ElevatorState { IDLE, MOVING_UP, MOVING_DOWN, MAINTENANCE }
```

### Why Request is an ABSTRACT CLASS with two subclasses (design decision)
A single Request with a `type` flag means `direction` is meaningless junk for
car calls — "fields valid sometimes" is a god-object smell. Splitting into
HallCall/CarCall makes the type system enforce correctness: CarCall simply has
NO direction field. Rule learned:
> Fields that are "valid sometimes, junk other times based on a type flag" ->
> split into subclasses.
Why abstract (not interface): it holds SHARED STATE (the `floor` field);
interfaces can't. And `abstract` forbids `new Request(5)` — a "generic request"
doesn't exist in reality; it's always concretely hall or car.

`protected final int floor`:
- **protected** = who can SEE it (subclasses + package). NOT about changing.
- **final** = can it CHANGE? never after the constructor -> immutable.
Two orthogonal keywords: visibility vs mutability.
`super(floor)` in HallCall's ctor = "parent, you set the floor (it's yours,
and it's final so only YOUR ctor may assign it); I'll set my own direction."

## Step 3 — Relationships
```
ElevatorController ◆── many Elevator            has-a (composition, 1-to-many)
ElevatorController ◇── ElevatorSelectionStrategy has-a (AGGREGATION — swappable;
                                                  field is non-final + setStrategy())
Elevator ◆── TreeSet<Integer> targets            has-a
Elevator ── ElevatorState / Direction            has-a (enum values)
HallCall / CarCall ──▷ Request                   is-a
DirectionalScanStrategy ──▷ ElevatorSelectionStrategy   is-a (the ONLY is-a
                                                  besides Request — healthy design
                                                  is mostly has-a)
```
Composition (◆ filled diamond) = owned, dies with parent (targets belong to
that elevator). Aggregation (◇ hollow) = plugged in, swappable (the strategy).

---

## The core algorithm — SCAN/LOOK with a TreeSet

### Why TreeSet<Integer> for pending floors (3 reasons)
Requests arrive 8, 3, 5. An ArrayList (insertion order) would send the car
8 -> down to 3 -> up to 5. Insane. TreeSet:
1. **Auto-sorted** -> serve floors in order (3,5,8) = the elevator algorithm.
2. **Set** -> two people pressing "5" stores it once.
3. **ceiling()/floor() in O(log n)** -> "nearest target above/below me" for free.

### ceiling() and floor() — the number-line picture
```
set = {2, 5, 9}
   --●---------●---------●------>
     2         5         9
ceiling(x) = nearest element AT OR RIGHT of x  (>= x)
floor(x)   = nearest element AT OR LEFT  of x  (<= x)
ceiling(4)=5   floor(4)=2
ceiling(5)=5   floor(5)=5      <- x itself counts ("at or")
ceiling(10)=null               <- nothing at/above -> null is the signal
floor(1)=null
```
Memory hook: ceiling is ABOVE you in a room; floor is BELOW you. (TreeSet's
"floor" has NOTHING to do with elevator floors — name collision.)

### Backing structure: red-black tree (why TreeSet is O(log n))
- A plain Binary Search Tree (left smaller / right larger) degrades to a linked
  list if you insert sorted data (1,2,3,4,5 all chain right) -> O(n).
- Self-balancing trees auto-fix their shape. A **red-black tree** colors nodes
  RED/BLACK with rules (root black; no two consecutive reds; equal black-count
  per path) that mathematically keep height ~log n; violations trigger
  recolor/rotate in O(log n).
- You don't memorize rotations. You DO say: "TreeSet/TreeMap are backed by
  red-black trees — self-balancing BSTs — so add/contains/ceiling/floor are all
  O(log n) regardless of insertion order."

### The three key methods (traced)

**addRequest(Request r):** add floor to set; if IDLE, compute initial
direction and switch state to moving.

**move() — one tick, one floor:**
```
if targets empty -> IDLE (state + direction), return
currentFloor += (direction == UP ? +1 : -1)
if targets.contains(currentFloor):        <- arrived at a requested floor?
    targets.remove(currentFloor); print "stopped"
updateDirection()                          <- may flip
```
HAND-TRACE that made it click (floor 0, target {5}, UP):
```
move1: floor 1, contains(1)? NO -> passing through, set unchanged
move2: floor 2, NO   move3: floor 3, NO   move4: floor 4, NO
move5: floor 5, contains(5)? YES -> remove, print; set {} -> IDLE
```
MISTAKE I MADE: thinking the car "removes 2 because 2 is there" — 2 was never
in the set. `targetFloors` holds only REQUESTED floors; currentFloor is just a
position. The car PASSES THROUGH non-target floors, STOPS only on targets.

**updateDirection() — the SCAN/LOOK core:**
```
if empty -> IDLE
above = targets.ceiling(currentFloor)     "anything left at/above me?"
below = targets.floor(currentFloor)       "anything left at/below me?"
if direction == UP:  direction = (above != null) ? UP : DOWN   // keep going, else FLIP
else:                direction = (below != null) ? DOWN : UP
```
Real-elevator behavior simulated: keep sweeping one direction while work
remains that way; flip when exhausted. (Named the "elevator algorithm" —
SCAN/LOOK — literally after this machine.)

---

## The dispatch Strategy (which elevator gets the request)

Interface:
```java
interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
```

DirectionalScanStrategy — TWO-PASS design:
```
PASS 1 (ideal candidates): skip MAINTENANCE; candidate if
   - IDLE, or
   - moving in the request's direction AND hasn't passed the floor yet:
       isOnTheWay: UP-> car.floor <= reqFloor;  DOWN-> car.floor >= reqFloor
   pick the NEAREST candidate (min |carFloor - reqFloor|)
PASS 2 (fallback): if none ideal, pick nearest non-maintenance elevator anyway.
```
**Why the fallback matters — STARVATION.** Without it, when every car is moving
the wrong way, the request returns null and the person waits FOREVER. The
fallback guarantees every request is eventually served ("degrade gracefully:
nearest car finishes its work, then comes"). Say the word *starvation* in
interviews.

Runtime swap: strategy field is non-final + `setStrategy()` — e.g., switch to a
least-busy strategy at peak hours. (That's the whole point of Strategy.)

---

## Extension & concurrency questions (asked at L3/L4)

**Multi-bank buildings (low-rise 1-10 / high-rise 11-30):**
- Option A: one `ElevatorController` PER BANK + a top dispatcher routing by
  floor. (Controller already encapsulates "a group with a strategy" — reuse it.)
- Option B: each Elevator gets [minFloor, maxFloor]; strategy filters
  `!e.canServe(floor)`. Handles overlapping ranges too.
The question tests whether the design EXTENDS without rewrite = the OCP payoff.

**Concurrency gap:** shared mutable state = targetFloors (TreeSet is NOT
thread-safe), currentFloor/state/direction (strategy READS while move() WRITES
-> race).
Fix: guard Elevator's mutating methods with synchronized or a **per-elevator
lock** — NOT a controller-level lock, or all elevators serialize. Fine-grained
locking so independent elevators move in parallel. (Best: actor-style — one
thread per elevator consuming a command queue.)

---

## Interview narrative (say this in ~30s)
> "Multiple elevators with a swappable dispatch algorithm — Strategy decouples
> scheduling. Hall vs car calls modeled as a Request hierarchy. Each elevator
> runs SCAN/LOOK using a TreeSet of target floors: ceiling/floor give the
> nearest pending target above/below in O(log n), and the car flips direction
> only when nothing remains its way. Dispatch prefers idle or same-direction
> on-the-way cars, nearest first, with a nearest-available fallback to prevent
> starvation. Concurrency: per-elevator locks so cars move in parallel;
> extensible to elevator banks with a controller per bank."
