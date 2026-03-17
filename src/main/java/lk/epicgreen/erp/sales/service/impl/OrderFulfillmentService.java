package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.OrderFulfillmentDTO;
import lk.epicgreen.erp.sales.entity.OrderFulfillment;
import lk.epicgreen.erp.sales.mapper.OrderFulfillmentMapper;
import lk.epicgreen.erp.sales.repository.OrderFulfillmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderFulfillmentService {
    
    private final OrderFulfillmentRepository fulfillmentRepository;
    private final OrderFulfillmentMapper fulfillmentMapper;
    
    public List<OrderFulfillmentDTO> getAllFulfillments() {
        return fulfillmentRepository.findAll().stream()
            .map(fulfillmentMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public OrderFulfillmentDTO getFulfillmentByOrderId(Long orderId) {
        OrderFulfillment fulfillment = fulfillmentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Fulfillment not found for order: " + orderId));
        return fulfillmentMapper.toDTO(fulfillment);
    }
    
    public OrderFulfillmentDTO createOrUpdateFulfillment(OrderFulfillmentDTO fulfillmentDTO) {
        OrderFulfillment fulfillment = fulfillmentRepository.findByOrderId(fulfillmentDTO.getOrderId())
            .orElse(new OrderFulfillment());
        
        fulfillmentMapper.updateEntityFromDTO(fulfillmentDTO, fulfillment);
        OrderFulfillment savedFulfillment = fulfillmentRepository.save(fulfillment);
        
        return fulfillmentMapper.toDTO(savedFulfillment);
    }
}