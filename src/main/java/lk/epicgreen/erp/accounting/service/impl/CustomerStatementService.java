package lk.epicgreen.erp.accounting.service.impl;


import lk.epicgreen.erp.accounting.dto.response.LedgerEntryDTO;
import lk.epicgreen.erp.accounting.dto.response.StatementDTO;
import lk.epicgreen.erp.customer.entity.CustomerStatement;
import lk.epicgreen.erp.customer.repository.CustomerStatementRepository;
import lk.epicgreen.erp.customer.service.CustomerLedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerStatementService {
    private final CustomerStatementRepository repository;
    private final CustomerLedgerService ledgerService;

    public StatementDTO generateStatement(Long customerId, LocalDate fromDate, LocalDate toDate, String username) {
        List<LedgerEntryDTO> transactions = ledgerService.getCustomerLedgerByDateRange(customerId, fromDate, toDate);
        
        StatementDTO statement = new StatementDTO();
        statement.setCustomerId(customerId);
        statement.setFromDate(fromDate);
        statement.setToDate(toDate);
        statement.setTransactions(transactions);
        return statement;
    }

    @Transactional(readOnly = true)
    public List<StatementDTO> getCustomerStatements(Long customerId) {
        return repository.findByCustomerIdOrderByStatementDateDesc(customerId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private StatementDTO toDTO(CustomerStatement entity) {
        StatementDTO dto = new StatementDTO();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setCustomerName(entity.getCustomer().getCustomerName());
        dto.setStatementNumber(entity.getStatementNumber());
        dto.setStatementDate(entity.getStatementDate());
        dto.setFromDate(entity.getFromDate());
        dto.setToDate(entity.getToDate());
        dto.setOpeningBalance(entity.getOpeningBalance());
        dto.setTotalDebit(entity.getTotalDebit());
        dto.setTotalCredit(entity.getTotalCredit());
        dto.setClosingBalance(entity.getClosingBalance());
        return dto;
    }
}
