# HOW TO ADD YOUR OWN API — two recipes

Prerequisite reading: LEARNING-NOTES.md §10 (the three layers) makes everything here obvious.

---

## Recipe A — instant endpoint (one file, 2 minutes)

For anything that doesn't need the database: a returned `Map` (or any object with getters) becomes JSON automatically.

Create `src/main/java/com/company/apiserver/HelloController.java`:

```java
package com.company.apiserver;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Map<String, String> hello(
            @RequestParam(name = "name", required = false, defaultValue = "dakshin") String name) {
        return Map.of("greeting", "Hello, " + name + "!");
    }
}
```

Restart the server (IntelliJ: red ■ then green ▶) → `http://localhost:8080/api/hello?name=world`. Done. Any class in `com.company.apiserver` (or below) annotated `@RestController` is auto-discovered — no registration anywhere.

---

## Recipe B — full CRUD API with database (the real pattern)

Worked example: a **Notes** API (`/api/notes`). Same structure as the Task API — after building this once yourself, you can build anything. Create package `com.company.apiserver.note` with these four files:

### 1. The entity — what a Note IS (one row in a new table)

`Note.java`:

```java
package com.company.apiserver.note;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "text must not be blank")
    @Column(nullable = false, length = 1000)
    private String text;

    protected Note() { }              // for JPA

    public Note(String text) { this.text = text; }

    public Long getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
```

Restart and Hibernate creates the `notes` table automatically (`ddl-auto=create-drop`) — verify in the H2 console.

### 2. The repository — database access for free

`NoteRepository.java`:

```java
package com.company.apiserver.note;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
```

That's the whole file. `findAll`, `findById`, `save`, `deleteById`... all inherited.

### 3. The service — business logic

`NoteService.java`:

```java
package com.company.apiserver.note;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository repository;

    public NoteService(NoteRepository repository) { this.repository = repository; }

    public List<Note> getAll() { return repository.findAll(); }

    public Note create(Note note) { return repository.save(note); }
}
```

### 4. The controller — HTTP in, JSON out

`NoteController.java`:

```java
package com.company.apiserver.note;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) { this.service = service; }

    @GetMapping
    public List<Note> getAll() { return service.getAll(); }

    @PostMapping
    public ResponseEntity<Note> create(@Valid @RequestBody Note note) {
        Note created = service.create(note);
        return ResponseEntity.created(URI.create("/api/notes/" + created.getId())).body(created);
    }
}
```

### 5. Restart and use it

```powershell
irm http://localhost:8080/api/notes -Method Post -ContentType "application/json" -Body '{"text":"my first own API!"}'
irm http://localhost:8080/api/notes
```

### 6. Prove it with a test (this is "local tests")

`src/test/java/com/company/apiserver/NoteApiTests.java`:

```java
package com.company.apiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NoteApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createThenList() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"note from a test\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").exists());
    }
}
```

Run: right-click it in IntelliJ → Run, or `.\mvnw.cmd test`.

---

## Growth checklist — from here to a "real" API

Work through these on your Notes API, in order; each maps to something the Task API already demonstrates, so you always have a working reference next door:

1. `GET /api/notes/{id}` with a proper 404 (copy the `TaskNotFoundException` + handler pattern)
2. `PUT` and `DELETE` (see `TaskController`)
3. A derived query, e.g. `findByTextContainingIgnoreCase` exposed as `/api/notes/search?q=...` (see `TaskRepository.findByCompleted`)
4. Seed rows for notes in `data.sql`
5. Add your requests to `requests.http`
6. A validation rule + its 400 test (see `blankTitle_isRejectedWith400AndFieldError`)
7. Extra: exempt or specially limit `/api/notes` in `RateLimitFilter` — you own the filter
