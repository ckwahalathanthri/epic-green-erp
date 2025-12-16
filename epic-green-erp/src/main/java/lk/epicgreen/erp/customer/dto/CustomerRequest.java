package lk.epicgreen.erp.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer Request DTO
 * DTO for customer operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    
    @NotBlank(message = "Customer code is required")
    private String customerCode;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    private String businessName; // Alias for customerName
    
    private String customerType;
    
    @NotBlank(message = "Contact person is required")
    private String contactPerson;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    
    private String mobile;
    
    private String fax;
    
    private String website;
    
    private String address;
    
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String stateProvince;
    
    private String province; // Alias for stateProvince
    
    private String postalCode;
    
    private String country;
    
    private String taxNumber;
    
    private String registrationNumber;
    
    private String paymentTerms;
    
    private Integer creditDays;
    
    private Double creditLimit;
    
    private String salesRepUserId;
    
    private String salesRepId; // Alias
    
    private String salesRepName;
    
    private String territory;
    
    private String route;
    
    private String customerGroup;
    
    private String priceList;
    
    private Double discountPercentage;
    
    private String notes;
}
