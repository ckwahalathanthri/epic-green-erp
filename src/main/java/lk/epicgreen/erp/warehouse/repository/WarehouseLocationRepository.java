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
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.locationCode = :locationCode")
    Optional<WarehouseLocation> findByWarehouseIdAndLocationCode(@Param("warehouseId") Long warehouseId, @Param("locationCode") String locationCode);

    /**
     * Find all locations for a warehouse
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId")
    List<WarehouseLocation> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find all locations for a warehouse with pagination
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId")
    Page<WarehouseLocation> findByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);

    /**
     * Find active locations for a warehouse
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.isActive = true")
    List<WarehouseLocation> findByWarehouseIdAndIsActiveTrue(@Param("warehouseId") Long warehouseId);

    /**
     * Find all active locations
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.isActive = true")
    List<WarehouseLocation> findByIsActiveTrue();

    /**
     * Find all active locations with pagination
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.isActive = true")
    Page<WarehouseLocation> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive locations
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.isActive = false")
    List<WarehouseLocation> findByIsActiveFalse();

    /**
     * Find locations by location code
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.locationCode = :locationCode")
    List<WarehouseLocation> findByLocationCode(@Param("locationCode") String locationCode);

    /**
     * Find locations by aisle
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.aisle = :aisle")
    List<WarehouseLocation> findByWarehouseIdAndAisle(@Param("warehouseId") Long warehouseId, @Param("aisle") String aisle);

    /**
     * Find locations by rack
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.rack = :rack")
    List<WarehouseLocation> findByWarehouseIdAndRack(@Param("warehouseId") Long warehouseId, @Param("rack") String rack);

    /**
     * Find locations by shelf
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.shelf = :shelf")
    List<WarehouseLocation> findByWarehouseIdAndShelf(@Param("warehouseId") Long warehouseId, @Param("shelf") String shelf);

    /**
     * Find locations by bin
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.bin = :bin")
    List<WarehouseLocation> findByWarehouseIdAndBin(@Param("warehouseId") Long warehouseId, @Param("bin") String bin);

    /**
     * Find locations by aisle and rack
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.aisle = :aisle AND wl.rack = :rack")
    List<WarehouseLocation> findByWarehouseIdAndAisleAndRack(@Param("warehouseId") Long warehouseId, @Param("aisle") String aisle, @Param("rack") String rack);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if location code exists for warehouse
     */
    @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.locationCode = :locationCode")
    boolean existsByWarehouseIdAndLocationCode(@Param("warehouseId") Long warehouseId, @Param("locationCode") String locationCode);

    /**
     * Check if location code exists for warehouse excluding specific location ID
     */
    @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.locationCode = :locationCode AND wl.id <> :id")
    boolean existsByWarehouseIdAndLocationCodeAndIdNot(@Param("warehouseId") Long warehouseId, @Param("locationCode") String locationCode, @Param("id") Long id);

    // ==================== SEARCH METHODS ====================

    /**
     * Search locations by location code containing (case-insensitive)
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE LOWER(wl.locationCode) LIKE LOWER(CONCAT('%', :locationCode, '%'))")
    Page<WarehouseLocation> findByLocationCodeContainingIgnoreCase(@Param("locationCode") String locationCode, Pageable pageable);

    /**
     * Search locations by location name containing (case-insensitive)
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE LOWER(wl.locationName) LIKE LOWER(CONCAT('%', :locationName, '%'))")
    Page<WarehouseLocation> findByLocationNameContainingIgnoreCase(@Param("locationName") String locationName, Pageable pageable);

    /**
     * Search active locations by keyword for a warehouse
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.isActive = true AND " +
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
            "(:warehouseId IS NULL OR wl.warehouse.id = :warehouseId) AND " +
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
    @Query("SELECT COUNT(wl) FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId")
    long countByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Count active locations for a warehouse
     */
    @Query("SELECT COUNT(wl) FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.isActive = true")
    long countByWarehouseIdAndIsActiveTrue(@Param("warehouseId") Long warehouseId);

    /**
     * Count all active locations
     */
    @Query("SELECT COUNT(wl) FROM WarehouseLocation wl WHERE wl.isActive = true")
    long countByIsActiveTrue();

    /**
     * Count locations by aisle
     */
    @Query("SELECT COUNT(wl) FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.aisle = :aisle")
    long countByWarehouseIdAndAisle(@Param("warehouseId") Long warehouseId, @Param("aisle") String aisle);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find all distinct aisles for a warehouse
     */
    @Query("SELECT DISTINCT wl.aisle FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId " +
            "AND wl.aisle IS NOT NULL ORDER BY wl.aisle")
    List<String> findDistinctAislesByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find all distinct racks for a warehouse
     */
    @Query("SELECT DISTINCT wl.rack FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId " +
            "AND wl.rack IS NOT NULL ORDER BY wl.rack")
    List<String> findDistinctRacksByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find all distinct shelves for a warehouse
     */
    @Query("SELECT DISTINCT wl.shelf FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId " +
            "AND wl.shelf IS NOT NULL ORDER BY wl.shelf")
    List<String> findDistinctShelvesByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find all distinct bins for a warehouse
     */
    @Query("SELECT DISTINCT wl.bin FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId " +
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
            "FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId")
    Object getLocationStatistics(@Param("warehouseId") Long warehouseId);

    /**
     * Find locations with inventory
     */
    @Query("SELECT DISTINCT wl FROM WarehouseLocation wl " +
            "WHERE EXISTS (SELECT 1 FROM Inventory i WHERE i.location.id = wl.id)")
    List<WarehouseLocation> findLocationsWithInventory();

    /**
     * Find empty locations (no inventory)
     */
    @Query("SELECT wl FROM WarehouseLocation wl " +
            "WHERE wl.warehouse.id = :warehouseId AND wl.isActive = true AND " +
            "NOT EXISTS (SELECT 1 FROM Inventory i WHERE i.location.id = wl.id)")
    List<WarehouseLocation> findEmptyLocations(@Param("warehouseId") Long warehouseId);

    /**
     * Get locations grouped by aisle
     */
    @Query("SELECT wl.aisle, COUNT(wl) as locationCount " +
            "FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId " +
            "GROUP BY wl.aisle ORDER BY wl.aisle")
    List<Object[]> getLocationsByAisle(@Param("warehouseId") Long warehouseId);

    /**
     * Find all locations for warehouse ordered by location code
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId ORDER BY wl.locationCode ASC")
    List<WarehouseLocation> findByWarehouseIdOrderByLocationCodeAsc(@Param("warehouseId") Long warehouseId);

    /**
     * Find active locations for warehouse ordered by location code
     */
    @Query("SELECT wl FROM WarehouseLocation wl WHERE wl.warehouse.id = :warehouseId AND wl.isActive = true ORDER BY wl.locationCode ASC")
    List<WarehouseLocation> findByWarehouseIdAndIsActiveTrueOrderByLocationCodeAsc(@Param("warehouseId") Long warehouseId);

    @Query("SELECT wl FROM WarehouseLocation wl WHERE " +
            "LOWER(wl.locationCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(wl.locationName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<WarehouseLocation> searchLocations(@Param("keyword") String keyword);
}