package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierBankAccountDTO;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierBankAccount;
import lk.epicgreen.erp.supplier.repository.SupplierBankAccountRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierBankAccountService {
    private final SupplierBankAccountRepository repository;
    private final SupplierRepository supplierRepository;

    public SupplierBankAccountDTO create(SupplierBankAccountDTO dto, String username) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierBankAccount entity = new SupplierBankAccount();
        entity.setSupplier(supplier);
        entity.setBankName(dto.getBankName());
        entity.setBranchName(dto.getBranchName());
        entity.setAccountHolderName(dto.getAccountHolderName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountType(dto.getAccountType());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setIban(dto.getIban());
        entity.setCurrency(dto.getCurrency());
        entity.setIsPrimary(dto.getIsPrimary());
        entity.setIsActive(dto.getIsActive());
        entity.setNotes(dto.getNotes());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    public SupplierBankAccountDTO update(Long id, SupplierBankAccountDTO dto, String username) {
        SupplierBankAccount entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bank account not found"));
        entity.setBankName(dto.getBankName());
        entity.setBranchName(dto.getBranchName());
        entity.setAccountHolderName(dto.getAccountHolderName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountType(dto.getAccountType());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setIban(dto.getIban());
        entity.setCurrency(dto.getCurrency());
        entity.setIsPrimary(dto.getIsPrimary());
        entity.setIsActive(dto.getIsActive());
        entity.setNotes(dto.getNotes());
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierBankAccountDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Bank account not found"));
    }

    @Transactional(readOnly = true)
    public List<SupplierBankAccountDTO> getBySupplierId(Long supplierId) {
        return repository.findBySupplierId(supplierId).stream()
            .map(this::toDTO).collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SupplierBankAccountDTO toDTO(SupplierBankAccount entity) {
        SupplierBankAccountDTO dto = new SupplierBankAccountDTO();
        dto.setId(entity.getId());
        dto.setSupplierId(entity.getSupplier().getId());
        dto.setSupplierName(entity.getSupplier().getSupplierName());
        dto.setBankName(entity.getBankName());
        dto.setBranchName(entity.getBranchName());
        dto.setAccountHolderName(entity.getAccountHolderName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setAccountType(entity.getAccountType());
        dto.setSwiftCode(entity.getSwiftCode());
        dto.setIban(entity.getIban());
        dto.setCurrency(entity.getCurrency());
        dto.setIsPrimary(entity.getIsPrimary());
        dto.setIsActive(entity.getIsActive());
        dto.setNotes(entity.getNotes());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
