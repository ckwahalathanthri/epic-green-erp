package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.ChequeRequest;
import lk.epicgreen.erp.payment.entity.Cheque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Cheque Service Interface
 * Service for cheque operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ChequeService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Cheque createCheque(ChequeRequest request);
    Cheque updateCheque(Long id, ChequeRequest request);
    void deleteCheque(Long id);
    Cheque getChequeById(Long id);
    Cheque getChequeByNumber(String chequeNumber);
    List<Cheque> getAllCheques();
    Page<Cheque> getAllCheques(Pageable pageable);
    Page<Cheque> searchCheques(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Cheque presentCheque(Long id, LocalDate presentationDate);
    Cheque clearCheque(Long id, LocalDate clearanceDate);
    Cheque bounceCheque(Long id, String bounceReason);
    Cheque cancelCheque(Long id, String cancellationReason);
    Cheque returnCheque(Long id, String returnReason);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Cheque> getPendingCheques();
    List<Cheque> getPresentedCheques();
    List<Cheque> getClearedCheques();
    List<Cheque> getBouncedCheques();
    List<Cheque> getCancelledCheques();
    List<Cheque> getPostDatedCheques();
    List<Cheque> getChequesDueForPresentation();
    List<Cheque> getOverdueCheques();
    List<Cheque> getChequesRequiringAction();
    List<Cheque> getChequesByCustomer(Long customerId);
    Page<Cheque> getChequesByCustomer(Long customerId, Pageable pageable);
    List<Cheque> getChequesByBank(String bankName);
    List<Cheque> getChequesByDateRange(LocalDate startDate, LocalDate endDate);
    List<Cheque> getRecentCheques(int limit);
    List<Cheque> getCustomerRecentCheques(Long customerId, int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateCheque(Cheque cheque);
    boolean canPresentCheque(Long chequeId);
    boolean canClearCheque(Long chequeId);
    boolean canBounceCheque(Long chequeId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Cheque> createBulkCheques(List<ChequeRequest> requests);
    int presentBulkCheques(List<Long> chequeIds, LocalDate presentationDate);
    int clearBulkCheques(List<Long> chequeIds, LocalDate clearanceDate);
    int deleteBulkCheques(List<Long> chequeIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getChequeStatistics();
    List<Map<String, Object>> getChequeStatusDistribution();
    List<Map<String, Object>> getBankDistribution();
    List<Map<String, Object>> getMonthlyChequeCount(LocalDate startDate, LocalDate endDate);
    Double getTotalChequeAmount();
    Double getAverageChequeAmount();
    Double getChequeBounceRate();
    Map<String, Object> getDashboardStatistics();
}
