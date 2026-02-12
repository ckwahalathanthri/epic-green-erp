package lk.epicgreen.erp.customer.service.impl;


import lk.epicgreen.erp.customer.dto.response.CustomerTypeDTO;
import lk.epicgreen.erp.customer.entity.CustomerType;
import lk.epicgreen.erp.customer.mapper.CustomerMapper;
import lk.epicgreen.erp.customer.repository.CustomerTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Type Service
 * Handles business logic for Customer Types
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;
    private final CustomerMapper customerMapper;

    public CustomerTypeDTO createCustomerType(CustomerTypeDTO dto, String username) {
        if (customerTypeRepository.existsByTypeCode(dto.getTypeCode())) {
            throw new RuntimeException("Customer type code already exists: " + dto.getTypeCode());
        }

        CustomerType customerType = customerMapper.toEntity(dto);
        customerType.setCreatedBy(username);
        customerType.setUpdatedBy(username);
        
        CustomerType saved = customerTypeRepository.save(customerType);
        return customerMapper.toDTO(saved);
    }

    public CustomerTypeDTO updateCustomerType(Long id, CustomerTypeDTO dto, String username) {
        CustomerType customerType = customerTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer type not found: " + id));

        customerType.setTypeCode(dto.getTypeCode());
        customerType.setTypeName(dto.getTypeName());
        customerType.setDescription(dto.getDescription());
        customerType.setIsActive(dto.getIsActive());
        customerType.setUpdatedBy(username);

        CustomerType updated = customerTypeRepository.save(customerType);
        return customerMapper.toDTO(updated);
    }

    @Transactional(readOnly = true)
    public CustomerTypeDTO getCustomerTypeById(Long id) {
        CustomerType customerType = customerTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer type not found: " + id));
        return customerMapper.toDTO(customerType);
    }

    @Transactional(readOnly = true)
    public Page<CustomerTypeDTO> getAllCustomerTypes(Pageable pageable) {
        return customerTypeRepository.findAll(pageable)
            .map(customerMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<CustomerTypeDTO> getActiveCustomerTypes() {
        return customerTypeRepository.findByIsActive(true).stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
    }

    public void deleteCustomerType(Long id) {
        if (!customerTypeRepository.existsById(id)) {
            throw new RuntimeException("Customer type not found: " + id);
        }
        customerTypeRepository.deleteById(id);
    }
}
