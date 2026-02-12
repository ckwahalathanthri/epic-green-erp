package lk.epicgreen.erp.credit.controller.service;


import lk.epicgreen.erp.credit.controller.dto.request.response.CustomerGroupDTO;
import lk.epicgreen.erp.credit.controller.entity.CustomerGroup;
import lk.epicgreen.erp.credit.controller.repo.CustomerGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerGroupService {
    private final CustomerGroupRepository repository;

    public CustomerGroupDTO create(CustomerGroupDTO dto, String username) {
        if (repository.existsByGroupCode(dto.getGroupCode())) {
            throw new RuntimeException("Group code already exists: " + dto.getGroupCode());
        }
        CustomerGroup entity = new CustomerGroup();
        entity.setGroupCode(dto.getGroupCode());
        entity.setGroupName(dto.getGroupName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setCreditLimitDefault(dto.getCreditLimitDefault());
        entity.setPaymentTermsDaysDefault(dto.getPaymentTermsDaysDefault());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        CustomerGroup saved = repository.save(entity);
        return toDTO(saved);
    }

    public CustomerGroupDTO update(Long id, CustomerGroupDTO dto, String username) {
        CustomerGroup entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found: " + id));
        entity.setGroupName(dto.getGroupName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setCreditLimitDefault(dto.getCreditLimitDefault());
        entity.setPaymentTermsDaysDefault(dto.getPaymentTermsDaysDefault());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        CustomerGroup updated = repository.save(entity);
        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public CustomerGroupDTO getById(Long id) {
        return repository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Group not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<CustomerGroupDTO> getAll() {
        return repository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerGroupDTO> getActive() {
        return repository.findByIsActive(true).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private CustomerGroupDTO toDTO(CustomerGroup entity) {
        CustomerGroupDTO dto = new CustomerGroupDTO();
        dto.setId(entity.getId());
        dto.setGroupCode(entity.getGroupCode());
        dto.setGroupName(entity.getGroupName());
        dto.setDescription(entity.getDescription());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setCreditLimitDefault(entity.getCreditLimitDefault());
        dto.setPaymentTermsDaysDefault(entity.getPaymentTermsDaysDefault());
        dto.setIsActive(entity.getIsActive());
        dto.setMemberCount(entity.getMembers() != null ? entity.getMembers().size() : 0);
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
