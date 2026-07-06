# 09 — LLD: Stock Trading — Order Book + Matching Engine (ground-up)

Built from ZERO domain knowledge, one prerequisite layer at a time. This is the
LLD where the data structures (heaps) directly power the design.

```
share -> exchange -> price -> bid/ask -> orders (market vs limit)
-> ORDER BOOK -> price-time priority -> MATCHING ENGINE -> code
```

---

## LAYER 1 — Shares & the exchange
- **Share** = a slice of ownership in a company (1,000 of 1,000,000 shares =
  0.1% owner = a **shareholder**). **Stock** = the collective word; share = the
  countable unit.
- **Why shares exist:** companies need big money to grow -> sell ownership
  slices to the public for cash instead of borrowing it all.
- **Stock exchange** = the marketplace where share buyers & sellers meet (NSE,
  BSE, NYSE). Farmers'-market analogy. It solves: finding a counterparty, fair
  price discovery, and safe/trusted settlement. Building the order book +
  matching engine = building the GUTS of an exchange.

## LAYER 2 — Price, bid & ask
- **Price** = cost of ONE share. Moves with demand vs supply (concert tickets:
  hot show -> prices soar; empty show -> prices drop).
- Core tension: buyer wants to pay LESS, seller wants MORE. Each states a price:
  - **BID** = what a BUYER will pay. Hook: **B**id = **B**uy.
  - **ASK** = what a SELLER will accept (they're *asking* for it).
  - **SPREAD** = highest bid <-> lowest ask gap. Bid sits BELOW ask normally.
- **A trade fires when they CROSS: highest bid >= lowest ask.**
  Bid 99 / Ask 101 -> no trade. Buyer raises to 101 -> MATCH at 101.

## LAYER 3 — Orders: market vs limit
- **Order** = your instruction to the exchange: side (BUY/SELL), quantity,
  (maybe) price. Like ordering food — a clear instruction it can act on.
- The trade-off driving the two types: SPEED vs PRICE CONTROL — pick one.
- **MARKET order** = "fill me NOW at the best available price." No price given.
  + immediate;  - could get a bad price. Hook: "the *market* price, whatever."
- **LIMIT order** = "only at MY price OR BETTER."
  - Limit BUY 99 -> fills at 99 or LOWER (cheaper = better for a buyer).
  - Limit SELL 101 -> fills at 101 or HIGHER.
  + price control;  - may never fill (or partially). Hook: a price *limit* you
  refuse to cross.
- **THE BRIDGE:** a market order fills instantly -> never stored. A limit order
  may WAIT -> it must live somewhere. That somewhere = the ORDER BOOK.

## LAYER 4 — The Order Book
**Definition:** the structure holding all WAITING limit orders, organized by
price. Two sides:
```
        ASKS (sell limits)         BIDS (buy limits)
   101 x 80   <- best (LOWEST)   99 x 100  <- best (HIGHEST)
   102 x 150                     98 x 200
   105 x 300                     95 x 500
```
- BID side sorted **highest first** (best buyer for a seller).
- ASK side sorted **lowest first** (best seller for a buyer).
- Requirement "best price instantly on top" = EXACTLY the heap property:
  > **BID side = MAX-heap on price. ASK side = MIN-heap on price.**
  peek O(1), offer/poll O(log n). (This is why heaps were the prerequisite.)

## LAYER 5 — Price-Time Priority
Problem: two buyers both bid 99 — who matches first? Need a fair tiebreak.

Prerequisite — **FIFO** (First In, First Out): whoever arrived first is served
first (billing-queue analogy; opposite = LIFO, a plate stack). Markets use FIFO
for fairness.

**Price-Time Priority (two levels):**
1. **Price** first: better price wins (highest bid / lowest ask).
2. **Time** tiebreak: same price -> EARLIER order wins (FIFO).
Why: fairness (better price or earlier commitment deserves priority) +
incentive (rewards competitive pricing and early orders -> liquid markets).

Worked: bids arrive A 99(t1), B 100(t2), C 99(t3) -> match order **B (best
price) -> A (99, earlier) -> C (99, later)**. VERIFIED in the demo run.

Data-structure consequence: the heap comparator is **(price, then timestamp)**
— each Order carries an arrival timestamp; price ties break by earliest.

## LAYER 6 — The Matching Engine
The BRAIN: pairs an incoming order against the opposite side while prices
cross.

Prerequisites — fills:
- **Fill** = executed. **Fully filled** = whole quantity traded.
- **Partial fill** = only part traded (the other side was smaller); the rest
  keeps matching or waits. Want 100, seller has 60 -> 60 trade, 40 remain.

**The algorithm (incoming BUY, limit P, qty Q):**
```
while Q > 0 and asks not empty:
    bestAsk = peek of ask heap
    if bestAsk.price <= P:                        # CROSS? buy limit >= ask
        tradeQty = min(Q, bestAsk.quantity)       # trade the SMALLER side
        record Trade @ bestAsk.price              # RESTING order's price!
        Q -= tradeQty;  bestAsk.quantity -= tradeQty
        if bestAsk.quantity == 0: poll()          # fully filled -> pop
    else: break                                    # best seller too expensive
if Q > 0: rest leftover in the BID heap           # becomes a resting order
```
(SELL mirrors against bids: crosses when bestBid.price >= sellPrice.
MARKET orders always cross; any unfilled remainder is DROPPED, never rested.)

Key conventions:
- **min(Q, resting.quantity)** — you trade what BOTH sides have; this IS the
  partial-fill mechanism.
- **Trade price = the RESTING order's price** — it was there first and set the
  price; the incoming order is the "aggressor" accepting it.

Worked (verified): asks 101x60, 102x100; BUY 100 @ 102 ->
60@101 (seller1 fully filled, popped), then 40@102 (seller2 partial, 60 left).
Buyer fully filled across two price levels — "sweeping the book."

## LAYER 7 — The code design
```
Side { BUY, SELL }     OrderType { MARKET, LIMIT }
Order   id (AtomicLong), side, type, price, quantity (MUTABLE), timestamp (nanoTime)
Trade   buyOrderId, sellOrderId, price, quantity
OrderBook   the two PriorityQueues
Exchange    submit(order) = the matching engine; trade log; synchronized intake
```

**The comparators ARE price-time priority, encoded:**
```java
// BIDS: price DESC (max-heap), ties -> earlier timestamp
new PriorityQueue<>(Comparator.comparingDouble((Order o) -> o.price).reversed()
                              .thenComparingLong(o -> o.timestamp));
// ASKS: price ASC (min-heap), ties -> earlier timestamp
new PriorityQueue<>(Comparator.comparingDouble((Order o) -> o.price)
                              .thenComparingLong(o -> o.timestamp));
```

**Heap + partial-fill trick:** heap order depends on (price, time), NOT
quantity — so you can `peek()` the head and mutate its `quantity` in place
without breaking heap invariants; `poll()` only when it hits 0. Clean.

**IDs:** `AtomicLong.getAndIncrement()` — counter++ is 3 steps (read/add/write)
and NOT atomic; concurrent submits could duplicate ids.

**Concurrency:** `submit()` is synchronized -> order intake SERIALIZED
(single-writer discipline) so the book can't be corrupted mid-match; matching
per order is atomic. Real exchanges do exactly this — one matching thread per
symbol.

## Gotchas (mine + classic)
1. Bid is BELOW ask; a trade needs a CROSS — memorize "bid >= ask fires."
2. "Limit at 99" = "99 OR BETTER" (cheaper for buyers, dearer for sellers).
3. Market orders never rest; leftover is dropped (no liquidity).
4. Priority = price FIRST, then time — not time alone.
5. Trade executes at the RESTING price, not the incoming order's.
6. AtomicLong for ids.

## Interview narrative
> "The order book is two heaps — bids max-heap, asks min-heap — with
> comparators of (price, then arrival time), which IS price-time priority
> encoded. The matching engine peeks the best opposite order; while prices
> cross it trades min(incoming, resting) at the resting price, popping
> fully-filled orders and resting any leftover limit quantity — partial fills
> fall out of the min. Market orders always cross and never rest. Intake is
> serialized (single writer per symbol) so matching is atomic; ids via
> AtomicLong."
