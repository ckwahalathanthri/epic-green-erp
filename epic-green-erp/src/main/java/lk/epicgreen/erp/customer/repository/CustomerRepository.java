package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Customer Repository
 * Repository for customer data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find customer by customer code
     */
    Optional<Customer> findByCustomerCode(String customerCode);
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customer by phone
     */
    Optional<Customer> findByPhone(String phone);
    
    /**
     * Find customer by tax number
     */
    Optional<Customer> findByTaxNumber(String taxNumber);
    
    /**
     * Check if customer exists by customer code
     */
    boolean existsByCustomerCode(String customerCode);
    
    /**
     * Check if customer exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer exists by phone
     */
    boolean existsByPhone(String phone);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find customers by customer type
     */
    List<Customer> findByCustomerType(String customerType);
    
    /**
     * Find customers by customer type with pagination
     */
    Page<Customer> findByCustomerType(String customerType, Pageable pageable);
    
    /**
     * Find customers by status
     */
    List<Customer> findByStatus(String status);
    
    /**
     * Find customers by status with pagination
     */
    Page<Customer> findByStatus(String status, Pageable pageable);
    
    /**
     * Find customers by credit status
     */
    List<Customer> findByCreditStatus(String creditStatus);
    
    /**
     * Find customers by payment terms
     */
    List<Customer> findByPaymentTerms(String paymentTerms);
    
    /**
     * Find customers by sales representative
     */
    List<Customer> findBySalesRepId(Long salesRepId);
    
    /**
     * Find customers by sales representative with pagination
     */
    Page<Customer> findBySalesRepId(Long salesRepId, Pageable pageable);
    
    /**
     * Find customers by route
     */
    List<Customer> findByRoute(String route);
    
    /**
     * Find customers by city
     */
    List<Customer> findByCity(String city);
    
    /**
     * Find customers by province
     */
    List<Customer> findByProvince(String province);
    
    /**
     * Find customers by country
     */
    List<Customer> findByCountry(String country);
    
    /**
     * Find customers by is active
     */
    List<Customer> findByIsActive(Boolean isActive);
    
    /**
     * Find customers by is active with pagination
     */
    Page<Customer> findByIsActive(Boolean isActive, Pageable pageable);
    
    /**
     * Find customers by is verified
     */
    List<Customer> findByIsVerified(Boolean isVerified);
    
    /**
     * Find customers by is blacklisted
     */
    List<Customer> findByIsBlacklisted(Boolean isBlacklisted);
    
    /**
     * Find customers by has credit facility
     */
    List<Customer> findByHasCreditFacility(Boolean hasCreditFacility);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find customers by created at between dates
     */
    List<Customer> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find customers by registration date between dates
     */
    List<Customer> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find customers by last order date between dates
     */
    List<Customer> findByLastOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find customers by customer type and status
     */
    List<Customer> findByCustomerTypeAndStatus(String customerType, String status);
    
    /**
     * Find customers by customer type and status with pagination
     */
    Page<Customer> findByCustomerTypeAndStatus(String customerType, String status, Pageable pageable);
    
    /**
     * Find customers by status and is active
     */
    List<Customer> findByStatusAndIsActive(String status, Boolean isActive);
    
    /**
     * Find customers by sales rep and status
     */
    List<Customer> findBySalesRepIdAndStatus(Long salesRepId, String status);
    
    /**
     * Find customers by route and is active
     */
    List<Customer> findByRouteAndIsActive(String route, Boolean isActive);
    
    /**
     * Find customers by city and province
     */
    List<Customer> findByCityAndProvince(String city, String province);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search customers
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.businessName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active customers
     */
    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.status = 'ACTIVE' " +
           "ORDER BY c.customerName ASC")
    List<Customer> findActiveCustomers();
    
    /**
     * Find active customers with pagination
     */
    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.status = 'ACTIVE' " +
           "ORDER BY c.customerName ASC")
    Page<Customer> findActiveCustomers(Pageable pageable);
    
    /**
     * Find inactive customers
     */
    @Query("SELECT c FROM Customer c WHERE c.isActive = false OR c.status = 'INACTIVE' " +
           "ORDER BY c.customerName ASC")
    List<Customer> findInactiveCustomers();
    
    /**
     * Find pending customers
     */
    @Query("SELECT c FROM Customer c WHERE c.status = 'PENDING' " +
           "ORDER BY c.createdAt ASC")
    List<Customer> findPendingCustomers();
    
    /**
     * Find suspended customers
     */
    @Query("SELECT c FROM Customer c WHERE c.status = 'SUSPENDED' " +
           "ORDER BY c.customerName ASC")
    List<Customer> findSuspendedCustomers();
    
    /**
     * Find blacklisted customers
     */
    @Query("SELECT c FROM Customer c WHERE c.isBlacklisted = true " +
           "ORDER BY c.customerName ASC")
    List<Customer> findBlacklistedCustomers();
    
    /**
     * Find customers with credit facility
     */
    @Query("SELECT c FROM Customer c WHERE c.hasCreditFacility = true " +
           "AND c.creditLimit > 0 " +
           "ORDER BY c.creditLimit DESC")
    List<Customer> findCustomersWithCreditFacility();
    
    /**
     * Find customers with credit limit exceeded
     */
    @Query("SELECT c FROM Customer c WHERE c.hasCreditFacility = true " +
           "AND c.currentOutstanding > c.creditLimit " +
           "AND c.creditStatus = 'EXCEEDED' " +
           "ORDER BY c.currentOutstanding DESC")
    List<Customer> findCustomersWithCreditExceeded();
    
    /**
     * Find customers with credit limit warning
     */
    @Query("SELECT c FROM Customer c WHERE c.hasCreditFacility = true " +
           "AND c.currentOutstanding >= (c.creditLimit * 0.8) " +
           "AND c.currentOutstanding <= c.creditLimit " +
           "ORDER BY c.currentOutstanding DESC")
    List<Customer> findCustomersWithCreditWarning();
    
    /**
     * Find customers with outstanding balance
     */
    @Query("SELECT c FROM Customer c WHERE c.currentOutstanding > 0 " +
           "ORDER BY c.currentOutstanding DESC")
    List<Customer> findCustomersWithOutstandingBalance();
    
    /**
     * Find customers with overdue balance
     */
    @Query("SELECT c FROM Customer c WHERE c.overdueAmount > 0 " +
           "ORDER BY c.overdueAmount DESC")
    List<Customer> findCustomersWithOverdueBalance();
    
    /**
     * Find unverified customers
     */
    @Query("SELECT c FROM Customer c WHERE c.isVerified = false " +
           "ORDER BY c.createdAt ASC")
    List<Customer> findUnverifiedCustomers();
    
    /**
     * Find customers without recent orders
     */
    @Query("SELECT c FROM Customer c WHERE c.lastOrderDate IS NULL OR " +
           "c.lastOrderDate < :thresholdDate " +
           "ORDER BY c.lastOrderDate ASC NULLS FIRST")
    List<Customer> findCustomersWithoutRecentOrders(@Param("thresholdDate") LocalDate thresholdDate);
    
    /**
     * Find top customers by total sales
     */
    @Query("SELECT c FROM Customer c WHERE c.totalSalesAmount > 0 " +
           "ORDER BY c.totalSalesAmount DESC")
    List<Customer> findTopCustomersByTotalSales(Pageable pageable);
    
    /**
     * Find recent customers
     */
    @Query("SELECT c FROM Customer c ORDER BY c.createdAt DESC")
    List<Customer> findRecentCustomers(Pageable pageable);
    
    /**
     * Find customers by route and active status
     */
    @Query("SELECT c FROM Customer c WHERE c.route = :route AND c.isActive = true " +
           "ORDER BY c.customerName ASC")
    List<Customer> findActiveCustomersByRoute(@Param("route") String route);
    
    /**
     * Find customers by sales rep and active status
     */
    @Query("SELECT c FROM Customer c WHERE c.salesRepId = :salesRepId AND c.isActive = true " +
           "ORDER BY c.customerName ASC")
    List<Customer> findActiveCustomersBySalesRep(@Param("salesRepId") Long salesRepId);
    
    /**
     * Find customers requiring action
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "(c.isVerified = false AND c.status = 'PENDING') OR " +
           "(c.hasCreditFacility = true AND c.currentOutstanding > c.creditLimit) OR " +
           "(c.overdueAmount > 0) " +
           "ORDER BY c.currentOutstanding DESC")
    List<Customer> findCustomersRequiringAction();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count customers by sales rep
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.salesRepId = :salesRepId")
    Long countBySalesRepId(@Param("salesRepId") Long salesRepId);
    
    /**
     * Count customers by customer type
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerType = :customerType")
    Long countByCustomerType(@Param("customerType") String customerType);
    
    /**
     * Count customers by status
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count active customers
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.isActive = true AND c.status = 'ACTIVE'")
    Long countActiveCustomers();
    
    /**
     * Count customers with credit facility
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.hasCreditFacility = true")
    Long countCustomersWithCreditFacility();
    
    /**
     * Count customers with credit exceeded
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.hasCreditFacility = true " +
           "AND c.currentOutstanding > c.creditLimit")
    Long countCustomersWithCreditExceeded();
    
    /**
     * Count customers with outstanding balance
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.currentOutstanding > 0")
    Long countCustomersWithOutstandingBalance();
    
    /**
     * Count unverified customers
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.isVerified = false")
    Long countUnverifiedCustomers();
    
    /**
     * Get customer type distribution
     */
    @Query("SELECT c.customerType, COUNT(c) as customerCount FROM Customer c " +
           "GROUP BY c.customerType ORDER BY customerCount DESC")
    List<Object[]> getCustomerTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT c.status, COUNT(c) as customerCount FROM Customer c " +
           "GROUP BY c.status ORDER BY customerCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get credit status distribution
     */
    @Query("SELECT c.creditStatus, COUNT(c) as customerCount FROM Customer c " +
           "WHERE c.hasCreditFacility = true " +
           "GROUP BY c.creditStatus ORDER BY customerCount DESC")
    List<Object[]> getCreditStatusDistribution();
    
    /**
     * Get payment terms distribution
     */
    @Query("SELECT c.paymentTerms, COUNT(c) as customerCount FROM Customer c " +
           "GROUP BY c.paymentTerms ORDER BY customerCount DESC")
    List<Object[]> getPaymentTermsDistribution();
    
    /**
     * Get route distribution
     */
    @Query("SELECT c.route, COUNT(c) as customerCount FROM Customer c " +
           "WHERE c.route IS NOT NULL " +
           "GROUP BY c.route ORDER BY customerCount DESC")
    List<Object[]> getRouteDistribution();
    
    /**
     * Get city distribution
     */
    @Query("SELECT c.city, COUNT(c) as customerCount FROM Customer c " +
           "WHERE c.city IS NOT NULL " +
           "GROUP BY c.city ORDER BY customerCount DESC")
    List<Object[]> getCityDistribution();
    
    /**
     * Get province distribution
     */
    @Query("SELECT c.province, COUNT(c) as customerCount FROM Customer c " +
           "WHERE c.province IS NOT NULL " +
           "GROUP BY c.province ORDER BY customerCount DESC")
    List<Object[]> getProvinceDistribution();
    
    /**
     * Get monthly registration count
     */
    @Query("SELECT YEAR(c.registrationDate) as year, MONTH(c.registrationDate) as month, " +
           "COUNT(c) as customerCount FROM Customer c " +
           "WHERE c.registrationDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(c.registrationDate), MONTH(c.registrationDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyRegistrationCount(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Get total sales amount
     */
    @Query("SELECT SUM(c.totalSalesAmount) FROM Customer c")
    Double getTotalSalesAmount();
    
    /**
     * Get total outstanding amount
     */
    @Query("SELECT SUM(c.currentOutstanding) FROM Customer c WHERE c.currentOutstanding > 0")
    Double getTotalOutstandingAmount();
    
    /**
     * Get total overdue amount
     */
    @Query("SELECT SUM(c.overdueAmount) FROM Customer c WHERE c.overdueAmount > 0")
    Double getTotalOverdueAmount();
    
    /**
     * Get total credit limit
     */
    @Query("SELECT SUM(c.creditLimit) FROM Customer c WHERE c.hasCreditFacility = true")
    Double getTotalCreditLimit();
    
    /**
     * Get total available credit
     */
    @Query("SELECT SUM(c.availableCredit) FROM Customer c WHERE c.hasCreditFacility = true")
    Double getTotalAvailableCredit();
    
    /**
     * Get average order value
     */
    @Query("SELECT AVG(c.averageOrderValue) FROM Customer c WHERE c.averageOrderValue > 0")
    Double getAverageOrderValue();
    
    /**
     * Get customers by sales rep with totals
     */
    @Query("SELECT c.salesRepId, c.salesRepName, COUNT(c) as customerCount, " +
           "SUM(c.totalSalesAmount) as totalSales FROM Customer c " +
           "WHERE c.salesRepId IS NOT NULL " +
           "GROUP BY c.salesRepId, c.salesRepName ORDER BY totalSales DESC")
    List<Object[]> getCustomersBySalesRepWithTotals();
    
    /**
     * Get credit utilization rate
     */
    @Query("SELECT " +
           "(SELECT SUM(c.currentOutstanding) FROM Customer c WHERE c.hasCreditFacility = true) * 100.0 / " +
           "(SELECT SUM(c.creditLimit) FROM Customer c WHERE c.hasCreditFacility = true) " +
           "FROM Customer c WHERE c.hasCreditFacility = true")
    Double getCreditUtilizationRate();
    
    /**
     * Find customers by tags
     */
    @Query("SELECT c FROM Customer c WHERE c.tags LIKE CONCAT('%', :tag, '%')")
    List<Customer> findByTag(@Param("tag") String tag);
}
