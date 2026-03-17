package lk.epicgreen.erp.credit.controller.service;


import lk.epicgreen.erp.credit.controller.dto.request.response.PaymentTermsDTO;
import lk.epicgreen.erp.credit.controller.entity.PaymentTerms;
import lk.epicgreen.erp.credit.controller.repo.PaymentTermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentTermsService {
    private final PaymentTermsRepository repository;

    public PaymentTermsDTO create(PaymentTermsDTO dto, String username) {
        if (repository.existsByTermsCode(dto.getTermsCode())) {
            throw new RuntimeException("Terms code already exists");
        }
        PaymentTerms entity = new PaymentTerms();
        entity.setTermsCode(dto.getTermsCode());
        entity.setTermsName(dto.getTermsName());
        entity.setDays(dto.getDays());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setDiscountDays(dto.getDiscountDays());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        PaymentTerms saved = repository.save(entity);
        return toDTO(saved);
    }

    public PaymentTermsDTO update(Long id, PaymentTermsDTO dto, String username) {
        PaymentTerms entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment terms not found"));
        entity.setTermsName(dto.getTermsName());
        entity.setDays(dto.getDays());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setDiscountDays(dto.getDiscountDays());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        PaymentTerms updated = repository.save(entity);
        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public PaymentTermsDTO getById(Long id) {
        return repository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Payment terms not found"));
    }

    @Transactional(readOnly = true)
    public List<PaymentTermsDTO> getAll() {
        return repository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentTermsDTO> getActive() {
        return repository.findByIsActive(true).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private PaymentTermsDTO toDTO(PaymentTerms entity) {
        PaymentTermsDTO dto = new PaymentTermsDTO();
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
