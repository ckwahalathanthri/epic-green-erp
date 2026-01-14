package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.BillOfMaterials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BillOfMaterials entity
 * Based on ACTUAL database schema: bill_of_materials table
 *
 * Fields: bom_code, finished_product_id (BIGINT), bom_version, output_quantity,
 *         output_uom_id (BIGINT), production_time_minutes, labor_cost, overhead_cost,
 *         is_active, effective_from, effective_to, remarks
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long>, JpaSpecificationExecutor<BillOfMaterials> {

    // ==================== FINDER METHODS ====================

    /**
     * Find BOM by BOM code
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.bomCode = :bomCode")
    Optional<BillOfMaterials> findByBomCode(@Param("bomCode") String bomCode);

    /**
     * Find all BOMs for a finished product
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId")
    List<BillOfMaterials> findByFinishedProductId(@Param("finishedProductId") Long finishedProductId);

    /**
     * Find all BOMs for a finished product with pagination
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId")
    Page<BillOfMaterials> findByFinishedProductId(@Param("finishedProductId") Long finishedProductId, Pageable pageable);

    /**
     * Find all active BOMs
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.isActive = true")
    List<BillOfMaterials> findByIsActiveTrue();

    /**
     * Find all active BOMs with pagination
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.isActive = true")
    Page<BillOfMaterials> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive BOMs
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.isActive = false")
    List<BillOfMaterials> findByIsActiveFalse();

    /**
     * Find active BOMs for a finished product
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId AND bom.isActive = true")
    Optional<BillOfMaterials> findByFinishedProductIdAndIsActiveTrue(@Param("finishedProductId") Long finishedProductId);

    /**
     * Find BOMs by version
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.bomVersion = :bomVersion")
    List<BillOfMaterials> findByBomVersion(@Param("bomVersion") String bomVersion);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if BOM code exists
     */
    @Query("SELECT CASE WHEN COUNT(bom) > 0 THEN true ELSE false END FROM BillOfMaterials bom WHERE bom.bomCode = :bomCode")
    boolean existsByBomCode(@Param("bomCode") String bomCode);

    /**
     * Check if BOM code exists excluding specific BOM ID
     */
    @Query("SELECT CASE WHEN COUNT(bom) > 0 THEN true ELSE false END FROM BillOfMaterials bom WHERE bom.bomCode = :bomCode AND bom.id <> :id")
    boolean existsByBomCodeAndIdNot(@Param("bomCode") String bomCode, @Param("id") Long id);

    // ==================== SEARCH METHODS ====================

    /**
     * Search BOMs by BOM code containing (case-insensitive)
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE LOWER(bom.bomCode) LIKE LOWER(CONCAT('%', :bomCode, '%'))")
    Page<BillOfMaterials> findByBomCodeContainingIgnoreCase(@Param("bomCode") String bomCode, Pageable pageable);

    /**
     * Search active BOMs by keyword
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.isActive = true AND " +
            "LOWER(bom.bomCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<BillOfMaterials> searchActiveBoms(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Search BOMs by multiple criteria
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE " +
            "(:bomCode IS NULL OR LOWER(bom.bomCode) LIKE LOWER(CONCAT('%', :bomCode, '%'))) AND " +
            "(:finishedProductId IS NULL OR bom.finishedProduct.id = :finishedProductId) AND " +
            "(:bomVersion IS NULL OR bom.bomVersion = :bomVersion) AND " +
            "(:isActive IS NULL OR bom.isActive = :isActive)")
    Page<BillOfMaterials> searchBoms(
            @Param("bomCode") String bomCode,
            @Param("finishedProductId") Long finishedProductId,
            @Param("bomVersion") String bomVersion,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count active BOMs
     */
    @Query("SELECT COUNT(bom) FROM BillOfMaterials bom WHERE bom.isActive = true")
    long countByIsActiveTrue();

    /**
     * Count inactive BOMs
     */
    @Query("SELECT COUNT(bom) FROM BillOfMaterials bom WHERE bom.isActive = false")
    long countByIsActiveFalse();

    /**
     * Count BOMs for a product
     */
    @Query("SELECT COUNT(bom) FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId")
    long countByFinishedProductId(@Param("finishedProductId") Long finishedProductId);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find current active BOM for a finished product
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId " +
            "AND bom.isActive = true AND bom.effectiveFrom <= CURRENT_DATE " +
            "AND (bom.effectiveTo IS NULL OR bom.effectiveTo >= CURRENT_DATE) " +
            "ORDER BY bom.effectiveFrom DESC")
    Optional<BillOfMaterials> findCurrentBomByProduct(@Param("finishedProductId") Long finishedProductId);

    /**
     * Find valid BOMs on a specific date
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveFrom <= :date " +
            "AND (bom.effectiveTo IS NULL OR bom.effectiveTo >= :date) AND bom.isActive = true")
    List<BillOfMaterials> findValidBomsOnDate(@Param("date") LocalDate date);

    /**
     * Find current valid BOMs
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveFrom <= CURRENT_DATE " +
            "AND (bom.effectiveTo IS NULL OR bom.effectiveTo >= CURRENT_DATE) AND bom.isActive = true")
    List<BillOfMaterials> findCurrentValidBoms();

    /**
     * Find expired BOMs
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveTo < CURRENT_DATE")
    List<BillOfMaterials> findExpiredBoms();

    /**
     * Find future BOMs
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveFrom > CURRENT_DATE")
    List<BillOfMaterials> findFutureBoms();

    /**
     * Find BOMs with no end date
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveTo IS NULL AND bom.isActive = true")
    List<BillOfMaterials> findBomsWithNoEndDate();

    /**
     * Get BOM statistics
     */
    @Query("SELECT " +
            "COUNT(bom) as totalBoms, " +
            "SUM(CASE WHEN bom.isActive = true THEN 1 ELSE 0 END) as activeBoms, " +
            "COUNT(DISTINCT bom.finishedProduct.id) as productsWithBom, " +
            "AVG(bom.productionTimeMinutes) as avgProductionTime " +
            "FROM BillOfMaterials bom")
    Object getBomStatistics();

    /**
     * Get BOMs grouped by finished product
     */
    @Query("SELECT bom.finishedProduct.id, COUNT(bom) as bomCount " +
            "FROM BillOfMaterials bom WHERE bom.isActive = true " +
            "GROUP BY bom.finishedProduct.id ORDER BY bomCount DESC")
    List<Object[]> getBomsByProduct();

    /**
     * Find BOMs by effective date range
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveFrom BETWEEN :startDate AND :endDate")
    List<BillOfMaterials> findByEffectiveFromBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find all BOMs ordered by code
     */
    @Query("SELECT bom FROM BillOfMaterials bom ORDER BY bom.bomCode ASC")
    List<BillOfMaterials> findAllByOrderByBomCodeAsc();

    /**
     * Find active BOMs ordered by effective date
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.isActive = true ORDER BY bom.effectiveFrom DESC")
    List<BillOfMaterials> findByIsActiveTrueOrderByEffectiveFromDesc();

    /**
     * Find BOMs by finished product ordered by version
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId " +
            "ORDER BY bom.bomVersion DESC, bom.effectiveFrom DESC")
    List<BillOfMaterials> findByProductOrderByVersion(@Param("finishedProductId") Long finishedProductId);

    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveTo BETWEEN :now AND :expiryDate AND bom.isActive = true " +
            "ORDER BY bom.effectiveTo ASC")
    List<BillOfMaterials> findExpiringSoon(@Param("now") LocalDate now, @Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.effectiveFrom <= :date " +
            "AND (bom.effectiveTo IS NULL OR bom.effectiveTo >= :date) AND bom.isActive = true")
    List<BillOfMaterials> findValidBomsForDate(@Param("date") LocalDate date);

    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProduct.id = :finishedProductId " +
            "AND bom.effectiveFrom <= :date " +
            "AND (bom.effectiveTo IS NULL OR bom.effectiveTo >= :date) " +
            "AND bom.isActive = true")
    Optional<BillOfMaterials> findValidBomByProductAndDate(@Param("finishedProductId") Long finishedProductId, @Param("date") LocalDate date);

    @Query("SELECT bom FROM BillOfMaterials bom WHERE " +
            "LOWER(bom.bomCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<BillOfMaterials> searchBoms(@Param("keyword") String keyword, Pageable pageable);
}