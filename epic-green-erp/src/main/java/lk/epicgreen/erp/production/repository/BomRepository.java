package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.BillOfMaterials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * BOM Repository
 * Repository for bill of materials data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface BomRepository extends JpaRepository<BillOfMaterials, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find BOM by BOM code
     */
    Optional<BillOfMaterials> findByBomCode(String bomCode);
    
    /**
     * Check if BOM exists by BOM code
     */
    boolean existsByBomCode(String bomCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find BOMs by product ID
     */
    List<BillOfMaterials> findByProductId(Long productId);
    
    /**
     * Find BOMs by product ID with pagination
     */
    Page<BillOfMaterials> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find BOMs by BOM type
     */
    List<BillOfMaterials> findByBomType(String bomType);
    
    /**
     * Find BOMs by BOM type with pagination
     */
    Page<BillOfMaterials> findByBomType(String bomType, Pageable pageable);
    
    /**
     * Find BOMs by status
     */
    List<BillOfMaterials> findByStatus(String status);
    
    /**
     * Find BOMs by status with pagination
     */
    Page<BillOfMaterials> findByStatus(String status, Pageable pageable);
    
    /**
     * Find BOMs by version
     */
    List<BillOfMaterials> findByVersion(String version);
    
    /**
     * Find BOMs by created by user
     */
    List<BillOfMaterials> findByCreatedByUserId(Long userId);
    
    /**
     * Find BOMs by approved by user
     */
    List<BillOfMaterials> findByApprovedByUserId(Long userId);
    
    /**
     * Find BOMs by is active
     */
    List<BillOfMaterials> findByIsActive(Boolean isActive);
    
    /**
     * Find BOMs by is active with pagination
     */
    Page<BillOfMaterials> findByIsActive(Boolean isActive, Pageable pageable);
    
    /**
     * Find BOMs by is approved
     */
    List<BillOfMaterials> findByIsApproved(Boolean isApproved);
    
    /**
     * Find BOMs by is default
     */
    List<BillOfMaterials> findByIsDefault(Boolean isDefault);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find BOMs by effective date between dates
     */
    List<BillOfMaterials> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find BOMs by expiry date between dates
     */
    List<BillOfMaterials> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find BOMs by created at between dates
     */
    List<BillOfMaterials> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find BOMs by product ID and status
     */
    List<BillOfMaterials> findByProductIdAndStatus(Long productId, String status);
    
    /**
     * Find BOMs by product ID and is active
     */
    List<BillOfMaterials> findByProductIdAndIsActive(Long productId, Boolean isActive);
    
    /**
     * Find BOMs by product ID and is default
     */
    Optional<BillOfMaterials> findByProductIdAndIsDefault(Long productId, Boolean isDefault);
    
    /**
     * Find BOMs by BOM type and status
     */
    List<BillOfMaterials> findByBomTypeAndStatus(String bomType, String status);
    
    /**
     * Find BOMs by status and is active
     */
    List<BillOfMaterials> findByStatusAndIsActive(String status, Boolean isActive);
    
    /**
     * Find BOMs by is approved and status
     */
    List<BillOfMaterials> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE " +
           "LOWER(b.bomCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.bomName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<BillOfMaterials> searchBoms(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.isActive = true AND b.status = 'ACTIVE' " +
           "ORDER BY b.bomName ASC")
    List<BillOfMaterials> findActiveBoms();
    
    /**
     * Find active BOMs with pagination
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.isActive = true AND b.status = 'ACTIVE' " +
           "ORDER BY b.bomName ASC")
    Page<BillOfMaterials> findActiveBoms(Pageable pageable);
    
    /**
     * Find draft BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.status = 'DRAFT' " +
           "ORDER BY b.createdAt DESC")
    List<BillOfMaterials> findDraftBoms();
    
    /**
     * Find approved BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.status = 'APPROVED' AND b.isApproved = true " +
           "ORDER BY b.bomName ASC")
    List<BillOfMaterials> findApprovedBoms();
    
    /**
     * Find obsolete BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.status = 'OBSOLETE' " +
           "ORDER BY b.bomName ASC")
    List<BillOfMaterials> findObsoleteBoms();
    
    /**
     * Find BOMs pending approval
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.isApproved = false " +
           "AND b.status NOT IN ('DRAFT', 'OBSOLETE') " +
           "ORDER BY b.createdAt ASC")
    List<BillOfMaterials> findBomsPendingApproval();
    
    /**
     * Find active default BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.isDefault = true " +
           "AND b.isActive = true " +
           "ORDER BY b.productName ASC")
    List<BillOfMaterials> findActiveDefaultBoms();
    
    /**
     * Find product active BOM
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.productId = :productId " +
           "AND b.isActive = true AND b.isDefault = true " +
           "ORDER BY b.version DESC")
    Optional<BillOfMaterials> findProductActiveBom(@Param("productId") Long productId);
    
    /**
     * Find product BOMs by version
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.productId = :productId " +
           "ORDER BY b.version DESC")
    List<BillOfMaterials> findProductBomsByVersion(@Param("productId") Long productId);
    
    /**
     * Find effective BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.effectiveDate <= :currentDate " +
           "AND (b.expiryDate IS NULL OR b.expiryDate >= :currentDate) " +
           "AND b.isActive = true " +
           "ORDER BY b.bomName ASC")
    List<BillOfMaterials> findEffectiveBoms(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find expired BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.expiryDate < :currentDate " +
           "AND b.isActive = true " +
           "ORDER BY b.expiryDate ASC")
    List<BillOfMaterials> findExpiredBoms(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find expiring soon BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.expiryDate BETWEEN :startDate AND :endDate " +
           "AND b.isActive = true " +
           "ORDER BY b.expiryDate ASC")
    List<BillOfMaterials> findExpiringSoonBoms(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);
    
    /**
     * Find recent BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b ORDER BY b.createdAt DESC")
    List<BillOfMaterials> findRecentBoms(Pageable pageable);
    
    /**
     * Find product recent BOMs
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.productId = :productId " +
           "ORDER BY b.createdAt DESC")
    List<BillOfMaterials> findProductRecentBoms(@Param("productId") Long productId, Pageable pageable);
    
    /**
     * Find BOMs requiring action
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE " +
           "(b.isApproved = false AND b.status = 'PENDING') OR " +
           "(b.expiryDate <= :thresholdDate AND b.isActive = true) " +
           "ORDER BY b.expiryDate ASC")
    List<BillOfMaterials> findBomsRequiringAction(@Param("thresholdDate") LocalDate thresholdDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count BOMs by product
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * Count BOMs by BOM type
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.bomType = :bomType")
    Long countByBomType(@Param("bomType") String bomType);
    
    /**
     * Count BOMs by status
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count active BOMs
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.isActive = true")
    Long countActiveBoms();
    
    /**
     * Count BOMs pending approval
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.isApproved = false " +
           "AND b.status NOT IN ('DRAFT', 'OBSOLETE')")
    Long countBomsPendingApproval();
    
    /**
     * Count expired BOMs
     */
    @Query("SELECT COUNT(b) FROM BillOfMaterials b WHERE b.expiryDate < :currentDate " +
           "AND b.isActive = true")
    Long countExpiredBoms(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Get BOM type distribution
     */
    @Query("SELECT b.bomType, COUNT(b) as bomCount FROM BillOfMaterials b " +
           "GROUP BY b.bomType ORDER BY bomCount DESC")
    List<Object[]> getBomTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT b.status, COUNT(b) as bomCount FROM BillOfMaterials b " +
           "GROUP BY b.status ORDER BY bomCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly BOM creation count
     */
    @Query("SELECT YEAR(b.createdAt) as year, MONTH(b.createdAt) as month, " +
           "COUNT(b) as bomCount FROM BillOfMaterials b " +
           "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyBomCreationCount(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get products with BOMs
     */
    @Query("SELECT b.productId, b.productName, COUNT(b) as bomCount FROM BillOfMaterials b " +
           "GROUP BY b.productId, b.productName ORDER BY bomCount DESC")
    List<Object[]> getProductsWithBoms();
    
    /**
     * Find BOMs by tags
     */
    @Query("SELECT b FROM BillOfMaterials b WHERE b.tags LIKE CONCAT('%', :tag, '%')")
    List<BillOfMaterials> findByTag(@Param("tag") String tag);
}
