package lk.epicgreen.erp.customer.repository;


import lk.epicgreen.erp.customer.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Customer Type Repository
 * Handles database operations for CustomerType entity
 */
@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {

    /**
     * Find customer type by code
     */
    Optional<CustomerType> findByTypeCode(String typeCode);

    /**
     * Check if type code exists
     */
    boolean existsByTypeCode(String typeCode);

    /**
     * Find active customer types
     */
    List<CustomerType> findByIsActive(Boolean isActive);

    /**
     * Find active customer types with pagination
     */
    Page<CustomerType> findByIsActive(Boolean isActive, Pageable pageable);
}
