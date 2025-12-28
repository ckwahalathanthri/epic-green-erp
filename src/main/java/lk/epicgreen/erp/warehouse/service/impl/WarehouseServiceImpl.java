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
}
