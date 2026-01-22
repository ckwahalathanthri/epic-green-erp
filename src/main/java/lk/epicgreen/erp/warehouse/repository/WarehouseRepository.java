package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Warehouse;
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
 * Repository interface for Warehouse entity
 * Based on ACTUAL database schema: warehouses table
 *
 * Fields: warehouse_code, warehouse_name, warehouse_type (ENUM: RAW_MATERIAL, FINISHED_GOODS, MIXED),
 *         address_line1, address_line2, city, state, postal_code,
 *         manager_id (BIGINT), contact_number, is_active
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    // ==================== FINDER METHODS ====================

    /**
     * Find warehouse by warehouse code
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseCode = :warehouseCode")
    Optional<Warehouse> findByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    /**
     * Find warehouse by warehouse name
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseName = :warehouseName")
    Optional<Warehouse> findByWarehouseName(@Param("warehouseName") String warehouseName);

    /**
     * Find all active warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true")
    List<Warehouse> findByIsActiveTrue();

    /**
     * Find all active warehouses with pagination
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true")
    Page<Warehouse> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = false")
    List<Warehouse> findByIsActiveFalse();

    /**
     * Find warehouses by type
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = :warehouseType")
    List<Warehouse> findByWarehouseType(@Param("warehouseType") String warehouseType);

    /**
     * Find warehouses by type with pagination
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = :warehouseType")
    Page<Warehouse> findByWarehouseType(@Param("warehouseType") String warehouseType, Pageable pageable);

    /**
     * Find active warehouses by type
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = :warehouseType AND w.isActive = true")
    List<Warehouse> findByWarehouseTypeAndIsActiveTrue(@Param("warehouseType") String warehouseType);

    /**
     * Find warehouses by manager ID
     */
    @Query("SELECT w FROM Warehouse w WHERE w.manager.id = :managerId")
    List<Warehouse> findByManagerId(@Param("managerId") Long managerId);

    /**
     * Find warehouses by city
     */
    @Query("SELECT w FROM Warehouse w WHERE w.city = :city")
    List<Warehouse> findByCity(@Param("city") String city);

    /**
     * Find warehouses by state
     */
    @Query("SELECT w FROM Warehouse w WHERE w.state = :state")
    List<Warehouse> findByState(@Param("state") String state);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if warehouse code exists
     */
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Warehouse w WHERE w.warehouseCode = :warehouseCode")
    boolean existsByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    /**
     * Check if warehouse name exists
     */
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Warehouse w WHERE w.warehouseName = :warehouseName")
    boolean existsByWarehouseName(@Param("warehouseName") String warehouseName);

    /**
     * Check if warehouse code exists excluding specific warehouse ID
     */
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Warehouse w WHERE w.warehouseCode = :warehouseCode AND w.id <> :id")
    boolean existsByWarehouseCodeAndIdNot(@Param("warehouseCode") String warehouseCode, @Param("id") Long id);

    /**
     * Check if warehouse name exists excluding specific warehouse ID
     */
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Warehouse w WHERE w.warehouseName = :warehouseName AND w.id <> :id")
    boolean existsByWarehouseNameAndIdNot(@Param("warehouseName") String warehouseName, @Param("id") Long id);

    // ==================== SEARCH METHODS ====================

    /**
     * Search warehouses by warehouse code containing (case-insensitive)
     */
    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :warehouseCode, '%'))")
    Page<Warehouse> findByWarehouseCodeContainingIgnoreCase(@Param("warehouseCode") String warehouseCode, Pageable pageable);

    /**
     * Search warehouses by warehouse name containing (case-insensitive)
     */
    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :warehouseName, '%'))")
    Page<Warehouse> findByWarehouseNameContainingIgnoreCase(@Param("warehouseName") String warehouseName, Pageable pageable);

    /**
     * Search active warehouses by keyword
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true AND " +
            "(LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Warehouse> searchActiveWarehouses(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Search warehouses by multiple criteria
     */
    @Query("SELECT w FROM Warehouse w WHERE " +
            "(:warehouseCode IS NULL OR LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :warehouseCode, '%'))) AND " +
            "(:warehouseName IS NULL OR LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :warehouseName, '%'))) AND " +
            "(:warehouseType IS NULL OR w.warehouseType = :warehouseType) AND " +
            "(:city IS NULL OR w.city = :city) AND " +
            "(:isActive IS NULL OR w.isActive = :isActive)")
    Page<Warehouse> searchWarehouses(
            @Param("warehouseCode") String warehouseCode,
            @Param("warehouseName") String warehouseName,
            @Param("warehouseType") String warehouseType,
            @Param("city") String city,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count active warehouses
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = true")
    long countByIsActiveTrue();

    /**
     * Count inactive warehouses
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = false")
    long countByIsActiveFalse();

    /**
     * Count warehouses by type
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.warehouseType = :warehouseType")
    long countByWarehouseType(@Param("warehouseType") String warehouseType);

    /**
     * Count warehouses by manager
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.manager.id = :managerId")
    long countByManagerId(@Param("managerId") Long managerId);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find raw material warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'RAW_MATERIAL' AND w.isActive = true")
    List<Warehouse> findRawMaterialWarehouses();

    /**
     * Find finished goods warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'FINISHED_GOODS' AND w.isActive = true")
    List<Warehouse> findFinishedGoodsWarehouses();

    /**
     * Find mixed warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'MIXED' AND w.isActive = true")
    List<Warehouse> findMixedWarehouses();

    /**
     * Find all distinct cities
     */
    @Query("SELECT DISTINCT w.city FROM Warehouse w WHERE w.city IS NOT NULL ORDER BY w.city")
    List<String> findAllDistinctCities();

    /**
     * Find all distinct states
     */
    @Query("SELECT DISTINCT w.state FROM Warehouse w WHERE w.state IS NOT NULL ORDER BY w.state")
    List<String> findAllDistinctStates();

    /**
     * Get warehouse statistics
     */
    @Query("SELECT " +
            "COUNT(w) as totalWarehouses, " +
            "SUM(CASE WHEN w.isActive = true THEN 1 ELSE 0 END) as activeWarehouses, " +
            "SUM(CASE WHEN w.warehouseType = 'RAW_MATERIAL' THEN 1 ELSE 0 END) as rawMaterialWarehouses, " +
            "SUM(CASE WHEN w.warehouseType = 'FINISHED_GOODS' THEN 1 ELSE 0 END) as finishedGoodsWarehouses, " +
            "SUM(CASE WHEN w.warehouseType = 'MIXED' THEN 1 ELSE 0 END) as mixedWarehouses " +
            "FROM Warehouse w")
    Object getWarehouseStatistics();

    /**
     * Get warehouses grouped by type
     */
    @Query("SELECT w.warehouseType, COUNT(w) as warehouseCount " +
            "FROM Warehouse w WHERE w.isActive = true GROUP BY w.warehouseType")
    List<Object[]> getWarehousesByType();

    /**
     * Get warehouses grouped by city
     */
    @Query("SELECT w.city, COUNT(w) as warehouseCount " +
            "FROM Warehouse w WHERE w.isActive = true AND w.city IS NOT NULL " +
            "GROUP BY w.city ORDER BY warehouseCount DESC")
    List<Object[]> getWarehousesByCity();

    /**
     * Find warehouses without manager
     */
    @Query("SELECT w FROM Warehouse w WHERE w.manager.id IS NULL AND w.isActive = true")
    List<Warehouse> findWarehousesWithoutManager();

    /**
     * Find all warehouses ordered by code
     */
    @Query("SELECT w FROM Warehouse w ORDER BY w.warehouseCode ASC")
    List<Warehouse> findAllByOrderByWarehouseCodeAsc();

    /**
     * Find active warehouses ordered by name
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findByIsActiveTrueOrderByWarehouseNameAsc();

    /**
     * Find warehouses by type and city
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = :warehouseType AND w.city = :city AND w.isActive = true")
    List<Warehouse> findByWarehouseTypeAndCityAndIsActiveTrue(@Param("warehouseType") String warehouseType, @Param("city") String city);

    @Query("SELECT w FROM Warehouse w WHERE " +
            "LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Warehouse> searchWarehouses(@Param("keyword") String keyword, Pageable pageable);
}