package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Supplier entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierService {

    /**
     * Create new supplier
     */
    SupplierResponse createSupplier(SupplierRequest request);

    /**
     * Update existing supplier
     */
    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    /**
     * Activate supplier
     */
    void activateSupplier(Long id);

    /**
     * Deactivate supplier
     */
    void deactivateSupplier(Long id);

    /**
     * Delete supplier (soft delete)
     */
    void deleteSupplier(Long id);

    /**
     * Get supplier by ID
     */
    SupplierResponse getSupplierById(Long id);

    /**
     * Get supplier by code
     */
    SupplierResponse getSupplierByCode(String supplierCode);

    /**
     * Get all suppliers with pagination
     */
    PageResponse<SupplierResponse> getAllSuppliers(Pageable pageable);

    /**
     * Get all active suppliers
     */
    List<SupplierResponse> getAllActiveSuppliers();

    /**
     * Get suppliers by type
     */
    PageResponse<SupplierResponse> getSuppliersByType(String supplierType, Pageable pageable);

    /**
     * Get raw material suppliers
     */
    List<SupplierResponse> getRawMaterialSuppliers();

    /**
     * Get packaging suppliers
     */
    List<SupplierResponse> getPackagingSuppliers();

    /**
     * Get service suppliers
     */
    List<SupplierResponse> getServiceSuppliers();

    /**
     * Search suppliers
     */
    PageResponse<SupplierResponse> searchSuppliers(String keyword, Pageable pageable);

    /**
     * Get suppliers with outstanding balance
     */
    List<SupplierResponse> getSuppliersWithOutstandingBalance();

    /**
     * Get suppliers exceeding credit limit
     */
    List<SupplierResponse> getSuppliersExceedingCreditLimit();

    /**
     * Update supplier rating
     */
    void updateSupplierRating(Long id, BigDecimal rating);

    /**
     * Get supplier current balance
     */
    BigDecimal getSupplierBalance(Long id);
}
