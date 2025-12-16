package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.WorkOrderRequest;
import lk.epicgreen.erp.production.entity.WorkOrder;
import lk.epicgreen.erp.production.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * WorkOrder Service Implementation
 * Implementation of work order service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {
    
    private final WorkOrderRepository workOrderRepository;
    
    @Override
    public WorkOrder createWorkOrder(WorkOrderRequest request) {
        log.info("Creating work order for product: {}", request.getProductId());
        
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderNumber(generateWorkOrderNumber());
        workOrder.setProductId(request.getProductId());
        workOrder.setProductName(request.getProductName());
        workOrder.setBomId(request.getBomId());
        workOrder.setSalesOrderId(request.getSalesOrderId());
        workOrder.setWorkOrderType(request.getWorkOrderType() != null ? request.getWorkOrderType() : "STANDARD");
        workOrder.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        workOrder.setPlannedQuantity(request.getPlannedQuantity() != null ? BigDecimal.valueOf(request.getPlannedQuantity()) : BigDecimal.ZERO);
        workOrder.setPlannedStartDate(request.getPlannedStartDate());
        workOrder.setPlannedEndDate(request.getPlannedEndDate());
        workOrder.setProductionLineId(request.getProductionLineId());
        workOrder.setSupervisorId(request.getSupervisorId());
        workOrder.setSupervisorName(request.getSupervisorName());
        workOrder.setStatus("DRAFT");
        workOrder.setIsApproved(false);
        workOrder.setIsCompleted(false);
        workOrder.setActualQuantity(0.0);
        workOrder.setNotes(request.getNotes());
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public WorkOrder updateWorkOrder(Long id, WorkOrderRequest request) {
        log.info("Updating work order: {}", id);
        WorkOrder existing = getWorkOrderById(id);
        
        if (existing.getIsApproved() || "IN_PROGRESS".equals(existing.getStatus())) {
            throw new RuntimeException("Cannot update approved or in-progress work order");
        }
        
        existing.setPlannedQuantity(request.getPlannedQuantity() != null ? BigDecimal.valueOf(request.getPlannedQuantity()) : BigDecimal.ZERO);
        existing.setPlannedStartDate(request.getPlannedStartDate());
        existing.setPlannedEndDate(request.getPlannedEndDate());
        existing.setPriority(request.getPriority());
        existing.setProductionLineId(request.getProductionLineId());
        existing.setSupervisorId(request.getSupervisorId());
        existing.setSupervisorName(request.getSupervisorName());
        existing.setNotes(request.getNotes());
        
        return workOrderRepository.save(existing);
    }
    
    @Override
    public void deleteWorkOrder(Long id) {
        log.info("Deleting work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        if (workOrder.getIsApproved() || "IN_PROGRESS".equals(workOrder.getStatus())) {
            throw new RuntimeException("Cannot delete approved or in-progress work order");
        }
        
        workOrderRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public WorkOrder getWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Work order not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public WorkOrder getWorkOrderByNumber(String workOrderNumber) {
        return workOrderRepository.findByWorkOrderNumber(workOrderNumber)
            .orElseThrow(() -> new RuntimeException("Work order not found with number: " + workOrderNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkOrder> getAllWorkOrders(Pageable pageable) {
        return workOrderRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkOrder> searchWorkOrders(String keyword, Pageable pageable) {
        return workOrderRepository.searchWorkOrders(keyword, pageable);
    }
    
    @Override
    public WorkOrder approveWorkOrder(Long id, Long approvedByUserId, String approvalNotes) {
        log.info("Approving work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        if (workOrder.getIsApproved()) {
            throw new RuntimeException("Work order already approved");
        }
        
        workOrder.setIsApproved(true);
        workOrder.setApprovedDate(LocalDate.now());
        workOrder.setApprovedByUserId(approvedByUserId);
        workOrder.setApprovalNotes(approvalNotes);
        workOrder.setStatus("APPROVED");
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public WorkOrder startWorkOrder(Long id, LocalDate actualStartDate) {
        log.info("Starting work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        if (!workOrder.getIsApproved()) {
            throw new RuntimeException("Work order must be approved before starting");
        }
        
        workOrder.setActualStartDate(actualStartDate);
        workOrder.setStatus("IN_PROGRESS");
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public WorkOrder completeWorkOrder(Long id, LocalDate actualEndDate, Double actualQuantity) {
        log.info("Completing work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        if (!"IN_PROGRESS".equals(workOrder.getStatus())) {
            throw new RuntimeException("Work order must be in progress to complete");
        }
        
        workOrder.setActualEndDate(actualEndDate);
        workOrder.setActualQuantity(actualQuantity);
        workOrder.setIsCompleted(true);
        workOrder.setStatus("COMPLETED");
        workOrder.setCompletedDate(LocalDate.now());
        
        // Calculate variance (variance can be computed as actualQuantity - plannedQuantity if needed)
        BigDecimal actualQtyBD = BigDecimal.valueOf(actualQuantity != null ? actualQuantity : 0.0);
        BigDecimal plannedQty = workOrder.getPlannedQuantity();
        BigDecimal varianceBD = actualQtyBD.subtract(plannedQty != null ? plannedQty : BigDecimal.ZERO);
        // Variance is just calculated, not stored - could be added to entity if needed
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public WorkOrder cancelWorkOrder(Long id, String cancellationReason) {
        log.info("Cancelling work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        if (workOrder.getIsCompleted()) {
            throw new RuntimeException("Cannot cancel completed work order");
        }
        
        workOrder.setStatus("CANCELLED");
        workOrder.setCancellationReason(cancellationReason);
        workOrder.setCancelledDate(LocalDate.now());
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public WorkOrder rejectWorkOrder(Long id, String rejectionReason) {
        log.info("Rejecting work order: {}", id);
        WorkOrder workOrder = getWorkOrderById(id);
        
        workOrder.setStatus("CANCELLED");
        workOrder.setRejectionReason(rejectionReason);
        workOrder.setRejectedDate(LocalDate.now());
        
        return workOrderRepository.save(workOrder);
    }
    
    @Override
    public void updateActualQuantity(Long workOrderId, Double quantity) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        BigDecimal currentQty = workOrder.getActualQuantity() != null ? workOrder.getActualQuantity() : BigDecimal.ZERO;
        BigDecimal additionalQty = BigDecimal.valueOf(quantity != null ? quantity : 0.0);
        workOrder.setActualQuantity(currentQty.add(additionalQty));
        workOrderRepository.save(workOrder);
    }
    
    @Override
    public void recordMaterialConsumption(Long workOrderId, Long materialId, Double quantity) {
        // Implementation would record material consumption
        log.info("Recording material consumption for work order: {}", workOrderId);
    }
    
    @Override
    public void recordLabourHours(Long workOrderId, Double hours) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        BigDecimal currentHours = workOrder.getActualLabourHours() != null ? workOrder.getActualLabourHours() : BigDecimal.ZERO;
        BigDecimal additionalHours = BigDecimal.valueOf(hours != null ? hours : 0.0);
        workOrder.setActualLabourHours(currentHours.add(additionalHours));
        workOrderRepository.save(workOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getDraftWorkOrders() {
        return workOrderRepository.findDraftWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getPendingWorkOrders() {
        return workOrderRepository.findPendingWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getApprovedWorkOrders() {
        return workOrderRepository.findApprovedWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getInProgressWorkOrders() {
        return workOrderRepository.findInProgressWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getCompletedWorkOrders() {
        return workOrderRepository.findCompletedWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getCancelledWorkOrders() {
        return workOrderRepository.findCancelledWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersPendingApproval() {
        return workOrderRepository.findWorkOrdersPendingApproval();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getOverdueWorkOrders() {
        return workOrderRepository.findOverdueWorkOrders(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getHighPriorityWorkOrders() {
        return workOrderRepository.findHighPriorityWorkOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getTodaysWorkOrders() {
        return workOrderRepository.findTodaysWorkOrders(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersRequiringAction() {
        return workOrderRepository.findWorkOrdersRequiringAction(LocalDate.now(), LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByProduct(Long productId) {
        return workOrderRepository.findByProductId(productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByProductionLine(Long productionLineId) {
        return workOrderRepository.findProductionLineWorkOrders(productionLineId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersBySupervisor(Long supervisorId) {
        return workOrderRepository.findSupervisorWorkOrders(supervisorId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return workOrderRepository.findByPlannedStartDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getRecentWorkOrders(int limit) {
        return workOrderRepository.findRecentWorkOrders(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateWorkOrder(WorkOrder workOrder) {
        return workOrder.getProductId() != null &&
               workOrder.getPlannedQuantity() != null &&
               workOrder.getPlannedQuantity().compareTo(BigDecimal.ZERO) > 0 &&
               workOrder.getPlannedStartDate() != null &&
               workOrder.getPlannedEndDate() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canApproveWorkOrder(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        return !workOrder.getIsApproved() && !"CANCELLED".equals(workOrder.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canStartWorkOrder(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        return workOrder.getIsApproved() && "APPROVED".equals(workOrder.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCompleteWorkOrder(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        return "IN_PROGRESS".equals(workOrder.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCancelWorkOrder(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        return !workOrder.getIsCompleted();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateCompletionPercentage(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        if (workOrder.getPlannedQuantity() != null && workOrder.getPlannedQuantity().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal actualQty = workOrder.getActualQuantity() != null ? workOrder.getActualQuantity() : BigDecimal.ZERO;
            BigDecimal plannedQty = workOrder.getPlannedQuantity();
            BigDecimal percentage = actualQty.divide(plannedQty, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            return percentage.doubleValue();
        }
        return 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateVariance(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        BigDecimal actualQty = workOrder.getActualQuantity() != null ? workOrder.getActualQuantity() : BigDecimal.ZERO;
        BigDecimal plannedQty = workOrder.getPlannedQuantity() != null ? workOrder.getPlannedQuantity() : BigDecimal.ZERO;
        BigDecimal variance = actualQty.subtract(plannedQty);
        return variance.doubleValue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateWorkOrderMetrics(Long workOrderId) {
        WorkOrder workOrder = getWorkOrderById(workOrderId);
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("completionPercentage", calculateCompletionPercentage(workOrderId));
        metrics.put("variance", calculateVariance(workOrderId));
        metrics.put("isOnTime", workOrder.getActualEndDate() != null && 
                    workOrder.getActualEndDate().isBefore(workOrder.getPlannedEndDate().plusDays(1)));
        
        return metrics;
    }
    
    @Override
    public List<WorkOrder> createBulkWorkOrders(List<WorkOrderRequest> requests) {
        return requests.stream()
            .map(this::createWorkOrder)
            .collect(Collectors.toList());
    }
    
    @Override
    public int approveBulkWorkOrders(List<Long> workOrderIds, Long approvedByUserId) {
        int count = 0;
        for (Long id : workOrderIds) {
            try {
                approveWorkOrder(id, approvedByUserId, "Bulk approval");
                count++;
            } catch (Exception e) {
                log.error("Error approving work order: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkWorkOrders(List<Long> workOrderIds) {
        int count = 0;
        for (Long id : workOrderIds) {
            try {
                deleteWorkOrder(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting work order: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getWorkOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalWorkOrders", workOrderRepository.count());
        stats.put("pendingWorkOrders", workOrderRepository.countPendingWorkOrders());
        stats.put("workOrdersPendingApproval", workOrderRepository.countWorkOrdersPendingApproval());
        stats.put("inProgressWorkOrders", workOrderRepository.countInProgressWorkOrders());
        stats.put("overdueWorkOrders", workOrderRepository.countOverdueWorkOrders(LocalDate.now()));
        stats.put("totalPlannedQuantity", workOrderRepository.getTotalPlannedQuantity());
        stats.put("totalActualQuantity", workOrderRepository.getTotalActualQuantity());
        stats.put("productionEfficiency", getProductionEfficiency());
        stats.put("onTimeCompletionRate", getOnTimeCompletionRate());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWorkOrderTypeDistribution() {
        List<Object[]> results = workOrderRepository.getWorkOrderTypeDistribution();
        return convertToMapList(results, "workOrderType", "workOrderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = workOrderRepository.getStatusDistribution();
        return convertToMapList(results, "status", "workOrderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPriorityDistribution() {
        List<Object[]> results = workOrderRepository.getPriorityDistribution();
        return convertToMapList(results, "priority", "workOrderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyWorkOrderCount(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = workOrderRepository.getMonthlyWorkOrderCount(startDate, endDate);
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("workOrderCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProductionLinePerformance() {
        List<Object[]> results = workOrderRepository.getProductionLinePerformance();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("productionLineId", result[0]);
                map.put("productionLineName", result[1]);
                map.put("workOrderCount", result[2]);
                map.put("totalProduced", result[3]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getProductionEfficiency() {
        Double efficiency = workOrderRepository.getProductionEfficiency();
        return efficiency != null ? efficiency : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getOnTimeCompletionRate() {
        Double rate = workOrderRepository.getOnTimeCompletionRate();
        return rate != null ? rate : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getWorkOrderStatistics());
        dashboard.put("typeDistribution", getWorkOrderTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("priorityDistribution", getPriorityDistribution());
        dashboard.put("productionLinePerformance", getProductionLinePerformance());
        
        return dashboard;
    }
    
    private String generateWorkOrderNumber() {
        return "WO-" + System.currentTimeMillis();
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
