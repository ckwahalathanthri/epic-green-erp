package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.FinancialPeriodRequest;
import lk.epicgreen.erp.accounting.dto.response.FinancialPeriodResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Financial Period entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface FinancialPeriodService {

    FinancialPeriodResponse createPeriod(FinancialPeriodRequest request);
    FinancialPeriodResponse updatePeriod(Long id, FinancialPeriodRequest request);
    void closePeriod(Long id, Long closedBy);
    void deletePeriod(Long id);
    
    FinancialPeriodResponse getPeriodById(Long id);
    FinancialPeriodResponse getPeriodByCode(String periodCode);
    PageResponse<FinancialPeriodResponse> getAllPeriods(Pageable pageable);
    
    List<FinancialPeriodResponse> getPeriodsByFiscalYear(Integer fiscalYear);
    List<FinancialPeriodResponse> getPeriodsByType(String periodType);
    List<FinancialPeriodResponse> getOpenPeriods();
    FinancialPeriodResponse getCurrentPeriod(LocalDate date);
    
    boolean canDelete(Long id);
    boolean canUpdate(Long id);
}
