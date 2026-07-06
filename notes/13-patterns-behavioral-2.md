# 13 — Design Patterns: the remaining Behavioral set

> Completes GoF coverage. `03-patterns-structural-behavioral.md` went deep on
> Observer, Chain of Responsibility, Strategy, State, Template Method. This
> file goes deep on the rest: **Command, Iterator, Mediator, Memento, Visitor,
> Interpreter**, plus the non-GoF but everywhere-useful **Null Object**.
>
> Runnable code for each:
> `src/main/java/com/company/patterns/{command,iterator,mediator,memento,visitor,interpreter,nullobject}/`

---

## COMMAND — turn a request into an object

### The problem
A button (or keybinding, or API endpoint, or queue consumer) needs to trigger
"insert text" / "delete" / "turn on lights" — but the button shouldn't KNOW any
of that. And you want undo, redo, logging, queuing, macro-recording.

None of that is possible while the request is just a method call. Make the
request an OBJECT and all of it falls out.

### The shape (4 roles)
```java
interface Command { void execute(); void undo(); }

class InsertTextCommand implements Command {         // CONCRETE COMMAND
    private final TextEditor editor;                 // -> the RECEIVER
    private final String text;
    public void execute() { editor.insert(text); }
    public void undo()    { editor.deleteLast(text.length()); }
}

class CommandInvoker {                               // INVOKER
    private final Deque<Command> history = new ArrayDeque<>();
    void run(Command c) { c.execute(); history.push(c); }
    void undo()         { if (!history.isEmpty()) history.pop().undo(); }
}
```
- **Receiver** (TextEditor) does the real work, knows nothing about commands.
- **Command** binds "which action + which receiver + which args" into one object.
- **Invoker** executes and remembers, knows nothing about text editing.

### The undo insight
Undo works because each command **carries what it needs to reverse itself** —
DeleteLastCommand remembers the deleted characters at execute() time so undo()
can restore them. State needed for reversal is captured at execution, not
reconstructed later.

### Where you've already seen it
- Your parking-lot CLI variant (`com/uditagarwal/commands/*`) — each console
  command is a Command object; `CommandExecutorFactory` picks one. Real code,
  in this repo.
- `Runnable` / `Callable` — commands without undo; the thread pool is the invoker.
- GUI frameworks (Swing `Action`), transactional outbox entries, message-queue
  jobs, text-editor undo stacks, database migration scripts (up = execute,
  down = undo).

### Command vs Strategy (both wrap "behavior in an object")
- Strategy: N interchangeable ways to do THE SAME job; caller picks one.
- Command: N DIFFERENT jobs made uniform so infrastructure (queues, undo
  stacks, buttons) can handle them identically.

### Interview line
> "Command reifies a request into an object binding receiver + action + args,
> so requests can be queued, logged, and undone. The undo stack falls out for
> free: execute pushes, undo pops and reverses."

---

## ITERATOR — walk a collection without exposing its guts

### The problem
Client code loops over your collection with `for (int i = 0; i < x.size(); i++)
x.get(i)` — now the client is COUPLED to "it's indexable". Switch to a linked
list, a tree, or a DB cursor and every loop breaks.

### The shape
```java
class Playlist implements Iterable<String> {
    private final List<String> songs = new ArrayList<>();   // HIDDEN
    public Iterator<String> iterator() { return new PlaylistIterator(); }

    private class PlaylistIterator implements Iterator<String> {
        private int cursor = 0;
        public boolean hasNext() { return cursor < songs.size(); }
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return songs.get(cursor++);
        }
    }
}
// for (String song : playlist) { ... }   <- works because Iterable
```

### The three facts that score points
1. **Every for-each you've ever written is this pattern.** `for (T x : c)`
   compiles to `iterator()/hasNext()/next()`.
2. **`Collection.iterator()` is a textbook Factory Method** (see 02 §B.8) —
   each collection subclass decides which concrete iterator to create.
3. **Fail-fast iterators** (ArrayList's) throw
   `ConcurrentModificationException` if the collection is structurally modified
   mid-iteration — they track a modCount. Concurrent collections
   (CopyOnWriteArrayList) return snapshot iterators instead — ties into
   11-concurrency.md.

### When you'd implement it yourself
Custom traversal orders (tree in-order vs level-order = two iterators over one
structure), lazy/streaming sources (DB cursor, file lines), or hiding storage
so you can change it later.

### Interview line
> "Iterator decouples traversal from storage — clients see hasNext/next, never
> the underlying structure. Iterable is the hook that makes for-each work, and
> iterator() itself is a Factory Method."

---

## MEDIATOR — peers talk through a hub, not to each other

### The problem
N objects that all interact directly = N² references and any change ripples
everywhere. Chat users messaging each other, form fields
enabling/disabling each other, planes coordinating landing.

### The shape
```java
interface ChatMediator {
    void broadcast(String from, String message);
    void direct(String from, String to, String message);
}
class User {
    private final ChatMediator room;      // the ONLY reference a user holds
    void send(String msg) { room.broadcast(name, msg); }
}
```
Users hold ONE reference (the room). The room owns the routing rules. Adding a
user, muting, rate-limiting a spammer: all changes live in the mediator.

### The classic analogy
Air-traffic control. Planes don't call each other to negotiate the runway;
they all talk to the tower. The tower is the mediator.

### Mediator vs its look-alikes
| | Direction | Purpose |
|---|---|---|
| **Observer** | one-way broadcast | "something happened, whoever cares reacts" |
| **Facade** | one-way, outside->in | simplify a subsystem for external clients |
| **Mediator** | two-way, among peers | own the INTERACTION RULES between colleagues |

### The trap (say this in the interview)
A mediator that accumulates every rule becomes a **God object** — you've traded
N² coupling for one class that knows everything. If the mediator grows fat,
split it by interaction type or reintroduce direct links where they're natural.

### Real examples
Chat rooms, GUI dialog controllers (field X disables button Y), event buses
(a mediator flavored with observer), Kafka's broker is mediator-ish: producers
and consumers never know each other — compare `07-kafka-pubsub.md`.

### Interview line
> "Mediator replaces N² peer references with a hub that owns the interaction
> rules — air-traffic control. Watch the trade-off: overload it and you've
> built a God object."

---

## MEMENTO — snapshot state without exposing internals

### The problem
Undo/rollback needs yesterday's state, but exposing getters/setters for every
private field so someone else can save/restore breaks encapsulation completely.

### The shape (3 roles)
```java
class Editor {                                     // ORIGINATOR
    private StringBuilder content;
    EditorState snapshot()          { return new EditorState(content.toString()); }
    void restore(EditorState state) { content = new StringBuilder(state.content()); }
}
final class EditorState {                          // MEMENTO: immutable
    private final String content;                  // no setters, ever
}
class History {                                    // CARETAKER
    private final Deque<EditorState> states = new ArrayDeque<>();
    void save(EditorState s) { states.push(s); }   // stores, never inspects
    EditorState pop()        { return states.pop(); }
}
```
The caretaker holds snapshots it **cannot read** — only the originator knows
what's inside. Encapsulation survives.

### Command-undo vs Memento-undo (know both, pick per case)
| | Command undo | Memento undo |
|---|---|---|
| Stores | operations (reversible) | full state snapshots |
| Memory | cheap | expensive (mitigate: diffs, cap history) |
| Correctness | each op must be truly reversible | trivially correct |
| Combo | **the pro move: Command for redo/replay + Memento for checkpoints** | |

### Real examples
Editor undo, DB transaction savepoints (`SAVEPOINT` ≈ memento), game save
files, form "discard changes", object serialization used for rollback.

### Interview line
> "Memento captures state in an opaque immutable object only the originator can
> read; the caretaker just stacks them. Versus Command-undo: snapshots are
> memory-heavy but trivially correct — real editors combine both."

---

## VISITOR — new operations over a stable hierarchy, without touching it

### The problem
Shapes: Circle, Rectangle. Operations keep arriving: area, XML export, JSON
export, rendering, validation... Adding each as a method means EVERY class
changes EVERY time (and area logic is smeared across all shapes instead of
living in one place).

### The shape — double dispatch
```java
interface Shape        { void accept(ShapeVisitor v); }
interface ShapeVisitor { void visit(Circle c); void visit(Rectangle r); }

class Circle implements Shape {
    public void accept(ShapeVisitor v) { v.visit(this); }   // `this` IS Circle
}
class AreaVisitor implements ShapeVisitor {
    public void visit(Circle c)    { total += Math.PI * c.radius * c.radius; }
    public void visit(Rectangle r) { total += r.w * r.h; }
}
```
Why the accept() dance? Java picks overloads at COMPILE time by static type —
`visitor.visit(shape)` wouldn't compile/dispatch to the right overload. So we
bounce: runtime dispatch picks the element's accept(), and inside Circle,
`this` is statically a Circle, so `v.visit(this)` binds the right overload.
Two dispatches = **double dispatch**. That's the whole trick.

### The trade-off (THE thing to say)
> Visitor makes ADDING OPERATIONS cheap (one new visitor class) but ADDING
> ELEMENT TYPES expensive (every visitor grows a method).
So it fits **stable hierarchies with growing operations**: compiler ASTs
(javac's TreeVisitor), document elements, file formats. If the element
hierarchy itself is what grows — avoid Visitor.

### Real examples
Compiler/AST tooling (`javax.lang.model.element.ElementVisitor`), ANTLR parse
trees, `java.nio.file.FileVisitor` (walkFileTree), static-analysis rules.

### Interview line
> "Visitor moves each operation into its own class over a stable element
> hierarchy via double dispatch — accept(this) picks the right overload.
> Cheap new operations, costly new element types: that asymmetry decides
> when to use it."

---

## INTERPRETER — a grammar as a class hierarchy

### The idea
Represent each rule of a tiny language as a class; a sentence becomes a tree of
those classes (an **AST — Abstract Syntax Tree**); evaluating = asking the root
to interpret itself. It's Composite applied to grammars.

### The shape
```java
interface Expression { int interpret(); }

class NumberLiteral implements Expression {          // TERMINAL (leaf)
    public int interpret() { return value; }
}
class Add implements Expression {                    // NON-TERMINAL (node)
    private final Expression left, right;
    public int interpret() { return left.interpret() + right.interpret(); }
}
// (5+3)-(2+1):
new Subtract(new Add(num(5), num(3)), new Add(num(2), num(1))).interpret();
```
The demo also includes a mini postfix parser showing how text becomes the tree.

### Honest guidance (this is the senior take)
You will almost never hand-roll Interpreter at work — for a real language
you'd use ANTLR or a parser combinator. Know the pattern because (a) the AST
shape underlies regex, SQL engines, Spring Expression Language, template
engines, and every compiler; (b) small rule/filter DSLs ("price > 100 AND
category = books") genuinely land here.

### Interview line
> "Interpreter models a grammar as terminal/non-terminal expression classes and
> evaluates by walking the AST — Composite for languages. In practice I'd reach
> for a parser generator, but the AST shape is what regex and SQL engines are."

---

## NULL OBJECT — a do-nothing implementation instead of null

### The disease
```java
if (logger != null) logger.log("...");     // repeated 40 times
```
Forget one check -> NullPointerException at 3am.

### The cure
```java
interface Logger {
    void log(String message);
    Logger NO_OP = message -> { };          // shared, stateless, does nothing
}
class OrderService {
    private final Logger logger;            // NEVER null, by convention
    void placeOrder(String item) {
        logger.log("placing order");        // no check. ever.
    }
}
new OrderService(Logger.NO_OP);             // silence, safely
```
Callers stop distinguishing "have one" from "don't" — absence becomes just
another (inert) implementation.

### Where the JDK already does this
`Collections.emptyList()` (null object for lists — you iterate it, zero
iterations, no NPE), `Optional.empty()`, Mockito's default stubs, Spring's
`NoOpCacheManager`.

### When NOT to
When absence is MEANINGFUL and the caller must react to it (missing user ≠
silently-do-nothing user!). Null Object silences absence — only use it where
silence is the correct behavior. Otherwise return `Optional` and force the
caller to decide.

### Interview line
> "Null Object replaces null-with-checks by an inert implementation —
> Collections.emptyList is the canonical one. The judgment call: only when
> doing nothing IS the right handling of absence; otherwise Optional."

---

## Recap table (this file)

| Pattern | One-liner | Undo? / key trick |
|---|---|---|
| Command | request as an object (receiver+action+args) | undo = pop history & reverse |
| Iterator | traversal without exposing storage | for-each IS this pattern |
| Mediator | peers talk via a hub owning interaction rules | beware the God-object trap |
| Memento | opaque immutable snapshot, caretaker can't peek | state-undo vs Command's op-undo |
| Visitor | new ops over stable hierarchy | double dispatch via accept(this) |
| Interpreter | grammar as classes, sentence as AST | Composite for languages |
| Null Object | inert impl instead of null | only when silence is correct |

### GoF checklist — where everything is covered now

| Creational | Where |
|---|---|
| Singleton, Factory Method, Abstract Factory, Builder | 02 |
| Prototype | 12 |

| Structural | Where |
|---|---|
| Decorator, Adapter | 03 |
| Proxy, Facade, Composite, Bridge, Flyweight | 12 |

| Behavioral | Where |
|---|---|
| Observer, Chain of Resp., Strategy, State, Template Method | 03 |
| Command, Iterator, Mediator, Memento, Visitor, Interpreter | 13 |
| (+ Null Object, non-GoF) | 13 |

Next batch: non-GoF working-engineer patterns (Repository/DAO/DTO, Unit of
Work, MVC/MVP/MVVM, CQRS, resilience patterns, Saga) and the remaining LLD
case studies (LRU cache, vending machine, chess, splitwise, ATM...).
