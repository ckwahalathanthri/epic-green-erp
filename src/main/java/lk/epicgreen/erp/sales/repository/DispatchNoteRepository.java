package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.DispatchNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DispatchNote entity
 * Based on ACTUAL database schema: dispatch_notes table
 * 
 * Fields: dispatch_number, dispatch_date, order_id (BIGINT), customer_id (BIGINT),
 *         warehouse_id (BIGINT), vehicle_number, driver_name, driver_mobile,
 *         delivery_address_id (BIGINT), route_code,
 *         status (ENUM: PENDING, LOADING, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED),
 *         dispatch_time, delivery_time, delivered_by (BIGINT),
 *         received_by_name, received_by_signature, delivery_photo_url,
 *         gps_latitude, gps_longitude, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface DispatchNoteRepository extends JpaRepository<DispatchNote, Long>, JpaSpecificationExecutor<DispatchNote> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find dispatch note by dispatch number
     */
    Optional<DispatchNote> findByDispatchNumber(String dispatchNumber);
    
    /**
     * Find all dispatch notes for an order
     */
    List<DispatchNote> findByOrderId(Long orderId);
    
    /**
     * Find all dispatch notes for a customer
     */
    List<DispatchNote> findByCustomerId(Long customerId);
    
    /**
     * Find all dispatch notes for a customer with pagination
     */
    Page<DispatchNote> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find dispatch notes by status
     */
    List<DispatchNote> findByStatus(String status);
    
    /**
     * Find dispatch notes by status with pagination
     */
    Page<DispatchNote> findByStatus(String status, Pageable pageable);
    
    /**
     * Find dispatch notes by warehouse
     */
    List<DispatchNote> findByWarehouseId(Long warehouseId);
    
    /**
     * Find dispatch notes by route code
     */
    List<DispatchNote> findByRouteCode(String routeCode);
    
    /**
     * Find dispatch notes by dispatch date
     */
    List<DispatchNote> findByDispatchDate(LocalDate dispatchDate);
    
    /**
     * Find dispatch notes by dispatch date range
     */
    List<DispatchNote> findByDispatchDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find dispatch notes by dispatch date range with pagination
     */
    Page<DispatchNote> findByDispatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find dispatch notes by delivered by user
     */
    List<DispatchNote> findByDeliveredBy(Long deliveredBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if dispatch number exists
     */
    boolean existsByDispatchNumber(String dispatchNumber);
    
    /**
     * Check if dispatch number exists excluding specific dispatch ID
     */
    boolean existsByDispatchNumberAndIdNot(String dispatchNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search dispatch notes by dispatch number containing (case-insensitive)
     */
    Page<DispatchNote> findByDispatchNumberContainingIgnoreCase(String dispatchNumber, Pageable pageable);
    
    /**
     * Search dispatch notes by vehicle number
     */
    Page<DispatchNote> findByVehicleNumberContainingIgnoreCase(String vehicleNumber, Pageable pageable);
    
    /**
     * Search dispatch notes by driver name
     */
    Page<DispatchNote> findByDriverNameContainingIgnoreCase(String driverName, Pageable pageable);
    
    /**
     * Search dispatch notes by multiple criteria
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE " +
           "(:dispatchNumber IS NULL OR LOWER(dn.dispatchNumber) LIKE LOWER(CONCAT('%', :dispatchNumber, '%'))) AND " +
           "(:customerId IS NULL OR dn.customer.id = :customerId) AND " +
           "(:status IS NULL OR dn.status = :status) AND " +
           "(:routeCode IS NULL OR dn.routeCode = :routeCode) AND " +
           "(:startDate IS NULL OR dn.dispatchDate >= :startDate) AND " +
           "(:endDate IS NULL OR dn.dispatchDate <= :endDate)")
    Page<DispatchNote> searchDispatchNotes(
            @Param("dispatchNumber") String dispatchNumber,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("routeCode") String routeCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count dispatch notes by status
     */
    long countByStatus(String status);
    
    /**
     * Count dispatch notes by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count dispatch notes in date range
     */
    long countByDispatchDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find pending dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'PENDING' ORDER BY dn.dispatchDate")
    List<DispatchNote> findPendingDispatchNotes();
    
    /**
     * Find loading dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'LOADING' ORDER BY dn.dispatchDate")
    List<DispatchNote> findLoadingDispatchNotes();
    
    /**
     * Find dispatched dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'DISPATCHED' ORDER BY dn.dispatchTime DESC")
    List<DispatchNote> findDispatchedNotes();
    
    /**
     * Find in-transit dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'IN_TRANSIT' ORDER BY dn.dispatchTime")
    List<DispatchNote> findInTransitNotes();
    
    /**
     * Find delivered dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'DELIVERED' ORDER BY dn.deliveryTime DESC")
    List<DispatchNote> findDeliveredNotes();
    
    /**
     * Find returned dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'RETURNED' ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findReturnedNotes();
    
    /**
     * Find dispatch notes by vehicle number
     */
    List<DispatchNote> findByVehicleNumber(String vehicleNumber);
    
    /**
     * Find dispatch notes by driver
     */
    List<DispatchNote> findByDriverName(String driverName);
    
    /**
     * Find dispatch notes with GPS coordinates
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.gpsLatitude IS NOT NULL AND dn.gpsLongitude IS NOT NULL")
    List<DispatchNote> findDispatchNotesWithGPS();
    
    /**
     * Find dispatch notes without GPS coordinates
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.gpsLatitude IS NULL OR dn.gpsLongitude IS NULL")
    List<DispatchNote> findDispatchNotesWithoutGPS();
    
    /**
     * Find dispatch notes with delivery photo
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.deliveryPhotoUrl IS NOT NULL")
    List<DispatchNote> findDispatchNotesWithPhoto();
    
    /**
     * Get dispatch statistics
     */
    @Query("SELECT " +
           "COUNT(dn) as totalDispatches, " +
           "SUM(CASE WHEN dn.status = 'PENDING' THEN 1 ELSE 0 END) as pendingDispatches, " +
           "SUM(CASE WHEN dn.status = 'DISPATCHED' THEN 1 ELSE 0 END) as dispatchedCount, " +
           "SUM(CASE WHEN dn.status = 'DELIVERED' THEN 1 ELSE 0 END) as deliveredCount, " +
           "SUM(CASE WHEN dn.status = 'RETURNED' THEN 1 ELSE 0 END) as returnedCount " +
           "FROM DispatchNote dn")
    Object getDispatchStatistics();
    
    /**
     * Get dispatch notes grouped by status
     */
    @Query("SELECT dn.status, COUNT(dn) as dispatchCount " +
           "FROM DispatchNote dn GROUP BY dn.status ORDER BY dispatchCount DESC")
    List<Object[]> getDispatchNotesByStatus();
    
    /**
     * Get dispatch notes grouped by route
     */
    @Query("SELECT dn.routeCode, COUNT(dn) as dispatchCount " +
           "FROM DispatchNote dn WHERE dn.routeCode IS NOT NULL " +
           "GROUP BY dn.routeCode ORDER BY dispatchCount DESC")
    List<Object[]> getDispatchNotesByRoute();
    
    /**
     * Get dispatch notes grouped by driver
     */
    @Query("SELECT dn.driverName, COUNT(dn) as dispatchCount " +
           "FROM DispatchNote dn WHERE dn.driverName IS NOT NULL " +
           "GROUP BY dn.driverName ORDER BY dispatchCount DESC")
    List<Object[]> getDispatchNotesByDriver();
    
    /**
     * Get dispatch notes grouped by vehicle
     */
    @Query("SELECT dn.vehicleNumber, COUNT(dn) as dispatchCount " +
           "FROM DispatchNote dn WHERE dn.vehicleNumber IS NOT NULL " +
           "GROUP BY dn.vehicleNumber ORDER BY dispatchCount DESC")
    List<Object[]> getDispatchNotesByVehicle();
    
    /**
     * Find dispatch notes dispatched in time range
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.dispatchTime BETWEEN :startTime AND :endTime " +
           "ORDER BY dn.dispatchTime DESC")
    List<DispatchNote> findDispatchNotesDispatchedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find dispatch notes delivered in time range
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.deliveryTime BETWEEN :startTime AND :endTime " +
           "ORDER BY dn.deliveryTime DESC")
    List<DispatchNote> findDispatchNotesDeliveredBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.dispatchDate = CURRENT_DATE ORDER BY dn.createdAt DESC")
    List<DispatchNote> findTodayDispatchNotes();
    
    /**
     * Find all dispatch notes ordered by date
     */
    List<DispatchNote> findAllByOrderByDispatchDateDescCreatedAtDesc();
    
    /**
     * Find dispatch notes by customer and status
     */
    List<DispatchNote> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find dispatch notes by route and status
     */
    List<DispatchNote> findByRouteCodeAndStatus(String routeCode, String status);
}
