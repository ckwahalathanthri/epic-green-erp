package lk.epicgreen.erp.credit.controller.service;


import lk.epicgreen.erp.credit.controller.dto.request.response.CreditLimitDTO;
import lk.epicgreen.erp.credit.controller.entity.CreditLimit;
import lk.epicgreen.erp.credit.controller.repo.CreditLimitRepository;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditLimitService {
    private final CreditLimitRepository repository;
    private final CustomerRepository customerRepository;

    public CreditLimitDTO create(CreditLimitDTO dto, String username) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CreditLimit entity = new CreditLimit();
        entity.setCustomer(customer);
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setCreditUsed(dto.getCreditUsed());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setIsActive(dto.getIsActive());
        entity.setApprovalStatus(dto.getApprovalStatus());
        entity.setNotes(dto.getNotes());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        
        CreditLimit saved = repository.save(entity);
        return toDTO(saved);
    }

    public CreditLimitDTO update(Long id, CreditLimitDTO dto, String username) {
        CreditLimit entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit limit not found"));
        
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setIsActive(dto.getIsActive());
        entity.setNotes(dto.getNotes());
        entity.setUpdatedBy(username);
        
        CreditLimit updated = repository.save(entity);
        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public CreditLimitDTO getById(Long id) {
        return repository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Credit limit not found"));
    }

    @Transactional(readOnly = true)
    public List<CreditLimitDTO> getByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CreditLimitDTO getActiveByCustomerId(Long customerId) {
        return repository.findActiveByCustomerId(customerId)
            .map(this::toDTO)
            .orElse(null);
    }

    public CreditLimitDTO approve(Long id, String username) {
        CreditLimit entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit limit not found"));
        entity.setApprovalStatus("APPROVED");
        entity.setApprovedBy(username);
        entity.setApprovalDate(java.time.LocalDateTime.now());
        CreditLimit updated = repository.save(entity);
        return toDTO(updated);
    }

    private CreditLimitDTO toDTO(CreditLimit entity) {
        CreditLimitDTO dto = new CreditLimitDTO();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setCustomerName(entity.getCustomer().getCustomerName());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setCreditUsed(entity.getCreditUsed());
        dto.setCreditAvailable(entity.getCreditAvailable());
        dto.setEffectiveDate(entity.getEffectiveDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setIsActive(entity.getIsActive());
        dto.setApprovalStatus(entity.getApprovalStatus());
        dto.setApprovedBy(entity.getApprovedBy());
        dto.setApprovalDate(entity.getApprovalDate());
        dto.setNotes(entity.getNotes());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
