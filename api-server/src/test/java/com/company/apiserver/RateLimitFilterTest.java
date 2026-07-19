package com.company.apiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Proves YOUR token bucket throttles real HTTP traffic.
 *
 * The properties below override application.properties FOR THIS TEST ONLY:
 * capacity 2, refill 0 (never refills) -> perfectly deterministic:
 * request 1 OK, request 2 OK, request 3 must be 429. No sleeps, no luck.
 *
 * Note: here filters stay ON (no addFilters=false) - the filter IS the
 * thing under test. MockMvc's fake requests all come from the same client
 * address, so they share one bucket.
 */
@SpringBootTest(properties = {
        "ratelimit.capacity=2",
        "ratelimit.refill-per-second=0"
})
@AutoConfigureMockMvc
class RateLimitFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void thirdRapidRequest_isThrottledWith429() throws Exception {
        mockMvc.perform(get("/api/ping")).andExpect(status().isOk());
        mockMvc.perform(get("/api/ping")).andExpect(status().isOk());
        mockMvc.perform(get("/api/ping")).andExpect(status().is(429));
    }
}
