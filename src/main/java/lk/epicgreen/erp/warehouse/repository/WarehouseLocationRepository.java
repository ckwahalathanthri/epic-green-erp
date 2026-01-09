package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
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
 * Repository interface for WarehouseLocation entity
 * Based on ACTUAL database schema: warehouse_locations table
 * 
 * Fields: warehouse_id (BIGINT), location_code, location_name, aisle, rack, shelf, bin, is_active
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long>, JpaSpecificationExecutor<WarehouseLocation> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find location by warehouse ID and location code
     */
    Optional<WarehouseLocation> findByWarehouseIdAndLocationCode(Long warehouseId, String locationCode);
    
    /**
     * Find all locations for a warehouse
     */
    List<WarehouseLocation> findByWarehouseId(Long warehouseId);
    
    /**
     * Find all locations for a warehouse with pagination
     */
    Page<WarehouseLocation> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find active locations for a warehouse
     */
    List<WarehouseLocation> findByWarehouseIdAndIsActiveTrue(Long warehouseId);
    
    /**
     * Find all active locations
     */
    List<WarehouseLocation> findByIsActiveTrue();
    
    /**
     * Find all active locations with pagination
     */
    Page<WarehouseLocation> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive locations
     */
    List<WarehouseLocation> findByIsActiveFalse();
    
    /**
     * Find locations by location code
     */
    List<WarehouseLocation> findByLocationCode(String locationCode);
    
    /**
     * Find locations by aisle
     */
    List<WarehouseLocation> findByWarehouseIdAndAisle(Long warehouseId, String aisle);
    
    /**
     * Find locations by rack
     */
    List<WarehouseLocation> findByWarehouseIdAndRack(Long warehouseId, String rack);
    
    /**
     * Find locations by shelf
     */
    List<WarehouseLocation> findByWarehouseIdAndShelf(Long warehouseId, String shelf);
    
    /**
     * Find locations by bin
     */
    List<WarehouseLocation> findByWarehouseIdAndBin(Long warehouseId, String bin);
    
    /**
     * Find locations by aisle and rack
     */
    List<WarehouseLocation> findByWarehouseIdAndAisleAndRack(Long warehouseId, String aisle, String rack);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if location code exists for warehouse
     */
    boolean existsByWarehouseIdAndLocationCode(Long warehouseId, String locationCode);
    
    /**
     * Check if location code exists for warehouse excluding specific location ID
     */
    boolean existsByWarehouseIdAndLocationCodeAndIdNot(Long warehouseId, String locationCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search locations by location code containing (case-insensitive)
     */
    Page<WarehouseLocation> findByLocationCodeContainingIgnoreCase(String locationCode, Pageable pageable);
    
    /**
     * Search locations by location name containing (case-insensitive)
     */
    Page<WarehouseLocation> findByLocationNameContainingIgnoreCase(String locationName, Pageable pageable);
    
    /**
     * Search active locations by keyword for a warehouse
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId AND wl.isActive = true AND " +
           "(LOWER(wl.locationCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(wl.locationName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<WarehouseLocation> searchActiveLocations(
            @Param("warehouseId") Long warehouseId,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    /**
     * Search locations by multiple criteria
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE " +
           "(:warehouseId IS NULL OR wl.warehouseId = :warehouseId) AND " +
           "(:locationCode IS NULL OR LOWER(wl.locationCode) LIKE LOWER(CONCAT('%', :locationCode, '%'))) AND " +
           "(:locationName IS NULL OR LOWER(wl.locationName) LIKE LOWER(CONCAT('%', :locationName, '%'))) AND " +
           "(:aisle IS NULL OR wl.aisle = :aisle) AND " +
           "(:rack IS NULL OR wl.rack = :rack) AND " +
           "(:isActive IS NULL OR wl.isActive = :isActive)")
    Page<WarehouseLocation> searchLocations(
            @Param("warehouseId") Long warehouseId,
            @Param("locationCode") String locationCode,
            @Param("locationName") String locationName,
            @Param("aisle") String aisle,
            @Param("rack") String rack,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count locations for a warehouse
     */
    long countByWarehouseId(Long warehouseId);
    
    /**
     * Count active locations for a warehouse
     */
    long countByWarehouseIdAndIsActiveTrue(Long warehouseId);
    
    /**
     * Count all active locations
     */
    long countByIsActiveTrue();
    
    /**
     * Count locations by aisle
     */
    long countByWarehouseIdAndAisle(Long warehouseId, String aisle);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find all distinct aisles for a warehouse
     */
    @Query("SELECT DISTINCT wl.aisle FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId " +
           "AND wl.aisle IS NOT NULL ORDER BY wl.aisle")
    List<String> findDistinctAislesByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find all distinct racks for a warehouse
     */
    @Query("SELECT DISTINCT wl.rack FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId " +
           "AND wl.rack IS NOT NULL ORDER BY wl.rack")
    List<String> findDistinctRacksByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find all distinct shelves for a warehouse
     */
    @Query("SELECT DISTINCT wl.shelf FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId " +
           "AND wl.shelf IS NOT NULL ORDER BY wl.shelf")
    List<String> findDistinctShelvesByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find all distinct bins for a warehouse
     */
    @Query("SELECT DISTINCT wl.bin FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId " +
           "AND wl.bin IS NOT NULL ORDER BY wl.bin")
    List<String> findDistinctBinsByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get location statistics for a warehouse
     */
    @Query("SELECT " +
           "COUNT(wl) as totalLocations, " +
           "SUM(CASE WHEN wl.isActive = true THEN 1 ELSE 0 END) as activeLocations, " +
           "COUNT(DISTINCT wl.aisle) as totalAisles, " +
           "COUNT(DISTINCT wl.rack) as totalRacks " +
           "FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId")
    Object getLocationStatistics(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find locations with inventory
     */
    @Query("SELECT DISTINCT wl FROM WarehouseLocation wl " +
           "WHERE EXISTS (SELECT 1 FROM Inventory i WHERE i.locationId = wl.id)")
    List<WarehouseLocation> findLocationsWithInventory();
    
    /**
     * Find empty locations (no inventory)
     */
    @Query("SELECT wl FROM WarehouseLocation wl " +
           "WHERE wl.warehouseId = :warehouseId AND wl.isActive = true AND " +
           "NOT EXISTS (SELECT 1 FROM Inventory i WHERE i.locationId = wl.id)")
    List<WarehouseLocation> findEmptyLocations(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get locations grouped by aisle
     */
    @Query("SELECT wl.aisle, COUNT(wl) as locationCount " +
           "FROM WarehouseLocation wl WHERE wl.warehouseId = :warehouseId " +
           "GROUP BY wl.aisle ORDER BY wl.aisle")
    List<Object[]> getLocationsByAisle(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find all locations for warehouse ordered by location code
     */
    List<WarehouseLocation> findByWarehouseIdOrderByLocationCodeAsc(Long warehouseId);
    
    /**
     * Find active locations for warehouse ordered by location code
     */
    List<WarehouseLocation> findByWarehouseIdAndIsActiveTrueOrderByLocationCodeAsc(Long warehouseId);

    List<WarehouseLocation> searchLocations(String keyword);
}
