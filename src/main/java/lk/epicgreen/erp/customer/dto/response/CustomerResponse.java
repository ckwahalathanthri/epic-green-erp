package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for customer response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String contactPerson;
    private String email;
    private String phone;
    private String mobile;
    private String taxId;
    private String paymentTerms;
    private BigDecimal creditLimit;
    private Integer creditDays;
    private BigDecimal currentBalance;
    
    // Billing Address
    private String billingAddressLine1;
    private String billingAddressLine2;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingPostalCode;
    
    // Shipping Address
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingPostalCode;
    
    // Sales Representative
    private Long assignedSalesRepId;
    private String assignedSalesRepName;
    
    private String region;
    private String routeCode;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
