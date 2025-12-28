package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.request.SupplierContactRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierContactResponse;

import java.util.List;

/**
 * Service interface for SupplierContact entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierContactService {

    /**
     * Create new supplier contact
     */
    SupplierContactResponse createSupplierContact(SupplierContactRequest request);

    /**
     * Update existing supplier contact
     */
    SupplierContactResponse updateSupplierContact(Long id, SupplierContactRequest request);

    /**
     * Delete supplier contact
     */
    void deleteSupplierContact(Long id);

    /**
     * Set contact as primary
     */
    void setPrimaryContact(Long id);

    /**
     * Get supplier contact by ID
     */
    SupplierContactResponse getSupplierContactById(Long id);

    /**
     * Get all contacts for a supplier
     */
    List<SupplierContactResponse> getContactsBySupplier(Long supplierId);

    /**
     * Get primary contact for a supplier
     */
    SupplierContactResponse getPrimaryContactBySupplier(Long supplierId);

    /**
     * Get all supplier contacts
     */
    List<SupplierContactResponse> getAllSupplierContacts();
}
