package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.WorkOrderRequest;
import lk.epicgreen.erp.production.dto.response.WorkOrderResponse;
import lk.epicgreen.erp.production.entity.WorkOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Mapper for WorkOrder entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class WorkOrderMapper {

    private final WorkOrderItemMapper workOrderItemMapper;

    public WorkOrderMapper(WorkOrderItemMapper workOrderItemMapper) {
        this.workOrderItemMapper = workOrderItemMapper;
    }

    public WorkOrder toEntity(WorkOrderRequest request) {
        if (request == null) {
            return null;
        }

        return WorkOrder.builder()
            .woNumber(request.getWoNumber())
            .woDate(request.getWoDate())
            .plannedQuantity(request.getPlannedQuantity())
            .actualQuantity(request.getActualQuantity() != null ? request.getActualQuantity() : BigDecimal.ZERO)
            .batchNumber(request.getBatchNumber())
            .manufacturingDate(request.getManufacturingDate())
            .expectedCompletionDate(request.getExpectedCompletionDate())
            .actualCompletionDate(request.getActualCompletionDate())
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
            .materialCost(request.getMaterialCost() != null ? request.getMaterialCost() : BigDecimal.ZERO)
            .laborCost(request.getLaborCost() != null ? request.getLaborCost() : BigDecimal.ZERO)
            .overheadCost(request.getOverheadCost() != null ? request.getOverheadCost() : BigDecimal.ZERO)
            .totalCost(request.getTotalCost() != null ? request.getTotalCost() : BigDecimal.ZERO)
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(WorkOrderRequest request, WorkOrder workOrder) {
        if (request == null || workOrder == null) {
            return;
        }

        workOrder.setWoNumber(request.getWoNumber());
        workOrder.setWoDate(request.getWoDate());
        workOrder.setPlannedQuantity(request.getPlannedQuantity());
        workOrder.setActualQuantity(request.getActualQuantity());
        workOrder.setBatchNumber(request.getBatchNumber());
        workOrder.setManufacturingDate(request.getManufacturingDate());
        workOrder.setExpectedCompletionDate(request.getExpectedCompletionDate());
        workOrder.setActualCompletionDate(request.getActualCompletionDate());
        
        if (request.getStatus() != null) {
            workOrder.setStatus(request.getStatus());
        }
        
        if (request.getPriority() != null) {
            workOrder.setPriority(request.getPriority());
        }
        
        workOrder.setMaterialCost(request.getMaterialCost());
        workOrder.setLaborCost(request.getLaborCost());
        workOrder.setOverheadCost(request.getOverheadCost());
        workOrder.setTotalCost(request.getTotalCost());
        workOrder.setRemarks(request.getRemarks());
    }

    public WorkOrderResponse toResponse(WorkOrder workOrder) {
        if (workOrder == null) {
            return null;
        }

        return WorkOrderResponse.builder()
            .id(workOrder.getId())
            .woNumber(workOrder.getWoNumber())
            .woDate(workOrder.getWoDate())
            .bomId(workOrder.getBom() != null ? workOrder.getBom().getId() : null)
            .bomCode(workOrder.getBom() != null ? workOrder.getBom().getBomCode() : null)
            .finishedProductId(workOrder.getFinishedProduct() != null ? workOrder.getFinishedProduct().getId() : null)
            .finishedProductCode(workOrder.getFinishedProduct() != null ? workOrder.getFinishedProduct().getProductCode() : null)
            .finishedProductName(workOrder.getFinishedProduct() != null ? workOrder.getFinishedProduct().getProductName() : null)
            .warehouseId(workOrder.getWarehouse() != null ? workOrder.getWarehouse().getId() : null)
            .warehouseCode(workOrder.getWarehouse() != null ? workOrder.getWarehouse().getWarehouseCode() : null)
            .warehouseName(workOrder.getWarehouse() != null ? workOrder.getWarehouse().getWarehouseName() : null)
            .plannedQuantity(workOrder.getPlannedQuantity())
            .actualQuantity(workOrder.getActualQuantity())
            .uomId(workOrder.getUom() != null ? workOrder.getUom().getId() : null)
            .uomCode(workOrder.getUom() != null ? workOrder.getUom().getUomCode() : null)
            .uomName(workOrder.getUom() != null ? workOrder.getUom().getUomName() : null)
            .batchNumber(workOrder.getBatchNumber())
            .manufacturingDate(workOrder.getManufacturingDate())
            .expectedCompletionDate(workOrder.getExpectedCompletionDate())
            .actualCompletionDate(workOrder.getActualCompletionDate())
            .status(workOrder.getStatus())
            .priority(workOrder.getPriority())
            .supervisorId(workOrder.getSupervisor() != null ? workOrder.getSupervisor().getId() : null)
            .supervisorName(workOrder.getSupervisor() != null ? 
                workOrder.getSupervisor().getFirstName() + " " + workOrder.getSupervisor().getLastName() : null)
            .materialCost(workOrder.getMaterialCost())
            .laborCost(workOrder.getLaborCost())
            .overheadCost(workOrder.getOverheadCost())
            .totalCost(workOrder.getTotalCost())
            .remarks(workOrder.getRemarks())
            .createdAt(workOrder.getCreatedAt())
            .createdBy(workOrder.getCreatedBy())
            .updatedAt(workOrder.getUpdatedAt())
            .items(workOrder.getItems() != null ? 
                workOrder.getItems().stream()
                    .map(workOrderItemMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
