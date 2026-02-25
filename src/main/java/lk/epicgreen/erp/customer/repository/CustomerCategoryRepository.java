package lk.epicgreen.erp.customer.repository;


import lk.epicgreen.erp.customer.entity.CustomerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Customer Category Repository
 * Handles database operations for CustomerCategory entity
 */
@Repository
public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory, Long> {

    /**
     * Find customer category by code
     */
    Optional<CustomerCategory> findByCategoryCode(String categoryCode);

    /**
     * Check if category code exists
     */
    boolean existsByCategoryCode(String categoryCode);

    /**
     * Find active customer categories
     */
    List<CustomerCategory> findByIsActive(Boolean isActive);

    /**
     * Find active customer categories with pagination
     */
    Page<CustomerCategory> findByIsActive(Boolean isActive, Pageable pageable);
}
