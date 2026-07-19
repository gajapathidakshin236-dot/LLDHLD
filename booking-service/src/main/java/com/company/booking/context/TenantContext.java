package com.company.booking.context;

/**
 * ThreadLocal holder for the current request's tenant id.
 * <p>
 * Populated by {@link com.company.booking.filter.TenantFilter} at request entry,
 * cleared in a finally block. Never leaves a value on the thread once the
 * request completes - Tomcat pools threads and stale ThreadLocal state is one
 * of the most common causes of cross-user data leaks.
 */
public final class TenantContext {

    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(Long tenantId)  { TENANT.set(tenantId); }

    /** Throws if no tenant on the thread - fail loudly rather than default silently. */
    public static Long get() {
        Long id = TENANT.get();
        if (id == null) throw new IllegalStateException("No tenant on request");
        return id;
    }

    public static void clear() { TENANT.remove(); }
}
