package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.DispatchNoteRequest;
import lk.epicgreen.erp.sales.entity.DispatchNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Dispatch Service Interface
 * Service for dispatch note operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface DispatchService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    DispatchNote createDispatchNote(DispatchNoteRequest request);
    DispatchNote updateDispatchNote(Long id, DispatchNoteRequest request);
    void deleteDispatchNote(Long id);
    DispatchNote getDispatchNoteById(Long id);
    DispatchNote getDispatchNoteByNumber(String dispatchNumber);
    List<DispatchNote> getAllDispatchNotes();
    Page<DispatchNote> getAllDispatchNotes(Pageable pageable);
    Page<DispatchNote> searchDispatchNotes(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    DispatchNote prepareDispatchNote(Long id);
    DispatchNote markAsReady(Long id);
    DispatchNote dispatchNote(Long id, Long driverId, Long vehicleId);
    DispatchNote markAsDelivered(Long id, LocalDate deliveredDate, Long deliveredByUserId);
    DispatchNote cancelDispatchNote(Long id, String cancellationReason);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<DispatchNote> getPendingDispatchNotes();
    List<DispatchNote> getPreparingDispatchNotes();
    List<DispatchNote> getReadyDispatchNotes();
    List<DispatchNote> getDispatchedNotes();
    List<DispatchNote> getDeliveredDispatchNotes();
    List<DispatchNote> getCancelledDispatchNotes();
    List<DispatchNote> getUndeliveredDispatchNotes();
    List<DispatchNote> getPartialDeliveries();
    List<DispatchNote> getOverdueDeliveries();
    List<DispatchNote> getTodaysDeliveries();
    List<DispatchNote> getDispatchNotesRequiringAction();
    List<DispatchNote> getDispatchNotesByCustomer(Long customerId);
    Page<DispatchNote> getDispatchNotesByCustomer(Long customerId, Pageable pageable);
    List<DispatchNote> getDispatchNotesByWarehouse(Long warehouseId);
    List<DispatchNote> getDispatchNotesByDriver(Long driverId);
    List<DispatchNote> getDispatchNotesByVehicle(Long vehicleId);
    List<DispatchNote> getDispatchNotesByDateRange(LocalDate startDate, LocalDate endDate);
    List<DispatchNote> getRecentDispatchNotes(int limit);
    List<DispatchNote> getCustomerRecentDispatchNotes(Long customerId, int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateDispatchNote(DispatchNote dispatchNote);
    boolean canDispatch(Long dispatchNoteId);
    boolean canCancel(Long dispatchNoteId);
    boolean canDeliver(Long dispatchNoteId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<DispatchNote> createBulkDispatchNotes(List<DispatchNoteRequest> requests);
    int dispatchBulkNotes(List<Long> dispatchNoteIds, Long driverId, Long vehicleId);
    int deleteBulkDispatchNotes(List<Long> dispatchNoteIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getDispatchStatistics();
    List<Map<String, Object>> getDispatchTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getDeliveryMethodDistribution();
    List<Map<String, Object>> getMonthlyDispatchCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getTopDrivers(int limit);
    Double getOnTimeDeliveryRate();
    Map<String, Object> getDashboardStatistics();
}
