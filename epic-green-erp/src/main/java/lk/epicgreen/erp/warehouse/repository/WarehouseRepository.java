package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Warehouse Repository
 * Repository for warehouse data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find warehouse by code
     */
    Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    
    /**
     * Check if warehouse exists by code
     */
    boolean existsByWarehouseCode(String warehouseCode);
    
    /**
     * Find warehouse by name
     */
    Optional<Warehouse> findByWarehouseName(String warehouseName);
    
    /**
     * Check if warehouse exists by name
     */
    boolean existsByWarehouseName(String warehouseName);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find warehouses by type
     */
    List<Warehouse> findByWarehouseType(String warehouseType);
    
    /**
     * Find warehouses by status
     */
    List<Warehouse> findByStatus(String status);
    
    /**
     * Find warehouses by is active
     */
    List<Warehouse> findByIsActive(Boolean isActive);
    
    /**
     * Find warehouses by is default
     */
    List<Warehouse> findByIsDefault(Boolean isDefault);
    
    /**
     * Find warehouses by manager ID
     */
    List<Warehouse> findByManagerId(Long managerId);
    
    /**
     * Find warehouses by city
     */
    List<Warehouse> findByCity(String city);
    
    /**
     * Find warehouses by state/province
     */
    List<Warehouse> findByStateProvince(String stateProvince);
    
    /**
     * Find warehouses by country
     */
    List<Warehouse> findByCountry(String country);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find warehouses by type and status
     */
    List<Warehouse> findByWarehouseTypeAndStatus(String warehouseType, String status);
    
    /**
     * Find warehouses by type and is active
     */
    List<Warehouse> findByWarehouseTypeAndIsActive(String warehouseType, Boolean isActive);
    
    /**
     * Find warehouses by status and is active
     */
    List<Warehouse> findByStatusAndIsActive(String status, Boolean isActive);
    
    /**
     * Find warehouses by city and is active
     */
    List<Warehouse> findByCityAndIsActive(String city, Boolean isActive);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find warehouses by created at between dates
     */
    List<Warehouse> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE " +
           "LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Warehouse> searchWarehouses(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true AND w.status = 'ACTIVE' " +
           "ORDER BY w.warehouseName ASC")
    List<Warehouse> findActiveWarehouses();
    
    /**
     * Find inactive warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = false OR w.status = 'INACTIVE' " +
           "ORDER BY w.warehouseName ASC")
    List<Warehouse> findInactiveWarehouses();
    
    /**
     * Find default warehouse
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isDefault = true AND w.isActive = true")
    Optional<Warehouse> findDefaultWarehouse();
    
    /**
     * Find main warehouse
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'MAIN' " +
           "AND w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findMainWarehouses();
    
    /**
     * Find branch warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'BRANCH' " +
           "AND w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findBranchWarehouses();
    
    /**
     * Find transit warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'TRANSIT' " +
           "AND w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findTransitWarehouses();
    
    /**
     * Find retail warehouses
     */
    @Query("SELECT w FROM Warehouse w WHERE w.warehouseType = 'RETAIL' " +
           "AND w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findRetailWarehouses();
    
    /**
     * Find warehouses by capacity range
     */
    @Query("SELECT w FROM Warehouse w WHERE w.capacity BETWEEN :minCapacity AND :maxCapacity " +
           "AND w.isActive = true ORDER BY w.capacity DESC")
    List<Warehouse> findByCapacityRange(@Param("minCapacity") Double minCapacity,
                                        @Param("maxCapacity") Double maxCapacity);
    
    /**
     * Find warehouses with low capacity utilization
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true " +
           "AND (w.currentStock / w.capacity) < :threshold ORDER BY (w.currentStock / w.capacity) ASC")
    List<Warehouse> findWarehousesWithLowUtilization(@Param("threshold") Double threshold);
    
    /**
     * Find warehouses with high capacity utilization
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true " +
           "AND (w.currentStock / w.capacity) > :threshold ORDER BY (w.currentStock / w.capacity) DESC")
    List<Warehouse> findWarehousesWithHighUtilization(@Param("threshold") Double threshold);
    
    /**
     * Find warehouses near capacity
     */
    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true " +
           "AND (w.currentStock / w.capacity) >= :threshold ORDER BY (w.currentStock / w.capacity) DESC")
    List<Warehouse> findWarehousesNearCapacity(@Param("threshold") Double threshold);
    
    /**
     * Find warehouses by region
     */
    @Query("SELECT w FROM Warehouse w WHERE " +
           "LOWER(w.stateProvince) LIKE LOWER(CONCAT('%', :region, '%')) OR " +
           "LOWER(w.city) LIKE LOWER(CONCAT('%', :region, '%')) " +
           "AND w.isActive = true ORDER BY w.warehouseName ASC")
    List<Warehouse> findByRegion(@Param("region") String region);
    
    /**
     * Find recent warehouses
     */
    @Query("SELECT w FROM Warehouse w ORDER BY w.createdAt DESC")
    List<Warehouse> findRecentWarehouses(Pageable pageable);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count warehouses by type
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.warehouseType = :warehouseType")
    Long countByWarehouseType(@Param("warehouseType") String warehouseType);
    
    /**
     * Count warehouses by status
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count active warehouses
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = true")
    Long countActiveWarehouses();
    
    /**
     * Count inactive warehouses
     */
    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = false")
    Long countInactiveWarehouses();
    
    /**
     * Get warehouse type distribution
     */
    @Query("SELECT w.warehouseType, COUNT(w) as warehouseCount FROM Warehouse w " +
           "GROUP BY w.warehouseType ORDER BY warehouseCount DESC")
    List<Object[]> getWarehouseTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT w.status, COUNT(w) as warehouseCount FROM Warehouse w " +
           "GROUP BY w.status ORDER BY warehouseCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get warehouses by city
     */
    @Query("SELECT w.city, COUNT(w) as warehouseCount FROM Warehouse w " +
           "WHERE w.isActive = true GROUP BY w.city ORDER BY warehouseCount DESC")
    List<Object[]> getWarehousesByCity();
    
    /**
     * Get warehouses by state/province
     */
    @Query("SELECT w.stateProvince, COUNT(w) as warehouseCount FROM Warehouse w " +
           "WHERE w.isActive = true GROUP BY w.stateProvince ORDER BY warehouseCount DESC")
    List<Object[]> getWarehousesByState();
    
    /**
     * Get total capacity
     */
    @Query("SELECT SUM(w.capacity) FROM Warehouse w WHERE w.isActive = true")
    Double getTotalCapacity();
    
    /**
     * Get total current stock
     */
    @Query("SELECT SUM(w.currentStock) FROM Warehouse w WHERE w.isActive = true")
    Double getTotalCurrentStock();
    
    /**
     * Get average capacity utilization
     */
    @Query("SELECT AVG(w.currentStock / w.capacity) * 100 FROM Warehouse w " +
           "WHERE w.isActive = true AND w.capacity > 0")
    Double getAverageCapacityUtilization();
    
    /**
     * Get capacity utilization by warehouse
     */
    @Query("SELECT w.warehouseId, w.warehouseName, w.capacity, w.currentStock, " +
           "(w.currentStock / w.capacity) * 100 as utilizationPercentage " +
           "FROM Warehouse w WHERE w.isActive = true AND w.capacity > 0 " +
           "ORDER BY utilizationPercentage DESC")
    List<Object[]> getCapacityUtilizationByWarehouse();
    
    /**
     * Find warehouses by tags
     */
    @Query("SELECT w FROM Warehouse w WHERE w.tags LIKE CONCAT('%', :tag, '%')")
    List<Warehouse> findByTag(@Param("tag") String tag);
}
