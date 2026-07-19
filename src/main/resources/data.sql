-- data.sql: Spring Boot runs this file automatically at startup
-- (after Hibernate created the tables - see defer-datasource-init).
-- It gives you data to look at immediately, without POSTing first.
-- Note: no "id" column in the INSERTs - the IDENTITY column assigns 1,2,3.

INSERT INTO tasks (title, description, completed, created_at) VALUES
  ('Learn Spring Boot basics', 'Read LEARNING-NOTES.md top to bottom', FALSE, CURRENT_TIMESTAMP);

INSERT INTO tasks (title, description, completed, created_at) VALUES
  ('Run the API locally', 'Follow SETUP-WINDOWS.md and hit http://localhost:8080/api/tasks', FALSE, CURRENT_TIMESTAMP);

INSERT INTO tasks (title, description, completed, created_at) VALUES
  ('Explore the H2 console', 'Open http://localhost:8080/h2-console and run: SELECT * FROM tasks', TRUE, CURRENT_TIMESTAMP);
