package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UnitOfMeasure entity (CORRECTED)
 * Matches actual database schema: base_unit (BOOLEAN), base_uom_id, uom_type, is_active
 * (NO symbol field, baseUnit is ID not entity)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long>, JpaSpecificationExecutor<UnitOfMeasure> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find unit of measure by code
     */
    Optional<UnitOfMeasure> findByUomCode(String uomCode);
    
    /**
     * Find unit of measure by name
     */
    Optional<UnitOfMeasure> findByUomName(String uomName);
    
    /**
     * Find all active units of measure
     */
    List<UnitOfMeasure> findByIsActiveTrue();
    
    /**
     * Find all active units with pagination
     */
    Page<UnitOfMeasure> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive units
     */
    List<UnitOfMeasure> findByIsActiveFalse();
    
    /**
     * Find base units (base_unit = true)
     */
    List<UnitOfMeasure> findByBaseUnitTrue();
    
    /**
     * Find derived units (base_unit = false)
     */
    List<UnitOfMeasure> findByBaseUnitFalse();
    
    /**
     * Find units by base UOM ID (base_uom_id)
     */
    List<UnitOfMeasure> findByBaseUomId(Long baseUomId);
    
    /**
     * Find units by UOM type
     */
    List<UnitOfMeasure> findByUomType(String uomType);
    
    /**
     * Find units by UOM type with pagination
     */
    Page<UnitOfMeasure> findByUomType(String uomType, Pageable pageable);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if UOM code exists
     */
    boolean existsByUomCode(String uomCode);
    
    /**
     * Check if UOM name exists
     */
    boolean existsByUomName(String uomName);
    
    /**
     * Check if UOM code exists excluding specific UOM ID
     */
    boolean existsByUomCodeAndIdNot(String uomCode, Long id);
    
    /**
     * Check if UOM name exists excluding specific UOM ID
     */
    boolean existsByUomNameAndIdNot(String uomName, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search units by UOM code containing (case-insensitive)
     */
    Page<UnitOfMeasure> findByUomCodeContainingIgnoreCase(String uomCode, Pageable pageable);
    
    /**
     * Search units by UOM name containing (case-insensitive)
     */
    Page<UnitOfMeasure> findByUomNameContainingIgnoreCase(String uomName, Pageable pageable);
    
    /**
     * Search active units by keyword
     */
    @Query("SELECT u FROM UnitOfMeasure u WHERE u.isActive = true AND " +
           "(LOWER(u.uomCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.uomName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<UnitOfMeasure> searchActiveUnits(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search units by multiple criteria
     */
    @Query("SELECT u FROM UnitOfMeasure u WHERE " +
           "(:uomCode IS NULL OR LOWER(u.uomCode) LIKE LOWER(CONCAT('%', :uomCode, '%'))) AND " +
           "(:uomName IS NULL OR LOWER(u.uomName) LIKE LOWER(CONCAT('%', :uomName, '%'))) AND " +
           "(:uomType IS NULL OR u.uomType = :uomType) AND " +
           "(:isActive IS NULL OR u.isActive = :isActive) AND " +
           "(:baseUnit IS NULL OR u.baseUnit = :baseUnit)")
    Page<UnitOfMeasure> searchUnits(
            @Param("uomCode") String uomCode,
            @Param("uomName") String uomName,
            @Param("uomType") String uomType,
            @Param("isActive") Boolean isActive,
            @Param("baseUnit") Boolean baseUnit,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active units
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive units
     */
    long countByIsActiveFalse();
    
    /**
     * Count base units
     */
    long countByBaseUnitTrue();
    
    /**
     * Count derived units for a base UOM
     */
    long countByBaseUomId(Long baseUomId);
    
    /**
     * Count units by type
     */
    long countByUomType(String uomType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find active base units
     */
    List<UnitOfMeasure> findByIsActiveTrueAndBaseUnitTrue();
    
    /**
     * Find active derived units for a base UOM
     */
    @Query("SELECT u FROM UnitOfMeasure u WHERE u.baseUomId = :baseUomId AND u.isActive = true")
    List<UnitOfMeasure> findActiveDerivedUnitsByBaseUomId(@Param("baseUomId") Long baseUomId);
    
    /**
     * Get unit statistics
     */
    @Query("SELECT " +
           "COUNT(u) as totalUnits, " +
           "SUM(CASE WHEN u.isActive = true THEN 1 ELSE 0 END) as activeUnits, " +
           "SUM(CASE WHEN u.baseUnit = true THEN 1 ELSE 0 END) as baseUnits " +
           "FROM UnitOfMeasure u")
    Object getUnitStatistics();
    
    /**
     * Get conversion factors for a base UOM
     */
    @Query("SELECT u.uomCode, u.uomName, u.conversionFactor " +
           "FROM UnitOfMeasure u WHERE u.baseUomId = :baseUomId AND u.isActive = true " +
           "ORDER BY u.conversionFactor")
    List<Object[]> getConversionFactorsByBaseUom(@Param("baseUomId") Long baseUomId);
    
    /**
     * Find units without base UOM (orphaned derived units)
     */
    @Query("SELECT u FROM UnitOfMeasure u WHERE u.baseUnit = false AND u.baseUomId IS NULL")
    List<UnitOfMeasure> findOrphanedDerivedUnits();
    
    /**
     * Find all units ordered by UOM code
     */
    List<UnitOfMeasure> findAllByOrderByUomCodeAsc();
    
    /**
     * Find active units ordered by UOM name
     */
    List<UnitOfMeasure> findByIsActiveTrueOrderByUomNameAsc();
    
    /**
     * Find units by type and active status
     */
    List<UnitOfMeasure> findByUomTypeAndIsActiveTrue(String uomType);
    
    /**
     * Get units count by type
     */
    @Query("SELECT u.uomType, COUNT(u) as unitCount " +
           "FROM UnitOfMeasure u GROUP BY u.uomType ORDER BY unitCount DESC")
    List<Object[]> getUnitCountByType();
}
