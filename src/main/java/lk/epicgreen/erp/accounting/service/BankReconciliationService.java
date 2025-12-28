package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.BankReconciliationRequest;
import lk.epicgreen.erp.accounting.dto.response.BankReconciliationResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Bank Reconciliation entity business logic
 * 
 * Bank Reconciliation Status Workflow:
 * DRAFT → IN_PROGRESS → COMPLETED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface BankReconciliationService {

    BankReconciliationResponse createReconciliation(BankReconciliationRequest request);
    BankReconciliationResponse updateReconciliation(Long id, BankReconciliationRequest request);
    void startReconciliation(Long id);
    void completeReconciliation(Long id, Long reconciledBy);
    void deleteReconciliation(Long id);
    
    BankReconciliationResponse getReconciliationById(Long id);
    BankReconciliationResponse getReconciliationByNumber(String reconciliationNumber);
    PageResponse<BankReconciliationResponse> getAllReconciliations(Pageable pageable);
    
    PageResponse<BankReconciliationResponse> getReconciliationsByStatus(String status, Pageable pageable);
    List<BankReconciliationResponse> getReconciliationsByBankAccount(Long bankAccountId);
    List<BankReconciliationResponse> getReconciliationsByDateRange(LocalDate startDate, LocalDate endDate);
    
    PageResponse<BankReconciliationResponse> searchReconciliations(String keyword, Pageable pageable);
    boolean canDelete(Long id);
    boolean canUpdate(Long id);
}
