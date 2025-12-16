package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.DispatchNote;
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
 * DispatchNote Repository
 * Repository for dispatch note data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface DispatchNoteRepository extends JpaRepository<DispatchNote, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find dispatch note by dispatch number
     */
    Optional<DispatchNote> findByDispatchNumber(String dispatchNumber);
    
    /**
     * Check if dispatch note exists by dispatch number
     */
    boolean existsByDispatchNumber(String dispatchNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find dispatch notes by sales order ID
     */
    List<DispatchNote> findBySalesOrderId(Long salesOrderId);
    
    /**
     * Find dispatch notes by customer ID
     */
    List<DispatchNote> findByCustomerId(Long customerId);
    
    /**
     * Find dispatch notes by customer ID with pagination
     */
    Page<DispatchNote> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find dispatch notes by warehouse ID
     */
    List<DispatchNote> findByWarehouseId(Long warehouseId);
    
    /**
     * Find dispatch notes by warehouse ID with pagination
     */
    Page<DispatchNote> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find dispatch notes by status
     */
    List<DispatchNote> findByStatus(String status);
    
    /**
     * Find dispatch notes by status with pagination
     */
    Page<DispatchNote> findByStatus(String status, Pageable pageable);
    
    /**
     * Find dispatch notes by dispatch type
     */
    List<DispatchNote> findByDispatchType(String dispatchType);
    
    /**
     * Find dispatch notes by delivery method
     */
    List<DispatchNote> findByDeliveryMethod(String deliveryMethod);
    
    /**
     * Find dispatch notes by driver ID
     */
    List<DispatchNote> findByDriverId(Long driverId);
    
    /**
     * Find dispatch notes by vehicle ID
     */
    List<DispatchNote> findByVehicleId(Long vehicleId);
    
    /**
     * Find dispatch notes by prepared by user
     */
    List<DispatchNote> findByPreparedByUserId(Long userId);
    
    /**
     * Find dispatch notes by delivered by user
     */
    List<DispatchNote> findByDeliveredByUserId(Long userId);
    
    /**
     * Find dispatch notes by is delivered
     */
    List<DispatchNote> findByIsDelivered(Boolean isDelivered);
    
    /**
     * Find dispatch notes by is partial
     */
    List<DispatchNote> findByIsPartial(Boolean isPartial);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find dispatch notes by dispatch date between dates
     */
    List<DispatchNote> findByDispatchDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find dispatch notes by dispatch date between dates with pagination
     */
    Page<DispatchNote> findByDispatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find dispatch notes by scheduled delivery date between dates
     */
    List<DispatchNote> findByScheduledDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find dispatch notes by delivered date between dates
     */
    List<DispatchNote> findByDeliveredDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find dispatch notes by created at between dates
     */
    List<DispatchNote> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find dispatch notes by customer ID and status
     */
    List<DispatchNote> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find dispatch notes by customer ID and status with pagination
     */
    Page<DispatchNote> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find dispatch notes by warehouse ID and status
     */
    List<DispatchNote> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find dispatch notes by dispatch type and status
     */
    List<DispatchNote> findByDispatchTypeAndStatus(String dispatchType, String status);
    
    /**
     * Find dispatch notes by is delivered and status
     */
    List<DispatchNote> findByIsDeliveredAndStatus(Boolean isDelivered, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE " +
           "LOWER(dn.dispatchNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(dn.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(dn.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<DispatchNote> searchDispatchNotes(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'PENDING' " +
           "ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findPendingDispatchNotes();
    
    /**
     * Find pending dispatch notes with pagination
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'PENDING' " +
           "ORDER BY dn.dispatchDate DESC")
    Page<DispatchNote> findPendingDispatchNotes(Pageable pageable);
    
    /**
     * Find preparing dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'PREPARING' " +
           "ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findPreparingDispatchNotes();
    
    /**
     * Find ready dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'READY' " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findReadyDispatchNotes();
    
    /**
     * Find dispatched notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'DISPATCHED' " +
           "ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findDispatchedNotes();
    
    /**
     * Find delivered dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'DELIVERED' AND dn.isDelivered = true " +
           "ORDER BY dn.deliveredDate DESC")
    List<DispatchNote> findDeliveredDispatchNotes();
    
    /**
     * Find cancelled dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.status = 'CANCELLED' " +
           "ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findCancelledDispatchNotes();
    
    /**
     * Find undelivered dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.isDelivered = false " +
           "AND dn.status NOT IN ('CANCELLED') " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findUndeliveredDispatchNotes();
    
    /**
     * Find partial deliveries
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.isPartial = true " +
           "ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findPartialDeliveries();
    
    /**
     * Find overdue deliveries
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.scheduledDeliveryDate < :currentDate " +
           "AND dn.isDelivered = false " +
           "AND dn.status NOT IN ('CANCELLED', 'DELIVERED') " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findOverdueDeliveries(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find today's deliveries
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.scheduledDeliveryDate = :today " +
           "AND dn.status NOT IN ('CANCELLED', 'DELIVERED') " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findTodaysDeliveries(@Param("today") LocalDate today);
    
    /**
     * Find driver deliveries
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.driverId = :driverId " +
           "AND dn.isDelivered = false " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findDriverDeliveries(@Param("driverId") Long driverId);
    
    /**
     * Find vehicle deliveries
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.vehicleId = :vehicleId " +
           "AND dn.isDelivered = false " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findVehicleDeliveries(@Param("vehicleId") Long vehicleId);
    
    /**
     * Find recent dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn ORDER BY dn.dispatchDate DESC, dn.createdAt DESC")
    List<DispatchNote> findRecentDispatchNotes(Pageable pageable);
    
    /**
     * Find customer recent dispatch notes
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.customerId = :customerId " +
           "ORDER BY dn.dispatchDate DESC, dn.createdAt DESC")
    List<DispatchNote> findCustomerRecentDispatchNotes(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find dispatch notes by date range and status
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.dispatchDate BETWEEN :startDate AND :endDate " +
           "AND dn.status = :status ORDER BY dn.dispatchDate DESC")
    List<DispatchNote> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("status") String status);
    
    /**
     * Find dispatch notes requiring action
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE " +
           "(dn.status = 'PENDING' OR dn.status = 'PREPARING') OR " +
           "(dn.status = 'READY' AND dn.scheduledDeliveryDate <= :currentDate) OR " +
           "(dn.status = 'DISPATCHED' AND dn.scheduledDeliveryDate < :overdueDate) " +
           "ORDER BY dn.scheduledDeliveryDate ASC")
    List<DispatchNote> findDispatchNotesRequiringAction(@Param("currentDate") LocalDate currentDate,
                                                        @Param("overdueDate") LocalDate overdueDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count dispatch notes by customer
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count dispatch notes by warehouse
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.warehouseId = :warehouseId")
    Long countByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Count dispatch notes by status
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending dispatch notes
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.status = 'PENDING'")
    Long countPendingDispatchNotes();
    
    /**
     * Count undelivered dispatch notes
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.isDelivered = false " +
           "AND dn.status NOT IN ('CANCELLED')")
    Long countUndeliveredDispatchNotes();
    
    /**
     * Count partial deliveries
     */
    @Query("SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.isPartial = true")
    Long countPartialDeliveries();
    
    /**
     * Get dispatch type distribution
     */
    @Query("SELECT dn.dispatchType, COUNT(dn) as dispatchCount FROM DispatchNote dn " +
           "GROUP BY dn.dispatchType ORDER BY dispatchCount DESC")
    List<Object[]> getDispatchTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT dn.status, COUNT(dn) as dispatchCount FROM DispatchNote dn " +
           "GROUP BY dn.status ORDER BY dispatchCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get delivery method distribution
     */
    @Query("SELECT dn.deliveryMethod, COUNT(dn) as dispatchCount FROM DispatchNote dn " +
           "GROUP BY dn.deliveryMethod ORDER BY dispatchCount DESC")
    List<Object[]> getDeliveryMethodDistribution();
    
    /**
     * Get monthly dispatch count
     */
    @Query("SELECT YEAR(dn.dispatchDate) as year, MONTH(dn.dispatchDate) as month, " +
           "COUNT(dn) as dispatchCount FROM DispatchNote dn " +
           "WHERE dn.dispatchDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(dn.dispatchDate), MONTH(dn.dispatchDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyDispatchCount(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    
    /**
     * Get top drivers
     */
    @Query("SELECT dn.driverId, dn.driverName, COUNT(dn) as deliveryCount FROM DispatchNote dn " +
           "WHERE dn.isDelivered = true AND dn.driverId IS NOT NULL " +
           "GROUP BY dn.driverId, dn.driverName ORDER BY deliveryCount DESC")
    List<Object[]> getTopDrivers(Pageable pageable);
    
    /**
     * Get delivery performance
     */
    @Query("SELECT " +
           "(SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.isDelivered = true " +
           "AND dn.deliveredDate <= dn.scheduledDeliveryDate) * 100.0 / " +
           "(SELECT COUNT(dn) FROM DispatchNote dn WHERE dn.isDelivered = true) " +
           "FROM DispatchNote dn WHERE dn.isDelivered = true")
    Double getOnTimeDeliveryRate();
    
    /**
     * Find dispatch notes by tags
     */
    @Query("SELECT dn FROM DispatchNote dn WHERE dn.tags LIKE CONCAT('%', :tag, '%')")
    List<DispatchNote> findByTag(@Param("tag") String tag);
}
