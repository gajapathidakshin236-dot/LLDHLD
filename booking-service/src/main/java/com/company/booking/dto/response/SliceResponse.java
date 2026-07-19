package com.company.booking.dto.response;

import org.springframework.data.domain.Slice;

import java.util.List;

/**
 * Wire shape for Slice results - the middle ground between PagedResponse and CursorPage.
 *
 * Compare what each exposes (this is the whole Page/Slice/keyset story in three DTOs):
 *   PagedResponse: totalElements + totalPages  (costs a COUNT query)
 *   SliceResponse: hasNext only                (costs fetching size+1 rows)
 *   CursorPage:    nextCursor + hasMore        (no offset at all - flat cost at any depth)
 */
public record SliceResponse<T>(
    List<T> content,
    int page,
    int size,
    boolean hasNext        // note what's absent: no totals - that's the point of Slice
) {
    public static <T> SliceResponse<T> from(Slice<T> s) {
        return new SliceResponse<>(s.getContent(), s.getNumber(), s.getSize(), s.hasNext());
    }
}
