package com.company.apiserver.task;

/**
 * A custom exception with a clear name and message.
 * RuntimeException = "unchecked": callers are not forced to try/catch it;
 * it travels up until GlobalExceptionHandler catches it and answers 404.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task with id " + id + " was not found");
    }
}
