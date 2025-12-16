package lk.epicgreen.erp.common.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for AuditLog entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    /**
     * Finds audit logs by entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByEntityNameAndEntityIdOrderByTimestampDesc(
            String entityName, 
            String entityId, 
            Pageable pageable
    );
    
    /**
     * Finds audit logs by entity name
     * 
     * @param entityName Entity name
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByEntityNameOrderByTimestampDesc(
            String entityName, 
            Pageable pageable
    );
    
    /**
     * Finds audit logs by user
     * 
     * @param performedBy Username
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByPerformedByOrderByTimestampDesc(
            String performedBy, 
            Pageable pageable
    );
    
    /**
     * Finds audit logs by action
     * 
     * @param action Action type
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByActionOrderByTimestampDesc(
            String action, 
            Pageable pageable
    );
    
    /**
     * Finds audit logs by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable
    );
    
    /**
     * Finds audit logs by module
     * 
     * @param module Module name
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByModuleOrderByTimestampDesc(
            String module, 
            Pageable pageable
    );
    
    /**
     * Finds failed audit logs
     * 
     * @param pageable Pagination
     * @return Page of audit logs
     */
    Page<AuditLog> findByStatusOrderByTimestampDesc(
            String status, 
            Pageable pageable
    );
    
    /**
     * Finds recent audit logs for entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param limit Limit
     * @return List of audit logs
     */
    @Query("SELECT a FROM AuditLog a WHERE a.entityName = :entityName AND a.entityId = :entityId " +
           "ORDER BY a.timestamp DESC LIMIT :limit")
    List<AuditLog> findRecentByEntity(
            @Param("entityName") String entityName,
            @Param("entityId") String entityId,
            @Param("limit") int limit
    );
    
    /**
     * Counts audit logs by entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @return Count
     */
    long countByEntityNameAndEntityId(String entityName, String entityId);
    
    /**
     * Counts audit logs by user in date range
     * 
     * @param performedBy Username
     * @param startDate Start date
     * @param endDate End date
     * @return Count
     */
    long countByPerformedByAndTimestampBetween(
            String performedBy, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );
    
    /**
     * Finds audit logs with search criteria
     * 
     * @param entityName Entity name (optional)
     * @param action Action (optional)
     * @param performedBy User (optional)
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
           "(:entityName IS NULL OR a.entityName = :entityName) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:performedBy IS NULL OR a.performedBy = :performedBy) AND " +
           "(:startDate IS NULL OR a.timestamp >= :startDate) AND " +
           "(:endDate IS NULL OR a.timestamp <= :endDate) " +
           "ORDER BY a.timestamp DESC")
    Page<AuditLog> search(
            @Param("entityName") String entityName,
            @Param("action") String action,
            @Param("performedBy") String performedBy,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
    
    /**
     * Deletes old audit logs
     * 
     * @param beforeDate Date before which to delete
     * @return Number of deleted records
     */
    long deleteByTimestampBefore(LocalDateTime beforeDate);
}
