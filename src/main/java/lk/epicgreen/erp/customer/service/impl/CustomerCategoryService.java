package lk.epicgreen.erp.customer.service.impl;


import lk.epicgreen.erp.customer.dto.response.CustomerCategoryDTO;

import lk.epicgreen.erp.customer.entity.CustomerCategory;
import lk.epicgreen.erp.customer.mapper.CustomerMapper;
import lk.epicgreen.erp.customer.repository.CustomerCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Category Service
 * Handles business logic for Customer Categories
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerCategoryService {

    private final CustomerCategoryRepository customerCategoryRepository;
    private final CustomerMapper customerMapper;

    public CustomerCategoryDTO createCustomerCategory(CustomerCategoryDTO dto, String username) {
        if (customerCategoryRepository.existsByCategoryCode(dto.getCategoryCode())) {
            throw new RuntimeException("Customer category code already exists: " + dto.getCategoryCode());
        }

        CustomerCategory customerCategory = customerMapper.toEntity(dto);
        customerCategory.setCreatedBy(username);
        customerCategory.setUpdatedBy(username);
        
        CustomerCategory saved = customerCategoryRepository.save(customerCategory);
        return customerMapper.toDTO(saved);
    }

    public CustomerCategoryDTO updateCustomerCategory(Long id, CustomerCategoryDTO dto, String username) {
        CustomerCategory customerCategory = customerCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer category not found: " + id));

        customerCategory.setCategoryCode(dto.getCategoryCode());
        customerCategory.setCategoryName(dto.getCategoryName());
        customerCategory.setDescription(dto.getDescription());
        customerCategory.setIsActive(dto.getIsActive());
        customerCategory.setUpdatedBy(username);

        CustomerCategory updated = customerCategoryRepository.save(customerCategory);
        return customerMapper.toDTO(updated);
    }

    @Transactional(readOnly = true)
    public CustomerCategoryDTO getCustomerCategoryById(Long id) {
        CustomerCategory customerCategory = customerCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer category not found: " + id));
        return customerMapper.toDTO(customerCategory);
    }

    @Transactional(readOnly = true)
    public Page<CustomerCategoryDTO> getAllCustomerCategories(Pageable pageable) {
        return customerCategoryRepository.findAll(pageable)
            .map(customerMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<CustomerCategoryDTO> getActiveCustomerCategories() {
        return customerCategoryRepository.findByIsActive(true).stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
    }

    public void deleteCustomerCategory(Long id) {
        if (!customerCategoryRepository.existsById(id)) {
            throw new RuntimeException("Customer category not found: " + id);
        }
        customerCategoryRepository.deleteById(id);
    }
}
