package com.company.apiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * LOCAL TESTS for the Task API. Run them with:
 *   .\mvnw.cmd test          (terminal)
 *   right-click the class -> Run   (IntelliJ)
 *
 * @SpringBootTest boots the REAL application (beans, H2 database, seed
 * data from data.sql) - but WITHOUT opening a network port. Instead,
 * MockMvc performs requests directly against the web layer in-process:
 * full routing, JSON, validation and error handling, no localhost needed.
 * This is how you test APIs without starting a server by hand.
 *
 * addFilters = false switches off servlet filters inside these tests -
 * otherwise our RateLimitFilter would 429 the rapid-fire test requests.
 * The filter has its own dedicated test: RateLimitFilterTest.
 *
 * Each test is INDEPENDENT of the others on purpose: no test relies on
 * changes another test made (they may run in any order).
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listTasks_returnsSeededData() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void getUnknownId_returns404WithJsonError() throws Exception {
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createTask_returns201WithGeneratedId() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"From a test\",\"description\":\"created by MockMvc\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("From a test"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void blankTitle_isRejectedWith400AndFieldError() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.title").exists());
    }

    @Test
    void updateTask_changesTheFields() throws Exception {
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated by test\",\"description\":\"x\",\"completed\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated by test"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTask_returns204_thenTaskIsGone() throws Exception {
        mockMvc.perform(delete("/api/tasks/2"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/tasks/2"))
                .andExpect(status().isNotFound());
    }
}
