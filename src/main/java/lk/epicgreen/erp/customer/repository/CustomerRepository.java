package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 * Based on ACTUAL database schema: customers table
 * 
 * Fields: customer_code, customer_name, customer_type (ENUM: WHOLESALE, RETAIL, DISTRIBUTOR, DIRECT),
 *         contact_person, email, phone, mobile, tax_id, payment_terms,
 *         credit_limit, credit_days, current_balance,
 *         billing_address_line1, billing_address_line2, billing_city, billing_state, billing_country, billing_postal_code,
 *         shipping_address_line1, shipping_address_line2, shipping_city, shipping_state, shipping_country, shipping_postal_code,
 *         assigned_sales_rep_id (BIGINT), region, route_code, is_active, deleted_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find customer by customer code
     */
    Optional<Customer> findByCustomerCode(String customerCode);
    
    /**
     * Find customer by customer name
     */
    Optional<Customer> findByCustomerName(String customerName);
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customer by mobile
     */
    Optional<Customer> findByMobile(String mobile);
    
    /**
     * Find all active customers
     */
    List<Customer> findByIsActiveTrue();
    
    /**
     * Find all active customers with pagination
     */
    Page<Customer> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive customers
     */
    List<Customer> findByIsActiveFalse();
    
    /**
     * Find customers by customer type
     */
    List<Customer> findByCustomerType(String customerType);
    
    /**
     * Find customers by customer type with pagination
     */
    Page<Customer> findByCustomerType(String customerType, Pageable pageable);
    
    /**
     * Find active customers by type
     */
    List<Customer> findByCustomerTypeAndIsActiveTrue(String customerType);
    
    /**
     * Find customers by assigned sales rep
     */
    List<Customer> findByAssignedSalesRepId(Long assignedSalesRepId);
    
    /**
     * Find customers by assigned sales rep with pagination
     */
    Page<Customer> findByAssignedSalesRepId(Long assignedSalesRepId, Pageable pageable);
    
    /**
     * Find customers by region
     */
    List<Customer> findByRegion(String region);
    
    /**
     * Find customers by route code
     */
    List<Customer> findByRouteCode(String routeCode);
    
    /**
     * Find customers by billing city
     */
    List<Customer> findByBillingCity(String billingCity);
    
    /**
     * Find customers by billing state
     */
    List<Customer> findByBillingState(String billingState);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if customer code exists
     */
    boolean existsByCustomerCode(String customerCode);
    
    /**
     * Check if customer name exists
     */
    boolean existsByCustomerName(String customerName);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer code exists excluding specific customer ID
     */
    boolean existsByCustomerCodeAndIdNot(String customerCode, Long id);
    
    /**
     * Check if email exists excluding specific customer ID
     */
    boolean existsByEmailAndIdNot(String email, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search customers by customer code containing (case-insensitive)
     */
    Page<Customer> findByCustomerCodeContainingIgnoreCase(String customerCode, Pageable pageable);
    
    /**
     * Search customers by customer name containing (case-insensitive)
     */
    Page<Customer> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    
    /**
     * Search active customers by keyword
     */
    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND " +
           "(LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.mobile) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Customer> searchActiveCustomers(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search customers by multiple criteria
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "(:customerCode IS NULL OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :customerCode, '%'))) AND " +
           "(:customerName IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:customerType IS NULL OR c.customerType = :customerType) AND " +
           "(:region IS NULL OR c.region = :region) AND " +
           "(:assignedSalesRepId IS NULL OR c.assignedSalesRepId = :assignedSalesRepId) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<Customer> searchCustomers(
            @Param("customerCode") String customerCode,
            @Param("customerName") String customerName,
            @Param("customerType") String customerType,
            @Param("region") String region,
            @Param("assignedSalesRepId") Long assignedSalesRepId,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active customers
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive customers
     */
    long countByIsActiveFalse();
    
    /**
     * Count customers by type
     */
    long countByCustomerType(String customerType);
    
    /**
     * Count customers by sales rep
     */
    long countByAssignedSalesRepId(Long assignedSalesRepId);
    
    /**
     * Count customers by region
     */
    long countByRegion(String region);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find wholesale customers
     */
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'WHOLESALE' AND c.isActive = true")
    List<Customer> findWholesaleCustomers();
    
    /**
     * Find retail customers
     */
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'RETAIL' AND c.isActive = true")
    List<Customer> findRetailCustomers();
    
    /**
     * Find distributor customers
     */
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'DISTRIBUTOR' AND c.isActive = true")
    List<Customer> findDistributorCustomers();
    
    /**
     * Find direct customers
     */
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'DIRECT' AND c.isActive = true")
    List<Customer> findDirectCustomers();
    
    /**
     * Find customers with credit limit exceeded
     */
    @Query("SELECT c FROM Customer c WHERE c.currentBalance > c.creditLimit AND c.isActive = true")
    List<Customer> findCustomersWithCreditLimitExceeded();
    
    /**
     * Find customers with outstanding balance
     */
    @Query("SELECT c FROM Customer c WHERE c.currentBalance > 0 AND c.isActive = true")
    List<Customer> findCustomersWithOutstandingBalance();
    
    /**
     * Find customers by credit limit range
     */
    @Query("SELECT c FROM Customer c WHERE c.creditLimit BETWEEN :minLimit AND :maxLimit AND c.isActive = true")
    List<Customer> findByCreditLimitRange(
            @Param("minLimit") BigDecimal minLimit,
            @Param("maxLimit") BigDecimal maxLimit);
    
    /**
     * Find customers by current balance range
     */
    @Query("SELECT c FROM Customer c WHERE c.currentBalance BETWEEN :minBalance AND :maxBalance AND c.isActive = true")
    List<Customer> findByCurrentBalanceRange(
            @Param("minBalance") BigDecimal minBalance,
            @Param("maxBalance") BigDecimal maxBalance);
    
    /**
     * Find all distinct regions
     */
    @Query("SELECT DISTINCT c.region FROM Customer c WHERE c.region IS NOT NULL ORDER BY c.region")
    List<String> findAllDistinctRegions();
    
    /**
     * Find all distinct route codes
     */
    @Query("SELECT DISTINCT c.routeCode FROM Customer c WHERE c.routeCode IS NOT NULL ORDER BY c.routeCode")
    List<String> findAllDistinctRouteCodes();
    
    /**
     * Find all distinct billing cities
     */
    @Query("SELECT DISTINCT c.billingCity FROM Customer c WHERE c.billingCity IS NOT NULL ORDER BY c.billingCity")
    List<String> findAllDistinctBillingCities();
    
    /**
     * Get customer statistics
     */
    @Query("SELECT " +
           "COUNT(c) as totalCustomers, " +
           "SUM(CASE WHEN c.isActive = true THEN 1 ELSE 0 END) as activeCustomers, " +
           "SUM(CASE WHEN c.customerType = 'WHOLESALE' THEN 1 ELSE 0 END) as wholesaleCustomers, " +
           "SUM(CASE WHEN c.customerType = 'RETAIL' THEN 1 ELSE 0 END) as retailCustomers, " +
           "SUM(CASE WHEN c.customerType = 'DISTRIBUTOR' THEN 1 ELSE 0 END) as distributorCustomers, " +
           "SUM(CASE WHEN c.customerType = 'DIRECT' THEN 1 ELSE 0 END) as directCustomers, " +
           "SUM(c.currentBalance) as totalOutstanding " +
           "FROM Customer c")
    Object getCustomerStatistics();
    
    /**
     * Get customers grouped by type
     */
    @Query("SELECT c.customerType, COUNT(c) as customerCount, SUM(c.currentBalance) as totalBalance " +
           "FROM Customer c WHERE c.isActive = true GROUP BY c.customerType")
    List<Object[]> getCustomersByType();
    
    /**
     * Get customers grouped by region
     */
    @Query("SELECT c.region, COUNT(c) as customerCount, SUM(c.currentBalance) as totalBalance " +
           "FROM Customer c WHERE c.isActive = true AND c.region IS NOT NULL " +
           "GROUP BY c.region ORDER BY customerCount DESC")
    List<Object[]> getCustomersByRegion();
    
    /**
     * Get customers grouped by sales rep
     */
    @Query("SELECT c.assignedSalesRepId, COUNT(c) as customerCount, SUM(c.currentBalance) as totalBalance " +
           "FROM Customer c WHERE c.isActive = true AND c.assignedSalesRepId IS NOT NULL " +
           "GROUP BY c.assignedSalesRepId ORDER BY customerCount DESC")
    List<Object[]> getCustomersBySalesRep();
    
    /**
     * Find customers without assigned sales rep
     */
    @Query("SELECT c FROM Customer c WHERE c.assignedSalesRepId IS NULL AND c.isActive = true")
    List<Customer> findCustomersWithoutSalesRep();
    
    /**
     * Find top customers by outstanding balance
     */
    @Query("SELECT c FROM Customer c WHERE c.isActive = true ORDER BY c.currentBalance DESC")
    List<Customer> findTopCustomersByBalance(Pageable pageable);
    
    /**
     * Find customers by type and region
     */
    List<Customer> findByCustomerTypeAndRegionAndIsActiveTrue(String customerType, String region);
    
    /**
     * Find customers by sales rep and region
     */
    List<Customer> findByAssignedSalesRepIdAndRegionAndIsActiveTrue(Long salesRepId, String region);
    
    /**
     * Find all customers ordered by code
     */
    List<Customer> findAllByOrderByCustomerCodeAsc();
    
    /**
     * Find active customers ordered by name
     */
    List<Customer> findByIsActiveTrueOrderByCustomerNameAsc();
}
