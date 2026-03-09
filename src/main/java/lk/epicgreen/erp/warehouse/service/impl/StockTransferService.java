package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.entity.StockTransfer;
import lk.epicgreen.erp.warehouse.entity.StockTransferItem;
import lk.epicgreen.erp.warehouse.repository.StockTransferItemRepository;
import lk.epicgreen.erp.warehouse.repository.StockTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockTransferService {
    private final StockTransferRepository repository;
    private final StockTransferItemRepository itemRepository;
    
    public List<StockTransfer> findAll() {
        return repository.findAll();
    }
    
    public StockTransfer findById(Long id) {
        return repository.findById(id).get();
    }
    
    public List<StockTransfer> findByStatus(String status) {
        return repository.findByTransferStatus(status);
    }
    
    public StockTransfer create(StockTransfer transfer) {
        return repository.save(transfer);
    }
    
    public StockTransfer confirm(Long id) {
        StockTransfer transfer = findById(id);
        transfer.confirm();
        return repository.save(transfer);
    }
    
    public StockTransfer ship(Long id) {
        StockTransfer transfer = findById(id);
        transfer.ship();
        return repository.save(transfer);
    }
    
    public StockTransfer receive(Long id) {
        StockTransfer transfer = findById(id);
        transfer.receive();
        return repository.save(transfer);
    }
    
    public List<StockTransferItem> getItems(Long transferId) {
        return itemRepository.findByTransferId(transferId);
    }
}
