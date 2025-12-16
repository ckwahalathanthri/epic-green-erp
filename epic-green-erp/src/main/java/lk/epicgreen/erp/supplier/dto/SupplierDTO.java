package lk.epicgreen.erp.supplier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Supplier DTO
 * Data transfer object for Supplier entity
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
public class SupplierDTO {
    
    private Long id;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String supplierType;
    
    private String registrationNumber;
    
    private String vatNumber;
    
    private String email;
    
    private String phoneNumber;
    
    private String mobileNumber;
    
    private String faxNumber;
    
    private String website;
    
    /**
     * Address fields
     */
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String country;
    
    private String fullAddress;
    
    /**
     * Financial fields
     */
    private BigDecimal creditLimit;
    
    private BigDecimal currentBalance;
    
    private BigDecimal availableCredit;
    
    private Integer paymentTermsDays;
    
    private String paymentTerms;
    
    private String currency;
    
    /**
     * Bank details
     */
    private String bankName;
    
    private String bankAccountNumber;
    
    private String bankAccountHolder;
    
    private String bankBranch;
    
    private String bankSwiftCode;
    
    /**
     * Rating and preferences
     */
    private Integer rating;
    
    private Boolean isPreferred;
    
    private Integer leadTimeDays;
    
    private BigDecimal minimumOrderValue;
    
    /**
     * Status fields
     */
    private String status;
    
    private Boolean isApproved;
    
    private String approvedBy;
    
    /**
     * Additional fields
     */
    private String termsAndConditions;
    
    private String notes;
    
    /**
     * Related data
     */
    private List<SupplierContactDTO> contacts;
    
    /**
     * Statistics
     */
    private Long totalPurchaseOrders;
    
    private BigDecimal totalPurchaseAmount;
    
    private BigDecimal totalPaymentAmount;
    
    private Long pendingPaymentCount;
    
    /**
     * Computed properties
     */
    private Boolean isActive;
    
    private Boolean isBlocked;
    
    private Boolean isCreditLimitExceeded;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
