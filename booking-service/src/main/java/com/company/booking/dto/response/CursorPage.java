package com.company.booking.dto.response;

import java.util.List;

/**
 * Wire shape for keyset pagination.
 * <p>
 * Deliberately does NOT have {@code page}, {@code totalElements}, {@code totalPages}.
 * That's the trade-off keyset makes: you gain flat-cost paging and drift-free
 * results, you give up random access and a total count.
 */
public record CursorPage<T>(
    List<T> content,
    String nextCursor,      // null -> end of feed
    boolean hasMore
) {
    public static <T> CursorPage<T> of(List<T> content, String nextCursor, boolean hasMore) {
        return new CursorPage<>(content, nextCursor, hasMore);
    }
}
