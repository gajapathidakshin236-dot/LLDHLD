package com.company.booking.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Our own wire-shape for offset pagination.
 * <p>
 * We do NOT return Spring's {@code Page<T>} directly. Its JSON shape
 * ({@code pageable}, {@code sort}, {@code empty}, etc.) is a Spring Data
 * implementation detail - unstable across versions, and Spring Boot 3 warns
 * about serializing {@code PageImpl}.
 */
public record PagedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    boolean hasNext
) {
    public static <T> PagedResponse<T> from(Page<T> p) {
        return new PagedResponse<>(
            p.getContent(),
            p.getNumber(),
            p.getSize(),
            p.getTotalElements(),
            p.getTotalPages(),
            p.isFirst(),
            p.isLast(),
            p.hasNext()
        );
    }
}
