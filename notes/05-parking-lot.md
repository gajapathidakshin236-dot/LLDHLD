# 05 — LLD: Parking Lot (full walkthrough)

The "Two Sum of LLD" — asked everywhere. Teaches: composition hierarchy
(Lot->Floor->Spot), THREE strategies in one system, SRP on fee-vs-payment, and
the catalog-vs-transaction distinction.

---

## Step 1 — Requirements
```
1. Check-in:  vehicle arrives -> assign a fitting spot -> issue TICKET
2. Check-out: scan ticket -> compute FEE by duration -> collect PAYMENT -> free spot
3. Vehicle types: BIKE, CAR, TRUCK (need different spot sizes)
4. Multi-FLOOR lot
5. Hourly pricing, rate varies by vehicle type
6. Spot allocation is a swappable policy (nearest floor first, ...)
7. LOT FULL -> reject entry
```

## Step 2 — Entities (with the two I MISSED first time)
```
ParkingLot            the whole structure (NOT a Singleton — see below)
ParkingFloor          <- MISSED: one floor holding its spots.
ParkingSpot           id, SpotType, occupied-by (Vehicle or null)
Vehicle               licensePlate + VehicleType (enum-only; no subclasses needed
                      — types differ by label/size, not behavior)
Ticket                spot + vehicle + entryTime + id  (issued at entry)
ParkingLotController  coordinator: parkVehicle / exitVehicle

Enums:  VehicleType { BIKE, CAR, TRUCK }
        SpotType    { BIKE, COMPACT, LARGE }   <- MISSED: the SIZE of a spot

Strategies (x3!):
  SpotAllocationStrategy -> NearestFirstAllocationStrategy
  FeeStrategy            -> HourlyFeeStrategy
  PaymentStrategy        -> UPIPayment, CardPayment, CashPayment
```
Why the misses mattered: without **ParkingFloor** you'd flatten all spots into
one list — no multi-floor structure. The lesson: model the CONTAINMENT
HIERARCHY (Lot has Floors has Spots), don't flatten it.

RECURRING NAMING LESSON: entities are NOUNS. I kept writing fields ("int item,
name") or behaviors ("finds the spot") instead of names (MenuItem, ParkingFloor).
> Name the noun. Fields and verbs come later.

## Step 3 — Relationships (10/12 first try; the 2 misses)
```
ParkingLot   ◆── many ParkingFloor      has-a (composition)
ParkingFloor ◆── many ParkingSpot       has-a (composition)
ParkingSpot  ◇── 0..1 Vehicle           has-a (null = free)
ParkingSpot  ──  SpotType               has-a  <- I said "is-a". WRONG:
                                            an enum is a LABEL/property you HOLD.
                                            You can't extend an enum. Spot HAS a type.
Vehicle      ──  VehicleType (ONE)      has-a  <- I said "has many". A car is ONE
                                            type, not several. "has-many" is just
                                            has-a with multiplicity; get it right.
Ticket       ──  ParkingSpot + Vehicle  has-a (references)
Controller   ◇── all 3 strategies       has-a (aggregation, swappable)
NearestFirst..., HourlyFee..., UPIPayment ──▷ their interfaces   is-a
```

---

## The 3 strategies — and the SRP catch (the big lesson of this LLD)

**MISTAKE I MADE:** "calculateFee ... and then call payment strat" — I wired
payment INSIDE fee calculation. That merges two responsibilities:

| Concern | Job | Lives in |
|---|---|---|
| COMPUTE the fee | "3h x ₹30 = ₹90" — pure math, returns a number | FeeStrategy |
| COLLECT the money | actually charge UPI/Card/Cash | PaymentStrategy |

Why they MUST stay separate (SRP):
- Can't show a "preview bill" without charging if they're fused.
- Can't unit-test fee math without triggering a payment.
- Naming truth: methods prefixed calculate/compute/get should be PURE (no side
  effects). pay/save/send DO things. `calculateFee` that charges money is a lie.

The controller ORCHESTRATES both, in order:
```java
public synchronized double exitVehicle(Ticket t, PaymentStrategy payment, LocalDateTime exit) {
    double fee = feeStrategy.calculateFee(t, exit);   // 1. compute (PURE)
    payment.pay(fee);                                  // 2. collect (separate)
    t.getSpot().vacate();                              // 3. free the spot
    return fee;
}
```

### Fee math detail (HourlyFeeStrategy)
```java
long minutes = Duration.between(t.getEntryTime(), exitTime).toMinutes();
long hours = (long) Math.ceil(minutes / 60.0);   // partial hour BILLS UP (61min = 2h)
if (hours == 0) hours = 1;                        // minimum 1 hour
return hours * ratePerHour(vehicleType);          // BIKE 10 / CAR 30 / TRUCK 50
```

### Spot-fit rule (size compatibility)
```java
boolean canFit(VehicleType vt) {         // a vehicle fits its size OR LARGER
    BIKE  -> any spot
    CAR   -> COMPACT or LARGE
    TRUCK -> LARGE only
}
```
occupy() throws if already taken; vacate() throws if already free — guard the
invariants at the lowest level.

### Allocation strategy
NearestFirstAllocationStrategy: scan floors in order (floor 0 first), return
the first free+fitting spot; null = LOT FULL for that vehicle type. Swappable
for least-busy-floor etc. — same Strategy shape as elevator dispatch.

---

## Why ParkingLot is NOT a Singleton (the production discussion)

Tempting ("one lot!") but:
1. **Concurrency:** two threads find the same free spot -> both occupy -> two
   cars, one spot. find+occupy must be ATOMIC. `synchronized parkVehicle` on a
   Singleton = ONE car enters at a time across the whole lot (awful). Better:
   per-SPOT locks/AtomicBoolean so different spots park in parallel.
2. **Testing:** getInstance() hardcoded -> can't inject a fake full lot; state
   leaks between tests.
3. **Multi-lot future:** company opens lot #2 -> Singleton forbids it.
4. **Horizontal scaling:** each JVM gets its own "singleton" -> two servers
   assign the same spot. State must move to DB/Redis anyway.
The right call: instantiate ONE lot and INJECT it (constructor DI). Same effect,
testable, extensible. Say: "I'd keep a single instance via DI rather than
enforce Singleton."

---

## The catalog-vs-transaction distinction (foreshadows Food Delivery)
Ticket references the Spot (catalog-ish) + captures entryTime (transactional).
Full form appears in Food Delivery: MenuItem (catalog) vs OrderItem (item +
quantity, the transaction line). Same idea as Product vs CartLine. When a
"thing" and "someone's use of the thing" differ, model both.

---

## Interview narrative
> "Multi-floor lot modeled as Lot->Floor->Spot composition. Three swappable
> strategies: spot allocation, fee calculation, and payment collection — fee
> calc is PURE and separate from payment (SRP), so we can preview a bill
> without charging. Spot-fit maps vehicle size to spot size; occupy/vacate
> guard their invariants. Not a Singleton — one injected instance, per-spot
> locking for concurrent entry, and state moves to a DB when distributed."
