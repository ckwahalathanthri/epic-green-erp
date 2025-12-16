package lk.epicgreen.erp.notification.repository;

import lk.epicgreen.erp.notification.entity.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * NotificationTemplate Repository
 * Repository for notification template data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find template by template code
     */
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);
    
    /**
     * Check if template exists by template code
     */
    boolean existsByTemplateCode(String templateCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find templates by notification type
     */
    List<NotificationTemplate> findByNotificationType(String notificationType);
    
    /**
     * Find templates by notification type with pagination
     */
    Page<NotificationTemplate> findByNotificationType(String notificationType, Pageable pageable);
    
    /**
     * Find templates by category
     */
    List<NotificationTemplate> findByCategory(String category);
    
    /**
     * Find templates by category with pagination
     */
    Page<NotificationTemplate> findByCategory(String category, Pageable pageable);
    
    /**
     * Find templates by event trigger
     */
    List<NotificationTemplate> findByEventTrigger(String eventTrigger);
    
    /**
     * Find templates by event trigger with pagination
     */
    Page<NotificationTemplate> findByEventTrigger(String eventTrigger, Pageable pageable);
    
    /**
     * Find templates by status
     */
    List<NotificationTemplate> findByStatus(String status);
    
    /**
     * Find templates by status with pagination
     */
    Page<NotificationTemplate> findByStatus(String status, Pageable pageable);
    
    /**
     * Find templates by locale
     */
    List<NotificationTemplate> findByLocale(String locale);
    
    /**
     * Find templates by locale with pagination
     */
    Page<NotificationTemplate> findByLocale(String locale, Pageable pageable);
    
    /**
     * Find active templates
     */
    List<NotificationTemplate> findByIsActive(Boolean isActive);
    
    /**
     * Find active templates with pagination
     */
    Page<NotificationTemplate> findByIsActive(Boolean isActive, Pageable pageable);
    
    /**
     * Find system templates
     */
    List<NotificationTemplate> findByIsSystem(Boolean isSystem);
    
    /**
     * Find public templates
     */
    List<NotificationTemplate> findByIsPublic(Boolean isPublic);
    
    /**
     * Find templates that are templates
     */
    List<NotificationTemplate> findByIsTemplate(Boolean isTemplate);
    
    /**
     * Find templates with auto-send enabled
     */
    List<NotificationTemplate> findByAutoSend(Boolean autoSend);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find templates by notification type and category
     */
    List<NotificationTemplate> findByNotificationTypeAndCategory(String notificationType, String category);
    
    /**
     * Find templates by notification type and status
     */
    List<NotificationTemplate> findByNotificationTypeAndStatus(String notificationType, String status);
    
    /**
     * Find active templates by notification type
     */
    List<NotificationTemplate> findByNotificationTypeAndIsActive(String notificationType, Boolean isActive);
    
    /**
     * Find templates by event trigger and auto-send
     */
    List<NotificationTemplate> findByEventTriggerAndAutoSend(String eventTrigger, Boolean autoSend);
    
    /**
     * Find active templates by event trigger and notification type
     */
    List<NotificationTemplate> findByEventTriggerAndNotificationTypeAndIsActive(
        String eventTrigger, String notificationType, Boolean isActive);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Find templates by template name pattern
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE LOWER(t.templateName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<NotificationTemplate> findByTemplateNameContaining(@Param("name") String name);
    
    /**
     * Search templates
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE " +
           "LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.templateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<NotificationTemplate> searchTemplates(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find most used templates
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE t.usageCount > 0 ORDER BY t.usageCount DESC")
    List<NotificationTemplate> findMostUsedTemplates(Pageable pageable);
    
    /**
     * Find templates by tags
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE t.tags LIKE CONCAT('%', :tag, '%')")
    List<NotificationTemplate> findByTag(@Param("tag") String tag);
    
    /**
     * Count templates by notification type
     */
    @Query("SELECT COUNT(t) FROM NotificationTemplate t WHERE t.notificationType = :notificationType")
    Long countByNotificationType(@Param("notificationType") String notificationType);
    
    /**
     * Count templates by category
     */
    @Query("SELECT COUNT(t) FROM NotificationTemplate t WHERE t.category = :category")
    Long countByCategory(@Param("category") String category);
    
    /**
     * Count active templates
     */
    @Query("SELECT COUNT(t) FROM NotificationTemplate t WHERE t.isActive = true")
    Long countActiveTemplates();
    
    /**
     * Get template type distribution
     */
    @Query("SELECT t.notificationType, COUNT(t) as templateCount FROM NotificationTemplate t " +
           "GROUP BY t.notificationType ORDER BY templateCount DESC")
    List<Object[]> getTemplateTypeDistribution();
    
    /**
     * Get category distribution
     */
    @Query("SELECT t.category, COUNT(t) as templateCount FROM NotificationTemplate t " +
           "GROUP BY t.category ORDER BY templateCount DESC")
    List<Object[]> getCategoryDistribution();
    
    /**
     * Find templates requiring approval
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE t.requiresApproval = true AND t.status = 'DRAFT'")
    List<NotificationTemplate> findTemplatesRequiringApproval();
    
    /**
     * Find templates by created user
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE t.createdByUserId = :userId ORDER BY t.createdAt DESC")
    List<NotificationTemplate> findByCreatedByUserId(@Param("userId") Long userId);
    
    /**
     * Find templates by created user with pagination
     */
    @Query("SELECT t FROM NotificationTemplate t WHERE t.createdByUserId = :userId ORDER BY t.createdAt DESC")
    Page<NotificationTemplate> findByCreatedByUserId(@Param("userId") Long userId, Pageable pageable);
}
