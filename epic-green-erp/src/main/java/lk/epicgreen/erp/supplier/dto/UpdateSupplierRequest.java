package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;

/**
 * Update Supplier Request DTO
 * Request object for updating an existing supplier
 * All fields are optional
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSupplierRequest {
    
    /**
     * Supplier name
     */
    @Size(min = 2, max = 200, message = ValidationMessages.NAME_SIZE)
    private String supplierName;
    
    /**
     * Supplier type
     */
    private String supplierType;
    
    /**
     * Business registration number
     */
    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNumber;
    
    /**
     * VAT/Tax number
     */
    @Size(max = 50, message = "VAT number must not exceed 50 characters")
    private String vatNumber;
    
    /**
     * Email address
     */
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Size(max = 100, message = ValidationMessages.EMAIL_MAX_LENGTH)
    private String email;
    
    /**
     * Phone number
     */
    @Pattern(regexp = AppConstants.PHONE_PATTERN, message = ValidationMessages.PHONE_INVALID)
    private String phoneNumber;
    
    /**
     * Mobile number
     */
    @Pattern(regexp = AppConstants.PHONE_PATTERN, message = ValidationMessages.PHONE_INVALID)
    private String mobileNumber;
    
    /**
     * Fax number
     */
    @Size(max = 20, message = "Fax number must not exceed 20 characters")
    private String faxNumber;
    
    /**
     * Website URL
     */
    @Size(max = 255, message = ValidationMessages.URL_MAX_LENGTH)
    private String website;
    
    /**
     * Address line 1
     */
    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;
    
    /**
     * City
     */
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    /**
     * State/Province
     */
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;
    
    /**
     * Postal code
     */
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;
    
    /**
     * Country
     */
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;
    
    /**
     * Credit limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid credit limit format")
    private BigDecimal creditLimit;
    
    /**
     * Payment terms in days
     */
    @Min(value = 0, message = "Payment terms must be non-negative")
    @Max(value = 365, message = "Payment terms cannot exceed 365 days")
    private Integer paymentTermsDays;
    
    /**
     * Payment terms description
     */
    @Size(max = 255, message = "Payment terms must not exceed 255 characters")
    private String paymentTerms;
    
    /**
     * Currency code
     */
    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String currency;
    
    /**
     * Supplier rating (1-5)
     */
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
    
    /**
     * Bank name
     */
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;
    
    /**
     * Bank account number
     */
    @Size(max = 50, message = "Bank account number must not exceed 50 characters")
    private String bankAccountNumber;
    
    /**
     * Bank account holder name
     */
    @Size(max = 100, message = "Bank account holder name must not exceed 100 characters")
    private String bankAccountHolder;
    
    /**
     * Bank branch
     */
    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;
    
    /**
     * Bank SWIFT/BIC code
     */
    @Size(max = 20, message = "Bank SWIFT code must not exceed 20 characters")
    private String bankSwiftCode;
    
    /**
     * Status (ACTIVE, INACTIVE, BLOCKED, PENDING)
     */
    private String status;
    
    /**
     * Is preferred supplier
     */
    private Boolean isPreferred;
    
    /**
     * Lead time in days
     */
    @Min(value = 0, message = "Lead time must be non-negative")
    private Integer leadTimeDays;
    
    /**
     * Minimum order value
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum order value must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid minimum order value format")
    private BigDecimal minimumOrderValue;
    
    /**
     * Terms and conditions
     */
    @Size(max = 5000, message = "Terms and conditions must not exceed 5000 characters")
    private String termsAndConditions;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
}
