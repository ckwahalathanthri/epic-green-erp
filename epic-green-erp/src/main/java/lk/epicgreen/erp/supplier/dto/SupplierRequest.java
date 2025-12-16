package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplier Request DTO
 * DTO for supplier operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    
    @NotBlank(message = "Supplier code is required")
    private String supplierCode;
    
    @NotBlank(message = "Supplier name is required")
    private String supplierName;
    
    private String supplierType;
    
    @NotBlank(message = "Contact person is required")
    private String contactPerson;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    
    private String mobile;
    
    private String fax;
    
    private String website;
    
    private String address;
    
    private String city;
    
    private String stateProvince;
    
    private String postalCode;
    
    private String country;
    
    private String taxNumber;
    
    private String registrationNumber;
    
    private String paymentTerms;
    
    private Integer creditDays;
    
    private Double creditLimit;
    
    private String bankName;
    
    private String bankAccountNumber;
    
    private String bankAccountName;
    
    private String bankBranch;
    
    private String notes;
}
