package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Supplier Request DTO
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    
    private String supplierCode;
    
    @NotBlank(message = "Supplier name is required")
    @Size(max = 200, message = "Supplier name cannot exceed 200 characters")
    private String supplierName;
    
    @Size(max = 200, message = "Company name cannot exceed 200 characters")
    private String companyName;
    
    @Size(max = 200, message = "Contact person cannot exceed 200 characters")
    private String contactPerson;
    
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;
    
    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;
    
    @Size(max = 20, message = "Mobile cannot exceed 20 characters")
    private String mobile;
    
    @Size(max = 20, message = "Fax cannot exceed 20 characters")
    private String fax;
    
    @Size(max = 200, message = "Website cannot exceed 200 characters")
    private String website;
    
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;
    
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "State/Province cannot exceed 100 characters")
    private String stateProvince;
    
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
    
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;
    
    @Size(max = 50, message = "Tax number cannot exceed 50 characters")
    private String taxNumber;
    
    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;
    
    @Size(max = 50, message = "Supplier type cannot exceed 50 characters")
    private String supplierType;
    
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
    
    @DecimalMin(value = "0.0", message = "Credit limit must be positive")
    private BigDecimal creditLimit;
    
    @Min(value = 0, message = "Credit days must be positive")
    private Integer creditDays;
    
    @Size(max = 500, message = "Payment terms cannot exceed 500 characters")
    private String paymentTerms;
    
    @Size(max = 500, message = "Delivery terms cannot exceed 500 characters")
    private String deliveryTerms;
    
    @Size(max = 200, message = "Bank name cannot exceed 200 characters")
    private String bankName;
    
    @Size(max = 50, message = "Bank account number cannot exceed 50 characters")
    private String bankAccountNo;
    
    @Size(max = 200, message = "Bank branch cannot exceed 200 characters")
    private String bankBranch;
    
    @Size(max = 50, message = "Bank SWIFT code cannot exceed 50 characters")
    private String bankSwiftCode;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}
