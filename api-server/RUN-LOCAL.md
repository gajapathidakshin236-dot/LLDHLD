# RUN-LOCAL — start the server on your machine, step by step

Two ways to run: **IntelliJ** (you already use it for this repo — easiest) or **terminal**. Both end with a live server at `http://localhost:8080`.

---

## Way 1 — IntelliJ (recommended)

### 1. Open api-server as its own project

File → **Open** → navigate to `Documents\GitHub\LLDHLD\api-server` → OK → "Trust project". IntelliJ detects `pom.xml` and imports the Maven project — first time it downloads Spring Boot libraries (~150–200 MB, a few minutes; watch the progress bar bottom-right).

> Open the `api-server` folder itself, not LLDHLD — the root pom doesn't know about this module, so opening the root won't import it automatically. (If you prefer one window: open LLDHLD, then right-click `api-server/pom.xml` → **Add as Maven Project**.)

### 2. Check the project JDK (once)

File → Project Structure → Project → **SDK** should show a JDK 17+ (your `ms-17` is perfect). If empty: pick "Add SDK → Download JDK → 17 or 21".

### 3. Run

Open `src/main/java/com/company/apiserver/ApiServerApplication.java` → click the **green ▶** next to `main()` → "Run 'ApiServerApplication'".

**Success looks like** (end of the console):

```
Tomcat started on port 8080 (http) with context path '/'
Started ApiServerApplication in 3.1 seconds
```

The console stays "busy" — correct: the server is alive in it. Stop = red ■.

---

## Way 2 — Terminal (PowerShell)

Needs Java 17+ visible to the terminal. Check: `java -version`.

- **If it prints 17+**: you're set.
- **If not found / too old**, either install one — `winget install EclipseAdoptium.Temurin.21.JDK`, then reopen PowerShell — **or reuse the JDK IntelliJ already has**: run `dir "$env:USERPROFILE\.jdks"` — if you see e.g. `ms-17.0.15`, do:

  ```powershell
  setx JAVA_HOME "$env:USERPROFILE\.jdks\ms-17.0.15"    # use YOUR folder name
  ```

  then **open a new PowerShell** (setx only affects new windows).

Then:

```powershell
cd $env:USERPROFILE\Documents\GitHub\LLDHLD\api-server
.\mvnw.cmd spring-boot:run
```

First run self-downloads Maven + libraries (one-time, a few minutes). Success line: `Tomcat started on port 8080`. Stop with Ctrl+C.

---

## Test-drive your API

Browser first: **http://localhost:8080/api/tasks** → 3 seeded tasks as JSON. Then, easiest of all, open `requests.http` in IntelliJ and click the ▶ next to each request. Or in a second PowerShell window (`irm` = Invoke-RestMethod):

```powershell
irm http://localhost:8080/api/tasks                    # list
irm http://localhost:8080/api/tasks/1                  # one task
irm http://localhost:8080/api/tasks -Method Post -ContentType "application/json" -Body '{"title":"Buy milk","description":"2 litres"}'
irm http://localhost:8080/api/tasks/1 -Method Put -ContentType "application/json" -Body '{"title":"Done!","description":"x","completed":true}'
irm http://localhost:8080/api/tasks/2 -Method Delete
```

Watch the server console while you do this — `spring.jpa.show-sql=true` prints every SQL statement your calls generate.

**Errors, on purpose** (raw view via real curl):

```powershell
curl.exe -i http://localhost:8080/api/tasks/9999       # 404 JSON error
curl.exe -i -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"\"}"   # 400 validation
```

**Your rate limiter, live** — fire 10 requests fast:

```powershell
1..10 | % { curl.exe -s -o NUL -w "%{http_code}`n" http://localhost:8080/api/ping }
```

Expected: five `200`s (burst capacity), then `429`s — your `TokenBucketStrategy` rejecting real HTTP traffic. Wait 3 seconds, run again: a few `200`s are back (refill 1 token/sec). Tune `ratelimit.capacity` / `ratelimit.refill-per-second` in `application.properties` and restart to feel the difference.

**Look inside the database**: http://localhost:8080/h2-console → JDBC URL `jdbc:h2:mem:taskdb`, user `sa`, empty password → Connect → `SELECT * FROM TASKS;`. In-memory = every restart begins fresh with the 3 seed rows.

---

## Run the local tests

- **IntelliJ**: right-click `src/test/java` → **Run 'All Tests'**. Green checkmarks: 6 API tests, 1 rate-limit filter test, 4 plain-JUnit strategy tests.
- **Terminal**: `.\mvnw.cmd test` → ends with `BUILD SUCCESS` and `Tests run: 11, Failures: 0`.

Tests boot the app internally (no port, no running server needed) — you can run them anytime, even while the real server is up.

---

## Troubleshooting

| Problem | Cause → Fix |
|---|---|
| `Port 8080 was already in use` | Another app (or a previous run!) owns 8080. Stop the old run, or `netstat -ano \| findstr :8080` → `taskkill /PID <pid> /F`, or set `server.port=8081` in `application.properties`. |
| IntelliJ: red imports everywhere | Maven import didn't finish/failed. Right-click `pom.xml` → Maven → **Reload Project**; check internet. |
| IntelliJ: "release version 17 not supported" | Project SDK is older than 17. File → Project Structure → Project → SDK → pick 17+. |
| Terminal: `'java' is not recognized` | New PowerShell window after installing/setx. Verify `java -version`. |
| Terminal: `JAVA_HOME not found` from mvnw | Set it as shown in Way 2 (point at a real JDK folder, not `\bin`). |
| First run download fails | Internet/VPN/proxy blocking Maven Central. Retry without VPN — it resumes. |
| Browser: Whitelabel Error Page at `/` | Normal — no endpoint at root. Use `/api/tasks`, `/api/ping`, `/h2-console`. |
| Everything answers 429 | You drained the bucket. Wait a few seconds (1 token/sec) or raise `ratelimit.capacity`. |
| H2 console: `Database "taskdb" not found` | JDBC URL must be exactly `jdbc:h2:mem:taskdb`, and the server must be running. |
