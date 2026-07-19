package com.company.apiserver.task;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ENTITY = a Java class that maps to a database table.
 * One Task object  <->  one row in the "tasks" table.
 * Each field       <->  one column in that row.
 *
 * Hibernate (the ORM inside Spring Data JPA) reads these annotations at
 * startup and, because of ddl-auto=create-drop, CREATES the table for us:
 *
 *   CREATE TABLE tasks (
 *     id          BIGINT AUTO_INCREMENT PRIMARY KEY,
 *     title       VARCHAR(100) NOT NULL,
 *     description VARCHAR(500),
 *     completed   BOOLEAN NOT NULL,
 *     created_at  TIMESTAMP NOT NULL
 *   );
 */
@Entity
@Table(name = "tasks")
public class Task {

    /** Primary key. IDENTITY = the database itself assigns 1, 2, 3, ... */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @NotBlank / @Size are VALIDATION rules (checked when a request arrives,
     * because the controller method parameter is marked @Valid).
     * @Column describes the DATABASE column itself.
     */
    @NotBlank(message = "title must not be blank")
    @Size(max = 100, message = "title must be at most 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 500, message = "description must be at most 500 characters")
    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean completed;

    /** Java field createdAt -> column "created_at" (snake_case naming). */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * JPA requires a no-argument constructor so Hibernate can create empty
     * objects and then fill the fields from a database row.
     * "protected" hides it from normal application code.
     */
    protected Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    /** Runs automatically just before the row is first INSERTed. */
    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    /* Getters and setters: Jackson (the JSON library) uses these to turn
       JSON into Task objects (setters) and Task objects into JSON (getters). */

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
