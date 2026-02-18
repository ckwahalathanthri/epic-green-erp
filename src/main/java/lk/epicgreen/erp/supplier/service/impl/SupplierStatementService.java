package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.StatementDTO;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.entity.SupplierStatement;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.repository.SupplierStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierStatementService {
    private final SupplierStatementRepository repository;
    private final SupplierLedgerRepository ledgerRepository;
    private final SupplierRepository supplierRepository;

    public StatementDTO generateStatement(Long supplierId, LocalDate fromDate, LocalDate toDate, String username) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        List<SupplierLedger> transactions = ledgerRepository.findBySupplierIdAndDateRange(
            supplierId, fromDate, toDate);
        
        BigDecimal opening = ledgerRepository.calculateBalance(supplierId);
        BigDecimal debits = transactions.stream()
            .map(SupplierLedger::getDebitAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal credits = transactions.stream()
            .map(SupplierLedger::getCreditAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal closing = opening.add(debits).subtract(credits);
        
        SupplierStatement statement = new SupplierStatement();
        statement.setSupplier(supplier);
        statement.setStatementNumber("STMT-" + supplierId + "-" + System.currentTimeMillis());
        statement.setFromDate(fromDate);
        statement.setToDate(toDate);
        statement.setOpeningBalance(opening);
        statement.setTotalDebits(debits);
        statement.setTotalCredits(credits);
        statement.setClosingBalance(closing);
        statement.setGeneratedBy(username);
        
        SupplierStatement saved = repository.save(statement);
        
        StatementDTO dto = new StatementDTO();
        dto.setId(saved.getId());
        dto.setSupplierId(supplierId);
        dto.setSupplierName(supplier.getSupplierName());
        dto.setStatementNumber(saved.getStatementNumber());
        dto.setFromDate(saved.getFromDate());
        dto.setToDate(saved.getToDate());
        dto.setOpeningBalance(saved.getOpeningBalance());
        dto.setTotalDebits(saved.getTotalDebits());
        dto.setTotalCredits(saved.getTotalCredits());
        dto.setClosingBalance(saved.getClosingBalance());
        dto.setGeneratedBy(saved.getGeneratedBy());
        dto.setGeneratedAt(saved.getGeneratedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<StatementDTO> getBySupplier(Long supplierId) {
        return repository.findBySupplierId(supplierId).stream()
            .map(this::toDTO).collect(Collectors.toList());
    }

    private StatementDTO toDTO(SupplierStatement entity) {
        StatementDTO dto = new StatementDTO();
        dto.setId(entity.getId());
        dto.setSupplierId(entity.getSupplier().getId());
        dto.setSupplierName(entity.getSupplier().getSupplierName());
        dto.setStatementNumber(entity.getStatementNumber());
        dto.setFromDate(entity.getFromDate());
        dto.setToDate(entity.getToDate());
        dto.setOpeningBalance(entity.getOpeningBalance());
        dto.setTotalDebits(entity.getTotalDebits());
        dto.setTotalCredits(entity.getTotalCredits());
        dto.setClosingBalance(entity.getClosingBalance());
        dto.setGeneratedBy(entity.getGeneratedBy());
        dto.setGeneratedAt(entity.getGeneratedAt());
        return dto;
    }
}
