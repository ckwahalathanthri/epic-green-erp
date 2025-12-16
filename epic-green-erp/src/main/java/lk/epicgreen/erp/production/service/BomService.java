package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.BomRequest;
import lk.epicgreen.erp.production.entity.Bom;
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
    
    Bom createBom(BomRequest request);
    Bom updateBom(Long id, BomRequest request);
    void deleteBom(Long id);
    Bom getBomById(Long id);
    Bom getBomByCode(String bomCode);
    List<Bom> getAllBoms();
    Page<Bom> getAllBoms(Pageable pageable);
    Page<Bom> searchBoms(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Bom approveBom(Long id, Long approvedByUserId, String approvalNotes);
    Bom activateBom(Long id);
    Bom deactivateBom(Long id);
    Bom markAsObsolete(Long id, String obsoleteReason);
    Bom setAsDefault(Long productId, Long bomId);
    
    // ===================================================================
    // VERSION OPERATIONS
    // ===================================================================
    
    Bom createNewVersion(Long bomId, String newVersion);
    List<Bom> getBomVersions(Long productId);
    Bom getLatestBomVersion(Long productId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Bom> getActiveBoms();
    List<Bom> getDraftBoms();
    List<Bom> getApprovedBoms();
    List<Bom> getObsoleteBoms();
    List<Bom> getBomsPendingApproval();
    List<Bom> getActiveDefaultBoms();
    Bom getProductActiveBom(Long productId);
    List<Bom> getProductBoms(Long productId);
    List<Bom> getEffectiveBoms();
    List<Bom> getExpiredBoms();
    List<Bom> getExpiringSoonBoms(int days);
    List<Bom> getBomsRequiringAction();
    List<Bom> getBomsByType(String bomType);
    List<Bom> getRecentBoms(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateBom(Bom bom);
    boolean canApproveBom(Long bomId);
    boolean canActivateBom(Long bomId);
    boolean canMarkAsObsolete(Long bomId);
    boolean isBomCodeAvailable(String bomCode);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Bom> createBulkBoms(List<BomRequest> requests);
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
