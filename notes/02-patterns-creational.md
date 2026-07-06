# 02 — Design Patterns: Creational (Singleton, Factory family, Builder)

A **design pattern** = a named, reusable solution to a recurring design problem.
Not code you copy — a *shape* you recognize and apply.

> This file merges the session notes on all three creational patterns with the
> full **Factory Method deep dive** (§B below), which pairs with runnable code
> at `src/main/java/com/company/factorymethod/`.

---

## SINGLETON — "exactly one instance, globally accessible"

### First: what is an "instance"?
A **class** is a blueprint. An **instance** is a real object built from it,
living in memory. `new Dog()` three times = three instances. **Singleton** = a
class designed so only ONE instance can ever exist; everyone shares it.

Why: one shared log file, one config, one connection pool. Copies would each
have their own state, defeating the point.

### The universal shape
```
private static <field to hold the one instance>
private constructor                  (nobody else can `new` it)
public static getInstance()          (the single access point)
```

`private static Logger instance;` = a SLOT at class level (static = one copy for
the whole class, not per object). `if (instance == null)` asks: "is the slot
still empty? if so build the object and fill it; else return what's there."

### The 5 levels (each fixes the previous one's flaw)

**Level 1 — Basic (lazy, NOT thread-safe)**
```java
public static Logger getInstance() {
    if (instance == null) instance = new Logger();   // two threads can BOTH see null
    return instance;                                  // -> two instances. BUG.
}
```

**Level 2 — synchronized method (safe, slow)**
```java
public static synchronized Logger getInstance() { ... same body ... }
```
`synchronized` on a static method locks on `Logger.class` — one thread at a time
in the whole JVM. Correct, but EVERY call locks forever, even after the instance
exists. Fine for low-traffic (config at startup), bad on hot paths.

**Level 3 — Double-Checked Locking (DCL)**
```java
private static volatile Logger instance;      // volatile is REQUIRED
public static Logger getInstance() {
    if (instance == null) {                    // check 1: no lock, fast path
        synchronized (Logger.class) {
            if (instance == null) {            // check 2: inside lock
                instance = new Logger();
            }
        }
    }
    return instance;
}
```
- Check 1 skips the lock once the instance exists (fast).
- Check 2 handles two threads that both passed check 1 before either locked.
- **Why volatile:** `new Logger()` is really (1) allocate (2) initialize
  (3) assign reference. The JVM may reorder 1->3->2; another thread could then
  see a non-null but HALF-BUILT object. `volatile` forbids that reordering.
  Classic interview gotcha.

**Level 4 — Bill Pugh / static holder (the recommended default)**
```java
public class ConfigManager {
    private ConfigManager() { System.out.println("Loading config..."); }

    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }
    public static ConfigManager getInstance() { return Holder.INSTANCE; }
}
```
Why it works — TWO JVM guarantees:
1. A nested class is loaded only when FIRST REFERENCED -> `Holder` (and hence
   INSTANCE) isn't created until `getInstance()` is first called = **lazy**.
2. Class initialization is **thread-safe and exactly-once by JVM spec** — if 10
   threads hit it simultaneously, the JVM picks one to initialize; others wait.
So: lazy + thread-safe with ZERO synchronization code. `Holder` is just a
private drawer whose only job is to hold the instance. There is NO `if
(instance == null)` in Bill Pugh — class loading IS the null check.

MISTAKE I MADE FIRST TIME: adding an `if (instance == null)` inside Holder and
mixing Level 2's style into Level 4. They are different mechanisms — Bill Pugh
has no check, no method on Holder, just one static final field.

Also: `static` cannot go on a TOP-LEVEL class (`class static X` won't compile).
It's valid on nested classes, fields, methods, and static blocks.

**Level 5 — Enum singleton (bulletproof)**
```java
public enum Logger { INSTANCE; public void log(String m) { ... } }
```
Thread-safe, serialization-safe, and REFLECTION-PROOF (reflection can break
levels 1-4 by force-calling the private constructor via setAccessible; enums
reject that). Downside: can't extend another class.

### Comparison table
| Approach | Lazy | Thread-safe | Reflection-safe | Verdict |
|---|---|---|---|---|
| Eager static final | no | yes | no | fine if cheap to build |
| synchronized method | yes | yes | no | slow on hot paths |
| DCL + volatile | yes | yes | no | classic, valid |
| Bill Pugh holder | yes | yes | no | **best default** |
| Enum | yes | yes | yes | **bulletproof** |

### Singleton in PRODUCTION — why it's often the wrong tool
1. **Concurrency** — a singleton holding mutable state is shared by all threads;
   coarse locking kills throughput, fine-grained locking is complex.
2. **Testability** — global state leaks between tests; `X.getInstance()`
   hardcoded means you can't inject a mock.
3. **Hidden coupling** — ANY code anywhere can grab it and mutate it;
   dependencies become invisible.
4. **Doesn't scale horizontally** — each JVM behind a load balancer has its OWN
   singleton; "one instance" becomes a lie. State must move to Redis/DB anyway.
5. Lifecycle: can't have two later (second parking lot!), can't easily reset.

**The modern replacement:** ONE instance managed by DI (Spring bean, singleton
scope) — same effect, but injected, testable, explicit. GoF Singleton is fine
only for genuinely stateless things: config, loggers, connection pools.

**Two-question test before using Singleton:**
1. Mutable per-user/request state? YES -> don't.
2. Would I want to mock it in tests? YES -> don't (use DI).

### Interview lines
> "I'd use Bill Pugh's holder — lazy and thread-safe via JVM class-loading
> guarantees, no synchronization — or enum if I need reflection safety."
> "In production I'd prefer a DI-managed single instance: hand-rolled singletons
> hurt concurrency, testability, and don't survive horizontal scaling."

---

# THE FACTORY FAMILY

## A. Overview — Simple Factory vs Factory Method vs Abstract Factory

### The disease all three cure
```java
if (type.equals("EMAIL")) { new EmailNotification().send(m); }
else if (type.equals("SMS")) { new SMSNotification().send(m); }
// business logic full of `new` + if-else. Every new type = MODIFY this class
// (OCP violated), tight coupling to every concrete class, untestable.
```
Cure: **"Don't ask `new` — ask a factory."** Creation moves out of business logic.

### 1) Simple Factory (most common in practice; not officially GoF)
```java
class NotificationFactory {
    public Notification create(String type) {
        switch (type) {
            case "EMAIL": return new EmailNotification();
            case "SMS":   return new SMSNotification();
            default: throw new IllegalArgumentException("Unknown: " + type);
        }
    }
}
class NotificationService {
    private final NotificationFactory factory;     // injected
    public void send(String type, String msg) {
        factory.create(type).send(msg);            // knows only the INTERFACE
    }
}
```
The switch didn't disappear — it moved to ONE isolated place. Business logic is
clean; adding a type = new class + one factory line.

### 2) Factory Method (the real GoF pattern — INHERITANCE decides)
The "factory" is an overridable METHOD in a base class. The base defines the
ALGORITHM; subclasses decide which product it uses.
```java
abstract class CheckoutFlow {
    public void checkout(double amount) {
        Payment p = createPayment();      // <- the factory method
        p.pay(amount);
    }
    protected abstract Payment createPayment();   // subclasses decide
}
class UPICheckout  extends CheckoutFlow { protected Payment createPayment() { return new UPIPayment(); } }
class CardCheckout extends CheckoutFlow { protected Payment createPayment() { return new CardPayment(); } }
```
Real examples: `Collection.iterator()`, Spring `BeanFactory`, servlet lifecycle.

(Full deep dive on this pattern in §B below.)

### 3) Abstract Factory (creates a FAMILY that must match)
Multiple product types that vary TOGETHER. Cross-platform UI: a Windows button
must pair with a Windows checkbox, never a Mac one.
```java
interface UIFactory { Button createButton(); Checkbox createCheckbox(); }
class WindowsUIFactory implements UIFactory { ...windows versions... }
class MacUIFactory     implements UIFactory { ...mac versions... }
```
Real example: JDBC — one driver produces matched Connection + Statement +
ResultSet. DocumentBuilderFactory.

### THE KILLER TEST (this fixed my interview blind spot)
> **Step 1: COUNT THE PRODUCT TYPES being created.**
>   - ONE product type -> Simple Factory or Factory Method.
>   - MULTIPLE types that must match as a set -> **Abstract Factory**.
> **Step 2 (if one type): who decides the concrete?**
>   - A switch on input in one class -> **Simple Factory**.
>   - A subclass overriding a create method -> **Factory Method**.

Do NOT count subclasses — count PRODUCT TYPES. "Many subclasses" is just
polymorphism, not Abstract Factory.

Worked classifications:
- Level subclasses each override createEnemies() -> ONE product (Enemy),
  subclass decides -> **Factory Method**.
- MySQL vs Postgres each with matched Connection/Statement/ResultSet -> THREE
  products varying together -> **Abstract Factory**.
- Dropdown value -> one method returns matching processor -> **Simple Factory**.

### Interview line
> "Factory Method creates ONE product with inheritance choosing the concrete
> type; Abstract Factory creates a FAMILY of matched products. Quick test:
> count the product types. JDBC drivers are the canonical Abstract Factory."

---

## B. Factory Method — the full deep dive

> Pairs with runnable code at `src/main/java/com/company/factorymethod/`.
>
> Why this got its own deep dive: the repo already had **Abstract Factory** in
> `src/main/java/com/company/abstractfactory/`, but **Factory Method** was
> missing — and Factory Method is the smaller, simpler pattern that Abstract
> Factory is *built on top of*. People mix them up constantly. Getting this one
> solid makes the difference clean forever.

### B.1 What is Factory Method, in one sentence?

> **Factory Method** is a creational pattern in which a class **defines a method whose return type is an interface or abstract class**, and **subclasses decide which concrete class to instantiate**.

In other words: the parent class says *"someone is going to create a `Transport`, but I'm not going to commit to which kind. My subclasses will."*

That's it. One method, returns an abstraction, gets overridden by subclasses. The pattern is genuinely that small. Everything else in this section is *why* that tiny structure is worth giving a name.

### B.2 The problem it solves

#### The motivating scenario

You're building a **logistics management app**. Originally it only handles **road logistics** with trucks. The core method looks like this:

```java
public class Logistics {

    public void planDelivery() {
        Truck truck = new Truck();
        truck.deliver();
    }
}
```

This is fine. It works. It ships.

Then the requirement changes: the company expands into **sea logistics**. You need to also deliver with `Ship` objects.

#### The naive fix (don't do this)

You're tempted to add an `if`:

```java
public class Logistics {

    public void planDelivery(String mode) {
        if (mode.equals("ROAD")) {
            Truck truck = new Truck();
            truck.deliver();
        } else if (mode.equals("SEA")) {
            Ship ship = new Ship();
            ship.deliver();
        }
    }
}
```

Four problems with this code:

1. **It violates OCP (Open/Closed Principle).** Every new transport type (`Train`, `Drone`, `Plane`) forces you to *edit* `Logistics`. The class is not closed against change.
2. **It violates DIP (Dependency Inversion Principle).** `Logistics` (high-level policy) directly depends on `Truck` and `Ship` (low-level concrete classes).
3. **`if` chains never stay at one level.** Soon the chain forks: "if SEA and the package weighs more than 1 ton, use FreightShip; otherwise use Speedboat." The chain becomes a maze.
4. **Hard to test.** You can't substitute a fake `Truck` without mocking the `new` operator (which Java makes painful).

#### The Factory Method fix

You **don't decide** which transport to build inside `Logistics`. Instead, you **declare** that *someone* will build one, and you **defer the decision** to a subclass:

```java
public abstract class Logistics {

    // The factory method. Subclasses override it.
    protected abstract Transport createTransport();

    // The "business logic" that uses the product.
    public void planDelivery() {
        Transport t = createTransport();   // subclass decides which concrete type
        t.deliver();
    }
}
```

Two concrete creators, each picking a concrete product:

```java
public class RoadLogistics extends Logistics {
    @Override
    protected Transport createTransport() {
        return new Truck();
    }
}

public class SeaLogistics extends Logistics {
    @Override
    protected Transport createTransport() {
        return new Ship();
    }
}
```

That's the entire pattern. Adding a `Drone` later means writing one new class (`AirLogistics extends Logistics`). You **don't touch** `Logistics` again. **OCP satisfied.**

### B.3 GoF participants (the vocabulary you'll need in interviews)

The original *Design Patterns* book names four roles:

| Role                 | What it does                                                                                                                                                  | In our example       |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------- |
| **Product**          | The interface (or abstract class) of objects the factory method creates. The thing the *client of the creator* will actually use.                            | `Transport`          |
| **ConcreteProduct**  | A concrete implementation of `Product`.                                                                                                                       | `Truck`, `Ship`      |
| **Creator**          | Declares the factory method (returning `Product`). Usually also contains the business logic that *uses* the product. May provide a default implementation.    | `Logistics`          |
| **ConcreteCreator**  | Overrides the factory method to return a specific `ConcreteProduct`.                                                                                          | `RoadLogistics`, `SeaLogistics` |

A picture:

```
        ┌──────────────────────┐                ┌──────────────┐
        │      Logistics       │                │  Transport   │  (Product)
        │  + planDelivery()    │                │ + deliver()  │
        │  # createTransport() │ ─────uses────► └──────┬───────┘
        └──────────┬───────────┘                       │
                   │                                   │
       ┌───────────┴────────────┐               ┌──────┴──────┐
       ▼                        ▼               ▼             ▼
┌────────────────┐     ┌────────────────┐   ┌───────┐    ┌──────┐
│ RoadLogistics  │     │  SeaLogistics  │   │ Truck │    │ Ship │
│ createTransport│     │ createTransport│   └───────┘    └──────┘
└────────────────┘     └────────────────┘  (ConcreteProducts)
   (ConcreteCreators)
```

The two hierarchies are **parallel**. That parallel structure is the signature of Factory Method — when you see it on a whiteboard, this is the pattern.

### B.4 Why bother — what does this buy you?

Concretely:

- **Open/Closed.** New product type = new pair of classes. No edits to existing classes.
- **Single Responsibility.** `Logistics` no longer knows how to build a `Truck`; it just knows what to do with a `Transport`.
- **Testability.** In a unit test you can write a `FakeLogistics extends Logistics` that returns a stub `Transport`. No mocks of constructors needed.
- **Decoupling.** Client code only ever references `Transport` and `Logistics`. The concrete classes `Truck`/`Ship` are touched only at the boundaries.
- **Easier to evolve product families together.** If `Truck` needs an extra field for a road-permit, you change `Truck` + `RoadLogistics` — `SeaLogistics` doesn't know or care.

### B.5 What it costs

Patterns are never free. Factory Method's price:

- **More classes.** Two new classes per product type. For one product, this is over-engineering.
- **Forces inheritance.** You must subclass the creator to get a new product. If subclassing is awkward (e.g. you already have a deep hierarchy), this is friction. *Composition* — passing in a factory object — would be more flexible. That's the Strategy / "parameterized factory" pattern.
- **Indirection cost.** A new reader of the code can't go from `planDelivery()` to `Truck` in one click — they have to know which `ConcreteCreator` is in play at runtime.

If you have **only one product type, ever**, do NOT use this pattern. A plain constructor or a `new` call is fine. Reach for Factory Method when you *expect at least two* concrete products and you want adding a third to be cheap.

### B.6 Variations you'll meet in the wild

#### B.6.1 Factory Method with a default implementation

The creator gives a default product, and subclasses override only if they want something else:

```java
public abstract class Logistics {

    protected Transport createTransport() {
        return new Truck();   // sensible default
    }

    public void planDelivery() { createTransport().deliver(); }
}
```

Useful when 80% of subclasses want the same thing.

#### B.6.2 Parameterised Factory Method

The factory method takes an argument that picks between a small, *known* set of products:

```java
protected Transport createTransport(TransportType type) {
    return switch (type) {
        case ROAD -> new Truck();
        case SEA  -> new Ship();
        case AIR  -> new Drone();
    };
}
```

This is the spot where Factory Method **edges into Simple Factory** — see B.7. The line is: if subclasses still override it, it's Factory Method; if there are no subclasses and it's just a static helper on a `TransportFactory` class, it's "Simple Factory" (which is not actually a GoF pattern).

#### B.6.3 Static Factory Methods (Joshua Bloch, *Effective Java*)

`Item 1` of *Effective Java* recommends **static factory methods** instead of constructors. Different idea, same family. Examples in the JDK:

```java
Integer.valueOf(42);            // can cache!
List.of("a", "b", "c");         // returns an immutable List
Optional.of(x);                 // names what it does
Calendar.getInstance();         // returns the right subclass for the locale
```

Why use a static factory method instead of `new`?

1. **They have names.** `BigInteger.probablePrime(...)` reads better than `new BigInteger(int, int, Random)`.
2. **They can cache.** `Integer.valueOf(127)` returns a cached instance.
3. **They can return a subtype.** `Calendar.getInstance()` may return `GregorianCalendar` or `BuddhistCalendar` depending on locale — and you, the caller, never have to care.
4. **They centralise validation.** Throw early, throw in one place.

These are NOT GoF Factory Method (which requires inheritance). But they solve the same family of problems and you'll see them called "factory methods" colloquially. Be precise about which you mean.

### B.7 Factory Method vs. Abstract Factory vs. "Simple Factory"

This is the part everyone gets wrong. Memorise this table.

| Feature                        | Simple Factory                                  | Factory Method                                                | Abstract Factory                                                        |
| ------------------------------ | ----------------------------------------------- | ------------------------------------------------------------- | ----------------------------------------------------------------------- |
| Is it in the GoF book?         | No (folk pattern)                               | Yes                                                           | Yes                                                                     |
| Mechanism                      | A static method on a factory class              | A method overridden by subclasses                             | An object with multiple factory methods, swapped at runtime             |
| Creates **one** product?       | Yes                                             | Yes                                                           | No — creates a **family** of related products                           |
| Adds new product type by…      | Editing the `switch`                            | Adding a new ConcreteCreator subclass                         | Adding a new factory class implementing the abstract factory interface  |
| Uses inheritance?              | No                                              | Yes (subclass overrides)                                      | Yes (the factory hierarchy) — and usually contains Factory Methods inside |
| Your repo example              | n/a                                             | `com.company.factorymethod`                                   | `com.company.abstractfactory`                                           |

The short version, memorise this:

- **Factory Method** = ONE method, ONE product, decided by inheritance.
- **Abstract Factory** = ONE object, MANY products that go together, swapped at runtime.

Or even shorter: *Abstract Factory is a bunch of Factory Methods bundled into one object so the products stay matched.*

In your `abstractfactory` package, `GUIFactory#createButton` and `GUIFactory#createCheckbox` are both factory methods. The reason `GUIFactory` exists is to ensure you get a matched `WindowsButton` + `WindowsCheckbox`, never a `WindowsButton` + `MacCheckbox`. That's the "family" guarantee that Factory Method alone doesn't give you.

### B.8 Factory Method in the JDK (the ones you've already touched without noticing)

You don't need to memorise these, but recognising them in your own code is the goal.

- `java.util.Calendar#getInstance()` — returns whatever Calendar subtype matches the locale.
- `java.text.NumberFormat#getInstance()` — same idea, returns a locale-appropriate `NumberFormat`.
- `java.nio.file.Path#of(String, String...)` — returns a `Path` whose concrete class depends on the underlying filesystem.
- `java.util.Iterator` returned by `Collection#iterator()` — the collection IS the creator; each implementation overrides `iterator()` to return its own iterator type. **This is textbook Factory Method.**
- `java.net.URLStreamHandlerFactory` — pluggable, lets you teach the JVM how to handle new URL schemes.

When you see a method named `getInstance`, `of`, `from`, `newInstance`, `create*`, or `valueOf` returning an interface or abstract type — start asking whether it's a Factory Method (subclass overrides) or a Static Factory (no overriding).

### B.9 Where Factory Method goes wrong (the edge cases)

1. **You don't actually have two product types.** Then you're paying the indirection tax for nothing. Just `new` it.
2. **The "creator" has nothing to do besides creating.** Then it's not really a creator — it's just a factory class, and you probably want Abstract Factory or a static factory method.
3. **You need to swap the factory at runtime per request.** Inheritance freezes the choice at compile-time. Composition (Strategy) is the right answer.
4. **The product needs lots of construction parameters.** Factory Method by itself produces a single instance with no parameters. Combine with **Builder** for complex construction.
5. **The concrete creator subclasses need shared state.** Then your "creator hierarchy" is doing two jobs (creating + state-holding). Split them.
6. **Two-level inheritance — creator subclasses also have their own variants.** This is where Factory Method's inheritance bites. Refactor toward composition.
7. **`createTransport()` is `public`.** It usually shouldn't be. The factory method is an *internal* extension point, not part of the creator's public API. Mark it `protected` (`abstract`). Clients call `planDelivery()`, not `createTransport()`.

### B.10 When NOT to use Factory Method

- You only have one concrete product and no realistic expectation of more.
- The product is a pure value object with no behaviour — just use a constructor.
- The choice of product depends on data that's only available at the call site — pass a factory in (composition / Strategy), don't subclass.
- You're using a DI container (Spring, Guice) — let the container handle "which concrete type" via configuration. Factory Method in that context is usually a code smell.

### B.11 Comparing with the other creational patterns in this repo

| Pattern             | Question it answers                                                | Returns                       |
| ------------------- | ------------------------------------------------------------------ | ----------------------------- |
| **Singleton**       | "How do I ensure exactly one instance lives in the JVM?"           | The one instance              |
| **Factory Method**  | "Which concrete subclass should I instantiate, decided by a subclass override?" | One product                   |
| **Abstract Factory**| "How do I create a *family* of related products, swappable as a set?" | A factory; the factory makes products |
| **Builder**         | "How do I assemble a complex object step by step?"                 | A fully-built product after fluent calls |
| **Prototype**       | "How do I make a copy of an existing object as the construction strategy?" | A clone of an existing object  |

Notice: **Abstract Factory uses Factory Method internally**. Builder and Factory Method are orthogonal — you'll sometimes see a Factory Method that returns a Builder.

### B.12 Exercises

(Do at least 1, 2, 3. They each take ~10 minutes.)

1. Open `com.company.factorymethod` and trace what happens when `Main` runs. Write the call sequence on paper: `Main → ? → ? → ? → Truck.deliver()`. Don't peek.
2. Add a `Drone` `Transport` and an `AirLogistics extends Logistics` that creates it. Confirm you did NOT have to edit `Logistics`, `Truck`, `Ship`, `RoadLogistics`, or `SeaLogistics`. That is the OCP win, on display.
3. Refactor `Logistics#planDelivery()` so it returns a tracking-id string instead of `void`. The factory method shape should not need to change. Notice how the *business logic* and the *creation logic* evolve independently — that's the SRP win.
4. (Harder.) Convert this from inheritance-based Factory Method to **composition-based** by introducing a `TransportFactory` interface that `Logistics` holds as a field. The result is closer to Strategy / Dependency Injection. Write 3 lines comparing the two approaches: when would you prefer each?
5. Find one example of Factory Method in your existing `abstractfactory` package. Hint: every method on `GUIFactory` is one. Write down which methods are the factory methods, what the Product is for each, and what role `WindowsFactory` plays in GoF terms.
6. Open `java.util.Calendar` source (Cmd-click in IntelliJ). Read `getInstance(...)`. Decide whether you'd call it Factory Method or Static Factory Method (Bloch-style). Defend your answer.
7. (Senior-track.) The Spring `BeanFactory` looks like a factory but is closer to Service Locator. In one paragraph: how is `BeanFactory#getBean(Class)` different from a Factory Method, and why does that matter for testability?

### B.13 Recap (one screen)

- Factory Method = method returning an interface, subclasses pick the concrete class.
- Four GoF roles: Product, ConcreteProduct, Creator, ConcreteCreator.
- Use it when you have ≥2 concrete products and want adding a third to be cheap.
- It's the building block; **Abstract Factory** is a bundle of Factory Methods that keeps a *family* of products matched.
- *Static factory methods* (Bloch) are a different beast — same family, no inheritance.
- Don't use it for one-product cases, for value objects, or when DI containers already pick concrete types for you.

---

## BUILDER — step-by-step construction of complex objects

### The two diseases
**Telescoping constructors:**
```java
new Pizza("Large","Thin",true,false,true,false,true,false,true,false);
// what is the 7th boolean?? swap two and the compiler won't notice.
```
**JavaBeans setters:** object is mutable, can exist half-built between setters,
no way to enforce required fields.

### The pattern
```java
public class User {
    private final String name;                 // ALL fields final -> immutable
    private final String email; private final int age;

    private User(Builder b) {                  // PRIVATE ctor: only Builder builds
        this.name = b.name; this.email = b.email; this.age = b.age;
    }

    public static class Builder {              // static nested
        private final String name;             // REQUIRED -> builder ctor
        private String email = "N/A";          // optional -> defaulted
        private int age = 0;

        public Builder(String name) { this.name = name; }
        public Builder email(String e) { this.email = e; return this; }  // return this
        public Builder age(int a)      { this.age = a;   return this; }  // = chaining

        public User build() {                  // THE validation gate
            if (name == null || name.isEmpty()) throw new IllegalStateException("name required");
            if (age < 0) throw new IllegalStateException("age >= 0");
            return new User(this);
        }
    }
}
// Usage:
User u = new User.Builder("Alice").email("a@x.com").age(28).build();
```

### The mechanics that matter
- **`return this`** in each setter is what enables the fluent chain — each call
  returns the Builder itself, so the next call attaches on.
- **`build()` is the single validation gate** — required fields + cross-field
  rules checked in ONE place, before the object can exist.
- **Immutability**: final fields, no setters on the product, private ctor.
- Required = builder constructor args; optional = chained methods with defaults.

### When to use / not
Use: 4+ params, many optional, want immutability + validation.
Skip: simple 2-param objects (overkill). Real work: Lombok `@Builder` generates
all this — know the manual version for interviews.

### Builder vs Factory
Factory decides **WHAT type** to make (one call). Builder decides **HOW to
configure** one complex object (many calls then build()).

### Real examples
StringBuilder, HttpRequest.newBuilder(), every AWS SDK client, Stream.builder().

### Interview line
> "Builder solves telescoping constructors: fluent API, immutable product,
> validation centralized in build(). StringBuilder and HttpRequest.Builder are
> the canonical examples."
