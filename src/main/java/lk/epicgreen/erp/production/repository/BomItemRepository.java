package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.BomItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for BomItem entity
 * Based on ACTUAL database schema: bom_items table
 * 
 * Fields: bom_id (BIGINT), raw_material_id (BIGINT), quantity_required,
 *         uom_id (BIGINT), wastage_percentage, standard_cost,
 *         sequence_number, is_critical, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface BomItemRepository extends JpaRepository<BomItem, Long>, JpaSpecificationExecutor<BomItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for a BOM
     */
    List<BomItem> findByBomId(Long bomId);
    
    /**
     * Find all items for a BOM with pagination
     */
    Page<BomItem> findByBomId(Long bomId, Pageable pageable);
    
    /**
     * Find all items for a BOM ordered by sequence
     */
    List<BomItem> findByBomIdOrderBySequenceNumberAsc(Long bomId);
    
    /**
     * Find all items for a raw material
     */
    List<BomItem> findByRawMaterialId(Long rawMaterialId);
    
    /**
     * Find all items for a raw material with pagination
     */
    Page<BomItem> findByRawMaterialId(Long rawMaterialId, Pageable pageable);
    
    /**
     * Find items by BOM and raw material
     */
    List<BomItem> findByBomIdAndRawMaterialId(Long bomId, Long rawMaterialId);
    
    /**
     * Find critical items for a BOM
     */
    List<BomItem> findByBomIdAndIsCriticalTrue(Long bomId);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for a BOM
     */
    long countByBomId(Long bomId);
    
    /**
     * Count items for a raw material
     */
    long countByRawMaterialId(Long rawMaterialId);
    
    /**
     * Count critical items for a BOM
     */
    long countByBomIdAndIsCriticalTrue(Long bomId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for a BOM
     */
    @Modifying
    @Query("DELETE FROM BomItem bi WHERE bi.bomId = :bomId")
    void deleteAllByBomId(@Param("bomId") Long bomId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity required for a raw material across all BOMs
     */
    @Query("SELECT SUM(bi.quantityRequired) FROM BomItem bi WHERE bi.rawMaterialId = :rawMaterialId")
    BigDecimal getTotalQuantityRequiredByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Get total cost for a BOM
     */
    @Query("SELECT SUM(bi.standardCost * bi.quantityRequired) FROM BomItem bi WHERE bi.bomId = :bomId")
    BigDecimal getTotalCostByBom(@Param("bomId") Long bomId);
    
    /**
     * Get total quantity for a BOM
     */
    @Query("SELECT SUM(bi.quantityRequired) FROM BomItem bi WHERE bi.bomId = :bomId")
    BigDecimal getTotalQuantityByBom(@Param("bomId") Long bomId);
    
    /**
     * Find items with wastage
     */
    @Query("SELECT bi FROM BomItem bi WHERE bi.wastagePercentage > 0")
    List<BomItem> findItemsWithWastage();
    
    /**
     * Find items with wastage for a BOM
     */
    @Query("SELECT bi FROM BomItem bi WHERE bi.bomId = :bomId AND bi.wastagePercentage > 0")
    List<BomItem> findItemsWithWastageByBom(@Param("bomId") Long bomId);
    
    /**
     * Find all critical items
     */
    @Query("SELECT bi FROM BomItem bi WHERE bi.isCritical = true")
    List<BomItem> findCriticalItems();
    
    /**
     * Get BOM item statistics by raw material
     */
    @Query("SELECT bi.rawMaterialId, COUNT(bi) as bomCount, " +
           "SUM(bi.quantityRequired) as totalQuantity, AVG(bi.wastagePercentage) as avgWastage " +
           "FROM BomItem bi GROUP BY bi.rawMaterialId ORDER BY bomCount DESC")
    List<Object[]> getBomItemStatisticsByRawMaterial();
    
    /**
     * Get most used raw materials
     */
    @Query("SELECT bi.rawMaterialId, COUNT(DISTINCT bi.bomId) as bomCount, " +
           "SUM(bi.quantityRequired) as totalQuantity " +
           "FROM BomItem bi GROUP BY bi.rawMaterialId ORDER BY bomCount DESC")
    List<Object[]> getMostUsedRawMaterials(Pageable pageable);
    
    /**
     * Find items by BOM ordered by cost
     */
    @Query("SELECT bi FROM BomItem bi WHERE bi.bomId = :bomId " +
           "ORDER BY (bi.standardCost * bi.quantityRequired) DESC")
    List<BomItem> findByBomOrderByCost(@Param("bomId") Long bomId);
    
    /**
     * Find all items ordered by BOM
     */
    @Query("SELECT bi FROM BomItem bi ORDER BY bi.bomId, bi.sequenceNumber")
    List<BomItem> findAllOrderedByBom();
    
    /**
     * Find items with high wastage percentage
     */
    @Query("SELECT bi FROM BomItem bi WHERE bi.wastagePercentage >= :threshold")
    List<BomItem> findItemsWithHighWastage(@Param("threshold") BigDecimal threshold);
}
