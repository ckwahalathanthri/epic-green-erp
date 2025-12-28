package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerContact;
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
 * Repository interface for CustomerContact entity
 * Based on ACTUAL database schema: customer_contacts table
 * 
 * Fields: customer_id (BIGINT), contact_name, designation, email, phone, mobile, is_primary
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long>, JpaSpecificationExecutor<CustomerContact> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all contacts for a customer
     */
    List<CustomerContact> findByCustomerId(Long customerId);
    
    /**
     * Find all contacts for a customer with pagination
     */
    Page<CustomerContact> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find primary contact for a customer
     */
    Optional<CustomerContact> findByCustomerIdAndIsPrimaryTrue(Long customerId);
    
    /**
     * Find contacts by contact name
     */
    List<CustomerContact> findByContactNameContainingIgnoreCase(String contactName);
    
    /**
     * Find contacts by email
     */
    List<CustomerContact> findByEmail(String email);
    
    /**
     * Find contacts by mobile
     */
    List<CustomerContact> findByMobile(String mobile);
    
    /**
     * Find contacts by designation
     */
    List<CustomerContact> findByDesignation(String designation);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if primary contact exists for customer
     */
    boolean existsByCustomerIdAndIsPrimaryTrue(Long customerId);
    
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
     * Search contacts by keyword for a customer
     */
    @Query("SELECT cc FROM CustomerContact cc WHERE cc.customerId = :customerId AND " +
           "(LOWER(cc.contactName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cc.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cc.mobile) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<CustomerContact> searchCustomerContacts(
            @Param("customerId") Long customerId,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    /**
     * Search contacts by multiple criteria
     */
    @Query("SELECT cc FROM CustomerContact cc WHERE " +
           "(:customerId IS NULL OR cc.customerId = :customerId) AND " +
           "(:contactName IS NULL OR LOWER(cc.contactName) LIKE LOWER(CONCAT('%', :contactName, '%'))) AND " +
           "(:email IS NULL OR LOWER(cc.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:designation IS NULL OR cc.designation = :designation)")
    Page<CustomerContact> searchContacts(
            @Param("customerId") Long customerId,
            @Param("contactName") String contactName,
            @Param("email") String email,
            @Param("designation") String designation,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count contacts for a customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count primary contacts
     */
    long countByIsPrimaryTrue();
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all contacts for a customer
     */
    @Modifying
    @Query("DELETE FROM CustomerContact cc WHERE cc.customerId = :customerId")
    void deleteAllByCustomerId(@Param("customerId") Long customerId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find all primary contacts
     */
    @Query("SELECT cc FROM CustomerContact cc WHERE cc.isPrimary = true")
    List<CustomerContact> findAllPrimaryContacts();
    
    /**
     * Find all distinct designations
     */
    @Query("SELECT DISTINCT cc.designation FROM CustomerContact cc WHERE cc.designation IS NOT NULL ORDER BY cc.designation")
    List<String> findAllDistinctDesignations();
    
    /**
     * Find contacts for a customer ordered by name
     */
    List<CustomerContact> findByCustomerIdOrderByContactNameAsc(Long customerId);
    
    /**
     * Find contacts for a customer ordered by primary status
     */
    @Query("SELECT cc FROM CustomerContact cc WHERE cc.customerId = :customerId ORDER BY cc.isPrimary DESC, cc.contactName")
    List<CustomerContact> findByCustomerIdOrderByPrimaryAndName(@Param("customerId") Long customerId);
    
    /**
     * Get contact statistics
     */
    @Query("SELECT " +
           "COUNT(cc) as totalContacts, " +
           "SUM(CASE WHEN cc.isPrimary = true THEN 1 ELSE 0 END) as primaryContacts, " +
           "COUNT(DISTINCT cc.customerId) as customersWithContacts " +
           "FROM CustomerContact cc")
    Object getContactStatistics();
}
