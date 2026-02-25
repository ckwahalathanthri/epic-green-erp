package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.dto.response.ProductionOrderDTO;
import lk.epicgreen.erp.production.entity.ProductionOrder;
import lk.epicgreen.erp.production.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionOrderService {
    private final ProductionOrderRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public Object getById(Long id) {
        return repository.findById(id).get();
    }
    
    public Object create(ProductionOrderDTO dto) {
        return repository.save(toEntity(dto));
    }

    public ProductionOrder toEntity(ProductionOrderDTO dto) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderDate(dto.getOrderDate());
        order.setProductId(dto.getProductId());
        order.setOrderStatus(dto.getOrderStatus());

        return order;
    }
}