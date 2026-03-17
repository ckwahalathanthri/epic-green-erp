package lk.epicgreen.erp.accounting.service.impl;


import lk.epicgreen.erp.accounting.dto.response.AgingReportDTO;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import lk.epicgreen.erp.customer.repository.CustomerLedgerRepository;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgingReportService {
    private final CustomerRepository customerRepository;
    private final CustomerLedgerRepository ledgerRepository;

    public List<AgingReportDTO> generateAgingReport() {
        List<CustomerLedger> legers=ledgerRepository.findByBalanceGreaterThan(BigDecimal.ZERO);

        LocalDate todaysDate= LocalDate.now();

        Map<Long, AgingReportDTO> reportMap=new HashMap<>();

        List<AgingReportDTO> agingReport=new ArrayList<>();
        for(CustomerLedger ledger:legers){
            Long customerid=ledger.getCustomer().getId();

            long daysOverdue= ChronoUnit.DAYS.between(
                    ledger.getDueDate(),
                    todaysDate
            );
            AgingReportDTO dto=reportMap.computeIfAbsent(customerid,id->
                    boilerplateCode(customerid,ledger.getCustomer().getCustomerName(),ledger.getBalance())
            );
            if(daysOverdue<=30){
                dto.setDays30(ledger.getBalance());

            }else if(daysOverdue<=60){
                dto.setDays60(ledger.getBalance());

            }else if(daysOverdue<=90){
                dto.setDays90(ledger.getBalance());
            }

        }
        return new ArrayList<>(reportMap.values());
    }

    public AgingReportDTO boilerplateCode(Long cusId,String cusName,BigDecimal amount){
        AgingReportDTO dto=new AgingReportDTO();
        dto.setCustomerId(cusId);
        dto.setCustomerName(cusName);
        dto.setCurrentAmount(amount);
        return dto;
    }

    public AgingReportDTO getCustomerAging(Long customerId) {
        CustomerLedger customerledger =ledgerRepository.findByCustomerIdAndBalanceGreaterThan(customerId,BigDecimal.ZERO);
        LocalDate today= LocalDate.now();

        Long daysOverdue= ChronoUnit.DAYS.between(
                customerledger.getDueDate(),
                today
        );
        AgingReportDTO dto=boilerplateCode(customerId, customerledger.getCustomer().getCustomerName(), customerledger.getBalance());

        if(daysOverdue<=30){
            dto.setDays30(customerledger.getBalance());
            return dto;
        }else if(daysOverdue<=60){
            dto.setDays60(customerledger.getBalance());
            return dto;
        }else if (daysOverdue<=90){
            dto.setDays90(customerledger.getBalance());
            return dto;
        }
        return dto;
    }
}
