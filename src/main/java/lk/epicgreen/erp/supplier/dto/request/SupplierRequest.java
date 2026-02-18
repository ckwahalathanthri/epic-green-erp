package lk.epicgreen.erp.supplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating/updating supplier
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    @Size(max = 20, message = "Supplier code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Supplier code must contain only uppercase letters, numbers, hyphens and underscores")
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    @Size(max = 200, message = "Supplier name must not exceed 200 characters")
    private String supplierName;

//    @NotBlank(message = "Supplier type is required")
//    @Pattern(regexp = "^(RAW_MATERIAL|PACKAGING|SERVICES|OTHER)$",
//             message = "Supplier type must be one of: RAW_MATERIAL, PACKAGING, SERVICES, OTHER")
    private String supplierType;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid phone format")
    private String phone;

    @Size(max = 20, message = "Mobile must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid mobile format")
    private String mobile;

    @Size(max = 50, message = "Tax ID must not exceed 50 characters")
    private String taxId;

    @Size(max = 50, message = "Payment terms must not exceed 50 characters")
    private String paymentTerms;

    @DecimalMin(value = "0.0", message = "Credit limit must be >= 0")
    private BigDecimal creditLimit;

    @Min(value = 0, message = "Credit days must be >= 0")
    private Integer creditDays;

    @Size(max = 200, message = "Address line 1 must not exceed 200 characters")
    private String addressLine1;

    @Size(max = 200, message = "Address line 2 must not exceed 200 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Size(max = 50, message = "Bank account number must not exceed 50 characters")
    private String bankAccountNumber;

    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;

    @DecimalMin(value = "0.0", message = "Rating must be >= 0")
    @DecimalMax(value = "5.0", message = "Rating must be <= 5.0")
    private BigDecimal rating;

    private Boolean isActive;
}
