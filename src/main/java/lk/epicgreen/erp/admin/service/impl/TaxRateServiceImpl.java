package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.TaxRateRequest;
import lk.epicgreen.erp.admin.dto.response.TaxRateResponse;
import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.mapper.TaxRateMapper;
import lk.epicgreen.erp.admin.repository.TaxRateRepository;
import lk.epicgreen.erp.admin.service.TaxRateService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TaxRateService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaxRateServiceImpl implements TaxRateService {

    private final TaxRateRepository taxRateRepository;
    private final TaxRateMapper taxRateMapper;

    @Override
    @Transactional
    public TaxRateResponse createTaxRate(TaxRateRequest request) {
        log.info("Creating new tax rate: {}", request.getTaxCode());

        validateUniqueTaxCode(request.getTaxCode(), null);

        TaxRate taxRate = taxRateMapper.toEntity(request);
        taxRate.setIsActive(true);

        TaxRate savedTaxRate = taxRateRepository.save(taxRate);
        log.info("Tax rate created successfully: {}", savedTaxRate.getTaxCode());

        return taxRateMapper.toResponse(savedTaxRate);
    }

    @Override
    @Transactional
    public TaxRateResponse updateTaxRate(Long id, TaxRateRequest request) {
        log.info("Updating tax rate: {}", id);

        TaxRate taxRate = findTaxRateById(id);
        validateUniqueTaxCode(request.getTaxCode(), id);

        taxRateMapper.updateEntityFromRequest(request, taxRate);

        TaxRate updatedTaxRate = taxRateRepository.save(taxRate);
        log.info("Tax rate updated successfully: {}", updatedTaxRate.getTaxCode());

        return taxRateMapper.toResponse(updatedTaxRate);
    }

    @Override
    @Transactional
    public void activateTaxRate(Long id) {
        log.info("Activating tax rate: {}", id);

        TaxRate taxRate = findTaxRateById(id);
        taxRate.setIsActive(true);
        taxRateRepository.save(taxRate);

        log.info("Tax rate activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateTaxRate(Long id) {
        log.info("Deactivating tax rate: {}", id);

        TaxRate taxRate = findTaxRateById(id);
        taxRate.setIsActive(false);
        taxRateRepository.save(taxRate);

        log.info("Tax rate deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteTaxRate(Long id) {
        log.info("Deleting tax rate: {}", id);

        TaxRate taxRate = findTaxRateById(id);
        taxRateRepository.delete(taxRate);

        log.info("Tax rate deleted successfully: {}", id);
    }

    @Override
    public TaxRateResponse getTaxRateById(Long id) {
        TaxRate taxRate = findTaxRateById(id);
        return taxRateMapper.toResponse(taxRate);
    }

    @Override
    public TaxRateResponse getTaxRateByCode(String taxCode) {
        TaxRate taxRate = taxRateRepository.findByTaxCode(taxCode)
            .orElseThrow(() -> new ResourceNotFoundException("Tax rate not found: " + taxCode));
        return taxRateMapper.toResponse(taxRate);
    }

    @Override
    public List<TaxRateResponse> getAllTaxRates() {
        List<TaxRate> taxRates = taxRateRepository.findAll();
        return taxRates.stream()
            .map(taxRateMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> getAllActiveTaxRates() {
        List<TaxRate> taxRates = taxRateRepository.findByIsActiveTrue();
        return taxRates.stream()
            .map(taxRateMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> getTaxRatesByType(String taxType) {
        List<TaxRate> taxRates = taxRateRepository.findByTaxType(taxType);
        return taxRates.stream()
            .map(taxRateMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> getActiveTaxRatesByDate(LocalDate date) {
        List<TaxRate> taxRates = taxRateRepository.findActiveTaxRatesByDate(date);
        return taxRates.stream()
            .map(taxRateMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private TaxRate findTaxRateById(Long id) {
        return taxRateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tax rate not found: " + id));
    }

    private void validateUniqueTaxCode(String taxCode, Long excludeId) {
        if (excludeId == null) {
            if (taxRateRepository.existsByTaxCode(taxCode)) {
                throw new DuplicateResourceException("Tax code already exists: " + taxCode);
            }
        } else {
            if (taxRateRepository.existsByTaxCodeAndIdNot(taxCode, excludeId)) {
                throw new DuplicateResourceException("Tax code already exists: " + taxCode);
            }
        }
    }
}
