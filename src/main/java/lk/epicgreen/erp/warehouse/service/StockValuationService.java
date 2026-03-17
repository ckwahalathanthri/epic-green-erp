package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.entity.StockValuation;
import lk.epicgreen.erp.warehouse.repository.StockValuationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockValuationService {
    private final StockValuationRepository repository;
    
    public List<StockValuation> findByDate(LocalDate date) {
        return repository.findByValuationDate(date);
    }
}