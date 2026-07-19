package com.company.booking.controller;

import com.company.booking.context.TenantContext;
import com.company.booking.dto.response.ShowResponse;
import com.company.booking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    /** First call misses cache, subsequent calls hit Caffeine - watch the log. */
    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(
            ShowResponse.from(showService.getShow(TenantContext.get(), id)));
    }

    @GetMapping
    public ResponseEntity<List<ShowResponse>> list() {
        List<ShowResponse> shows = showService.listShows(TenantContext.get())
            .stream().map(ShowResponse::from).toList();
        return ResponseEntity.ok(shows);
    }

    /** Demonstrates cache eviction after a mutation. */
    @PatchMapping("/{id}/title")
    public ResponseEntity<ShowResponse> updateTitle(@PathVariable Long id,
                                                    @RequestParam String title) {
        return ResponseEntity.ok(
            ShowResponse.from(showService.updateTitle(TenantContext.get(), id, title)));
    }

    /**
     * PUT - full replacement (every field required; see UpdateShowRequest for
     * the PUT-vs-PATCH story). Concurrent PUTs: slower writer gets 409 via
     * optimistic locking.
     */
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<ShowResponse> update(
            @PathVariable Long id,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody
            com.company.booking.dto.request.UpdateShowRequest req) {
        return ResponseEntity.ok(
            ShowResponse.from(showService.updateShow(TenantContext.get(), id, req)));
    }
}
