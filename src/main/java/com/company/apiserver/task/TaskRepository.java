package com.company.apiserver.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * REPOSITORY = the layer that talks to the database.
 *
 * Notice there is NO implementation class anywhere in this project.
 * Spring Data JPA generates the implementation at startup.
 *
 * Just by extending JpaRepository&lt;Task, Long&gt;
 * (entity type = Task, primary-key type = Long) we inherit, ready to use:
 *
 *   findAll()        -> SELECT * FROM tasks
 *   findById(id)     -> SELECT * FROM tasks WHERE id = ?
 *   save(task)       -> INSERT or UPDATE
 *   deleteById(id)   -> DELETE FROM tasks WHERE id = ?
 *   existsById(id)   -> SELECT COUNT(*) ...
 *   ... and about 15 more.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * DERIVED QUERY: Spring parses the METHOD NAME and writes the SQL.
     * "findByCompleted"  ->  SELECT * FROM tasks WHERE completed = ?
     */
    List<Task> findByCompleted(boolean completed);
}
