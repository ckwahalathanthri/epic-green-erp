package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.request.SupplierContactRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierContactResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierContact;
import lk.epicgreen.erp.supplier.mapper.SupplierContactMapper;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.repository.SupplierContactRepository;
import lk.epicgreen.erp.supplier.service.SupplierContactService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierContactService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplierContactServiceImpl implements SupplierContactService {

    private final SupplierContactRepository supplierContactRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierContactMapper supplierContactMapper;

    @Override
    @Transactional
    public SupplierContactResponse createSupplierContact(SupplierContactRequest request) {
        log.info("Creating new supplier contact for supplier: {}", request.getSupplierId());

        // Verify supplier exists
        Supplier supplier = findSupplierById(request.getSupplierId());

        // Create contact entity
        SupplierContact contact = supplierContactMapper.toEntity(request);
        contact.setSupplier(supplier);

        // If this is set as primary, unset any existing primary contacts
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetPrimaryContacts(request.getSupplierId());
        }

        SupplierContact savedContact = supplierContactRepository.save(contact);
        log.info("Supplier contact created successfully: {}", savedContact.getId());

        return supplierContactMapper.toResponse(savedContact);
    }

    @Override
    @Transactional
    public SupplierContactResponse updateSupplierContact(Long id, SupplierContactRequest request) {
        log.info("Updating supplier contact: {}", id);

        SupplierContact contact = findSupplierContactById(id);

        // Update fields
        supplierContactMapper.updateEntityFromRequest(request, contact);

        // If setting as primary, unset other primary contacts
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetPrimaryContacts(contact.getSupplier().getId(), id);
        }

        SupplierContact updatedContact = supplierContactRepository.save(contact);
        log.info("Supplier contact updated successfully: {}", updatedContact.getId());

        return supplierContactMapper.toResponse(updatedContact);
    }

    @Override
    @Transactional
    public void deleteSupplierContact(Long id) {
        log.info("Deleting supplier contact: {}", id);

        SupplierContact contact = findSupplierContactById(id);
        supplierContactRepository.delete(contact);

        log.info("Supplier contact deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void setPrimaryContact(Long id) {
        log.info("Setting primary contact: {}", id);

        SupplierContact contact = findSupplierContactById(id);
        
        // Unset all primary contacts for this supplier
        unsetPrimaryContacts(contact.getSupplier().getId(), id);
        
        // Set this contact as primary
        contact.setIsPrimary(true);
        supplierContactRepository.save(contact);

        log.info("Primary contact set successfully: {}", id);
    }

    @Override
    public SupplierContactResponse getSupplierContactById(Long id) {
        SupplierContact contact = findSupplierContactById(id);
        return supplierContactMapper.toResponse(contact);
    }

    @Override
    public List<SupplierContactResponse> getContactsBySupplier(Long supplierId) {
        List<SupplierContact> contacts = supplierContactRepository.findBySupplierId(supplierId);
        return contacts.stream()
            .map(supplierContactMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public SupplierContactResponse getPrimaryContactBySupplier(Long supplierId) {
        SupplierContact contact = (SupplierContact) supplierContactRepository.findBySupplierIdAndIsPrimaryTrue(supplierId);
        return contact != null ? supplierContactMapper.toResponse(contact) : null;
    }

    @Override
    public List<SupplierContactResponse> getAllSupplierContacts() {
        List<SupplierContact> contacts = supplierContactRepository.findAll();
        return contacts.stream()
            .map(supplierContactMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private SupplierContact findSupplierContactById(Long id) {
        return supplierContactRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier contact not found: " + id));
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findByIdAndDeletedAtIsNull(id);
    }

    private void unsetPrimaryContacts(Long supplierId) {
        unsetPrimaryContacts(supplierId, null);
    }

    private void unsetPrimaryContacts(Long supplierId, Long excludeContactId) {
        List<SupplierContact> primaryContacts = supplierContactRepository.findBySupplierIdAndIsPrimaryTrue(supplierId);

        for (SupplierContact contact : primaryContacts) {
            if (excludeContactId == null || !contact.getId().equals(excludeContactId)) {
                contact.setIsPrimary(false);
                supplierContactRepository.save(contact);
            }
        }
    }
}
