package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierCategoryDTO;
import lk.epicgreen.erp.supplier.entity.SupplierCategory;
import lk.epicgreen.erp.supplier.mapper.SupplierMapper;
import lk.epicgreen.erp.supplier.repository.SupplierCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierCategoryService {
    private final SupplierCategoryRepository repository;
    private final SupplierMapper mapper;

    public SupplierCategoryDTO createSupplierCategory(SupplierCategoryDTO dto, String username) {
        if (repository.existsByCategoryCode(dto.getCategoryCode())) {
            throw new RuntimeException("Supplier category code already exists");
        }
        SupplierCategory entity = mapper.toEntity(dto);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return mapper.toDTO(repository.save(entity));
    }

    public SupplierCategoryDTO updateSupplierCategory(Long id, SupplierCategoryDTO dto, String username) {
        SupplierCategory entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier category not found"));
        entity.setCategoryCode(dto.getCategoryCode());
        entity.setCategoryName(dto.getCategoryName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(dto.getIsActive());
        entity.setUpdatedBy(username);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierCategoryDTO getSupplierCategoryById(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new RuntimeException("Supplier category not found"));
    }

    @Transactional(readOnly = true)
    public Page<SupplierCategoryDTO> getAllSupplierCategories(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<SupplierCategoryDTO> getActiveSupplierCategories() {
        return repository.findByIsActive(true).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public void deleteSupplierCategory(Long id) {
        repository.deleteById(id);
    }
}
