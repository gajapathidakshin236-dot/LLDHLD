# LLD Index

Master catalog of all Low-Level Design work in this repo, with the design
patterns each one demonstrates.

## Location map

| LLD | Path | Files | Status |
|---|---|---|---|
| Elevator (v1) | `untitled/src/LLD/ELEVATOR/` | 11 | Updated — added Controller, Demo, NearestCarStrategy, fixed direction bug |
| Elevator (v2, more docs) | `src/main/java/com/company/elevator/` | 10 | Already polished with extensive docs |
| Parking Lot | `src/main/java/com/company/parkinglot/` | 14 | Well-documented, strategy DI |
| Parking Lot (uditagarwal) | `src/main/java/com/uditagarwal/` | 20+ | Command-pattern CLI variant |
| Rate Limiter | `src/main/java/com/company/ratelimite/` | 6 | Token Bucket + Sliding Window strategies |
| Rate Limiter (Spring project) | `RateLimier/rate-limiter/` | Maven | Standalone Spring Boot app |
| Kafka Pub/Sub | `src/main/java/com/company/DSA/KafkaLLD/` | 9 | **Updated** — Broker, Producer, Consumer, ConsumerGroup, offset store |

## Design patterns used

- **Strategy** — Elevator (`ElevatorSelectionStrategy`), Parking Lot
  (`SpotAllocationStrategy`, `FeeStrategy`, `PaymentStrategy`), Rate Limiter
  (`RateLimitingStrategy`), Kafka (`Partitioner`).
- **Command** — Parking Lot CLI (`uditagarwal/commands/*`).
- **Factory** — `CommandExecutorFactory`, `patterns/factory/*`.
- **Singleton** — `com/company/singleton/`, `patterns/singleton/`.
- **Builder** — `com/company/builder/`, `patterns/builder/`.
- **Observer / Decorator / Adapter / Chain of Responsibility** —
  `com/company/patterns/*` reference implementations.

## Elevator LLD (v1) — what's inside

Under `untitled/src/LLD/ELEVATOR/`:

| File | Role |
|---|---|
| `Direction.java` | enum {UP, DOWN, IDLE} |
| `ElevatorState.java` | enum {IDLE, MOVING_UP, MOVING_DOWN, MAINTENANCE} |
| `Request.java` | abstract base (holds floor) |
| `HallCall.java` | Request from a hallway button (has direction) |
| `CarCall.java` | Request from inside the cabin |
| `Elevator.java` | Cabin: state + motion + target set |
| `ElevatorSelectionStrategy.java` | Strategy interface |
| `DirectionalScanStrategy.java` | Prefers idle / on-the-way elevators |
| `NearestCarStrategy.java` | Simplest — closest non-maintenance elevator |
| `ElevatorController.java` | Façade — routes requests, ticks the simulation |
| `ElevatorDemo.java` | Runnable main showing the flow |

## Kafka Pub/Sub — what's inside

Under `src/main/java/com/company/DSA/KafkaLLD/`:

| File | Role |
|---|---|
| `Message.java` | Immutable record with key + value + timestamp |
| `Partition.java` | Append-only log (index == offset) |
| `Topic.java` | Named collection of partitions |
| `Partitioner.java` | Strategy + `KeyHashPartitioner`, `RoundRobinPartitioner` |
| `Broker.java` | Central hub — routes publishes, tracks per-group offsets |
| `Producer.java` | Client wrapper that calls `broker.publish` |
| `Consumer.java` | Reads its assigned partitions via the broker |
| `ConsumerGroup.java` | Round-robin assignment across consumers |
| `KafkaDemo.java` | Runnable demo — 2 groups, independent offsets |

## Rate Limiter — what's inside

Under `src/main/java/com/company/ratelimite/`:

- `RateLimitingStrategy` — one-method strategy interface `allowRequest(clientId)`.
- `TokenBucketStrategy` + `TokenBucket` — bursty allowance, lazy refill.
- `SlidingWindowLogStrategy` — precise, per-timestamp bookkeeping.
- `RateLimiterController` — DI façade with runtime swap-strategy support.
- `RateLimiterDemo` — shows burst behaviour + runtime strategy swap.

## Parking Lot — what's inside

Under `src/main/java/com/company/parkinglot/`:

- Models — `Vehicle`, `VehicleType`, `ParkingSpot`, `SpotType`, `ParkingFloor`,
  `ParkingLot`, `Ticket`.
- Strategies — `SpotAllocationStrategy` (impl: `NearestFirstAllocationStrategy`),
  `FeeStrategy` (impl: `HourlyFeeStrategy`), `PaymentStrategy`.
- `ParkingLotController` — synchronized entry/exit; injects the 3 strategies.
- `ParkingLotDemo` — end-to-end runnable walk-through.
