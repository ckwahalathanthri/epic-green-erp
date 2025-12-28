package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.dto.request.MaterialConsumptionRequest;
import lk.epicgreen.erp.production.dto.response.MaterialConsumptionResponse;
import lk.epicgreen.erp.production.entity.MaterialConsumption;
import lk.epicgreen.erp.production.entity.WorkOrder;
import lk.epicgreen.erp.production.entity.WorkOrderItem;
import lk.epicgreen.erp.production.mapper.MaterialConsumptionMapper;
import lk.epicgreen.erp.production.repository.MaterialConsumptionRepository;
import lk.epicgreen.erp.production.repository.WorkOrderRepository;
import lk.epicgreen.erp.production.repository.WorkOrderItemRepository;
import lk.epicgreen.erp.production.service.MaterialConsumptionService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of MaterialConsumptionService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MaterialConsumptionServiceImpl implements MaterialConsumptionService {

    private final MaterialConsumptionRepository materialConsumptionRepository;
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderItemRepository workOrderItemRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final MaterialConsumptionMapper materialConsumptionMapper;

    @Override
    @Transactional
    public MaterialConsumptionResponse recordConsumption(MaterialConsumptionRequest request) {
        log.info("Recording material consumption for work order: {}", request.getWoId());

        // Verify work order exists
        WorkOrder workOrder = findWorkOrderById(request.getWoId());

        // Verify raw material exists
        Product rawMaterial = findProductById(request.getRawMaterialId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Verify UOM exists
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        // Create material consumption entity
        MaterialConsumption consumption = materialConsumptionMapper.toEntity(request);
        consumption.setWorkOrder(workOrder);
        consumption.setRawMaterial(rawMaterial);
        consumption.setWarehouse(warehouse);
        consumption.setUom(uom);

        // Set work order item if provided
        if (request.getWoItemId() != null) {
            WorkOrderItem woItem = findWorkOrderItemById(request.getWoItemId());
            consumption.setWorkOrderItem(woItem);
            
            // Update work order item consumed quantity
            BigDecimal currentConsumed = woItem.getConsumedQuantity() != null ? 
                woItem.getConsumedQuantity() : BigDecimal.ZERO;
            woItem.setConsumedQuantity(currentConsumed.add(request.getQuantityConsumed()));
            
            // Update status if fully consumed
            if (woItem.getConsumedQuantity().compareTo(woItem.getPlannedQuantity()) >= 0) {
                woItem.setStatus("CONSUMED");
            }
            
            workOrderItemRepository.save(woItem);
        }

        MaterialConsumption savedConsumption = materialConsumptionRepository.save(consumption);
        log.info("Material consumption recorded successfully: {}", savedConsumption.getId());

        return materialConsumptionMapper.toResponse(savedConsumption);
    }

    @Override
    public MaterialConsumptionResponse getMaterialConsumptionById(Long id) {
        MaterialConsumption consumption = findMaterialConsumptionById(id);
        return materialConsumptionMapper.toResponse(consumption);
    }

    @Override
    public PageResponse<MaterialConsumptionResponse> getAllMaterialConsumption(Pageable pageable) {
        Page<MaterialConsumption> consumptionPage = materialConsumptionRepository.findAll(pageable);
        return createPageResponse(consumptionPage);
    }

    @Override
    public List<MaterialConsumptionResponse> getConsumptionByWorkOrder(Long woId) {
        List<MaterialConsumption> consumptions = materialConsumptionRepository.findByWoId(woId);
        return consumptions.stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MaterialConsumptionResponse> getConsumptionByWorkOrderItem(Long woItemId) {
        List<MaterialConsumption> consumptions = materialConsumptionRepository.findByWoItemId(woItemId);
        return consumptions.stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MaterialConsumptionResponse> getConsumptionByRawMaterial(Long rawMaterialId) {
        List<MaterialConsumption> consumptions = materialConsumptionRepository
            .findByRawMaterialId(rawMaterialId);
        return consumptions.stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MaterialConsumptionResponse> getConsumptionByWarehouse(Long warehouseId) {
        List<MaterialConsumption> consumptions = materialConsumptionRepository
            .findByWarehouseId(warehouseId);
        return consumptions.stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MaterialConsumptionResponse> getConsumptionByDateRange(LocalDate startDate, LocalDate endDate) {
        List<MaterialConsumption> consumptions = materialConsumptionRepository
            .findByConsumptionDateBetween(startDate, endDate);
        return consumptions.stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalConsumptionByWoAndMaterial(Long woId, Long rawMaterialId) {
        return materialConsumptionRepository
            .sumQuantityByWoAndMaterial(woId, rawMaterialId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalConsumptionCostByWorkOrder(Long woId) {
        return materialConsumptionRepository
            .sumTotalCostByWo(woId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public PageResponse<MaterialConsumptionResponse> searchMaterialConsumption(String keyword, Pageable pageable) {
        Page<MaterialConsumption> consumptionPage = materialConsumptionRepository
            .searchConsumptions(keyword, pageable);
        return createPageResponse(consumptionPage);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private MaterialConsumption findMaterialConsumptionById(Long id) {
        return materialConsumptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Material consumption not found: " + id));
    }

    private WorkOrder findWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Work Order not found: " + id));
    }

    private WorkOrderItem findWorkOrderItemById(Long id) {
        return workOrderItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Work Order Item not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private PageResponse<MaterialConsumptionResponse> createPageResponse(Page<MaterialConsumption> consumptionPage) {
        List<MaterialConsumptionResponse> content = consumptionPage.getContent().stream()
            .map(materialConsumptionMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<MaterialConsumptionResponse>builder()
            .content(content)
            .pageNumber(consumptionPage.getNumber())
            .pageSize(consumptionPage.getSize())
            .totalElements(consumptionPage.getTotalElements())
            .totalPages(consumptionPage.getTotalPages())
            .last(consumptionPage.isLast())
            .first(consumptionPage.isFirst())
            .empty(consumptionPage.isEmpty())
            .build();
    }
}
