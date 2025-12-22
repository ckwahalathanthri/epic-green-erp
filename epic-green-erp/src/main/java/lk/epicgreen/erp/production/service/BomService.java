package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.BomRequest;
import lk.epicgreen.erp.production.entity.BillOfMaterials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * BOM Service Interface
 * Service for bill of materials operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface BomService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================

    BillOfMaterials createBom(BomRequest request);
    BillOfMaterials updateBom(Long id, BomRequest request);
    void deleteBom(Long id);
    BillOfMaterials getBomById(Long id);
    BillOfMaterials getBomByCode(String bomCode);
    List<BillOfMaterials> getAllBoms();
    Page<BillOfMaterials> getAllBoms(Pageable pageable);
    Page<BillOfMaterials> searchBoms(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================

    BillOfMaterials approveBom(Long id, Long approvedByUserId, String approvalNotes);
    BillOfMaterials activateBom(Long id);
    BillOfMaterials deactivateBom(Long id);
    BillOfMaterials markAsObsolete(Long id, String obsoleteReason);
    BillOfMaterials setAsDefault(Long productId, Long bomId);
    
    // ===================================================================
    // VERSION OPERATIONS
    // ===================================================================

    BillOfMaterials createNewVersion(Long bomId, String newVersion);
    List<BillOfMaterials> getBomVersions(Long productId);
    BillOfMaterials getLatestBomVersion(Long productId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<BillOfMaterials> getActiveBoms();
    List<BillOfMaterials> getDraftBoms();
    List<BillOfMaterials> getApprovedBoms();
    List<BillOfMaterials> getObsoleteBoms();
    List<BillOfMaterials> getBomsPendingApproval();
    List<BillOfMaterials> getActiveDefaultBoms();
    BillOfMaterials getProductActiveBom(Long productId);
    List<BillOfMaterials> getProductBoms(Long productId);
    List<BillOfMaterials> getEffectiveBoms();
    List<BillOfMaterials> getExpiredBoms();
    List<BillOfMaterials> getExpiringSoonBoms(int days);
    List<BillOfMaterials> getBomsRequiringAction();
    List<BillOfMaterials> getBomsByType(String bomType);
    List<BillOfMaterials> getRecentBoms(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateBom(BillOfMaterials bom);
    boolean canApproveBom(Long bomId);
    boolean canActivateBom(Long bomId);
    boolean canMarkAsObsolete(Long bomId);
    boolean isBomCodeAvailable(String bomCode);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<BillOfMaterials> createBulkBoms(List<BomRequest> requests);
    int approveBulkBoms(List<Long> bomIds, Long approvedByUserId);
    int deleteBulkBoms(List<Long> bomIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getBomStatistics();
    List<Map<String, Object>> getBomTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getMonthlyBomCreationCount(LocalDateTime startDate, LocalDateTime endDate);
    List<Map<String, Object>> getProductsWithBoms();
    Map<String, Object> getDashboardStatistics();
}
