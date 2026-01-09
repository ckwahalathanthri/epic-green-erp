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
    Optional<BillOfMaterials> findByBomCode(String bomCode);
    
    /**
     * Find all BOMs for a finished product
     */
    List<BillOfMaterials> findByFinishedProductId(Long finishedProductId);
    
    /**
     * Find all BOMs for a finished product with pagination
     */
    Page<BillOfMaterials> findByFinishedProductId(Long finishedProductId, Pageable pageable);
    
    /**
     * Find all active BOMs
     */
    List<BillOfMaterials> findByIsActiveTrue();
    
    /**
     * Find all active BOMs with pagination
     */
    Page<BillOfMaterials> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive BOMs
     */
    List<BillOfMaterials> findByIsActiveFalse();
    
    /**
     * Find active BOMs for a finished product
     */
    Optional<BillOfMaterials> findByFinishedProductIdAndIsActiveTrue(Long finishedProductId);
    
    /**
     * Find BOMs by version
     */
    List<BillOfMaterials> findByBomVersion(String bomVersion);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if BOM code exists
     */
    boolean existsByBomCode(String bomCode);
    
    /**
     * Check if BOM code exists excluding specific BOM ID
     */
    boolean existsByBomCodeAndIdNot(String bomCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search BOMs by BOM code containing (case-insensitive)
     */
    Page<BillOfMaterials> findByBomCodeContainingIgnoreCase(String bomCode, Pageable pageable);
    
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
           "(:finishedProductId IS NULL OR bom.finishedProductId = :finishedProductId) AND " +
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
    long countByIsActiveTrue();
    
    /**
     * Count inactive BOMs
     */
    long countByIsActiveFalse();
    
    /**
     * Count BOMs for a product
     */
    long countByFinishedProductId(Long finishedProductId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find current active BOM for a finished product
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProductId = :finishedProductId " +
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
           "COUNT(DISTINCT bom.finishedProductId) as productsWithBom, " +
           "AVG(bom.productionTimeMinutes) as avgProductionTime " +
           "FROM BillOfMaterials bom")
    Object getBomStatistics();
    
    /**
     * Get BOMs grouped by finished product
     */
    @Query("SELECT bom.finishedProductId, COUNT(bom) as bomCount " +
           "FROM BillOfMaterials bom WHERE bom.isActive = true " +
           "GROUP BY bom.finishedProductId ORDER BY bomCount DESC")
    List<Object[]> getBomsByProduct();
    
    /**
     * Find BOMs by effective date range
     */
    List<BillOfMaterials> findByEffectiveFromBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find all BOMs ordered by code
     */
    List<BillOfMaterials> findAllByOrderByBomCodeAsc();
    
    /**
     * Find active BOMs ordered by effective date
     */
    List<BillOfMaterials> findByIsActiveTrueOrderByEffectiveFromDesc();
    
    /**
     * Find BOMs by finished product ordered by version
     */
    @Query("SELECT bom FROM BillOfMaterials bom WHERE bom.finishedProductId = :finishedProductId " +
           "ORDER BY bom.bomVersion DESC, bom.effectiveFrom DESC")
    List<BillOfMaterials> findByProductOrderByVersion(@Param("finishedProductId") Long finishedProductId);

    List<BillOfMaterials> findExpiringSoon(LocalDate now, LocalDate expiryDate);

    List<BillOfMaterials> findValidBomsForDate(LocalDate date);

    Optional<BillOfMaterials> findValidBomByProductAndDate(Long finishedProductId, LocalDate date);

    Page<BillOfMaterials> searchBoms(String keyword, Pageable pageable);
}
