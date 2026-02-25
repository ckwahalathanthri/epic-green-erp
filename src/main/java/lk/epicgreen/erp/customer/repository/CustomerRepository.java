package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customer.id = :customerId " +
            "AND cl.transactionDate BETWEEN :startDate AND :endDate ORDER BY cl.transactionDate ASC, cl.createdAt ASC")
    List<CustomerLedger> findByCustomerIdAndTransactionDateBetween(
            @Param("customerId") Long customerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
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
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.customerCode = :customerCode")
    boolean existsByCustomerCode(@Param("customerCode") String customerCode);
    
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
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.customerCode = :customerCode AND c.id <> :id")
    boolean existsByCustomerCodeAndIdNot(@Param("customerCode") String customerCode, Long id);
    
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
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.isActive = true")
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
    @Query("SELECT c FROM Customer c WHERE c.isActive = true ORDER BY c.customerName ASC")
    List<Customer> findByIsActiveTrueOrderByCustomerNameAsc();

    /*
       * Find customer by customer code excluding deleted records
     */
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND c.customerCode = :customerCode")
    Optional<Customer> findByCustomerCodeAndDeletedAtIsNull(@Param("customerCode") String customerCode);

    /**
     * Find customer by email excluding deleted records
     */
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND c.email = :email")
    Optional<Customer> findByEmailAndDeletedAtIsNull(@Param("email") String email);

       /**
        * Find customer by phone number excluding deleted records
        */
       @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND (c.phone = :phoneNumber OR c.mobile = :phoneNumber)")
    Optional<Customer> findByPhoneNumberAndDeletedAtIsNull(String phoneNumber);

       /**
        * Find all customers excluding deleted records
        */
       @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL")
    Page<Customer> findByDeletedAtIsNull(Pageable pageable);

    /**
       * Find all active customers excluding deleted records
     **/

    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.deletedAt IS NULL")
    List<Customer> findByIsActiveTrueAndDeletedAtIsNull();

       /**
        * Find customers by type excluding deleted records
        */

       @Query("SELECT c FROM Customer c WHERE c.customerType = :customerType AND c.deletedAt IS NULL")
    Page<Customer> findByCustomerTypeAndDeletedAtIsNull(String customerType, Pageable pageable);

    /**
    * Find active customers by type excluding deleted records
    */
    List<Customer> findByCustomerTypeAndIsActiveTrueAndDeletedAtIsNull(String customerType);

       /**
        * Find customers by assigned sales rep excluding deleted records
        */
    List<Customer> findByAssignedSalesRepIdAndDeletedAtIsNull(Long salesRepId);

       /**
        * Find customers by region excluding deleted records
        */
    List<Customer> findByRegionAndDeletedAtIsNull(String region);

       /**
              * Find customers by route code excluding deleted records
              */
    List<Customer> findByRouteCodeAndDeletedAtIsNull(String routeCode);

       /**
        * Search customers by keyword excluding deleted records
        */
       @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND " +
              "(LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
              "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
              "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);

       /**
        * Find customers exceeding their credit limit
        */

       @Query("SELECT c FROM Customer c WHERE c.currentBalance > c.creditLimit AND c.deletedAt IS NULL")
    List<Customer> findCustomersExceedingCreditLimit();

    /**
     * Find customer by ID excluding deleted records
     */
    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);
@Query("SELECT COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL")
    long countByDeletedAtIsNull();
@Query("SELECT COUNT(c) FROM Customer c WHERE c.isActive = true AND c.deletedAt IS NULL")
    long countByIsActiveTrueAndDeletedAtIsNull();
@Query("SELECT COUNT(c) FROM Customer c WHERE c.isActive = false AND c.deletedAt IS NULL")
    long countByIsActiveFalseAndDeletedAtIsNull();
@Query("SELECT COUNT(c) FROM Customer c WHERE c.isBlacklisted = true AND c.deletedAt IS NULL")
    long countByIsBlacklistedTrueAndDeletedAtIsNull();
@Query("SELECT COUNT(c) FROM Customer c WHERE c.hasCreditFacility = true AND c.deletedAt IS NULL")
    long countByHasCreditFacilityTrueAndDeletedAtIsNull();

    @Query("SELECT c.customerType, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.customerType")
    List<Object[]> countCustomersByType();
@Query("SELECT c.status, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.status")
    List<Object[]> countCustomersByStatus();

@Query("SELECT c.creditStatus, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.creditStatus")
    List<Object[]> countCustomersByCreditStatus();

@Query("SELECT c.paymentTerms, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.paymentTerms")
    List<Object[]> countCustomersByPaymentTerms();
@Query("SELECT c.region, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.region")
    List<Object[]> countCustomersByRoute();
@Query("SELECT c.billingCity, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.billingCity")
    List<Object[]> countCustomersByCity();
@Query("SELECT c.billingState, COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.billingState")
    List<Object[]> countCustomersByProvince();
@Query("SELECT FUNCTION('MONTH', c.createdAt), COUNT(c) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY FUNCTION('MONTH', c.createdAt)")
    List<Object[]> countMonthlyRegistrations();
@Query("SELECT c.assignedSalesRepId, COUNT(c), SUM(c.currentBalance) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.assignedSalesRepId")
    List<Object[]> findCustomersBySalesRepWithTotals();

@Query("SELECT SUM(o.totalAmount) FROM SalesOrder o JOIN o.customer c WHERE c.deletedAt IS NULL")
    Double calculateTotalCustomerSales();
@Query("SELECT SUM(c.currentBalance) FROM Customer c WHERE c.deletedAt IS NULL")
    Double calculateTotalOutstandingBalance();
@Query("SELECT SUM(c.currentBalance) FROM Customer c WHERE c.currentBalance > 0 AND c.deletedAt IS NULL")
    Double calculateTotalOverdueAmount();
@Query("SELECT SUM(c.creditLimit) FROM Customer c WHERE c.deletedAt IS NULL")
    Double calculateTotalCreditLimit();
@Query("SELECT SUM(c.creditLimit - c.currentBalance) FROM Customer c WHERE c.deletedAt IS NULL")
    Double calculateTotalAvailableCredit();

@Query("SELECT AVG(o.totalAmount) FROM SalesOrder o JOIN o.customer c WHERE c.deletedAt IS NULL")
    Double calculateAverageOrderValue();
@Query("SELECT (SUM(c.currentBalance) / SUM(c.creditLimit)) * 100 FROM Customer c WHERE c.creditLimit > 0 AND c.deletedAt IS NULL")
    Double calculateCreditUtilizationRate();
@Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND c.currentBalance > (c.creditLimit * 0.8)")
    List<Customer> findByDeletedAtIsNull();
@Query("SELECT c FROM Customer c WHERE c.currentBalance > 0 AND c.deletedAt IS NULL")
    List<Customer> findCustomersWithOverdueBalance();


}
