package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.AgingReportSupplierDTO;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgingReportServiceSupplier {
    private final SupplierLedgerRepository ledgerRepository;
    private final SupplierRepository supplierRepository;

    public List<AgingReportSupplierDTO> generateAgingReport() {
        LocalDate today = LocalDate.now();
        List<SupplierLedger> overduePayments = ledgerRepository.findOverduePayments(today);
        
        Map<Long, List<SupplierLedger>> groupedBySupplier = overduePayments.stream()
            .collect(Collectors.groupingBy(l -> l.getSupplier().getId()));
        
        return groupedBySupplier.entrySet().stream()
            .map(entry -> calculateAging(entry.getKey(), entry.getValue(), today))
            .collect(Collectors.toList());
    }

    public AgingReportSupplierDTO getBySupplier(Long supplierId) {
        LocalDate today = LocalDate.now();
        List<SupplierLedger> overduePayments = ledgerRepository.findOverduePayments(today).stream()
            .filter(l -> l.getSupplier().getId().equals(supplierId))
            .collect(Collectors.toList());
        
        return calculateAging(supplierId, overduePayments, today);
    }

    private AgingReportSupplierDTO calculateAging(Long supplierId, List<SupplierLedger> transactions, LocalDate today) {
        var supplier = supplierRepository.findById(supplierId);
        AgingReportSupplierDTO dto = new AgingReportSupplierDTO();
        dto.setSupplierId(supplierId);
//        dto.setSupplierName(supplier.getSupplierName());
//        dto.setSupplierCode(supplier.getSupplierCode());
        
        for (SupplierLedger ledger : transactions) {
            BigDecimal amount = ledger.getDebitAmount().subtract(ledger.getCreditAmount());
            long daysPastDue = java.time.temporal.ChronoUnit.DAYS.between(ledger.getDueDate(), today);
            
            if (daysPastDue <= 0) {
                dto.setCurrentAmount(dto.getCurrentAmount().add(amount));
            } else if (daysPastDue <= 30) {
                dto.setDays30Amount(dto.getDays30Amount().add(amount));
            } else if (daysPastDue <= 60) {
                dto.setDays60Amount(dto.getDays60Amount().add(amount));
            } else if (daysPastDue <= 90) {
                dto.setDays90Amount(dto.getDays90Amount().add(amount));
            } else {
                dto.setDays90PlusAmount(dto.getDays90PlusAmount().add(amount));
            }
        }
        
        dto.setTotalOutstanding(
            dto.getCurrentAmount()
                .add(dto.getDays30Amount())
                .add(dto.getDays60Amount())
                .add(dto.getDays90Amount())
                .add(dto.getDays90PlusAmount())
        );
        
        return dto;
    }
}
