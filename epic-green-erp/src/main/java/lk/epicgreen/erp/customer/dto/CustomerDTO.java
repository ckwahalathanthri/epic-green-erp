package lk.epicgreen.erp.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Customer DTO
 * Data transfer object for Customer entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    
    private Long id;
    
    private String customerCode;
    
    private String customerName;
    
    private String customerType;
    
    private String registrationNumber;
    
    private String taxNumber;
    
    /**
     * Contact information
     */
    private String email;
    
    private String phoneNumber;
    
    private String mobileNumber;
    
    private String faxNumber;
    
    private String website;
    
    /**
     * Credit terms
     */
    private BigDecimal creditLimit;
    
    private Integer creditDays;
    
    private String paymentTerms;
    
    private String currency;
    
    private String creditStatus;
    
    private BigDecimal outstandingBalance;
    
    private BigDecimal availableCredit;
    
    private BigDecimal creditUtilization;
    
    private Boolean isCreditLimitExceeded;
    
    /**
     * Pricing
     */
    private BigDecimal tradeDiscount;
    
    private String priceLevel;
    
    /**
     * Sales information
     */
    private String salesTerritory;
    
    private String salesRepresentative;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate customerSince;
    
    /**
     * Status
     */
    private String status;
    
    private Boolean isActive;
    
    /**
     * Loyalty
     */
    private Integer loyaltyPoints;
    
    /**
     * Notes
     */
    private String notes;
    
    private String internalNotes;
    
    /**
     * Related data
     */
    private List<CustomerContactDTO> contacts;
    
    private List<CustomerAddressDTO> addresses;
    
    private CustomerContactDTO primaryContact;
    
    private CustomerAddressDTO billingAddress;
    
    private CustomerAddressDTO shippingAddress;
    
    /**
     * Statistics
     */
    private Integer totalOrders;
    
    private BigDecimal totalSales;
    
    private BigDecimal totalPayments;
    
    private Integer overdueInvoices;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    /**
     * Customer Contact DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomerContactDTO {
        
        private Long id;
        
        private String contactName;
        
        private String designation;
        
        private String department;
        
        private String email;
        
        private String phoneNumber;
        
        private String mobileNumber;
        
        private Boolean isPrimary;
        
        private Boolean isActive;
        
        private String notes;
    }
    
    /**
     * Customer Address DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomerAddressDTO {
        
        private Long id;
        
        private String addressType;
        
        private String addressName;
        
        private String addressLine1;
        
        private String addressLine2;
        
        private String city;
        
        private String state;
        
        private String postalCode;
        
        private String country;
        
        private String contactPerson;
        
        private String phoneNumber;
        
        private Boolean isDefault;
        
        private Boolean isActive;
        
        private String notes;
        
        private String fullAddress;
        
        private String singleLineAddress;
    }
}
