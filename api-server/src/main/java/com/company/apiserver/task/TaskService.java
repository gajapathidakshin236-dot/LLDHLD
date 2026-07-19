package com.company.apiserver.task;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * SERVICE = the business-logic layer.
 * The controller stays thin (HTTP only), the repository stays dumb (SQL only),
 * and every decision/rule lives here in the middle.
 *
 * @Service tells Spring: create ONE instance of this class at startup and
 * hand it to whoever needs it ("dependency injection").
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * CONSTRUCTOR INJECTION: we never write "new TaskRepository()".
     * Spring sees this constructor needs a TaskRepository and passes in the
     * implementation it generated. This keeps classes loosely coupled and
     * easy to test (in a test you can pass a fake repository).
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    /**
     * findById returns an Optional&lt;Task&gt; - a box that either contains a
     * Task or is empty. orElseThrow = "give me the Task, or if the box is
     * empty, throw this exception" (which our handler turns into a 404).
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task); // INSERT; returns the task WITH its new id
    }

    /**
     * Update = load the existing row, copy the new values onto it, save.
     * Loading first (instead of blindly saving) gives us a proper 404 when
     * the id does not exist, and preserves fields like createdAt.
     */
    public Task updateTask(Long id, Task updatedData) {
        Task existing = getTaskById(id); // throws 404 if absent
        existing.setTitle(updatedData.getTitle());
        existing.setDescription(updatedData.getDescription());
        existing.setCompleted(updatedData.isCompleted());
        return taskRepository.save(existing); // UPDATE
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
