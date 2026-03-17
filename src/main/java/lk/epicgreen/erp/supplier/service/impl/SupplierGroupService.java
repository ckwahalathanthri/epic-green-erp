package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierGroupDTO;
import lk.epicgreen.erp.supplier.entity.SupplierGroup;
import lk.epicgreen.erp.supplier.repository.SupplierGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierGroupService {
    private final SupplierGroupRepository repository;

    public SupplierGroupDTO create(SupplierGroupDTO dto, String username) {
        if (repository.existsByGroupCode(dto.getGroupCode())) {
            throw new RuntimeException("Group code already exists");
        }
        SupplierGroup entity = new SupplierGroup();
        entity.setGroupCode(dto.getGroupCode());
        entity.setGroupName(dto.getGroupName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setCreditDaysDefault(dto.getCreditDaysDefault());
        entity.setPaymentTermsDaysDefault(dto.getPaymentTermsDaysDefault());
        entity.setLeadTimeDaysDefault(dto.getLeadTimeDaysDefault());
        entity.setMinimumOrderValue(dto.getMinimumOrderValue());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    public SupplierGroupDTO update(Long id, SupplierGroupDTO dto, String username) {
        SupplierGroup entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found"));
        entity.setGroupName(dto.getGroupName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setCreditDaysDefault(dto.getCreditDaysDefault());
        entity.setPaymentTermsDaysDefault(dto.getPaymentTermsDaysDefault());
        entity.setLeadTimeDaysDefault(dto.getLeadTimeDaysDefault());
        entity.setMinimumOrderValue(dto.getMinimumOrderValue());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierGroupDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Transactional(readOnly = true)
    public List<SupplierGroupDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupplierGroupDTO> getActive() {
        return repository.findByIsActive(true).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SupplierGroupDTO toDTO(SupplierGroup entity) {
        SupplierGroupDTO dto = new SupplierGroupDTO();
        dto.setId(entity.getId());
        dto.setGroupCode(entity.getGroupCode());
        dto.setGroupName(entity.getGroupName());
        dto.setDescription(entity.getDescription());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setCreditDaysDefault(entity.getCreditDaysDefault());
        dto.setPaymentTermsDaysDefault(entity.getPaymentTermsDaysDefault());
        dto.setLeadTimeDaysDefault(entity.getLeadTimeDaysDefault());
        dto.setMinimumOrderValue(entity.getMinimumOrderValue());
        dto.setIsActive(entity.getIsActive());
        dto.setMemberCount(entity.getMembers() != null ? entity.getMembers().size() : 0);
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
