package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.response.SupplierTypeDTO;
import lk.epicgreen.erp.supplier.entity.SupplierType;
import lk.epicgreen.erp.supplier.mapper.SupplierMapper;
import lk.epicgreen.erp.supplier.repository.SupplierTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierTypeService {
    private final SupplierTypeRepository repository;
    private final SupplierMapper mapper;

    public SupplierTypeDTO createSupplierType(SupplierTypeDTO dto, String username) {
        if (repository.existsByTypeCode(dto.getTypeCode())) {
            throw new RuntimeException("Supplier type code already exists");
        }
        SupplierType entity = mapper.toEntity(dto);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return mapper.toDTO(repository.save(entity));
    }

    public SupplierTypeDTO updateSupplierType(Long id, SupplierTypeDTO dto, String username) {
        SupplierType entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier type not found"));
        entity.setTypeCode(dto.getTypeCode());
        entity.setTypeName(dto.getTypeName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierTypeDTO getSupplierTypeById(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new RuntimeException("Supplier type not found"));
    }

    @Transactional(readOnly = true)
    public Page<SupplierTypeDTO> getAllSupplierTypes(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<SupplierTypeDTO> getActiveSupplierTypes() {
        return repository.findByIsActive(true).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public void deleteSupplierType(Long id) {
        repository.deleteById(id);
    }
}
