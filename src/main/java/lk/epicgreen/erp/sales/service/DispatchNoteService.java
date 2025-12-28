package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.request.DispatchNoteRequest;
import lk.epicgreen.erp.sales.dto.response.DispatchNoteResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for DispatchNote entity business logic
 * 
 * Dispatch Status Workflow:
 * PENDING → LOADING → DISPATCHED → IN_TRANSIT → DELIVERED
 * Can be RETURNED from IN_TRANSIT/DELIVERED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface DispatchNoteService {

    /**
     * Create a new Dispatch Note
     */
    DispatchNoteResponse createDispatchNote(DispatchNoteRequest request);

    /**
     * Update an existing Dispatch Note (only in PENDING status)
     */
    DispatchNoteResponse updateDispatchNote(Long id, DispatchNoteRequest request);

    /**
     * Start Loading (PENDING → LOADING)
     */
    void startLoading(Long id);

    /**
     * Mark as Dispatched (LOADING → DISPATCHED)
     */
    void markAsDispatched(Long id, Long deliveredBy);

    /**
     * Mark as In Transit (DISPATCHED → IN_TRANSIT)
     */
    void markAsInTransit(Long id);

    /**
     * Mark as Delivered (IN_TRANSIT → DELIVERED)
     */
    void markAsDelivered(Long id, String receivedByName, String receivedBySignature);

    /**
     * Mark as Returned
     */
    void markAsReturned(Long id, String reason);

    /**
     * Delete Dispatch Note (only in PENDING status)
     */
    void deleteDispatchNote(Long id);

    /**
     * Update GPS Location
     */
    void updateGPSLocation(Long id, java.math.BigDecimal latitude, java.math.BigDecimal longitude);

    /**
     * Upload Delivery Photo
     */
    void uploadDeliveryPhoto(Long id, String photoUrl);

    /**
     * Get Dispatch Note by ID
     */
    DispatchNoteResponse getDispatchNoteById(Long id);

    /**
     * Get Dispatch Note by number
     */
    DispatchNoteResponse getDispatchNoteByNumber(String dispatchNumber);

    /**
     * Get all Dispatch Notes (paginated)
     */
    PageResponse<DispatchNoteResponse> getAllDispatchNotes(Pageable pageable);

    /**
     * Get Dispatch Notes by status
     */
    PageResponse<DispatchNoteResponse> getDispatchNotesByStatus(String status, Pageable pageable);

    /**
     * Get Dispatch Notes by sales order
     */
    List<DispatchNoteResponse> getDispatchNotesByOrder(Long orderId);

    /**
     * Get Dispatch Notes by customer
     */
    List<DispatchNoteResponse> getDispatchNotesByCustomer(Long customerId);

    /**
     * Get Dispatch Notes by warehouse
     */
    List<DispatchNoteResponse> getDispatchNotesByWarehouse(Long warehouseId);

    /**
     * Get Dispatch Notes by date range
     */
    List<DispatchNoteResponse> getDispatchNotesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get Dispatch Notes by route
     */
    List<DispatchNoteResponse> getDispatchNotesByRoute(String routeCode);

    /**
     * Get Dispatch Notes by delivered by
     */
    List<DispatchNoteResponse> getDispatchNotesByDeliveredBy(Long deliveredBy);

    /**
     * Get In Transit Dispatches
     */
    List<DispatchNoteResponse> getInTransitDispatches();

    /**
     * Get Pending Dispatches
     */
    List<DispatchNoteResponse> getPendingDispatches();

    /**
     * Search Dispatch Notes
     */
    PageResponse<DispatchNoteResponse> searchDispatchNotes(String keyword, Pageable pageable);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);
}
