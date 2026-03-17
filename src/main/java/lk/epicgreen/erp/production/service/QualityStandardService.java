package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.repository.QualityStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QualityStandardService {
    private final QualityStandardRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public List getByProduct(Long productId) {
        return repository.findByProductId(productId);
    }
}