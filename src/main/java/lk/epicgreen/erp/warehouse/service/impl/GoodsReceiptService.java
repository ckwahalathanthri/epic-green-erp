package lk.epicgreen.erp.warehouse.service.impl;



import lk.epicgreen.erp.warehouse.entity.GoodsReceipt;
import lk.epicgreen.erp.warehouse.repository.GoodsReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsReceiptService {
    private final GoodsReceiptRepository repository;
    
    public List<GoodsReceipt> findAll() {
        return repository.findAll();
    }
    
    public GoodsReceipt findById(Long id) {
        return repository.findById(id).get();
    }
    
    public GoodsReceipt create(GoodsReceipt receipt) {
        return repository.save(receipt);
    }
    
    public GoodsReceipt post(Long id) {
        GoodsReceipt receipt = findById(id);
        receipt.post();
        return repository.save(receipt);
    }
}
