# 08 — LLD: Food Delivery / Swiggy-Zomato Lite (full walkthrough)

Teaches: the GUARDED STATE MACHINE (the centerpiece), catalog-vs-transaction
modeling (MenuItem vs OrderItem), and two more Strategy applications.

---

## Step 1 — Requirements
```
1. Customer browses Restaurants; each Restaurant has a Menu of MenuItems
2. Customer places an Order containing OrderItems (item + quantity)
3. Order assigned to a DeliveryAgent — VIA A STRATEGY (nearest / least-busy;
   same shape as elevator dispatch & spot allocation — 3rd time seeing it)
4. STATE MACHINE:
   PLACED -> CONFIRMED (restaurant accepts) -> PREPARING -> READY_FOR_PICKUP
   -> OUT_FOR_DELIVERY -> DELIVERED     (+ CANCELLED branches off early states)
5. Bill = sum(line totals) + delivery fee (Strategy: distance-based/flat)
   [+ tax - discount in a fuller version]
```

## Step 2 — Entities (with the one everyone misses)
```
Customer / Restaurant / Menu / MenuItem(id, name, price)
Order        items, status, customer, agent, bill
OrderItem    <- THE MISSED ONE: MenuItem + quantity (the transaction line)
DeliveryAgent
FoodDeliveryService                        coordinator
OrderStatus (enum)                         the state machine
AgentAssignmentStrategy -> NearestAgentStrategy
DeliveryFeeStrategy     -> DistanceBasedFee / FlatFee
```

### MenuItem vs OrderItem — catalog vs transaction (burn this in)
- **MenuItem** = "Margherita, ₹300" — the CATALOG entry; exists whether or not
  anyone orders it.
- **OrderItem** = "2 x Margherita" — THIS customer's line in THIS order.
Without OrderItem there's no clean home for QUANTITY. Same distinction as
Product vs CartLine, Spot vs Ticket. Whenever "the thing" and "someone's use of
the thing" differ, model both.
```java
class OrderItem { MenuItem item; int quantity; double lineTotal() { return item.getPrice()*quantity; } }
class Order     { List<OrderItem> items; ... }
```

## Step 3 — Relationships (11/12; the one repeat slip)
All has-a except the strategy implementations:
`NearestAgentStrategy ──▷ AgentAssignmentStrategy` = **is-a**. I wrote has-a
despite answering the IDENTICAL shape (#11 DistanceBasedFee is-a
DeliveryFeeStrategy) correctly one line earlier. The rule, final form:
> **implements/extends -> is-a. Every single time. A concrete strategy IS one
> specific way of doing the thing — it doesn't HOLD one.**

---

## THE CENTERPIECE: the guarded state machine

### The problem naive code has
`order.status = newStatus;` allows ANY jump: PLACED -> DELIVERED (deliver an
uncooked order!), DELIVERED -> PREPARING (un-deliver!). A bug factory. We must
ENFORCE legal transitions.

### The transition graph
```
PLACED --> CONFIRMED --> PREPARING --> READY_FOR_PICKUP --> OUT_FOR_DELIVERY --> DELIVERED
  |            |             |
  +------------+-------------+--> CANCELLED
DELIVERED, CANCELLED = TERMINAL (no arrows out)
```
Rules encoded: (a) ONE STEP at a time — no skipping ahead; (b) terminal states
have no exits; (c) cancel only from early states (can't cancel what's already
out for delivery / delivered).

### Approach 1 — enum + allowed-transitions map (what we built; right weight here)
```java
enum OrderStatus {
    PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP,
    OUT_FOR_DELIVERY, DELIVERED, CANCELLED;

    private Set<OrderStatus> allowedNext;

    // Enums can't reference siblings in their constructors (not all built yet),
    // so the graph is wired in a STATIC BLOCK:
    static {
        PLACED.allowedNext           = Set.of(CONFIRMED, CANCELLED);
        CONFIRMED.allowedNext        = Set.of(PREPARING, CANCELLED);
        PREPARING.allowedNext        = Set.of(READY_FOR_PICKUP, CANCELLED);
        READY_FOR_PICKUP.allowedNext = Set.of(OUT_FOR_DELIVERY);
        OUT_FOR_DELIVERY.allowedNext = Set.of(DELIVERED);
        DELIVERED.allowedNext        = Set.of();     // terminal: empty = no way out
        CANCELLED.allowedNext        = Set.of();
    }
    boolean canTransitionTo(OrderStatus next) { return allowedNext.contains(next); }
}
```
```java
// in Order — the guard:
public void transitionTo(OrderStatus next) {
    if (!status.canTransitionTo(next))
        throw new IllegalStateException("Cannot go from " + status + " to " + next);
    this.status = next;
}
```
Verified behavior (from the run): the legal chain passes; DELIVERED->PREPARING
blocked; PLACED->DELIVERED blocked. Each state's allowedNext set = the arrows
leaving it; the guard just asks "is there an arrow from here to there?"

WHY PLACED->PREPARING throws even though PREPARING is "forward": PLACED's set
is {CONFIRMED, CANCELLED} only — the machine forbids SKIPPING confirmed. One
hop at a time; that's the whole safety property.

### Approach 2 — full State pattern (each state = a class)
Use when states have RICH DIFFERENT BEHAVIOR, not just transition gating:
```java
interface OrderState { void next(Order o); void cancel(Order o); }
class PlacedState implements OrderState {
    public void next(Order o)   { o.setState(new ConfirmedState()); }
    public void cancel(Order o) { o.setState(new CancelledState()); }   // allowed here
}
class OutForDeliveryState implements OrderState {
    public void next(Order o)   { o.setState(new DeliveredState()); }
    public void cancel(Order o) { throw new IllegalStateException("too late"); } // rule LIVES HERE
}
```
`order.next()` does different things depending on which state object is
installed — polymorphism instead of a central if-else. The "can't cancel
out-for-delivery" rule lives INSIDE OutForDeliveryState.cancel().

### The judgment (senior one-liner)
> "States that mainly gate legal transitions -> enum + transition map (right
> weight). Full State pattern earns its class-per-state cost when each state
> behaves substantially differently. For order status, enum wins."

Natural extension to NAME (not build): **Observer** — every transitionTo could
notify subscribers ("your order is out for delivery!") — customer app, SMS.

---

## The two strategies (routine by now)
```java
interface AgentAssignmentStrategy { DeliveryAgent assign(List<DeliveryAgent>, Order); }
class NearestAgentStrategy { ... min distance ... }        // same shape as elevator dispatch

interface DeliveryFeeStrategy { double calculateFee(int distanceKm); }
class DistanceBasedFeeStrategy { return 20 + km*10; }      // base + per-km
class FlatFeeStrategy          { return 50; }
```
Bill assembly in the coordinator: `sum(orderItem.lineTotal()) + fee` — and note
fee CALCULATION stays pure (Parking Lot's SRP lesson carried forward).

## The static-counter ID lesson (came up here)
```java
private static int counter = 1;   // ONE copy shared by ALL Orders (static = class-level)
this.id = counter++;              // post-increment: use current, THEN bump
```
Each order gets a unique sequential id BECAUSE they share one counter. BUT:
**counter++ is NOT atomic** (read, add, write = 3 steps). Two concurrent
creates can both read 5 -> duplicate ids. Fix for multi-threaded creation:
```java
private static final AtomicInteger counter = new AtomicInteger(1);
this.id = counter.getAndIncrement();     // one indivisible operation
```
(Stock Trading uses AtomicLong for exactly this reason.)

## Interview narrative
> "Composition: Restaurant->Menu->MenuItem, Order->OrderItem (catalog vs
> transaction line — OrderItem carries quantity). The order lifecycle is a
> guarded state machine: an enum whose allowedNext sets encode the transition
> graph — one hop at a time, terminal states sealed, cancel only early. Agent
> assignment and delivery fee are Strategies. Status changes are a natural
> Observer hook for customer notifications. IDs via AtomicInteger since
> counter++ isn't atomic."
