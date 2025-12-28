package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.mapper.SupplierMapper;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.service.SupplierService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating new supplier: {}", request.getSupplierCode());

        // Validate unique constraint
        validateUniqueSupplierCode(request.getSupplierCode(), null);

        // Create supplier entity
        Supplier supplier = supplierMapper.toEntity(request);

        Supplier savedSupplier = supplierRepository.save(supplier);
        log.info("Supplier created successfully: {}", savedSupplier.getSupplierCode());

        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier: {}", id);

        Supplier supplier = findSupplierById(id);

        // Validate unique constraint
        validateUniqueSupplierCode(request.getSupplierCode(), id);

        // Update fields
        supplierMapper.updateEntityFromRequest(request, supplier);

        Supplier updatedSupplier = supplierRepository.save(supplier);
        log.info("Supplier updated successfully: {}", updatedSupplier.getSupplierCode());

        return supplierMapper.toResponse(updatedSupplier);
    }

    @Override
    @Transactional
    public void activateSupplier(Long id) {
        log.info("Activating supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsActive(true);
        supplierRepository.save(supplier);

        log.info("Supplier activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateSupplier(Long id) {
        log.info("Deactivating supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsActive(false);
        supplierRepository.save(supplier);

        log.info("Supplier deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier (soft delete): {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setDeletedAt(LocalDateTime.now());
        supplier.setIsActive(false);
        supplierRepository.save(supplier);

        log.info("Supplier deleted successfully: {}", id);
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = findSupplierById(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse getSupplierByCode(String supplierCode) {
        Supplier supplier = supplierRepository.findBySupplierCodeAndDeletedAtIsNull(supplierCode)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + supplierCode));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public PageResponse<SupplierResponse> getAllSuppliers(Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getAllActiveSuppliers() {
        List<Supplier> suppliers = supplierRepository.findByIsActiveTrueAndDeletedAtIsNull();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SupplierResponse> getSuppliersByType(String supplierType, Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.findBySupplierTypeAndDeletedAtIsNull(supplierType, pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getRawMaterialSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("RAW_MATERIAL");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getPackagingSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("PACKAGING");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getServiceSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("SERVICES");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SupplierResponse> searchSuppliers(String keyword, Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.searchSuppliers(keyword, pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getSuppliersWithOutstandingBalance() {
        List<Supplier> suppliers = supplierRepository.findSuppliersWithOutstandingBalance();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getSuppliersExceedingCreditLimit() {
        List<Supplier> suppliers = supplierRepository.findSuppliersExceedingCreditLimit();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSupplierRating(Long id, BigDecimal rating) {
        log.info("Updating supplier rating: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setRating(rating);
        supplierRepository.save(supplier);

        log.info("Supplier rating updated successfully: {}", id);
    }

    @Override
    public BigDecimal getSupplierBalance(Long id) {
        Supplier supplier = findSupplierById(id);
        return supplier.getCurrentBalance() != null ? supplier.getCurrentBalance() : BigDecimal.ZERO;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }

    private void validateUniqueSupplierCode(String supplierCode, Long excludeId) {
        if (excludeId == null) {
            if (supplierRepository.existsBySupplierCode(supplierCode)) {
                throw new DuplicateResourceException("Supplier code already exists: " + supplierCode);
            }
        } else {
            if (supplierRepository.existsBySupplierCodeAndIdNot(supplierCode, excludeId)) {
                throw new DuplicateResourceException("Supplier code already exists: " + supplierCode);
            }
        }
    }

    private PageResponse<SupplierResponse> createPageResponse(Page<Supplier> supplierPage) {
        List<SupplierResponse> content = supplierPage.getContent().stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SupplierResponse>builder()
            .content(content)
            .pageNumber(supplierPage.getNumber())
            .pageSize(supplierPage.getSize())
            .totalElements(supplierPage.getTotalElements())
            .totalPages(supplierPage.getTotalPages())
            .last(supplierPage.isLast())
            .first(supplierPage.isFirst())
            .empty(supplierPage.isEmpty())
            .build();
    }
}
