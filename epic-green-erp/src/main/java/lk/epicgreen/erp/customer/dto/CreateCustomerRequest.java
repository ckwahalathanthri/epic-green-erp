package lk.epicgreen.erp.customer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Customer Request DTO
 * Request object for creating customers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    
    /**
     * Customer name (required)
     */
    @NotBlank(message = "Customer name is required")
    @Size(max = 200, message = "Customer name must not exceed 200 characters")
    private String customerName;
    
    /**
     * Customer type (required)
     */
    @NotBlank(message = "Customer type is required")
    @Size(max = 20, message = "Customer type must not exceed 20 characters")
    private String customerType;
    
    /**
     * Business registration
     */
    @Size(max = 100, message = "Registration number must not exceed 100 characters")
    private String registrationNumber;
    
    @Size(max = 100, message = "Tax number must not exceed 100 characters")
    private String taxNumber;
    
    /**
     * Contact information
     */
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    private String mobileNumber;
    
    @Size(max = 20, message = "Fax number must not exceed 20 characters")
    private String faxNumber;
    
    @Size(max = 200, message = "Website must not exceed 200 characters")
    private String website;
    
    /**
     * Credit terms
     */
    @DecimalMin(value = "0.0", message = "Credit limit must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid credit limit format")
    private BigDecimal creditLimit;
    
    @Min(value = 0, message = "Credit days must be non-negative")
    @Max(value = 365, message = "Credit days must not exceed 365")
    private Integer creditDays;
    
    @Size(max = 255, message = "Payment terms must not exceed 255 characters")
    private String paymentTerms;
    
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    private String currency;
    
    /**
     * Pricing
     */
    @DecimalMin(value = "0.0", message = "Trade discount must be non-negative")
    @DecimalMax(value = "100.0", message = "Trade discount cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Invalid trade discount format")
    private BigDecimal tradeDiscount;
    
    @Size(max = 20, message = "Price level must not exceed 20 characters")
    private String priceLevel;
    
    /**
     * Sales information
     */
    @Size(max = 100, message = "Sales territory must not exceed 100 characters")
    private String salesTerritory;
    
    @Size(max = 50, message = "Sales representative must not exceed 50 characters")
    private String salesRepresentative;
    
    private LocalDate customerSince;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    @Size(max = 5000, message = "Internal notes must not exceed 5000 characters")
    private String internalNotes;
    
    /**
     * Contacts
     */
    @Valid
    private List<CreateCustomerContactRequest> contacts;
    
    /**
     * Addresses
     */
    @Valid
    private List<CreateCustomerAddressRequest> addresses;
    
    /**
     * Create Customer Contact Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCustomerContactRequest {
        
        @NotBlank(message = "Contact name is required")
        @Size(max = 100, message = "Contact name must not exceed 100 characters")
        private String contactName;
        
        @Size(max = 100, message = "Designation must not exceed 100 characters")
        private String designation;
        
        @Size(max = 100, message = "Department must not exceed 100 characters")
        private String department;
        
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        private String email;
        
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        private String phoneNumber;
        
        @Size(max = 20, message = "Mobile number must not exceed 20 characters")
        private String mobileNumber;
        
        private Boolean isPrimary;
        
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
    
    /**
     * Create Customer Address Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCustomerAddressRequest {
        
        @NotBlank(message = "Address type is required")
        @Size(max = 20, message = "Address type must not exceed 20 characters")
        private String addressType;
        
        @Size(max = 100, message = "Address name must not exceed 100 characters")
        private String addressName;
        
        @NotBlank(message = "Address line 1 is required")
        @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
        private String addressLine1;
        
        @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
        private String addressLine2;
        
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        private String city;
        
        @Size(max = 100, message = "State must not exceed 100 characters")
        private String state;
        
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        private String postalCode;
        
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        private String country;
        
        @Size(max = 100, message = "Contact person must not exceed 100 characters")
        private String contactPerson;
        
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        private String phoneNumber;
        
        private Boolean isDefault;
        
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
