# LEARNING NOTES — every concept in this project, from zero

These notes assume **nothing**. Each section teaches one concept: what it is, why it exists, what problem it solves — then points at the exact place in this project where you can see it working. Read in order; later sections build on earlier ones.

---

## 1. Programs and processes

A **program** is a file containing instructions (code). A **process** is a program *while it is running* — loaded into memory, executing, using the CPU. Double-click Chrome: the `chrome.exe` program becomes a Chrome process. One program can run as many processes.

Why you care here: when you run `.\mvnw.cmd spring-boot:run`, your API becomes a long-lived process that does not exit. It sits waiting for requests until you press Ctrl+C. That "stuck" terminal window is the process living.

## 2. Servers

A **server** is just a process whose job is to *wait for requests from other programs and answer them*. The word also gets used for the machine such processes run on, but the machine is nothing special — your laptop becomes a server the moment a waiting-and-answering process runs on it.

Why servers exist: computers need to ask other computers for things (a web page, saved data, a price). Someone has to be listening. The asking side is called the **client** (your browser, an app), the answering side the **server**.

In this project: Spring Boot starts an internal web server called **Tomcat** (§9) inside your process. Your browser is the client.

## 3. Networks: IP addresses, ports, and localhost

For a client to reach a server it needs an address. Every device on a network gets an **IP address** — a number like `142.250.183.14` — which identifies the *machine*.

But a machine runs many server processes at once (your API, maybe a database, maybe a game server). The IP alone can't say *which process* a request is for. So each listening process claims a **port** — a number from 1 to 65535 on that machine. Address = IP + port, written `IP:port`. Think apartment building: IP = street address, port = apartment number.

**localhost** is a special hostname that always means "this same machine" (it resolves to the IP `127.0.0.1`). Traffic to localhost never leaves your computer.

So `http://localhost:8080/api/tasks` reads as: *"talk to my own machine, apartment 8080, and ask for /api/tasks"*. Port **8080** is the conventional default for development web apps (the "real" web ports 80/443 need admin rights). You can change it in `application.properties` (`server.port`).

## 4. HTTP — the language of the request

Client and server must agree on the *format* of their conversation. **HTTP** (HyperText Transfer Protocol) is that agreement — a plain-text format for requests and responses that the whole web runs on.

An HTTP **request** has four parts:

```
POST /api/tasks HTTP/1.1              <- method + path
Host: localhost:8080                  <- headers (metadata,
Content-Type: application/json           key: value lines)
                                      <- blank line
{"title":"Buy milk"}                  <- body (optional payload)
```

The **method** states the intent:

| Method | Intent | In this project |
|---|---|---|
| GET | read, change nothing | list/fetch tasks |
| POST | create something new | create a task |
| PUT | replace/update existing | update a task |
| DELETE | remove | delete a task |

The **response** mirrors it: a **status code**, headers, body. Status codes are 3-digit numbers grouped by meaning — 2xx success (200 OK, 201 Created, 204 No Content), 4xx *you* (client) did something wrong (400 Bad Request, 404 Not Found), 5xx the *server* broke (500 Internal Server Error). This API deliberately uses the precise ones: look at `TaskController.java` — 201 for create, 204 for delete — and `GlobalExceptionHandler.java` — 404 and 400.

## 5. APIs and REST

An **API** (Application Programming Interface) is any defined way for one piece of software to talk to another. A **web API** is an API offered over HTTP: "send me *this* request, I'll answer *this* shape of data". No screens, no buttons — machine-to-machine.

Why: your future mobile app, a React website, and a script can all reuse the same backend if it exposes a web API instead of HTML pages.

**REST** is the most common *style* for designing web APIs. Its core ideas: model your data as **resources** named by URLs (`/api/tasks` = the collection, `/api/tasks/3` = task number 3); use the HTTP methods for their real meanings (table above); make responses self-describing with status codes. Compare the endpoint table in `README.md` — that shape (GET/POST on the collection, GET/PUT/DELETE on `/{id}`) is *the* classic REST CRUD layout. CRUD = Create, Read, Update, Delete — the four things you do to stored data.

## 6. JSON — the shape of the data

The two sides also need a format for the *data itself*. **JSON** (JavaScript Object Notation) is a small text format that every language can read and write:

```json
{ "id": 1, "title": "Buy milk", "completed": false, "tags": ["home", "urgent"] }
```

Rules: `{}` = object (key/value pairs), `[]` = list, strings in double quotes, plus numbers, `true`/`false`, `null`. That's the whole language.

In this project a library called **Jackson** (pulled in automatically, §8) converts between JSON text and Java objects both ways. You never call it explicitly: when `TaskController.createTask` declares a `@RequestBody Task task` parameter, Jackson parses the incoming JSON into a `Task` object using its setters; when a method returns a `Task`, Jackson turns the getters into JSON. That's why `Task.java` has getters/setters.

## 7. Java, the JVM/JDK, dependencies, and Maven

**Java** is the programming language this project is written in. Java source files (`.java`) are compiled into an intermediate form (bytecode) that runs on the **JVM** (Java Virtual Machine) — a program that executes bytecode the same way on Windows, Linux, or Mac. The **JDK** (Java Development Kit) is the full toolbox: the JVM plus the compiler. That's the one thing RUN-LOCAL.md has you install. Spring Boot 3 requires JDK 17 or newer.

**Dependencies.** Nobody writes a web server or JSON parser from scratch — you reuse **libraries** (other people's packaged code, `.jar` files). A real app needs dozens, each needing others, each existing in many versions. Managing that by hand is hopeless. A **build tool** solves it: you *declare* what you need, it downloads the right versions (with all their sub-dependencies) from **Maven Central** — a huge public warehouse of Java libraries — caches them in `C:\Users\<you>\.m2\repository`, compiles your code against them, and can run/package the app.

**Maven** is the build tool used here. Its entire configuration is one file, **`pom.xml`**. Open it — three things matter:

1. The **parent**, `spring-boot-starter-parent 3.5.16`: our project inherits a giant, pre-tested table of library versions. This is why the dependencies below it have *no version numbers* — Spring Boot has already chosen combinations that work together.
2. The **dependencies** list — what we declared (next section).
3. The **spring-boot-maven-plugin** — teaches Maven the `spring-boot:run` command you type.

**The Maven wrapper** (`mvnw.cmd`, `mvnw`, `.mvn/`): tiny scripts committed *into* the project that download the exact right Maven version on first use. Problem solved: nobody has to install Maven, and everyone builds with the same version. That's why your only prerequisite is Java.

## 8. Frameworks, Spring, and Spring Boot

A **library** is code *you call* when you want. A **framework** is the opposite: it runs the show and *calls your code* at the right moments. You slot your classes into its structure.

**Spring** is Java's dominant framework for backend applications. Its core trick is the next section's dependency injection; around that grew an ecosystem (web, database, security, ...). Classic Spring, however, demanded lots of configuration before anything ran.

**Spring Boot** is Spring plus aggressive automation, built on two ideas:

- **Starters**: one-line bundles in `pom.xml`. `spring-boot-starter-web` = Spring MVC + Jackson + *embedded Tomcat*. `spring-boot-starter-data-jpa` = Hibernate + Spring Data (§13). Embedded Tomcat means the web server (§2) is just a library inside your app — run one command, server included. No separate installation.
- **Auto-configuration**: at startup, Spring Boot inspects what's on the classpath and configures it with sensible defaults. It sees H2 + JPA → creates a database connection. Sees web → starts Tomcat on 8080. You override defaults only where you care, in **`application.properties`** — open it, every line is commented. This is "convention over configuration": defaults for everything, settings only for exceptions.

`ApiServerApplication.java` is the ignition: `@SpringBootApplication` + `SpringApplication.run(...)` triggers all of the above.

## 9. Annotations, beans, and dependency injection

**Annotations** are the `@Word` tags on classes, methods, fields. They do nothing by themselves — they are structured *labels* that frameworks read to decide how to treat your code. `@RestController` = "this class handles HTTP". `@Entity` = "this class maps to a table". Spring is driven almost entirely by annotations.

**The problem DI solves.** `TaskController` needs `TaskService`, which needs `TaskRepository`. If each class built its own helpers with `new`, every class would be welded to concrete implementations — hard to swap, hard to test, and something must still construct the whole chain in order.

**Dependency injection (DI)** flips it: classes just *declare* what they need as constructor parameters, and the framework constructs and connects everything. At startup Spring scans your packages for `@RestController`, `@Service`, `@Repository`-style classes, creates **one instance of each** (these managed objects are called **beans**), sees that `TaskController`'s constructor wants a `TaskService`, and passes the right bean in. See it: `TaskService.java` — the constructor takes a `TaskRepository`; nowhere in the project does `new TaskService(...)` appear. Spring wires it.

Bonus: in a test you can hand `TaskService` a fake repository — no database needed. That's the design payoff.

## 10. The three-layer architecture

Backend code is conventionally split into three layers, each ignorant of the layers above it:

```
HTTP request
   ↓
CONTROLLER  (TaskController.java)   HTTP in/out ONLY: URLs, status codes, JSON. No logic.
   ↓
SERVICE     (TaskService.java)      Business rules: what "update" means, when to 404.
   ↓
REPOSITORY  (TaskRepository.java)   Database in/out ONLY.
   ↓
DATABASE    (H2, the "tasks" table)
```

Why bother for a small app? Because each layer can change independently (swap H2→PostgreSQL: only the bottom cares), be tested independently, and stay readable. Learn the pattern now — every Java codebase you'll ever join uses it.

## 11. Databases, tables, and SQL

Variables live in a process's memory — gone when it stops. A **database** is a separate specialized system that stores data durably and answers questions about it fast, even with millions of rows and many simultaneous users.

**Relational databases** (the dominant kind) organize data into **tables** — grids with typed, named **columns** and one **row** per record. Ours:

```
TABLE tasks
 id | title                    | description | completed | created_at
----+--------------------------+-------------+-----------+------------
  1 | Learn Spring Boot basics | Read ...    | FALSE     | 2026-07-19...
```

`id` is the **primary key** — a column guaranteed unique per row, used to point at exactly one row (`/api/tasks/3` uses it). Ours is auto-assigned by the database (1, 2, 3, ...).

**SQL** (Structured Query Language) is the standard language for talking to relational databases:

```sql
SELECT * FROM tasks WHERE completed = TRUE;
INSERT INTO tasks (title, completed, created_at) VALUES ('x', FALSE, CURRENT_TIMESTAMP);
UPDATE tasks SET completed = TRUE WHERE id = 1;
DELETE FROM tasks WHERE id = 2;
```

Read `src/main/resources/data.sql` — real SQL, run automatically at startup to give you seed rows.

## 12. H2 — the database in this project

Real databases (PostgreSQL, MySQL) are separate server processes you must install, start, and connect to. Heavy for learning. **H2** is a full SQL database written in Java that runs *inside* your application process, and in **in-memory mode** keeps all data in RAM.

Consequences: zero installation, instant start, and **every restart wipes the data** — the app is reborn with the tables freshly created and the 3 `data.sql` rows. For learning this is a feature (guaranteed clean state); for production you'd point the same code at PostgreSQL by changing a few lines of `application.properties`.

H2 ships a web UI, enabled here: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:taskdb`, user `sa`, empty password). The **JDBC URL** is Java's standard connection-string format: `jdbc:h2:mem:taskdb` = engine H2, in-memory, database named `taskdb` — matching `spring.datasource.url` in `application.properties`.

## 13. ORM: JPA, Hibernate, and Spring Data JPA

**The problem.** Java thinks in objects; databases think in rows and SQL strings. Writing the translation by hand — build SQL, run it, loop over results, copy columns into fields — is repetitive, error-prone plumbing, multiplied by every entity and every operation.

**ORM** (Object-Relational Mapping) automates the translation: you *declare* how a class maps to a table, and the ORM generates the SQL. Three names, three roles:

- **JPA** (Jakarta Persistence API) — the standard *specification*: the annotations `@Entity`, `@Id`, `@Column`... (the `jakarta.persistence` imports in `Task.java`).
- **Hibernate** — the actual *implementation* doing the work (generating SQL, creating tables). Pulled in by `spring-boot-starter-data-jpa`.
- **Spring Data JPA** — a layer on top that eliminates even the data-access classes, next.

**The entity.** `Task.java`: `@Entity` + `@Table(name = "tasks")` declare the mapping; each field is a column (`@Column` tunes name/length/nullability; `createdAt` → `created_at`). Because `application.properties` sets `spring.jpa.hibernate.ddl-auto=create-drop`, Hibernate reads this class at startup and **creates the table for you** — the class's Javadoc shows the exact SQL. (`create-drop` = create at boot, drop at shutdown; learning-only. Real projects version their schema with migration tools like Flyway.)

**The repository.** `TaskRepository.java` is an *interface with no implementation anywhere*. By extending `JpaRepository<Task, Long>` it inherits `findAll()`, `findById()`, `save()`, `deleteById()`, `existsById()`... and Spring Data **generates the implementing class at startup**. Wilder still, **derived queries**: declare `List<Task> findByCompleted(boolean completed)` and Spring parses the *method name* and writes `SELECT * FROM tasks WHERE completed = ?` for you.

Watch it all happen: `spring.jpa.show-sql=true` makes every generated SQL statement print in the server window as you use the API.

## 14. Validation

Never trust incoming data — clients send garbage (empty titles, 5MB strings). Checking by hand with `if` statements scatters rules everywhere. **Bean Validation** (via `spring-boot-starter-validation`) makes rules declarative: annotate the field with the rule, next to the data it protects.

In `Task.java`: `@NotBlank(message = "title must not be blank")` and `@Size(max = 100, ...)`. Activated in `TaskController.java` by `@Valid` on the `@RequestBody` parameter: Jackson parses the JSON → validation runs → if any rule fails, Spring throws `MethodArgumentNotValidException` and your method never runs. Try it: request #7 in `requests.http` — the answer is a 400 listing exactly which field failed and why. That error's clean shape comes from the next section.

## 15. Error handling

Something always goes wrong: an id that doesn't exist, invalid input. Two bad options: let exceptions escape (ugly default error pages, leaked internals) or try/catch in every endpoint (duplication). Spring's answer: **one central translator**.

`GlobalExceptionHandler.java` is marked `@RestControllerAdvice` — "advice" applying to all controllers. Each `@ExceptionHandler` method catches one exception type thrown *anywhere* during a request and converts it to a proper JSON response: `TaskNotFoundException` (our own class — see `TaskNotFoundException.java`, thrown by the service) → **404** with a clear message; `MethodArgumentNotValidException` → **400** with a `fieldErrors` map. Controllers and services stay clean — they just throw.

## 16. One request, end to end

What actually happens when you run request #4 (`POST /api/tasks` with `{"title":"My first POST request", ...}`):

1. Your client opens a connection to `localhost:8080` (§3) and sends the HTTP request text (§4).
2. **Tomcat** (§8), listening on 8080 inside your process, receives it and hands it to Spring MVC.
3. Spring MVC routes it: path `/api/tasks` + method POST → `TaskController.createTask()` (the `@RequestMapping` + `@PostMapping` annotations, §9).
4. **Jackson** parses the JSON body into a `Task` object via its setters (§6).
5. **Validation** runs the `@NotBlank`/`@Size` rules because of `@Valid` (§14). Blank title? → exception → handler → 400, done. Valid? → your method runs.
6. The controller calls `taskService.createTask(task)` — the injected bean (§9).
7. The service calls `taskRepository.save(task)` (§10).
8. Spring Data JPA / **Hibernate** generates `INSERT INTO tasks (title, description, completed, created_at) VALUES (?,?,?,?)` (§13) — `@PrePersist` in `Task.java` stamps `createdAt` first — and **H2** stores the row, assigning the next `id` (§12). Watch the INSERT print in the server window (`show-sql`).
9. The saved `Task` (now with its `id`) returns controller-ward; the controller wraps it: status **201 Created** + a `Location: /api/tasks/4` header (§5).
10. Jackson serializes the `Task` to JSON via its getters; Tomcat sends the response; your client prints it.

Ten steps, five libraries, and you wrote ~30 lines of them. That's the framework deal (§8): you supply the unique parts, it supplies the machinery.

## 17. What happens at startup

Reading the log of `.\mvnw.cmd spring-boot:run` top to bottom:

1. Maven (via the wrapper) compiles `src/main/java` and launches the app on your JVM (§7).
2. `SpringApplication.run` boots Spring: component scan finds your annotated classes, beans are created and injected (§9).
3. Auto-configuration sees H2 + JPA on the classpath → connects to `jdbc:h2:mem:taskdb` (§8, §12).
4. Hibernate reads `@Entity` classes → `CREATE TABLE tasks ...` (§13) — visible in the log.
5. Spring runs `data.sql` → 3 INSERTs (§11). (`spring.jpa.defer-datasource-init=true` in `application.properties` forces this *after* step 4 — otherwise the INSERTs would hit a missing table.)
6. Tomcat binds port 8080 → `Tomcat started on port 8080` → the process now waits (§1–2). Ready.

## 18. Where to go next — exercises

Do these in order; each breaks if you misunderstood something above, which is the point:

1. **See the layers**: change the 404 message in `TaskNotFoundException.java`, restart, hit `/api/tasks/9999`.
2. **Add a field**: give `Task` a `dueDate` (`LocalDate`) — field, getter/setter, restart (Hibernate recreates the table with the new column — check in the H2 console), POST a task with `"dueDate": "2026-08-01"`.
3. **Add a derived query**: in `TaskRepository`, add `List<Task> findByTitleContainingIgnoreCase(String part)`; expose it as `GET /api/tasks/search?q=...` through service and controller. You'll touch all three layers.
4. **Add a real test**: look up `@SpringBootTest` + `MockMvc` (the `spring-boot-starter-test` dependency is already in `pom.xml`) and assert that `GET /api/tasks` returns 200.
5. **Make data survive restarts**: in `application.properties` change the URL to `jdbc:h2:file:./data/taskdb` and `ddl-auto` to `update`. Restart twice, notice your data persists. You now understand why in-memory vs file matters.
6. **Graduate**: install PostgreSQL, swap the datasource URL/driver, see how little else changes. Then read about Flyway (schema migrations) — the production replacement for `ddl-auto`.
