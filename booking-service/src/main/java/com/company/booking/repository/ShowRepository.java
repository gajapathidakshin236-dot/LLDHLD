package com.company.booking.repository;

import com.company.booking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Extending {@link JpaRepository} gives us save/findById/findAll for free.
 * Spring Data generates the implementation at startup from the interface signature.
 */
public interface ShowRepository extends JpaRepository<Show, Long> {

    /** Derived query: Spring parses the method name -> "WHERE tenantId = ?". */
    List<Show> findAllByTenantId(Long tenantId);

    /** Tenant-scoped lookup - never look up by id alone in a multi-tenant app. */
    Optional<Show> findByIdAndTenantId(Long id, Long tenantId);
}
