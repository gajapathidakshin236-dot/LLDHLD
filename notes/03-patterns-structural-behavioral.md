# 03 — Design Patterns: Structural & Behavioral

---

## DECORATOR — add behavior by WRAPPING (same interface)

### The disease: subclass explosion
Pizza with 6 optional toppings via inheritance = 2^6 = **64 classes**
(CheesePizza, CheeseAndPepperoniPizza, ...). Decorator: **6 classes**, stack any
combination.

### The shape (the two-part trick)
A decorator **IS-A** the interface AND **HAS-A** an instance of it:

```java
interface Pizza { String getDescription(); double getCost(); }

class PlainPizza implements Pizza {
    public String getDescription() { return "Plain pizza"; }
    public double getCost()        { return 100; }
}

abstract class PizzaDecorator implements Pizza {   // IS-A Pizza
    protected Pizza pizza;                          // HAS-A Pizza (the wrapped one)
    public PizzaDecorator(Pizza pizza) { this.pizza = pizza; }
}

class Cheese extends PizzaDecorator {
    public Cheese(Pizza p) { super(p); }
    public String getDescription() { return pizza.getDescription() + ", cheese"; }
    public double getCost()        { return pizza.getCost() + 30; }
}
```

Usage — recursive wrapping:
```java
Pizza p = new Mushrooms(new Pepperoni(new Cheese(new PlainPizza())));
p.getCost();   // 100 + 30 + 50 + 25 — each layer asks the inner one, adds its bit
```

### MISTAKE I MADE: `class PizzaDecor extends NormalPizza`
Extending a CONCRETE pizza hard-wires the decorator to that one type — you
couldn't decorate ThinCrustPizza with the same decorators. The fix that IS the
pattern: `implements Pizza` + hold a `Pizza` field -> wraps ANY pizza.
(Composition over inheritance, again.)

### Why the abstract decorator can skip implementing the interface methods
Java rule: a CONCRETE class implementing an interface must implement every
method; an **abstract class is exempt**. The unimplemented methods become
inherited ABSTRACT methods — the obligation passes down until the first
concrete subclass (Cheese) MUST pay it. The abstract base exists to share the
common plumbing (the wrapped field + constructor) and defer the varying parts.
(Same trick as `AbstractList`: implements List, leaves get()/size() abstract.)

### Real examples
Java I/O: `new BufferedReader(new InputStreamReader(new FileInputStream(f)))` —
each layer adds one capability. Spring Security filters. HTTP middleware.

### Interview line
> "Decorator adds behavior dynamically by wrapping objects that share the same
> interface; each wrapper delegates and adds its contribution. Java's buffered
> streams are the classic example. It avoids the 2^n subclass explosion."

---

## ADAPTER — make INCOMPATIBLE interfaces fit (changes the interface)

### The analogy
Travel plug adapter: Indian charger <-> US socket. Neither changes; the adapter
sits between, translating shapes.

### The shape
Adapter **implements the TARGET interface** (what your code expects), **wraps
the ADAPTEE** (the legacy/3rd-party class), and **translates** the call —
method names, parameter order, units:

```java
interface PaymentProcessor { void processPayment(double amount, String currency); }

class OldPaymentGateway {                       // can't modify (3rd-party JAR)
    void doPayment(String currencyCode, double amountInPaise) { ... }
}

class PaymentGatewayAdapter implements PaymentProcessor {
    private final OldPaymentGateway legacy;     // object adapter = COMPOSITION
    public PaymentGatewayAdapter(OldPaymentGateway legacy) { this.legacy = legacy; }
    public void processPayment(double amount, String currency) {
        legacy.doPayment(currency, amount * 100);   // reorder args, rupees->paise
    }
}
```

### Two flavors
- **Object adapter** (above): holds the adaptee — composition. **Preferred.**
- **Class adapter**: `extends` the adaptee — inheritance. Java's single
  inheritance limits it; avoid.

### Adapter vs the look-alikes
| Pattern | Intent | Interface change? |
|---|---|---|
| **Adapter** | make incompatible fit | **YES** |
| Decorator | add behavior | no (same interface) |
| Facade | simplify a complex subsystem | new simpler interface over many |
| Proxy | control access | no (same interface) |

### Real examples
`Arrays.asList(array)` (array->List), `InputStreamReader` (bytes->chars),
Spring MVC HandlerAdapter, every SDK wrapper (Stripe/Razorpay -> your domain).

### Interview line
> "Adapter wraps an incompatible class to conform to the target interface,
> translating names/params/units. Object adapter (composition) over class
> adapter (inheritance). InputStreamReader is the canonical example."

---

## OBSERVER — one-to-many notifications (pub/sub)

### The problem
Object A's state changes and many others must react — but hardcoding the list
in A means every new listener = modify A (OCP broken), tight coupling, and no
runtime join/leave.

### The shape
- **Subject** (publisher): keeps a list of observers; notifies all on change.
- **Observer** (subscriber): registers; reacts to `update(...)`.

```java
interface Observer { void update(int temperature); }

class WeatherStation {                                    // the Subject
    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    private int temperature;
    public void subscribe(Observer o)   { observers.add(o); }
    public void unsubscribe(Observer o) { observers.remove(o); }
    public void setTemperature(int t) { this.temperature = t; notifyObservers(); }
    private void notifyObservers() { for (Observer o : observers) o.update(temperature); }
}
class PhoneDisplay implements Observer { public void update(int t) { ... } }
```
Add a TVDisplay later: write the class, subscribe. Subject untouched. OCP.

### Details that score points
- **Thread-safety:** plain ArrayList risks ConcurrentModificationException if
  someone unsubscribes during a notification loop. **CopyOnWriteArrayList** is
  built for "many reads, few writes" — exactly the observer profile.
- **Push vs pull:** push sends the data in `update(t)` (simple); pull sends
  `update(this)` and observers query what they need (flexible).

### Real examples
Spring ApplicationEventPublisher/@EventListener, Kafka consumers, RxJava, DOM
event listeners. Also the natural fit for "notify customer on order status
change" in Food Delivery.

### Interview line
> "Observer decouples the subject from subscribers via a subscription list;
> new observers need no subject changes. Use CopyOnWriteArrayList for the
> listener list. Kafka consumers and Spring events are real-world versions."

---

## CHAIN OF RESPONSIBILITY — pass the request along handlers

### The analogy
Support escalation: L1 tries; can't help -> passes to L2 -> L3. You send one
request; SOMEONE in the chain handles it.

### The shape
Each handler holds a `next`; it handles the request and/or forwards it:

```java
abstract class Logger {
    public static final int INFO = 1, DEBUG = 2, ERROR = 3;
    protected int level;
    protected Logger next;
    public Logger setNext(Logger next) { this.next = next; return next; }  // returns next -> chainable
    public void log(int msgLevel, String msg) {
        if (msgLevel >= this.level) write(msg);       // handle if it's my level
        if (next != null) next.log(msgLevel, msg);    // ALWAYS forward
    }
    protected abstract void write(String msg);
}
class ConsoleLogger extends Logger { ... }   // INFO+
class FileLogger    extends Logger { ... }   // DEBUG+
class EmailLogger   extends Logger { ... }   // ERROR+
```
```java
Logger chain = new ConsoleLogger(INFO);
chain.setNext(new FileLogger(DEBUG)).setNext(new EmailLogger(ERROR));
chain.log(ERROR, "DB down!");   // console + file + email all fire
```

### Two flavors
1. **Stop at first handler** that can deal with it (support escalation, ATM
   dispenser 2000->500->100 notes).
2. **All applicable handlers run** (the logger above).
Both are valid CoR.

### Real examples
Servlet Filter chain, Spring Security filter chain, Express/Node middleware,
Log4j hierarchy.

### Interview line
> "CoR decouples sender from receiver: each handler processes and/or forwards.
> Kills giant if-else trees; handlers can be added/reordered freely. Servlet
> filters and middleware pipelines are the real-world shape."

---

## STRATEGY — swappable algorithms behind one interface

The most-used pattern, period. Used in EVERY system we built:

| System | Strategy interface | Implementations |
|---|---|---|
| Elevator | ElevatorSelectionStrategy | DirectionalScan (SCAN/LOOK), nearest, least-busy |
| Parking Lot | SpotAllocationStrategy / FeeStrategy / PaymentStrategy | nearest-first / hourly / UPI-Card-Cash |
| Rate Limiter | RateLimitingStrategy | TokenBucket, SlidingWindowLog |
| Kafka | Partitioner | KeyHash, RoundRobin |
| Food Delivery | AgentAssignmentStrategy / DeliveryFeeStrategy | nearest / distance-based |

### The shape
```java
interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
class Controller {
    private ElevatorSelectionStrategy strategy;                    // NOT final ->
    public void setStrategy(ElevatorSelectionStrategy s) { ... }   // runtime swap
}
```
Why interface (not abstract class): pure behavior, no shared state; DIP; a class
can implement many interfaces.

### The chess insight (why Strategy > inheritance for behavior)
Pawn promotion: with inheritance you'd replace the Pawn OBJECT with a Queen
object and fix all references. With Strategy you swap the BEHAVIOR on the same
object: `pawn.setMovement(new QueenMovement());` — same identity, new behavior.
That runtime swap is what Strategy buys and inheritance can't.

### Interview line
> "Strategy encapsulates interchangeable algorithms behind one interface,
> selected/injected at runtime. Every 'different ways to do X' — dispatch,
> pricing, partitioning — is a Strategy."

---

## STATE — behavior changes with internal state

### Two implementations (know both + when)

**A) Enum + allowed-transitions map** (lighter — used in Food Delivery):
states mainly gate WHICH transitions are legal.
```java
enum OrderStatus {
    PLACED, CONFIRMED, ..., DELIVERED, CANCELLED;
    private Set<OrderStatus> allowedNext;
    static {                                     // wire the legal graph
        PLACED.allowedNext = Set.of(CONFIRMED, CANCELLED);
        ...
        DELIVERED.allowedNext = Set.of();        // terminal: empty set = no way out
    }
    boolean canTransitionTo(OrderStatus next) { return allowedNext.contains(next); }
}
// in Order:
void transitionTo(OrderStatus next) {
    if (!status.canTransitionTo(next)) throw new IllegalStateException(...);
    this.status = next;
}
```
Guards BOTH failure modes: no skipping (PLACED->DELIVERED blocked) and no going
back from terminal (DELIVERED->PREPARING blocked).

**B) Full State pattern** (each state = a class): when states have RICH,
DIFFERENT behavior, not just legal-transition gating.
```java
interface OrderState { void next(Order o); void cancel(Order o); }
class OutForDeliveryState implements OrderState {
    public void cancel(Order o) { throw new IllegalStateException("too late"); }
    public void next(Order o)   { o.setState(new DeliveredState()); }
}
```
The rule "can't cancel out-for-delivery" LIVES INSIDE OutForDeliveryState.cancel
— behavior in the state, no central if-else.

### The judgment call (senior signal)
> "For state machines where states mainly gate transitions, enum + transition
> map is the right weight. Full State pattern earns its class-per-state cost
> when each state behaves substantially differently."

---

## TEMPLATE METHOD — fixed skeleton, subclass fills the steps

### The shape
Base class defines the algorithm's ORDER (the template method, marked `final`
so subclasses can't reorder it); varying steps are abstract:

```java
abstract class Beverage {
    public final void prepare() {          // final: skeleton is locked
        boilWater();        // shared, concrete
        brew();             // varies -> abstract
        pourInCup();        // shared
        addCondiments();    // varies -> abstract
    }
    private void boilWater() { ... }
    private void pourInCup() { ... }
    protected abstract void brew();
    protected abstract void addCondiments();
}
class Tea extends Beverage    { brew() = steep;  addCondiments() = lemon; }
class Coffee extends Beverage { brew() = drip;   addCondiments() = milk+sugar; }
```
Optional **hook methods**: concrete methods with default bodies subclasses MAY
override (e.g., `boolean customerWantsCondiments() { return true; }`).

### Where you've already used it without naming it
- **JdbcTemplate**: Spring owns connection open/close/exception translation
  (skeleton); you supply the SQL + row mapping (steps).
- **AbstractList**: implements iterator/contains/indexOf; you supply get()+size().
- **HttpServlet.service()** routes to your doGet/doPost.
- **JUnit** lifecycle: @BeforeEach -> your test -> @AfterEach.
"The framework calls your code at the right time" = usually Template Method.

### Template Method vs Strategy (classic confusion)
| Template Method | Strategy |
|---|---|
| INHERITANCE — subclass overrides steps | COMPOSITION — inject algorithm object |
| Fixes the skeleton, varies steps | Swaps the whole algorithm |
| Locked at compile time | Swappable at runtime |
| "same recipe, different ingredients" | "different recipe, plugged in" |

Need runtime swap -> Strategy. Fixed structure, varying steps -> Template Method.

### Interview line
> "Template Method defines an algorithm skeleton in a base class — the template
> is final; abstract steps are deferred to subclasses. JdbcTemplate is the
> canonical Spring example."

---

## RAPID-FIRE: the rest of GoF (name + 1-line use case)
- **Prototype** — clone an existing object instead of building from scratch.
- **Facade** — one simple interface over a messy subsystem (e.g., `VideoConverter.convert()` hiding codecs).
- **Proxy** — a stand-in controlling access: lazy loading, caching, auth.
  **Spring AOP is dynamic proxies** — @Transactional/@Cacheable work this way.
- **Composite** — tree of objects treated uniformly (folders & files both "FileSystemNode").
- **Bridge** — split abstraction from implementation so both vary independently.
- **Flyweight** — share heavy immutable objects to save memory (glyphs, Integer cache).
- **Command** — encapsulate a request as an object (undo/redo, task queues).
- **Iterator** — sequential access without exposing internals (java.util.Iterator).
- **Mediator** — central hub coordinating peers (air-traffic control; chatroom).
- **Memento** — snapshot & restore state (undo).
- **Visitor** — add operations to a class family without modifying it.
- **Interpreter** — grammar evaluator; rarely asked, skip.

### Most-used in the wild (ranked)
Strategy, Factory, Singleton, Observer, Builder — the big 5 (all covered deep).
Then: Decorator, Template Method, Proxy (Spring!), Adapter, State, CoR.
