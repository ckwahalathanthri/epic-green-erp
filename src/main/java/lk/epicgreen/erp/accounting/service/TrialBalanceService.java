package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.response.TrialBalanceResponse;

import java.util.List;

/**
 * Service interface for Trial Balance entity (reporting)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface TrialBalanceService {

    List<TrialBalanceResponse> generateTrialBalance(Long periodId, Long generatedBy);
    List<TrialBalanceResponse> getTrialBalanceByPeriod(Long periodId);
    TrialBalanceResponse getTrialBalanceById(Long id);
}
