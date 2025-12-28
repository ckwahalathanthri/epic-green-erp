package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.TaxRateRequest;
import lk.epicgreen.erp.admin.dto.response.TaxRateResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for TaxRate entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface TaxRateService {

    /**
     * Create new tax rate
     */
    TaxRateResponse createTaxRate(TaxRateRequest request);

    /**
     * Update existing tax rate
     */
    TaxRateResponse updateTaxRate(Long id, TaxRateRequest request);

    /**
     * Activate tax rate
     */
    void activateTaxRate(Long id);

    /**
     * Deactivate tax rate
     */
    void deactivateTaxRate(Long id);

    /**
     * Delete tax rate
     */
    void deleteTaxRate(Long id);

    /**
     * Get tax rate by ID
     */
    TaxRateResponse getTaxRateById(Long id);

    /**
     * Get tax rate by code
     */
    TaxRateResponse getTaxRateByCode(String taxCode);

    /**
     * Get all tax rates
     */
    List<TaxRateResponse> getAllTaxRates();

    /**
     * Get all active tax rates
     */
    List<TaxRateResponse> getAllActiveTaxRates();

    /**
     * Get tax rates by type
     */
    List<TaxRateResponse> getTaxRatesByType(String taxType);

    /**
     * Get active tax rates by date
     */
    List<TaxRateResponse> getActiveTaxRatesByDate(LocalDate date);
}
