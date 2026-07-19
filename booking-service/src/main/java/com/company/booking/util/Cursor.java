package com.company.booking.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

/**
 * Opaque base64 cursor for keyset pagination.
 * <p>
 * Composite - {@codue (createdAt, id)} - because {@code created_at} is not uniqe.
 * Two bookings created in the same millisecond would collide; the id breaks the tie
 * so we never skip or duplicate rows at a page boundary.
 * <p>
 * Encoded as base64 so clients treat it as opaque and don't parse or construct it.
 * That leaves us free to change the sort key later without breaking every client.
 */
public record Cursor(Instant createdAt, Long id) {

    public String encode() {
        String raw = createdAt.toEpochMilli() + ":" + id;
        return Base64.getUrlEncoder().withoutPadding()
                     .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static Cursor decode(String encoded) {
        if (encoded == null || encoded.isBlank()) return null;
        String raw = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
        int sep = raw.indexOf(':');
        if (sep < 0) throw new IllegalArgumentException("Malformed cursor");
        return new Cursor(
            Instant.ofEpochMilli(Long.parseLong(raw.substring(0, sep))),
            Long.parseLong(raw.substring(sep + 1))
        );
    }
}
