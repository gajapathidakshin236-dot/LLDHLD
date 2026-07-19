package com.company.booking;

import com.company.booking.context.TenantContext;
import com.company.booking.dto.request.CreateBookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * END-TO-END tests through the REAL pipeline:
 * MockMvc drives the full stack - filters (correlation id, tenant), interceptors
 * (rate limit, logging), argument resolvers, controller, services, repositories,
 * H2 - everything except a real network socket.
 *
 * WHY EVERY REQUEST SENDS X-Tenant-Id: @AutoConfigureMockMvc registers the real
 * TenantFilter, so a missing header is a real 400 (and we test exactly that).
 *
 * WHY THE PAYMENT ASSERTIONS ARE EXACT: PaymentService is deterministic -
 * userId ending in 0 declines, everything else succeeds. Deterministic fakes
 * -> exact assertions -> no flaky tests. (See AUDIT FIX #3 in PaymentService.)
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookingServiceApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    // For any direct service calls in future tests; requests themselves get the
    // tenant from the header via TenantFilter.
    @BeforeEach void setTenant()   { TenantContext.set(1L); }
    @AfterEach  void clearTenant() { TenantContext.clear(); }

    // =========================================================================
    // Offset pagination
    // =========================================================================

    @Test
    void offset_list_returns_page_shape() throws Exception {
        mvc.perform(get("/api/v1/bookings?page=0&size=5")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.content", hasSize(5)))
           .andExpect(jsonPath("$.page").value(0))
           .andExpect(jsonPath("$.size").value(5))
           .andExpect(jsonPath("$.totalElements").isNumber())   // Page ran a COUNT query
           .andExpect(jsonPath("$.hasNext").value(true));
    }

    @Test
    void bad_sort_property_returns_400_not_500() throws Exception {
        // PropertyReferenceException must be a client error - AUDIT FIX #4
        mvc.perform(get("/api/v1/bookings?sort=notAField,desc")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // Slice pagination
    // =========================================================================

    @Test
    void by_status_slice_has_hasNext_but_no_totals() throws Exception {
        mvc.perform(get("/api/v1/bookings/by-status?status=CONFIRMED&size=10")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content").isArray())
           .andExpect(jsonPath("$.hasNext").isBoolean())
           .andExpect(jsonPath("$.totalElements").doesNotExist());  // Slice = no COUNT
    }

    @Test
    void invalid_status_enum_returns_400() throws Exception {
        mvc.perform(get("/api/v1/bookings/by-status?status=NOT_A_STATUS")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isBadRequest());   // MethodArgumentTypeMismatch -> 400
    }

    // =========================================================================
    // Keyset (cursor) pagination
    // =========================================================================

    @Test
    void feed_keyset_paginates_end_to_end() throws Exception {
        String first = mvc.perform(get("/api/v1/bookings/feed?size=10")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content", hasSize(10)))
           .andExpect(jsonPath("$.hasMore").value(true))
           .andExpect(jsonPath("$.nextCursor").isString())
           .andReturn().getResponse().getContentAsString();

        String cursor = om.readTree(first).get("nextCursor").asText();

        // page 2 via the opaque cursor - no page number anywhere
        mvc.perform(get("/api/v1/bookings/feed?size=10&cursor=" + cursor)
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void feed_size_zero_violates_min_returns_400() throws Exception {
        // @Min(1) on a @RequestParam -> jakarta ConstraintViolationException -> 400
        mvc.perform(get("/api/v1/bookings/feed?size=0")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void garbage_cursor_returns_400() throws Exception {
        mvc.perform(get("/api/v1/bookings/feed?cursor=%%%notbase64%%%")
                .header("X-Tenant-Id", "1"))
           .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // Create flow - both payment branches (deterministic, see PaymentService)
    // =========================================================================

    @Test
    void create_booking_payment_success_is_CONFIRMED() throws Exception {
        var req = new CreateBookingRequest(1L, 9001L, "Z99");   // 9001 % 10 != 0 -> success

        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.seatNo").value("Z99"))
           .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void create_booking_payment_declined_is_FAILED() throws Exception {
        var req = new CreateBookingRequest(1L, 9010L, "Z98");   // 9010 % 10 == 0 -> declined

        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
           .andExpect(status().isCreated())                     // row exists; status says FAILED
           .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void duplicate_seat_returns_409() throws Exception {
        var req = new CreateBookingRequest(1L, 9002L, "A1");    // A1 is seeded in data.sql

        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
           .andExpect(status().isConflict());
    }

    // =========================================================================
    // Error contract
    // =========================================================================

    @Test
    void validation_400_on_missing_seat_with_field_errors() throws Exception {
        String body = """
            { "showId": 1, "userId": 9003 }
            """;

        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fieldErrors").isArray());     // structured, not a string blob
    }

    @Test
    void malformed_json_returns_400() throws Exception {
        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{not json"))
           .andExpect(status().isBadRequest());                 // HttpMessageNotReadable -> 400
    }

    @Test
    void missing_tenant_header_returns_400() throws Exception {
        mvc.perform(get("/api/v1/bookings"))
           .andExpect(status().isBadRequest());                 // TenantFilter rejects pre-controller
    }

    @Test
    void unknown_show_returns_404() throws Exception {
        var req = new CreateBookingRequest(999L, 9004L, "Q1");

        mvc.perform(post("/api/v1/bookings")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
           .andExpect(status().isNotFound());
    }

    // =========================================================================
    // Multi-tenancy isolation
    // =========================================================================

    @Test
    void tenant_2_cannot_see_tenant_1_shows() throws Exception {
        // Show 1 belongs to tenant 1; tenant 2 asking for it must get 404 (not 403 -
        // don't even reveal that the resource exists).
        mvc.perform(get("/api/v1/shows/1")
                .header("X-Tenant-Id", "2"))
           .andExpect(status().isNotFound());
    }
}
