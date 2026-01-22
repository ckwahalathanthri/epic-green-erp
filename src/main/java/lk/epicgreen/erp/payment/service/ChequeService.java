package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.request.ChequeRequest;
import lk.epicgreen.erp.payment.dto.response.ChequeResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.payment.entity.Cheque;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Cheque entity business logic (PDC tracking)
 * 
 * Cheque Status Workflow:
 * RECEIVED → DEPOSITED → CLEARED
 * Can be BOUNCED, RETURNED, or CANCELLED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ChequeService {

    /**
     * Create a new Cheque
     */
    ChequeResponse createCheque(ChequeRequest request);

    /**
     * Update an existing Cheque (only in RECEIVED status)
     */
    ChequeResponse updateCheque(Long id, ChequeRequest request);

    /**
     * Deposit Cheque (RECEIVED → DEPOSITED)
     */
    void depositCheque(Long id, LocalDate depositDate);

    /**
     * Clear Cheque (DEPOSITED → CLEARED)
     */
    void clearCheque(Long id, LocalDate clearanceDate);

    /**
     * Bounce Cheque (DEPOSITED → BOUNCED)
     */
    void bounceCheque(Long id, String bounceReason, BigDecimal bounceCharges);

    /**
     * Return Cheque (any status → RETURNED)
     */
    Cheque returnCheque(Long id, String reason);

    /**
     * Cancel Cheque (RECEIVED → CANCELLED)
     */
    Cheque cancelCheque(Long id, String reason);

    /**
     * Delete Cheque (only in RECEIVED status)
     */
    void deleteCheque(Long id);

    /**
     * Get Cheque by ID
     */
    ChequeResponse getChequeById(Long id);

    /**
     * Get Cheque by cheque number
     */
    ChequeResponse getChequeByChequeNumber(String chequeNumber);

    /**
     * Get all Cheques (paginated)
     */
    PageResponse<ChequeResponse> getAllCheques(Pageable pageable);

    Cheque getChequeByNumber(String chequeNumber);

    Cheque presentCheque(Long id,LocalDate presentationDate);

    List<Cheque> getPendingCheques();

    List<Cheque> getPresentedCheques();

    List<Cheque> getClearedCheques();

    List<Cheque> getCancelledCheques();

    List<Cheque> getPostDatedCheques();

    List<Cheque> getChequesDueForPresentation();

    List<Cheque> getOverdueCheques();

    List<Cheque> getChequesRequiringAction();

    List<Cheque> getRecentCheques(Pageable pageable);

    List<Cheque> getCustomerRecentCheques(Long customerId,Pageable page);

    boolean canPresentCheque(Long id);

    boolean canClearCheque(Long id);

    boolean canBounceCheque(Long id);

    List<Cheque> createBulkCheques(List<ChequeRequest> requests);

    int presentBulkCheques(List<Long> chequeIds,LocalDate presentationDate);

    int clearBulkCheques(List<Long>chequeIds,LocalDate clearanceDate);

    int deleteBulkCheques(List<Long> chequeIds);

    Map<String,Object> getChequeStatistics();

    List<Map<String,Object>> getChequeStatusDistribution();
    List<Map<String,Object>> getBankDistribution();

    List<Map<String,Object>> getMonthlyChequeCount(LocalDate startDate,LocalDate endDate);

    Map<String,Object> getDashboardStatistics();

    double getTotalChequeAmount();

    double getAverageChequeAmount();

    double getChequeBounceRate();
    /**
     * Get Cheques by status
     */
    PageResponse<ChequeResponse> getChequesByStatus(String status, Pageable pageable);

    /**
     * Get Cheques by payment
     */
    List<ChequeResponse> getChequesByPayment(Long paymentId);

    /**
     * Get Cheques by customer
     */
    List<ChequeResponse> getChequesByCustomer(Long customerId);

    /**
     * Get Cheques by date range (cheque_date)
     */
    List<ChequeResponse> getChequesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get Cheques by bank
     */
    List<ChequeResponse> getChequesByBank(String bankName);

    /**
     * Get Cheques pending deposit (status = RECEIVED)
     */
    List<ChequeResponse> getPendingDepositCheques();

    /**
     * Get Cheques pending clearance (status = DEPOSITED)
     */
    List<ChequeResponse> getPendingClearanceCheques();

    /**
     * Get Due Cheques (cheque_date <= given date and status = RECEIVED)
     */
    List<ChequeResponse> getDueCheques(LocalDate asOfDate);

    /**
     * Get Bounced Cheques
     */
    List<ChequeResponse> getBouncedCheques();

    /**
     * Search Cheques
     */
    PageResponse<ChequeResponse> searchCheques(String keyword, Pageable pageable);

    /**
     * Get total cheque amount by customer
     */
    BigDecimal getTotalChequeAmountByCustomer(Long customerId);

    /**
     * Get total pending clearance amount
     */
    BigDecimal getTotalPendingClearanceAmount();

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);
}
