package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.entity.SalesQuotation;
import lk.epicgreen.erp.sales.repository.SalesQuotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationSchedularService {

    private final SalesQuotationRepository quotationRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireQuotations(){
        List<String> nonExpiredStatuses=new ArrayList<>();
        nonExpiredStatuses.add("EXPIRED");
        nonExpiredStatuses.add("CONVERTED");
        List<SalesQuotation> expiredQuotations=quotationRepository.findByQuotationStatusNotInAndValidUntilLessThan(nonExpiredStatuses, java.time.LocalDate.now());
        for(SalesQuotation expiredQuotation:expiredQuotations){
            expiredQuotation.setQuotationStatus("EXPIRED");
        }
        quotationRepository.saveAll(expiredQuotations);

    }
}
