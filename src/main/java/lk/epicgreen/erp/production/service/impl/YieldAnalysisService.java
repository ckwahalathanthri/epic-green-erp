package lk.epicgreen.erp.production.service.impl;
import lk.epicgreen.erp.production.repository.ProductionYieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class YieldAnalysisService {
    private final ProductionYieldRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public List getByCategory(String category) {
        return repository.findByYieldCategory(category);
    }
}