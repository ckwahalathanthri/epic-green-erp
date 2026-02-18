package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentTermsDTO;
import lk.epicgreen.erp.supplier.entity.SupplierPaymentTerms;
import lk.epicgreen.erp.supplier.repository.SupplierPaymentTermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierPaymentTermsService {
    private final SupplierPaymentTermsRepository repository;

    public SupplierPaymentTermsDTO create(SupplierPaymentTermsDTO dto, String username) {
        if (repository.existsByTermsCode(dto.getTermsCode())) {
            throw new RuntimeException("Terms code already exists");
        }
        SupplierPaymentTerms entity = new SupplierPaymentTerms();
        entity.setTermsCode(dto.getTermsCode());
        entity.setTermsName(dto.getTermsName());
        entity.setDays(dto.getDays());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setDiscountDays(dto.getDiscountDays());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    public SupplierPaymentTermsDTO update(Long id, SupplierPaymentTermsDTO dto, String username) {
        SupplierPaymentTerms entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment terms not found"));
        entity.setTermsName(dto.getTermsName());
        entity.setDays(dto.getDays());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setDiscountDays(dto.getDiscountDays());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierPaymentTermsDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Payment terms not found"));
    }

    @Transactional(readOnly = true)
    public List<SupplierPaymentTermsDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupplierPaymentTermsDTO> getActive() {
        return repository.findByIsActive(true).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SupplierPaymentTermsDTO toDTO(SupplierPaymentTerms entity) {
        SupplierPaymentTermsDTO dto = new SupplierPaymentTermsDTO();
        dto.setId(entity.getId());
        dto.setTermsCode(entity.getTermsCode());
        dto.setTermsName(entity.getTermsName());
        dto.setDays(entity.getDays());
        dto.setDescription(entity.getDescription());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setDiscountDays(entity.getDiscountDays());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
