# 12 — Design Patterns: the remaining Structural set (+ Prototype)

> Completes the pattern coverage started in `02-patterns-creational.md` and
> `03-patterns-structural-behavioral.md`. Those files went deep on Singleton,
> Factory family, Builder, Decorator, Adapter, Observer, Chain of
> Responsibility, Strategy, State, Template Method. This file goes deep on the
> ones that were only rapid-fire before: **Prototype** (the creational
> leftover), then **Proxy, Facade, Composite, Bridge, Flyweight**.
>
> Runnable code for every pattern here:
> `src/main/java/com/company/prototype/` (already existed) and
> `src/main/java/com/company/patterns/{proxy,facade,composite,bridge,flyweight}/`.

---

## PROTOTYPE — create by COPYING an existing object

### The problem it solves
Sometimes building an object from scratch is expensive or annoying:
- construction needs a DB call / file parse / heavy computation, OR
- the object has 20 fields and you want "the same as that one, but with X changed".

Instead of re-running construction, **clone a pre-built instance** and tweak it.

### The shape
```java
public interface Prototype<T> {
    T copy();                       // NOT Java's Cloneable — see gotcha below
}

public class Circle implements Prototype<Circle> {
    private int x, y, radius;
    public Circle(Circle source) {              // COPY CONSTRUCTOR does the work
        this.x = source.x; this.y = source.y; this.radius = source.radius;
    }
    @Override public Circle copy() { return new Circle(this); }
}
```

### A registry of ready-made prototypes
The pattern's classic companion: preload configured instances, hand out copies.

```java
class PrototypeRegistry {
    private final Map<String, Prototype<?>> prototypes = new HashMap<>();
    void register(String key, Prototype<?> p) { prototypes.put(key, p); }
    Object create(String key) { return prototypes.get(key).copy(); }
}
// registry.register("default-invoice", fullyConfiguredInvoice);
// Invoice inv = (Invoice) registry.create("default-invoice");  // instant
```
(Your repo's `com/company/prototype/` has exactly this: Shape/Circle/Rectangle
+ PrototypeRegistry.)

### THE gotcha: deep vs shallow copy
- **Shallow copy** = copy the fields as-is. Primitives are fine; object fields
  copy the REFERENCE — both copies now share the same inner object. Mutate the
  list in the clone and the original changes too. Classic bug.
- **Deep copy** = also copy the objects the fields point to (recursively, or at
  least the MUTABLE ones).

```java
public Order(Order src) {
    this.id = src.id;                                  // primitive: fine
    this.items = new ArrayList<>(src.items);           // NEW list (shallow inner!)
    this.address = new Address(src.address);           // deep-copy mutable object
    this.status = src.status;                          // enum: immutable, share ok
}
```
Rule: **immutable fields (String, enums, LocalDate, wrappers) can be shared;
mutable fields (collections, your own mutable classes) must be copied.**

### Why copy constructors beat `Cloneable`
Java's built-in `clone()`/`Cloneable` is widely considered broken (Effective
Java Item 13): `clone()` skips constructors, `Cloneable` is a marker interface
with weird semantics, checked `CloneNotSupportedException`, and default clone is
shallow. **Prefer a copy constructor or a `copy()` method you write yourself.**

### When to use / not
Use: expensive construction; "same but slightly different" objects; registry of
pre-configured templates. Skip: cheap objects (just `new`), or when a Builder
communicates the variations more clearly.

### Interview line
> "Prototype creates objects by copying a pre-built instance — copy constructor
> over Cloneable. The interview trap is shallow vs deep: immutables can be
> shared, mutable fields must be copied."

---

## PROXY — same interface, but CONTROLS ACCESS to the real object

### The idea
A stand-in object that implements the SAME interface as the real one and sits
in front of it. The client can't tell the difference. The proxy decides
if/when/how the real object is reached.

### The three flavors that matter

**1) Virtual proxy — lazy loading.** Real object is expensive; don't build it
until first use.
```java
class ImageProxy implements Image {
    private final String filename;
    private RealImage real;                       // null until needed
    public void display() {
        if (real == null) real = new RealImage(filename);   // first use only
        real.display();
    }
}
```
Hibernate does exactly this: `order.getCustomer()` returns a proxy; the SQL
runs only when you touch a field (and throws LazyInitializationException if
the session is gone — now you know why).

**2) Protection proxy — permission gate.**
```java
class SecureDocument implements Document {
    public void read(String user) {
        if (!allowed.contains(user)) throw new SecurityException(...);
        target.read(user);                        // only then delegate
    }
}
```
The real document stays permission-free (SRP: access rules live in one place).

**3) Remote proxy — local stub for a remote object.** gRPC/Feign client stubs:
you call a method; the proxy serializes it into a network call. The interface
hides the network.

### Proxy vs Decorator (the classic confusion)
IDENTICAL shape (implement interface + wrap instance). Different INTENT:

| | Decorator | Proxy |
|---|---|---|
| Purpose | ADD behavior | CONTROL access |
| Who stacks them | client stacks many freely | usually ONE proxy, often created by infrastructure |
| Example | milk on coffee | lazy-load, auth check, remote stub |

Say the intent out loud in an interview and you're done: "same shape, decorator
adds, proxy guards."

### Real examples
Spring AOP / `@Transactional` (Spring wraps your bean in a proxy that opens the
transaction), Hibernate lazy loading, gRPC stubs, `java.lang.reflect.Proxy`,
Mockito mocks.

### Interview line
> "Proxy implements the subject's interface and controls access — lazy init,
> permissions, remoting. Same shape as Decorator; different intent. Spring's
> @Transactional is a proxy around your bean."

### Code
`src/main/java/com/company/patterns/proxy/ProxyDemo.java` — virtual +
protection proxies, runnable.

---

## FACADE — one simple front door to a messy subsystem

### The problem
Using a subsystem correctly takes six calls in the right order with the right
settings (amp on, volume 7, projector on, widescreen, lights 10%...). Every
client repeats that choreography, and every client breaks when it changes.

### The shape
```java
class HomeTheaterFacade {
    private final Amplifier amp; private final Projector projector; private final Lights lights;
    void watchMovie(String movie) {
        lights.dim(10); projector.on(); projector.wideScreenMode();
        amp.on(); amp.setVolume(7);
    }
    void endMovie() { amp.off(); projector.off(); lights.full(); }
}
```
Client: `theater.watchMovie("Interstellar")` — one call.

### The three rules that make it a Facade (not a God class)
1. **The subsystem classes still exist and remain directly usable.** Facade is
   a convenience, not a wall. Power users can still talk to `Amplifier`.
2. **The facade holds NO business state** — it only choreographs.
3. **It exposes the few common use cases**, not every knob (if it re-exposes
   everything, you've built a pointless pass-through layer).

### Facade vs Adapter vs Mediator
- **Adapter** CHANGES one interface to fit what a client expects (shape fix).
- **Facade** SIMPLIFIES many interfaces into one (surface reduction).
- **Mediator** centralizes PEER-TO-PEER communication between colleagues
  (behavioral, two-way); facade is one-way (client -> subsystem).

### Real examples
`SLF4J` over logging backends, Spring's `JdbcTemplate` (facade over raw JDBC:
connections, statements, exception translation), every "Service" class that
orchestrates repositories, `javax.faces.context.FacesContext`.

### Interview line
> "Facade gives one intention-revealing entry point over a subsystem while
> leaving the subsystem usable directly. JdbcTemplate is the canonical example
> — it hides connection/statement/cleanup choreography."

### Code
`src/main/java/com/company/patterns/facade/FacadeDemo.java`

---

## COMPOSITE — a tree where leaf and group share one interface

### The problem
A directory contains files AND directories. An order contains items AND
bundles-of-items. A UI panel contains widgets AND panels. You want to call
`size()` / `price()` / `render()` on ANY node without caring which kind it is.

### The shape
```java
interface FileSystemNode {              // COMPONENT: the uniform interface
    long size();
}
class File implements FileSystemNode {  // LEAF: answers directly
    public long size() { return bytes; }
}
class Directory implements FileSystemNode {          // COMPOSITE
    private final List<FileSystemNode> children = new ArrayList<>();
    public long size() {                             // recursion hidden HERE
        long total = 0;
        for (FileSystemNode c : children) total += c.size();
        return total;
    }
}
```
`root.size()` walks the whole tree; the CLIENT writes zero recursion. That's
the whole pattern: **uniformity + recursion pushed into the composite.**

### Design decision people argue about
Where do `add()/remove()` live?
- On the **Composite only** (type-safe: you can't add a child to a File; but
  you lose uniformity — clients must know they hold a Directory to add).
- On the **Component** (uniform, GoF's choice; but File.add() must throw —
  an LSP smell).
Prefer type-safety: put child management on Composite. Say why.

### Real examples
Swing/JavaFX component trees, HTML DOM, your `05-parking-lot.md`
Lot->Floor->Spot is composite-ish (Lot aggregates Floors aggregates Spots),
GraphQL selection sets, organizational charts (Employee/Manager).

### Interview line
> "Composite makes single objects and groups share one interface so clients
> treat a whole tree like one object — the recursion lives inside the
> composite, not in the client. File-system size() is the canonical example."

### Code
`src/main/java/com/company/patterns/composite/CompositeDemo.java`

---

## BRIDGE — stop a 2-dimensional class explosion

### The disease (recognize it by class names)
Class names that MULTIPLY two words: `VectorCircle`, `RasterCircle`,
`VectorSquare`, `RasterSquare`. Shapes x Renderers = N*M classes. Add a
Triangle and an SVG renderer -> 3x3 = 9. Every new value in either dimension
multiplies.

### The cure
Split into TWO hierarchies; connect with composition (the "bridge"):

```java
interface Renderer {                    // IMPLEMENTATION dimension ("how")
    void renderCircle(double r);
    void renderSquare(double s);
}
abstract class Shape {                  // ABSTRACTION dimension ("what")
    protected final Renderer renderer;  // <- THE BRIDGE (injected)
    Shape(Renderer r) { this.renderer = r; }
    abstract void draw();
}
class Circle extends Shape {
    void draw() { renderer.renderCircle(radius); }   // delegate across bridge
}
```
3 shapes + 3 renderers = 6 classes, any pairing at runtime. ADDITIVE, not
multiplicative.

### "Isn't this just Strategy?"
Mechanically similar (hold an interface, delegate). The difference:
- **Strategy** swaps ONE ALGORITHM behind one method ("how to compute the fee").
- **Bridge** is an ARCHITECTURAL split of a whole class hierarchy into two
  independent dimensions that each grow their own subclasses.
Strategy is about behavior selection; Bridge is about hierarchy structure.

### Real examples
JDBC itself (your code -> JDBC API [abstraction] -> driver [implementation]),
SLF4J -> logback/log4j binding, AWT peers, "notification senders x message
types" designs.

### Interview line
> "When a hierarchy varies along two independent dimensions, Bridge splits it
> into abstraction + implementation joined by composition — class count goes
> from N*M to N+M and pairings become a runtime choice. JDBC api/driver is the
> living example."

### Code
`src/main/java/com/company/patterns/bridge/BridgeDemo.java`

---

## FLYWEIGHT — share the heavy immutable part of millions of objects

### The problem
1,000,000 trees on a game map. Each tree: x, y (16 bytes) + species name +
5MB texture. Naively every tree carries its own texture copy -> memory dies.
But there are only 3 SPECIES.

### The split (the entire pattern is this vocabulary)
- **INTRINSIC state** — identical across many objects, IMMUTABLE -> extract
  into a shared "flyweight" object (species, texture, color).
- **EXTRINSIC state** — unique per object -> stays outside the flyweight and
  is PASSED IN to its methods (x, y).

```java
class TreeType {                              // FLYWEIGHT: immutable, shared
    private final String species; private final String texture;
    void draw(int x, int y) { ... }           // extrinsic passed as parameters
}
class TreeTypeFactory {                       // the dedupe cache IS the pattern
    private static final Map<String, TreeType> CACHE = new HashMap<>();
    static TreeType get(String species, String texture) {
        return CACHE.computeIfAbsent(species + "|" + texture,
                k -> new TreeType(species, texture));
    }
}
class Tree {                                  // per-object part: tiny
    private final int x, y;                   // extrinsic
    private final TreeType type;              // shared reference
}
```
1M trees = 1M tiny Tree objects + 3 TreeType objects.

### Non-negotiable rule
**Flyweights must be IMMUTABLE.** They're shared by everyone; one mutation
corrupts every user. (Same logic as why singletons holding mutable state hurt.)

### You already use flyweights daily
- `Integer.valueOf(-128..127)` returns cached instances — that's why
  `Integer a = 100, b = 100; a == b` is true but `1000 == 1000` (boxed) is
  false. Interview trivia with a pattern behind it.
- `String` interning (`"abc" == "abc"` for literals).
- `Boolean.TRUE/FALSE`, enum values, `LocalDate` caching.

### When to use / not
Use: HUGE object counts with heavy repeated state (text glyphs, map tiles,
game sprites, cell styles in a spreadsheet). Skip: object counts are small, or
the "shared" part is mutable (then it isn't safely shareable).

### Interview line
> "Flyweight splits state into shared immutable intrinsic vs per-object
> extrinsic, dedupes the intrinsic behind a factory cache. Integer.valueOf's
> -128..127 cache is the JDK example everyone has met via the == trap."

### Code
`src/main/java/com/company/patterns/flyweight/FlyweightDemo.java`

---

## Recap table (this file)

| Pattern | One-liner | Recognize by |
|---|---|---|
| Prototype | create by copying a pre-built instance | expensive construction; "same but tweaked"; deep-vs-shallow question |
| Proxy | same interface, controls access | lazy-load / auth / remote stub; "client can't tell" |
| Facade | one simple front door, subsystem stays usable | choreography repeated in every client |
| Composite | leaf + group behind one interface, recursion inside | trees: directories, UI, org charts |
| Bridge | split 2-dimensional hierarchy into 2 + composition | multiplied class names (VectorCircle) |
| Flyweight | share immutable intrinsic state via factory cache | millions of similar objects |

Next: `13-patterns-behavioral-2.md` — Command, Iterator, Mediator, Memento,
Visitor, Interpreter, Null Object.
