package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseResponse;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.mapper.WarehouseMapper;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.service.WarehouseService;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of WarehouseService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        log.info("Creating new warehouse: {}", request.getWarehouseCode());

        // Validate unique constraint
        validateUniqueWarehouseCode(request.getWarehouseCode(), null);

        // Create warehouse entity
        Warehouse warehouse = warehouseMapper.toEntity(request);

        // Set manager if provided
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found: " + request.getManagerId()));
            warehouse.setManager(manager);
        }

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        log.info("Warehouse created successfully: {}", savedWarehouse.getWarehouseCode());

        return warehouseMapper.toResponse(savedWarehouse);
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        log.info("Updating warehouse: {}", id);

        Warehouse warehouse = findWarehouseById(id);

        // Validate unique constraint
        validateUniqueWarehouseCode(request.getWarehouseCode(), id);

        // Update fields
        warehouseMapper.updateEntityFromRequest(request, warehouse);

        // Update manager if provided
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found: " + request.getManagerId()));
            warehouse.setManager(manager);
        } else {
            warehouse.setManager(null);
        }

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        log.info("Warehouse updated successfully: {}", updatedWarehouse.getWarehouseCode());

        return warehouseMapper.toResponse(updatedWarehouse);
    }

    @Override
    @Transactional
    public void activateWarehouse(Long id) {
        log.info("Activating warehouse: {}", id);

        Warehouse warehouse = findWarehouseById(id);
        warehouse.setIsActive(true);
        warehouseRepository.save(warehouse);

        log.info("Warehouse activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateWarehouse(Long id) {
        log.info("Deactivating warehouse: {}", id);

        Warehouse warehouse = findWarehouseById(id);
        warehouse.setIsActive(false);
        warehouseRepository.save(warehouse);

        log.info("Warehouse deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void assignManager(Long warehouseId, Long managerId) {
        log.info("Assigning manager {} to warehouse {}", managerId, warehouseId);

        Warehouse warehouse = findWarehouseById(warehouseId);
        User manager = userRepository.findById(managerId)
            .orElseThrow(() -> new ResourceNotFoundException("Manager not found: " + managerId));

        warehouse.setManager(manager);
        warehouseRepository.save(warehouse);

        log.info("Manager assigned successfully");
    }

    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = findWarehouseById(id);
        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    public WarehouseResponse getWarehouseByCode(String warehouseCode) {
        Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + warehouseCode));
        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    public PageResponse<WarehouseResponse> getAllWarehouses(Pageable pageable) {
        Page<Warehouse> warehousePage = warehouseRepository.findAll(pageable);
        return createPageResponse(warehousePage);
    }

    @Override
    public List<WarehouseResponse> getAllActiveWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByIsActiveTrue();
        return warehouses.stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<WarehouseResponse> getWarehousesByType(String warehouseType, Pageable pageable) {
        Page<Warehouse> warehousePage = warehouseRepository.findByWarehouseType(warehouseType, pageable);
        return createPageResponse(warehousePage);
    }

    @Override
    public List<WarehouseResponse> getRawMaterialWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseTypeAndIsActiveTrue("RAW_MATERIAL");
        return warehouses.stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponse> getFinishedGoodsWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseTypeAndIsActiveTrue("FINISHED_GOODS");
        return warehouses.stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponse> getMixedWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseTypeAndIsActiveTrue("MIXED");
        return warehouses.stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponse> getWarehousesByManager(Long managerId) {
        List<Warehouse> warehouses = warehouseRepository.findByManagerId(managerId);
        return warehouses.stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<WarehouseResponse> searchWarehouses(String keyword, Pageable pageable) {
        Page<Warehouse> warehousePage = warehouseRepository.searchWarehouses(keyword, pageable);
        return createPageResponse(warehousePage);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private void validateUniqueWarehouseCode(String warehouseCode, Long excludeId) {
        if (excludeId == null) {
            if (warehouseRepository.existsByWarehouseCode(warehouseCode)) {
                throw new DuplicateResourceException("Warehouse code already exists: " + warehouseCode);
            }
        } else {
            if (warehouseRepository.existsByWarehouseCodeAndIdNot(warehouseCode, excludeId)) {
                throw new DuplicateResourceException("Warehouse code already exists: " + warehouseCode);
            }
        }
    }

    private PageResponse<WarehouseResponse> createPageResponse(Page<Warehouse> warehousePage) {
        List<WarehouseResponse> content = warehousePage.getContent().stream()
            .map(warehouseMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<WarehouseResponse>builder()
            .content(content)
            .pageNumber(warehousePage.getNumber())
            .pageSize(warehousePage.getSize())
            .totalElements(warehousePage.getTotalElements())
            .totalPages(warehousePage.getTotalPages())
            .last(warehousePage.isLast())
            .first(warehousePage.isFirst())
            .empty(warehousePage.isEmpty())
            .build();
    }

    @Override
    public void deleteWarehouse(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteWarehouse'");
    }

    @Override
    public WarehouseResponse getWarehouseByName(String warehouseName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehouseByName'");
    }

    @Override
    public WarehouseResponse setAsDefault(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAsDefault'");
    }

    @Override
    public List<WarehouseResponse> getActiveWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActiveWarehouses'");
    }

    @Override
    public List<WarehouseResponse> getInactiveWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInactiveWarehouses'");
    }

    @Override
    public WarehouseResponse getDefaultWarehouse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDefaultWarehouse'");
    }

    @Override
    public List<WarehouseResponse> getMainWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMainWarehouses'");
    }

    @Override
    public List<WarehouseResponse> getBranchWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBranchWarehouses'");
    }

    @Override
    public List<WarehouseResponse> getTransitWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransitWarehouses'");
    }

    @Override
    public List<WarehouseResponse> getRetailWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRetailWarehouses'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesByType(String warehouseType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByType'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesByCity(String city) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByCity'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesByState(String stateProvince) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByState'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesByRegion(String region) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByRegion'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesByCapacityRange(Double minCapacity, Double maxCapacity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByCapacityRange'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesWithLowUtilization(Double threshold) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesWithLowUtilization'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesWithHighUtilization(Double threshold) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesWithHighUtilization'");
    }

    @Override
    public List<WarehouseResponse> getWarehousesNearCapacity(Double threshold) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesNearCapacity'");
    }

    @Override
    public List<WarehouseResponse> getRecentWarehouses(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentWarehouses'");
    }

    @Override
    public void updateCurrentStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCurrentStock'");
    }

    @Override
    public void increaseStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseStock'");
    }

    @Override
    public void decreaseStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decreaseStock'");
    }

    @Override
    public Double getCapacityUtilization(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCapacityUtilization'");
    }

    @Override
    public Double getAvailableCapacity(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableCapacity'");
    }

    @Override
    public boolean isWarehouseCodeAvailable(String warehouseCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isWarehouseCodeAvailable'");
    }

    @Override
    public boolean isWarehouseNameAvailable(String warehouseName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isWarehouseNameAvailable'");
    }

    @Override
    public boolean canDeleteWarehouse(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canDeleteWarehouse'");
    }

    @Override
    public List<Warehouse> createBulkWarehouses(List<WarehouseRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBulkWarehouses'");
    }

    @Override
    public int activateBulkWarehouses(List<Long> warehouseIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activateBulkWarehouses'");
    }

    @Override
    public int deactivateBulkWarehouses(List<Long> warehouseIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateBulkWarehouses'");
    }

    @Override
    public int deleteBulkWarehouses(List<Long> warehouseIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBulkWarehouses'");
    }

    @Override
    public Map<String, Object> getWarehouseStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehouseStatistics'");
    }

    @Override
    public List<WarehouseResponse> getAllWarehouses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllWarehouses'");
    }

    @Override
    public List<Map<String, Object>> getTypeDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTypeDistribution'");
    }

    @Override
    public List<Map<String, Object>> getWarehousesByRegionStats() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehousesByRegionStats'");
    }

    @Override
    public List<Map<String, Object>> getCapacityUtilizationStats() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCapacityUtilizationStats'");
    }

    @Override
    public Double getTotalCapacity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalCapacity'");
    }

    @Override
    public Double getTotalCurrentStock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalCurrentStock'");
    }

    @Override
    public Double getAverageUtilization() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAverageUtilization'");
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
    }
}
