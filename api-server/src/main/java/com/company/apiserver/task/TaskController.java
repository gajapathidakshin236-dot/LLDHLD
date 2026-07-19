package com.company.apiserver.task;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * CONTROLLER = the HTTP layer. It maps URLs to Java methods.
 *
 * @RestController = @Controller + @ResponseBody:
 *   whatever a method returns is converted to JSON and sent as the response.
 * @RequestMapping("/api/tasks") = every path below starts with /api/tasks.
 *
 * Endpoint summary (base URL http://localhost:8080):
 *   GET    /api/tasks                  -> list all tasks
 *   GET    /api/tasks?completed=true   -> list only finished tasks
 *   GET    /api/tasks/3                -> one task (404 if absent)
 *   POST   /api/tasks                  -> create from JSON body (201)
 *   PUT    /api/tasks/3                -> replace task 3 with JSON body
 *   DELETE /api/tasks/3                -> delete (204, empty response)
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @RequestParam reads "?completed=true" from the URL.
     * required=false + wrapper type Boolean means: null when absent,
     * so plain GET /api/tasks still returns everything.
     */
    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(name = "completed", required = false) Boolean completed) {
        if (completed == null) {
            return taskService.getAllTasks();
        }
        return taskService.getTasksByCompleted(completed.booleanValue());
    }

    /** @PathVariable reads the {id} part of the URL, e.g. /api/tasks/3 -> 3 */
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id);
    }

    /**
     * @RequestBody: parse the JSON body of the request into a Task object.
     * @Valid: run the @NotBlank/@Size rules first; on failure Spring throws
     *         MethodArgumentNotValidException -> our handler returns 400.
     * We answer 201 Created plus a Location header pointing at the new task
     * (REST convention).
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task created = taskService.createTask(task);
        URI location = URI.create("/api/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable("id") Long id,
                           @Valid @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /** 204 No Content = "done, nothing to send back". */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
