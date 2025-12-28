package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseLocationRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseLocationResponse;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.mapper.WarehouseLocationMapper;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
import lk.epicgreen.erp.warehouse.service.WarehouseLocationService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of WarehouseLocationService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WarehouseLocationServiceImpl implements WarehouseLocationService {

    private final WarehouseLocationRepository warehouseLocationRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationMapper warehouseLocationMapper;

    @Override
    @Transactional
    public WarehouseLocationResponse createWarehouseLocation(WarehouseLocationRequest request) {
        log.info("Creating new warehouse location: {} for warehouse: {}", 
            request.getLocationCode(), request.getWarehouseId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Validate unique constraint (warehouse_id, location_code)
        validateUniqueLocationCode(request.getWarehouseId(), request.getLocationCode(), null);

        // Create location entity
        WarehouseLocation location = warehouseLocationMapper.toEntity(request);
        location.setWarehouse(warehouse);

        WarehouseLocation savedLocation = warehouseLocationRepository.save(location);
        log.info("Warehouse location created successfully: {}", savedLocation.getId());

        return warehouseLocationMapper.toResponse(savedLocation);
    }

    @Override
    @Transactional
    public WarehouseLocationResponse updateWarehouseLocation(Long id, WarehouseLocationRequest request) {
        log.info("Updating warehouse location: {}", id);

        WarehouseLocation location = findWarehouseLocationById(id);

        // Validate unique constraint (warehouse_id, location_code)
        validateUniqueLocationCode(request.getWarehouseId(), request.getLocationCode(), id);

        // Update fields
        warehouseLocationMapper.updateEntityFromRequest(request, location);

        // Update warehouse if changed
        if (!location.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            location.setWarehouse(warehouse);
        }

        WarehouseLocation updatedLocation = warehouseLocationRepository.save(location);
        log.info("Warehouse location updated successfully: {}", updatedLocation.getId());

        return warehouseLocationMapper.toResponse(updatedLocation);
    }

    @Override
    @Transactional
    public void activateWarehouseLocation(Long id) {
        log.info("Activating warehouse location: {}", id);

        WarehouseLocation location = findWarehouseLocationById(id);
        location.setIsActive(true);
        warehouseLocationRepository.save(location);

        log.info("Warehouse location activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateWarehouseLocation(Long id) {
        log.info("Deactivating warehouse location: {}", id);

        WarehouseLocation location = findWarehouseLocationById(id);
        location.setIsActive(false);
        warehouseLocationRepository.save(location);

        log.info("Warehouse location deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteWarehouseLocation(Long id) {
        log.info("Deleting warehouse location: {}", id);

        WarehouseLocation location = findWarehouseLocationById(id);
        warehouseLocationRepository.delete(location);

        log.info("Warehouse location deleted successfully: {}", id);
    }

    @Override
    public WarehouseLocationResponse getWarehouseLocationById(Long id) {
        WarehouseLocation location = findWarehouseLocationById(id);
        return warehouseLocationMapper.toResponse(location);
    }

    @Override
    public List<WarehouseLocationResponse> getLocationsByWarehouse(Long warehouseId) {
        List<WarehouseLocation> locations = warehouseLocationRepository.findByWarehouseId(warehouseId);
        return locations.stream()
            .map(warehouseLocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseLocationResponse> getActiveLocationsByWarehouse(Long warehouseId) {
        List<WarehouseLocation> locations = warehouseLocationRepository
            .findByWarehouseIdAndIsActiveTrue(warehouseId);
        return locations.stream()
            .map(warehouseLocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public WarehouseLocationResponse getLocationByWarehouseAndCode(Long warehouseId, String locationCode) {
        WarehouseLocation location = warehouseLocationRepository
            .findByWarehouseIdAndLocationCode(warehouseId, locationCode)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Warehouse location not found for warehouse: " + warehouseId + " and code: " + locationCode));
        return warehouseLocationMapper.toResponse(location);
    }

    @Override
    public List<WarehouseLocationResponse> searchLocations(String keyword) {
        List<WarehouseLocation> locations = warehouseLocationRepository.searchLocations(keyword);
        return locations.stream()
            .map(warehouseLocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private WarehouseLocation findWarehouseLocationById(Long id) {
        return warehouseLocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse location not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private void validateUniqueLocationCode(Long warehouseId, String locationCode, Long excludeId) {
        if (excludeId == null) {
            if (warehouseLocationRepository.existsByWarehouseIdAndLocationCode(warehouseId, locationCode)) {
                throw new DuplicateResourceException(
                    "Location code already exists for warehouse: " + warehouseId + ", code: " + locationCode);
            }
        } else {
            if (warehouseLocationRepository.existsByWarehouseIdAndLocationCodeAndIdNot(
                    warehouseId, locationCode, excludeId)) {
                throw new DuplicateResourceException(
                    "Location code already exists for warehouse: " + warehouseId + ", code: " + locationCode);
            }
        }
    }
}
