package lk.epicgreen.erp.notification.repository;

import lk.epicgreen.erp.notification.entity.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for NotificationTemplate entity
 * Based on ACTUAL database schema: notification_templates table
 * 
 * Fields: template_code (UNIQUE), template_name,
 *         notification_type (ENUM: EMAIL, SMS, PUSH, IN_APP),
 *         subject, body_template, variables (JSON),
 *         is_active, created_at, created_by (BIGINT),
 *         updated_at, updated_by (BIGINT)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long>, JpaSpecificationExecutor<NotificationTemplate> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find notification template by template code
     */
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);
    
    /**
     * Find notification templates by notification type
     */
    List<NotificationTemplate> findByNotificationType(String notificationType);

    List<NotificationTemplate> findByCategory(String category);
    
    /**
     * Find notification templates by notification type with pagination
     */
    Page<NotificationTemplate> findByNotificationType(String notificationType, Pageable pageable);
    
    /**
     * Find all active notification templates
     */
    List<NotificationTemplate> findByIsActiveTrue();
    
    /**
     * Find all active notification templates with pagination
     */
    Page<NotificationTemplate> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive notification templates
     */
    List<NotificationTemplate> findByIsActiveFalse();
    
    /**
     * Find notification templates by created by user
     */
    List<NotificationTemplate> findByCreatedBy(Long createdBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if template code exists
     */
    boolean existsByTemplateCode(String templateCode);
    
    /**
     * Check if template code exists excluding specific template ID
     */
    boolean existsByTemplateCodeAndIdNot(String templateCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search notification templates by template code containing (case-insensitive)
     */
    Page<NotificationTemplate> findByTemplateCodeContainingIgnoreCase(String templateCode, Pageable pageable);
    
    /**
     * Search notification templates by template name containing (case-insensitive)
     */
    Page<NotificationTemplate> findByTemplateNameContainingIgnoreCase(String templateName, Pageable pageable);
    
    /**
     * Search notification templates by subject containing (case-insensitive)
     */
    Page<NotificationTemplate> findBySubjectContainingIgnoreCase(String subject, Pageable pageable);
    
    /**
     * Search active notification templates by keyword
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.isActive = true AND " +
           "(LOWER(nt.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nt.templateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nt.subject) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<NotificationTemplate> searchActiveTemplates(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search notification templates by multiple criteria
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE " +
           "(:templateCode IS NULL OR LOWER(nt.templateCode) LIKE LOWER(CONCAT('%', :templateCode, '%'))) AND " +
           "(:templateName IS NULL OR LOWER(nt.templateName) LIKE LOWER(CONCAT('%', :templateName, '%'))) AND " +
           "(:notificationType IS NULL OR nt.notificationType = :notificationType) AND " +
           "(:isActive IS NULL OR nt.isActive = :isActive)")
    Page<NotificationTemplate> searchNotificationTemplates(
            @Param("templateCode") String templateCode,
            @Param("templateName") String templateName,
            @Param("notificationType") String notificationType,
            @Param("isActive") Boolean isActive,
            Pageable pageable);


    @Query("SELECT nt FROM NotificationTemplate nt WHERE " +
           "LOWER(nt.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nt.templateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nt.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nt.bodyTemplate) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<NotificationTemplate> searchTemplates(String keyword,Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active notification templates
     */
    long countByIsActiveTrue();
    
    /**
     * Count notification templates by notification type
     */
    long countByNotificationType(String notificationType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find EMAIL notification templates
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.notificationType = 'EMAIL' " +
           "AND nt.isActive = true ORDER BY nt.templateName")
    List<NotificationTemplate> findEmailTemplates();
    
    /**
     * Find SMS notification templates
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.notificationType = 'SMS' " +
           "AND nt.isActive = true ORDER BY nt.templateName")
    List<NotificationTemplate> findSmsTemplates();
    
    /**
     * Find PUSH notification templates
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.notificationType = 'PUSH' " +
           "AND nt.isActive = true ORDER BY nt.templateName")
    List<NotificationTemplate> findPushTemplates();
    
    /**
     * Find IN_APP notification templates
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.notificationType = 'IN_APP' " +
           "AND nt.isActive = true ORDER BY nt.templateName")
    List<NotificationTemplate> findInAppTemplates();
    
    /**
     * Find active templates by notification type
     */
    List<NotificationTemplate> findByNotificationTypeAndIsActiveTrue(String notificationType);
    
    /**
     * Get notification template statistics
     */
    @Query("SELECT " +
           "COUNT(nt) as totalTemplates, " +
           "SUM(CASE WHEN nt.isActive = true THEN 1 ELSE 0 END) as activeTemplates, " +
           "SUM(CASE WHEN nt.notificationType = 'EMAIL' THEN 1 ELSE 0 END) as emailTemplates, " +
           "SUM(CASE WHEN nt.notificationType = 'SMS' THEN 1 ELSE 0 END) as smsTemplates, " +
           "SUM(CASE WHEN nt.notificationType = 'PUSH' THEN 1 ELSE 0 END) as pushTemplates, " +
           "SUM(CASE WHEN nt.notificationType = 'IN_APP' THEN 1 ELSE 0 END) as inAppTemplates " +
           "FROM NotificationTemplate nt")
    Object getNotificationTemplateStatistics();
    
    /**
     * Get notification templates grouped by notification type
     */
    @Query("SELECT nt.notificationType, COUNT(nt) as templateCount " +
           "FROM NotificationTemplate nt WHERE nt.isActive = true " +
           "GROUP BY nt.notificationType ORDER BY templateCount DESC")
    List<Object[]> getNotificationTemplatesByType();
    
    /**
     * Find all notification templates ordered by template name
     */
    List<NotificationTemplate> findAllByOrderByTemplateNameAsc();
    
    /**
     * Find active notification templates ordered by template name
     */
    List<NotificationTemplate> findByIsActiveTrueOrderByTemplateNameAsc();
}
