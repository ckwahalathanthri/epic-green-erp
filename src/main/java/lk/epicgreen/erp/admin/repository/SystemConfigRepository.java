package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.SystemConfig;
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
 * Repository interface for SystemConfig entity (CORRECTED)
 * Matches actual database schema: config_key, config_value, config_group, data_type, is_encrypted
 * (NO is_editable or default_value fields)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long>, JpaSpecificationExecutor<SystemConfig> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find system config by config key
     */
    Optional<SystemConfig> findByConfigKey(String configKey);
    
    /**
     * Find system configs by group (config_group)
     */
    List<SystemConfig> findByConfigGroup(String configGroup);
    
    /**
     * Find system configs by group with pagination
     */
    Page<SystemConfig> findByConfigGroup(String configGroup, Pageable pageable);
    
    /**
     * Find system configs by data type
     */
    List<SystemConfig> findByDataType(String dataType);
    
    /**
     * Find all encrypted configs
     */
    List<SystemConfig> findByIsEncryptedTrue();
    
    /**
     * Find all non-encrypted configs
     */
    List<SystemConfig> findByIsEncryptedFalse();
    
    /**
     * Find configs by config keys
     */
    List<SystemConfig> findByConfigKeyIn(List<String> configKeys);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if config key exists
     */
    boolean existsByConfigKey(String configKey);
    
    /**
     * Check if config key exists excluding specific config ID
     */
    boolean existsByConfigKeyAndIdNot(String configKey, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search configs by config key containing (case-insensitive)
     */
    Page<SystemConfig> findByConfigKeyContainingIgnoreCase(String configKey, Pageable pageable);
    
    /**
     * Search configs by description containing (case-insensitive)
     */
    Page<SystemConfig> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    
    /**
     * Search configs by keyword
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE " +
           "LOWER(sc.configKey) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sc.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sc.configGroup) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SystemConfig> searchConfigs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search configs by multiple criteria
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE " +
           "(:configKey IS NULL OR LOWER(sc.configKey) LIKE LOWER(CONCAT('%', :configKey, '%'))) AND " +
           "(:configGroup IS NULL OR sc.configGroup = :configGroup) AND " +
           "(:dataType IS NULL OR sc.dataType = :dataType) AND " +
           "(:isEncrypted IS NULL OR sc.isEncrypted = :isEncrypted)")
    Page<SystemConfig> searchConfigs(
            @Param("configKey") String configKey,
            @Param("configGroup") String configGroup,
            @Param("dataType") String dataType,
            @Param("isEncrypted") Boolean isEncrypted,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count configs by group
     */
    long countByConfigGroup(String configGroup);
    
    /**
     * Count encrypted configs
     */
    long countByIsEncryptedTrue();
    
    /**
     * Count non-encrypted configs
     */
    long countByIsEncryptedFalse();
    
    /**
     * Count configs by data type
     */
    long countByDataType(String dataType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find all distinct groups
     */
    @Query("SELECT DISTINCT sc.configGroup FROM SystemConfig sc WHERE sc.configGroup IS NOT NULL ORDER BY sc.configGroup")
    List<String> findAllDistinctGroups();
    
    /**
     * Find all distinct data types
     */
    @Query("SELECT DISTINCT sc.dataType FROM SystemConfig sc WHERE sc.dataType IS NOT NULL ORDER BY sc.dataType")
    List<String> findAllDistinctDataTypes();
    
    /**
     * Find configs by group and data type
     */
    List<SystemConfig> findByConfigGroupAndDataType(String configGroup, String dataType);
    
    /**
     * Get config statistics
     */
    @Query("SELECT " +
           "COUNT(sc) as totalConfigs, " +
           "SUM(CASE WHEN sc.isEncrypted = true THEN 1 ELSE 0 END) as encryptedConfigs, " +
           "COUNT(DISTINCT sc.configGroup) as totalGroups, " +
           "COUNT(DISTINCT sc.dataType) as totalDataTypes " +
           "FROM SystemConfig sc")
    Object getConfigStatistics();
    
    /**
     * Get config count by group
     */
    @Query("SELECT sc.configGroup, COUNT(sc) as configCount " +
           "FROM SystemConfig sc GROUP BY sc.configGroup ORDER BY configCount DESC")
    List<Object[]> getConfigCountByGroup();
    
    /**
     * Get config count by data type
     */
    @Query("SELECT sc.dataType, COUNT(sc) as configCount " +
           "FROM SystemConfig sc GROUP BY sc.dataType ORDER BY configCount DESC")
    List<Object[]> getConfigCountByDataType();
    
    /**
     * Find all configs ordered by group and config key
     */
    List<SystemConfig> findAllByOrderByConfigGroupAscConfigKeyAsc();
    
    /**
     * Find configs by group ordered by config key
     */
    List<SystemConfig> findByConfigGroupOrderByConfigKeyAsc(String configGroup);
    
    /**
     * Find configs by data type with pagination
     */
    Page<SystemConfig> findByDataType(String dataType, Pageable pageable);
    
    /**
     * Find configs by group and encryption status
     */
    List<SystemConfig> findByConfigGroupAndIsEncrypted(String configGroup, boolean isEncrypted);
}
