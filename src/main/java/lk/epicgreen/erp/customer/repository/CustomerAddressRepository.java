package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerAddress;
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
 * Repository interface for CustomerAddress entity
 * Based on ACTUAL database schema: customer_addresses table
 * 
 * Fields: customer_id (BIGINT), address_type (ENUM: BILLING, SHIPPING, BOTH),
 *         address_name, address_line1, address_line2, city, state, country, postal_code,
 *         contact_person, contact_phone, is_default
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all addresses for a customer
     */
    List<CustomerAddress> findByCustomerId(Long customerId);
    
    /**
     * Find all addresses for a customer with pagination
     */
    Page<CustomerAddress> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find default address for a customer
     */
    Optional<CustomerAddress> findByCustomerIdAndIsDefaultTrue(Long customerId);
    
    /**
     * Find addresses by type for a customer
     */
    List<CustomerAddress> findByCustomerIdAndAddressType(Long customerId, String addressType);
    
    /**
     * Find billing addresses for a customer
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerId = :customerId AND " +
           "(ca.addressType = 'BILLING' OR ca.addressType = 'BOTH')")
    List<CustomerAddress> findBillingAddressesByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Find shipping addresses for a customer
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerId = :customerId AND " +
           "(ca.addressType = 'SHIPPING' OR ca.addressType = 'BOTH')")
    List<CustomerAddress> findShippingAddressesByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Find addresses by city
     */
    List<CustomerAddress> findByCity(String city);
    
    /**
     * Find addresses by state
     */
    List<CustomerAddress> findByState(String state);
    
    /**
     * Find addresses by country
     */
    List<CustomerAddress> findByCountry(String country);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if default address exists for customer
     */
    boolean existsByCustomerIdAndIsDefaultTrue(Long customerId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search addresses by keyword for a customer
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerId = :customerId AND " +
           "(LOWER(ca.addressName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ca.addressLine1) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ca.city) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<CustomerAddress> searchCustomerAddresses(
            @Param("customerId") Long customerId,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    /**
     * Search addresses by multiple criteria
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE " +
           "(:customerId IS NULL OR ca.customerId = :customerId) AND " +
           "(:addressType IS NULL OR ca.addressType = :addressType) AND " +
           "(:city IS NULL OR ca.city = :city) AND " +
           "(:state IS NULL OR ca.state = :state) AND " +
           "(:isDefault IS NULL OR ca.isDefault = :isDefault)")
    Page<CustomerAddress> searchAddresses(
            @Param("customerId") Long customerId,
            @Param("addressType") String addressType,
            @Param("city") String city,
            @Param("state") String state,
            @Param("isDefault") Boolean isDefault,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count addresses for a customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count addresses by type
     */
    long countByAddressType(String addressType);
    
    /**
     * Count default addresses
     */
    long countByIsDefaultTrue();
    
    /**
     * Count addresses by city
     */
    long countByCity(String city);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all addresses for a customer
     */
    @Modifying
    @Query("DELETE FROM CustomerAddress ca WHERE ca.customerId = :customerId")
    void deleteAllByCustomerId(@Param("customerId") Long customerId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find all billing addresses
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.addressType = 'BILLING' OR ca.addressType = 'BOTH'")
    List<CustomerAddress> findAllBillingAddresses();
    
    /**
     * Find all shipping addresses
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.addressType = 'SHIPPING' OR ca.addressType = 'BOTH'")
    List<CustomerAddress> findAllShippingAddresses();
    
    /**
     * Find all default addresses
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.isDefault = true")
    List<CustomerAddress> findAllDefaultAddresses();
    
    /**
     * Find all distinct cities
     */
    @Query("SELECT DISTINCT ca.city FROM CustomerAddress ca WHERE ca.city IS NOT NULL ORDER BY ca.city")
    List<String> findAllDistinctCities();
    
    /**
     * Find all distinct states
     */
    @Query("SELECT DISTINCT ca.state FROM CustomerAddress ca WHERE ca.state IS NOT NULL ORDER BY ca.state")
    List<String> findAllDistinctStates();
    
    /**
     * Find all distinct countries
     */
    @Query("SELECT DISTINCT ca.country FROM CustomerAddress ca WHERE ca.country IS NOT NULL ORDER BY ca.country")
    List<String> findAllDistinctCountries();
    
    /**
     * Find addresses for a customer ordered by default status
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerId = :customerId ORDER BY ca.isDefault DESC, ca.addressName")
    List<CustomerAddress> findByCustomerIdOrderByDefaultAndName(@Param("customerId") Long customerId);
    
    /**
     * Get address statistics
     */
    @Query("SELECT " +
           "COUNT(ca) as totalAddresses, " +
           "SUM(CASE WHEN ca.addressType = 'BILLING' THEN 1 ELSE 0 END) as billingAddresses, " +
           "SUM(CASE WHEN ca.addressType = 'SHIPPING' THEN 1 ELSE 0 END) as shippingAddresses, " +
           "SUM(CASE WHEN ca.addressType = 'BOTH' THEN 1 ELSE 0 END) as bothAddresses, " +
           "SUM(CASE WHEN ca.isDefault = true THEN 1 ELSE 0 END) as defaultAddresses " +
           "FROM CustomerAddress ca")
    Object getAddressStatistics();
    
    /**
     * Get addresses grouped by city
     */
    @Query("SELECT ca.city, COUNT(ca) as addressCount FROM CustomerAddress ca " +
           "WHERE ca.city IS NOT NULL GROUP BY ca.city ORDER BY addressCount DESC")
    List<Object[]> getAddressesByCity();
    
    /**
     * Get addresses grouped by state
     */
    @Query("SELECT ca.state, COUNT(ca) as addressCount FROM CustomerAddress ca " +
           "WHERE ca.state IS NOT NULL GROUP BY ca.state ORDER BY addressCount DESC")
    List<Object[]> getAddressesByState();
    
    /**
     * Find addresses by customer and type ordered by default
     */
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerId = :customerId AND ca.addressType = :addressType " +
           "ORDER BY ca.isDefault DESC, ca.addressName")
    List<CustomerAddress> findByCustomerIdAndAddressTypeOrderByDefault(
            @Param("customerId") Long customerId,
            @Param("addressType") String addressType);
}
