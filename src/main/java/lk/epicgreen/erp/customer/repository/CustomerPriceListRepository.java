package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerPriceList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CustomerPriceList entity
 * Based on ACTUAL database schema: customer_price_lists table
 *
 * Fields: customer_id (BIGINT), product_id (BIGINT),
 *         special_price, discount_percentage, min_quantity,
 *         valid_from, valid_to, is_active
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerPriceListRepository extends JpaRepository<CustomerPriceList, Long>, JpaSpecificationExecutor<CustomerPriceList> {

    // ==================== FINDER METHODS ====================

    /**
     * Find all price lists for a customer
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId")
    List<CustomerPriceList> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find all price lists for a customer with pagination
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId")
    Page<CustomerPriceList> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    /**
     * Find active price lists for a customer
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.isActive = true")
    List<CustomerPriceList> findByCustomerIdAndIsActiveTrue(@Param("customerId") Long customerId);

    /**
     * Find all price lists for a product
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.product.id = :productId")
    List<CustomerPriceList> findByProductId(@Param("productId") Long productId);

    /**
     * Find price list for a specific customer and product
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId")
    List<CustomerPriceList> findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    /**
     * Find active price list for customer and product
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId AND cpl.isActive = true")
    Optional<CustomerPriceList> findByCustomerIdAndProductIdAndIsActiveTrue(@Param("customerId") Long customerId, @Param("productId") Long productId);

    /**
     * Find all active price lists
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.isActive = true")
    List<CustomerPriceList> findByIsActiveTrue();

    /**
     * Find all active price lists with pagination
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.isActive = true")
    Page<CustomerPriceList> findByIsActiveTrue(Pageable pageable);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if price list exists for customer and product
     */
    @Query("SELECT CASE WHEN COUNT(cpl) > 0 THEN true ELSE false END FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId")
    boolean existsByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    /**
     * Check if active price list exists for customer and product
     */
    @Query("SELECT CASE WHEN COUNT(cpl) > 0 THEN true ELSE false END FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId AND cpl.isActive = true")
    boolean existsByCustomerIdAndProductIdAndIsActiveTrue(@Param("customerId") Long customerId, @Param("productId") Long productId);

    // ==================== SEARCH METHODS ====================

    /**
     * Search price lists by multiple criteria
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE " +
            "(:customerId IS NULL OR cpl.customer.id = :customerId) AND " +
            "(:productId IS NULL OR cpl.product.id = :productId) AND " +
            "(:isActive IS NULL OR cpl.isActive = :isActive)")
    Page<CustomerPriceList> searchPriceLists(
            @Param("customerId") Long customerId,
            @Param("productId") Long productId,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count price lists for a customer
     */
    @Query("SELECT COUNT(cpl) FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);

    /**
     * Count active price lists for a customer
     */
    @Query("SELECT COUNT(cpl) FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.isActive = true")
    long countByCustomerIdAndIsActiveTrue(@Param("customerId") Long customerId);

    /**
     * Count price lists for a product
     */
    @Query("SELECT COUNT(cpl) FROM CustomerPriceList cpl WHERE cpl.product.id = :productId")
    long countByProductId(@Param("productId") Long productId);

    /**
     * Count all active price lists
     */
    @Query("SELECT COUNT(cpl) FROM CustomerPriceList cpl WHERE cpl.isActive = true")
    long countByIsActiveTrue();

    // ==================== DELETE METHODS ====================

    /**
     * Delete all price lists for a customer
     */
    @Modifying
    @Query("DELETE FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId")
    void deleteAllByCustomerId(@Param("customerId") Long customerId);

    /**
     * Delete all price lists for a product
     */
    @Modifying
    @Query("DELETE FROM CustomerPriceList cpl WHERE cpl.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

    /**
     * Delete price list for customer and product
     */
    @Modifying
    @Query("DELETE FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId")
    void deleteByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find valid price lists on a specific date
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validFrom <= :date AND " +
            "(cpl.validTo IS NULL OR cpl.validTo >= :date) AND cpl.isActive = true")
    List<CustomerPriceList> findValidPriceListsOnDate(@Param("date") LocalDate date);

    /**
     * Find current valid price lists
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validFrom <= CURRENT_DATE AND " +
            "(cpl.validTo IS NULL OR cpl.validTo >= CURRENT_DATE) AND cpl.isActive = true")
    List<CustomerPriceList> findCurrentValidPriceLists();

    /**
     * Find valid price for customer and product on date
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.product.id = :productId AND cpl.validFrom <= :date AND " +
            "(cpl.validTo IS NULL OR cpl.validTo >= :date) AND cpl.isActive = true " +
            "ORDER BY cpl.validFrom DESC")
    Optional<CustomerPriceList> findValidPriceOnDate(
            @Param("customerId") Long customerId,
            @Param("productId") Long productId,
            @Param("date") LocalDate date);

    /**
     * Find current valid price for customer and product
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.product.id = :productId AND cpl.validFrom <= CURRENT_DATE AND " +
            "(cpl.validTo IS NULL OR cpl.validTo >= CURRENT_DATE) AND cpl.isActive = true " +
            "ORDER BY cpl.validFrom DESC")
    Optional<CustomerPriceList> findCurrentValidPrice(
            @Param("customerId") Long customerId,
            @Param("productId") Long productId);

    /**
     * Find expired price lists
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validTo < CURRENT_DATE")
    List<CustomerPriceList> findExpiredPriceLists();

    /**
     * Find future price lists
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validFrom > CURRENT_DATE")
    List<CustomerPriceList> findFuturePriceLists();

    /**
     * Find price lists by special price range
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.specialPrice BETWEEN :minPrice AND :maxPrice " +
            "AND cpl.isActive = true")
    List<CustomerPriceList> findBySpecialPriceRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find price lists by discount percentage range
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.discountPercentage BETWEEN :minDiscount AND :maxDiscount " +
            "AND cpl.isActive = true")
    List<CustomerPriceList> findByDiscountPercentageRange(
            @Param("minDiscount") BigDecimal minDiscount,
            @Param("maxDiscount") BigDecimal maxDiscount);

    /**
     * Find price lists with minimum quantity requirement
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.minQuantity > :quantity AND cpl.isActive = true")
    List<CustomerPriceList> findByMinQuantityGreaterThan(@Param("quantity") BigDecimal quantity);

    /**
     * Get price list statistics
     */
    @Query("SELECT " +
            "COUNT(cpl) as totalPriceLists, " +
            "SUM(CASE WHEN cpl.isActive = true THEN 1 ELSE 0 END) as activePriceLists, " +
            "AVG(cpl.specialPrice) as avgSpecialPrice, " +
            "AVG(cpl.discountPercentage) as avgDiscount, " +
            "COUNT(DISTINCT cpl.customer.id) as customersWithSpecialPricing " +
            "FROM CustomerPriceList cpl")
    Object getPriceListStatistics();

    /**
     * Get price lists grouped by customer
     */
    @Query("SELECT cpl.customer.id, COUNT(cpl) as priceListCount, AVG(cpl.discountPercentage) as avgDiscount " +
            "FROM CustomerPriceList cpl WHERE cpl.isActive = true " +
            "GROUP BY cpl.customer.id ORDER BY priceListCount DESC")
    List<Object[]> getPriceListsByCustomer();

    /**
     * Get price lists grouped by product
     */
    @Query("SELECT cpl.product.id, COUNT(cpl) as customerCount, AVG(cpl.specialPrice) as avgPrice " +
            "FROM CustomerPriceList cpl WHERE cpl.isActive = true " +
            "GROUP BY cpl.product.id ORDER BY customerCount DESC")
    List<Object[]> getPriceListsByProduct();

    /**
     * Find overlapping price lists for customer and product
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.product.id = :productId AND cpl.id != :excludeId AND " +
            "((cpl.validFrom <= :validTo AND (cpl.validTo IS NULL OR cpl.validTo >= :validFrom)))")
    List<CustomerPriceList> findOverlappingPriceLists(
            @Param("customerId") Long customerId,
            @Param("productId") Long productId,
            @Param("validFrom") LocalDate validFrom,
            @Param("validTo") LocalDate validTo,
            @Param("excludeId") Long excludeId);

    /**
     * Find all valid price lists for a customer
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.validFrom <= CURRENT_DATE AND (cpl.validTo IS NULL OR cpl.validTo >= CURRENT_DATE) " +
            "AND cpl.isActive = true ORDER BY cpl.validFrom DESC")
    List<CustomerPriceList> findCurrentValidPriceListsByCustomer(@Param("customerId") Long customerId);

    /**
     * Find price lists expiring soon (within X days)
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validTo BETWEEN CURRENT_DATE AND :futureDate " +
            "AND cpl.isActive = true ORDER BY cpl.validTo")
    List<CustomerPriceList> findPriceListsExpiringSoon(@Param("futureDate") LocalDate futureDate);

    /**
     * Find price lists with no end date
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.validTo IS NULL AND cpl.isActive = true")
    List<CustomerPriceList> findPriceListsWithNoEndDate();

    /**
     * Find top discounted products for a customer
     */
    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.isActive = true ORDER BY cpl.discountPercentage DESC")
    List<CustomerPriceList> findTopDiscountedProductsByCustomer(@Param("customerId") Long customerId, Pageable pageable);

    /**
     * Find customers with special pricing for a product
     */
    @Query("SELECT DISTINCT cpl.customer.id FROM CustomerPriceList cpl " +
            "WHERE cpl.product.id = :productId AND cpl.isActive = true")
    List<Long> findCustomersWithSpecialPricing(@Param("productId") Long productId);

    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.validFrom <= :date AND (cpl.validTo IS NULL OR cpl.validTo >= :date) " +
            "AND cpl.isActive = true ORDER BY cpl.validFrom DESC")
    List<CustomerPriceList> findValidPricesByCustomerAndDate(@Param("customerId") Long customerId, @Param("date") LocalDate date);

    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.customer.id = :customerId " +
            "AND cpl.product.id = :productId AND cpl.validFrom <= :date AND " +
            "(cpl.validTo IS NULL OR cpl.validTo >= :date) AND cpl.isActive = true " +
            "ORDER BY cpl.validFrom DESC")
    Optional<CustomerPriceList> findValidPriceByCustomerProductAndDate(@Param("customerId") Long customerId, @Param("productId") Long productId, @Param("date") LocalDate date);

    @Query("SELECT CASE WHEN COUNT(cpl) > 0 THEN true ELSE false END FROM CustomerPriceList cpl " +
            "WHERE cpl.customer.id = :customerId AND cpl.product.id = :productId " +
            "AND cpl.isActive = true AND cpl.id != :excludeId")
    boolean existsByCustomerIdAndProductIdAndIsActiveTrueAndIdNot(@Param("customerId") Long customerId, @Param("productId") Long productId, @Param("excludeId") Long excludeId);

    @Query("SELECT cpl FROM CustomerPriceList cpl WHERE cpl.id = :id")
    Optional<CustomerPriceList> findByIdAndDeletedAtIsNull(@Param("id") Long id);
}