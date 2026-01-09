package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.dto.request.BillOfMaterialsRequest;
import lk.epicgreen.erp.production.dto.request.BomItemRequest;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lk.epicgreen.erp.production.entity.BillOfMaterials;
import lk.epicgreen.erp.production.entity.BomItem;
import lk.epicgreen.erp.production.mapper.BillOfMaterialsMapper;
import lk.epicgreen.erp.production.mapper.BomItemMapper;
import lk.epicgreen.erp.production.repository.BillOfMaterialsRepository;
import lk.epicgreen.erp.production.repository.WorkOrderRepository;
import lk.epicgreen.erp.production.service.BillOfMaterialsService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of BillOfMaterialsService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BillOfMaterialsServiceImpl implements BillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final WorkOrderRepository workOrderRepository;
    private final BillOfMaterialsMapper billOfMaterialsMapper;
    private final BomItemMapper bomItemMapper;

    @Override
    @Transactional
    public BillOfMaterialsResponse createBillOfMaterials(BillOfMaterialsRequest request) {
        log.info("Creating new Bill of Materials: {}", request.getBomCode());

        // Validate unique constraint
        validateUniqueBomCode(request.getBomCode(), null);

        // Verify finished product exists
        Product finishedProduct = findProductById(request.getFinishedProductId());

        // Verify output UOM exists
        UnitOfMeasure outputUom = findUnitOfMeasureById(request.getOutputUomId());

        // Create BOM entity
        BillOfMaterials bom = billOfMaterialsMapper.toEntity(request);
        bom.setFinishedProduct(finishedProduct);
        bom.setOutputUom(outputUom);

        // Create BOM items
        List<BomItem> items = new ArrayList<>();
        for (BomItemRequest itemRequest : request.getItems()) {
            BomItem item = createBomItem(itemRequest);
            item.setBom(bom);
            items.add(item);
        }
        bom.setItems(items);

        BillOfMaterials savedBom = billOfMaterialsRepository.save(bom);
        log.info("Bill of Materials created successfully: {}", savedBom.getBomCode());

        return billOfMaterialsMapper.toResponse(savedBom);
    }

    @Override
    @Transactional
    public BillOfMaterialsResponse updateBillOfMaterials(Long id, BillOfMaterialsRequest request) {
        log.info("Updating Bill of Materials: {}", id);

        BillOfMaterials bom = findBillOfMaterialsById(id);

        // Validate unique constraint
        validateUniqueBomCode(request.getBomCode(), id);

        // Update fields
        billOfMaterialsMapper.updateEntityFromRequest(request, bom);

        // Update finished product if changed
        if (!bom.getFinishedProduct().getId().equals(request.getFinishedProductId())) {
            Product finishedProduct = findProductById(request.getFinishedProductId());
            bom.setFinishedProduct(finishedProduct);
        }

        // Update output UOM if changed
        if (!bom.getOutputUom().getId().equals(request.getOutputUomId())) {
            UnitOfMeasure outputUom = findUnitOfMeasureById(request.getOutputUomId());
            bom.setOutputUom(outputUom);
        }

        // Clear existing items and create new ones
        bom.getItems().clear();
        for (BomItemRequest itemRequest : request.getItems()) {
            BomItem item = createBomItem(itemRequest);
            item.setBom(bom);
            bom.getItems().add(item);
        }

        BillOfMaterials updatedBom = billOfMaterialsRepository.save(bom);
        log.info("Bill of Materials updated successfully: {}", updatedBom.getBomCode());

        return billOfMaterialsMapper.toResponse(updatedBom);
    }

    @Override
    @Transactional
    public void activateBillOfMaterials(Long id) {
        log.info("Activating Bill of Materials: {}", id);

        BillOfMaterials bom = findBillOfMaterialsById(id);
        bom.setIsActive(true);
        billOfMaterialsRepository.save(bom);

        log.info("Bill of Materials activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateBillOfMaterials(Long id) {
        log.info("Deactivating Bill of Materials: {}", id);

        BillOfMaterials bom = findBillOfMaterialsById(id);
        bom.setIsActive(false);
        billOfMaterialsRepository.save(bom);

        log.info("Bill of Materials deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteBillOfMaterials(Long id) {
        log.info("Deleting Bill of Materials: {}", id);

        BillOfMaterials bom = findBillOfMaterialsById(id);

        // Check if BOM is used in any work orders
        if (!canDelete(id)) {
            throw new InvalidOperationException(
                "Cannot delete Bill of Materials. It is being used in one or more work orders.");
        }

        billOfMaterialsRepository.delete(bom);

        log.info("Bill of Materials deleted successfully: {}", id);
    }

    @Override
    public BillOfMaterialsResponse getBillOfMaterialsById(Long id) {
        BillOfMaterials bom = findBillOfMaterialsById(id);
        return billOfMaterialsMapper.toResponse(bom);
    }

    @Override
    public BillOfMaterialsResponse getBillOfMaterialsByCode(String bomCode) {
        BillOfMaterials bom = billOfMaterialsRepository.findByBomCode(bomCode)
            .orElseThrow(() -> new ResourceNotFoundException("Bill of Materials not found: " + bomCode));
        return billOfMaterialsMapper.toResponse(bom);
    }

    @Override
    public PageResponse<BillOfMaterialsResponse> getAllBillOfMaterials(Pageable pageable) {
        Page<BillOfMaterials> bomPage = billOfMaterialsRepository.findAll(pageable);
        return createPageResponse(bomPage);
    }

    @Override
    public List<BillOfMaterialsResponse> getAllActiveBillOfMaterials() {
        List<BillOfMaterials> boms = billOfMaterialsRepository.findByIsActiveTrue();
        return boms.stream()
            .map(billOfMaterialsMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> getBillOfMaterialsByFinishedProduct(Long finishedProductId) {
        List<BillOfMaterials> boms = billOfMaterialsRepository
            .findByFinishedProductId(finishedProductId);
        return boms.stream()
            .map(billOfMaterialsMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BillOfMaterialsResponse getActiveBomByFinishedProduct(Long finishedProductId) {
        BillOfMaterials bom = billOfMaterialsRepository
            .findByFinishedProductIdAndIsActiveTrue(finishedProductId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Active Bill of Materials not found for product: " + finishedProductId));
        return billOfMaterialsMapper.toResponse(bom);
    }

    @Override
    public List<BillOfMaterialsResponse> getValidBillOfMaterialsForDate(LocalDate date) {
        List<BillOfMaterials> boms = billOfMaterialsRepository
            .findValidBomsForDate(date);
        return boms.stream()
            .map(billOfMaterialsMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BillOfMaterialsResponse getBomByFinishedProductAndDate(Long finishedProductId, LocalDate date) {
        BillOfMaterials bom = billOfMaterialsRepository
            .findValidBomByProductAndDate(finishedProductId, date)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Valid Bill of Materials not found for product: " + finishedProductId + " on date: " + date));
        return billOfMaterialsMapper.toResponse(bom);
    }

    @Override
    public PageResponse<BillOfMaterialsResponse> searchBillOfMaterials(String keyword, Pageable pageable) {
        Page<BillOfMaterials> bomPage = billOfMaterialsRepository.searchBoms(keyword, pageable);
        return createPageResponse(bomPage);
    }

    @Override
    public BigDecimal calculateTotalMaterialCost(Long bomId) {
        BillOfMaterials bom = findBillOfMaterialsById(bomId);
        
        if (bom.getItems() == null || bom.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return bom.getItems().stream()
            .map(item -> {
                if (item.getStandardCost() != null && item.getQuantityRequired() != null) {
                    return item.getStandardCost().multiply(item.getQuantityRequired());
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalCost(Long bomId) {
        BillOfMaterials bom = findBillOfMaterialsById(bomId);
        
        BigDecimal materialCost = calculateTotalMaterialCost(bomId);
        BigDecimal laborCost = bom.getLaborCost() != null ? bom.getLaborCost() : BigDecimal.ZERO;
        BigDecimal overheadCost = bom.getOverheadCost() != null ? bom.getOverheadCost() : BigDecimal.ZERO;

        return materialCost.add(laborCost).add(overheadCost);
    }

    @Override
    public boolean canDelete(Long bomId) {
        Integer count = workOrderRepository.countByBomId(bomId);
        return count == 0;
    }

    @Override
    public List<BillOfMaterialsResponse> getExpiringSoon(Integer daysBeforeExpiry) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysBeforeExpiry);
        List<BillOfMaterials> boms = billOfMaterialsRepository
            .findExpiringSoon(LocalDate.now(), expiryDate);
        return boms.stream()
            .map(billOfMaterialsMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private BillOfMaterials findBillOfMaterialsById(Long id) {
        return billOfMaterialsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill of Materials not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private void validateUniqueBomCode(String bomCode, Long excludeId) {
        if (excludeId == null) {
            if (billOfMaterialsRepository.existsByBomCode(bomCode)) {
                throw new DuplicateResourceException("BOM code already exists: " + bomCode);
            }
        } else {
            if (billOfMaterialsRepository.existsByBomCodeAndIdNot(bomCode, excludeId)) {
                throw new DuplicateResourceException("BOM code already exists: " + bomCode);
            }
        }
    }

    private BomItem createBomItem(BomItemRequest request) {
        Product rawMaterial = findProductById(request.getRawMaterialId());
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        BomItem item = bomItemMapper.toEntity(request);
        item.setRawMaterial(rawMaterial);
        item.setUom(uom);

        return item;
    }

    private PageResponse<BillOfMaterialsResponse> createPageResponse(Page<BillOfMaterials> bomPage) {
        List<BillOfMaterialsResponse> content = bomPage.getContent().stream()
            .map(billOfMaterialsMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<BillOfMaterialsResponse>builder()
            .content(content)
            .pageNumber(bomPage.getNumber())
            .pageSize(bomPage.getSize())
            .totalElements(bomPage.getTotalElements())
            .totalPages(bomPage.getTotalPages())
            .last(bomPage.isLast())
            .first(bomPage.isFirst())
            .empty(bomPage.isEmpty())
            .build();
    }

    @Override
    public BillOfMaterialsResponse createBom(BillOfMaterialsRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBom'");
    }

    @Override
    public BillOfMaterialsResponse updateBom(Long id, BillOfMaterialsRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBom'");
    }

    @Override
    public void deleteBom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBom'");
    }

    @Override
    public BillOfMaterialsResponse getBomById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomById'");
    }

    @Override
    public BillOfMaterialsResponse getBomByCode(String bomCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomByCode'");
    }

    @Override
    public Page<BillOfMaterialsResponse> getAllBoms(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getAllBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBoms'");
    }

    @Override
    public Page<BillOfMaterialsResponse> searchBoms(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchBoms'");
    }

    @Override
    public BillOfMaterialsResponse approveBom(Long id, Long approvedByUserId, String approvalNotes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveBom'");
    }

    @Override
    public BillOfMaterialsResponse activateBom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activateBom'");
    }

    @Override
    public BillOfMaterialsResponse deactivateBom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateBom'");
    }

    @Override
    public BillOfMaterialsResponse markAsObsolete(Long id, String obsoleteReason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAsObsolete'");
    }

    @Override
    public BillOfMaterialsResponse setAsDefault(Long productId, Long bomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAsDefault'");
    }

    @Override
    public BillOfMaterialsResponse createNewVersion(Long bomId, String newVersion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNewVersion'");
    }

    @Override
    public List<BillOfMaterialsResponse> getBomVersions(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomVersions'");
    }

    @Override
    public BillOfMaterialsResponse getLatestBomVersion(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLatestBomVersion'");
    }

    @Override
    public List<BillOfMaterialsResponse> getActiveBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActiveBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getDraftBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDraftBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getApprovedBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApprovedBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getObsoleteBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObsoleteBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getBomsPendingApproval() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomsPendingApproval'");
    }

    @Override
    public List<BillOfMaterialsResponse> getActiveDefaultBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActiveDefaultBoms'");
    }

    @Override
    public BillOfMaterialsResponse getProductActiveBom(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductActiveBom'");
    }

    @Override
    public List<BillOfMaterialsResponse> getProductBoms(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getEffectiveBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEffectiveBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getExpiredBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpiredBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getExpiringSoonBoms(int days) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpiringSoonBoms'");
    }

    @Override
    public List<BillOfMaterialsResponse> getBomsRequiringAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomsRequiringAction'");
    }

    @Override
    public List<BillOfMaterialsResponse> getBomsByType(String bomType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomsByType'");
    }

    @Override
    public List<BillOfMaterialsResponse> getRecentBoms(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentBoms'");
    }

    @Override
    public boolean canApproveBom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canApproveBom'");
    }

    @Override
    public boolean canActivateBom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canActivateBom'");
    }

    @Override
    public boolean canMarkAsObsolete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMarkAsObsolete'");
    }

    @Override
    public boolean isBomCodeAvailable(String bomCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBomCodeAvailable'");
    }

    @Override
    public List<BillOfMaterialsResponse> createBulkBoms(List<BillOfMaterialsRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBulkBoms'");
    }

    @Override
    public int approveBulkBoms(List<Long> bomIds, Long approvedByUserId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveBulkBoms'");
    }

    @Override
    public int deleteBulkBoms(List<Long> bomIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBulkBoms'");
    }

    @Override
    public Map<String, Object> getBomStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomStatistics'");
    }

    @Override
    public List<Map<String, Object>> getBomTypeDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBomTypeDistribution'");
    }

    @Override
    public List<Map<String, Object>> getStatusDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusDistribution'");
    }

    @Override
    public List<Map<String, Object>> getMonthlyBomCreationCount(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMonthlyBomCreationCount'");
    }

    @Override
    public List<Map<String, Object>> getProductsWithBoms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductsWithBoms'");
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
    }
}
