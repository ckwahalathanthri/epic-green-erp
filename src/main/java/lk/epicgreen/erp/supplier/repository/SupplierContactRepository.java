package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SupplierContact entity
 * Based on ACTUAL database schema: supplier_contacts table
 * 
 * Fields: supplier_id (BIGINT), contact_name, designation, email, phone, mobile, is_primary
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContact, Long>, JpaSpecificationExecutor<SupplierContact> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all contacts for a supplier
     */
    List<SupplierContact> findBySupplierId(Long supplierId);
    
    /**
     * Find all contacts for a supplier with pagination
     */
    Page<SupplierContact> findBySupplierId(Long supplierId, Pageable pageable);
    
    /**
     * Find primary contact for a supplier
     */
    Optional<SupplierContact> findBySupplierIdAndIsPrimaryTrue(Long supplierId);
    
    /**
     * Find contacts by contact name
     */
    List<SupplierContact> findByContactNameContainingIgnoreCase(String contactName);
    
    /**
     * Find contacts by email
     */
    List<SupplierContact> findByEmail(String email);
    
    /**
     * Find contacts by mobile
     */
    List<SupplierContact> findByMobile(String mobile);
    
    /**
     * Find contacts by designation
     */
    List<SupplierContact> findByDesignation(String designation);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if primary contact exists for supplier
     */
    boolean existsBySupplierIdAndIsPrimaryTrue(Long supplierId);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if mobile exists
     */
    boolean existsByMobile(String mobile);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search contacts by keyword for a supplier
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.supplierId = :supplierId AND " +
           "(LOWER(sc.contactName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sc.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sc.mobile) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<SupplierContact> searchSupplierContacts(
            @Param("supplierId") Long supplierId,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    /**
     * Search contacts by multiple criteria
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE " +
           "(:supplierId IS NULL OR sc.supplierId = :supplierId) AND " +
           "(:contactName IS NULL OR LOWER(sc.contactName) LIKE LOWER(CONCAT('%', :contactName, '%'))) AND " +
           "(:email IS NULL OR LOWER(sc.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:designation IS NULL OR sc.designation = :designation)")
    Page<SupplierContact> searchContacts(
            @Param("supplierId") Long supplierId,
            @Param("contactName") String contactName,
            @Param("email") String email,
            @Param("designation") String designation,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count contacts for a supplier
     */
    long countBySupplierId(Long supplierId);
    
    /**
     * Count primary contacts
     */
    long countByIsPrimaryTrue();
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all contacts for a supplier
     */
    @Modifying
    @Query("DELETE FROM SupplierContact sc WHERE sc.supplierId = :supplierId")
    void deleteAllBySupplierId(@Param("supplierId") Long supplierId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find all primary contacts
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.isPrimary = true")
    List<SupplierContact> findAllPrimaryContacts();
    
    /**
     * Find all distinct designations
     */
    @Query("SELECT DISTINCT sc.designation FROM SupplierContact sc WHERE sc.designation IS NOT NULL ORDER BY sc.designation")
    List<String> findAllDistinctDesignations();
    
    /**
     * Find contacts for a supplier ordered by name
     */
    List<SupplierContact> findBySupplierIdOrderByContactNameAsc(Long supplierId);
    
    /**
     * Find contacts for a supplier ordered by primary status
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.supplierId = :supplierId ORDER BY sc.isPrimary DESC, sc.contactName")
    List<SupplierContact> findBySupplierIdOrderByPrimaryAndName(@Param("supplierId") Long supplierId);
    
    /**
     * Get contact statistics
     */
    @Query("SELECT " +
           "COUNT(sc) as totalContacts, " +
           "SUM(CASE WHEN sc.isPrimary = true THEN 1 ELSE 0 END) as primaryContacts, " +
           "COUNT(DISTINCT sc.supplierId) as suppliersWithContacts " +
           "FROM SupplierContact sc")
    Object getContactStatistics();
    
    /**
     * Find contacts with email
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.email IS NOT NULL")
    List<SupplierContact> findContactsWithEmail();
    
    /**
     * Find contacts with mobile
     */
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.mobile IS NOT NULL")
    List<SupplierContact> findContactsWithMobile();
    
    /**
     * Find contacts by designation for a supplier
     */
    List<SupplierContact> findBySupplierIdAndDesignation(Long supplierId, String designation);
}
