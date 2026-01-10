package lk.epicgreen.erp.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating/updating customer
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Customer code is required")
    @Size(max = 20, message = "Customer code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Customer code must contain only uppercase letters, numbers, hyphens and underscores")
    private String customerCode;

    @NotBlank(message = "Customer name is required")
    @Size(max = 200, message = "Customer name must not exceed 200 characters")
    private String customerName;

    @NotBlank(message = "Customer type is required")
    @Pattern(regexp = "^(WHOLESALE|RETAIL|DISTRIBUTOR|DIRECT)$", 
             message = "Customer type must be one of: WHOLESALE, RETAIL, DISTRIBUTOR, DIRECT")
    private String customerType;

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

    // Billing Address
    @Size(max = 200, message = "Billing address line 1 must not exceed 200 characters")
    private String billingAddressLine1;

    @Size(max = 200, message = "Billing address line 2 must not exceed 200 characters")
    private String billingAddressLine2;

    @Size(max = 100, message = "Billing city must not exceed 100 characters")
    private String billingCity;

    @Size(max = 100, message = "Billing state must not exceed 100 characters")
    private String billingState;

    @Size(max = 100, message = "Billing country must not exceed 100 characters")
    private String billingCountry;

    @Size(max = 20, message = "Billing postal code must not exceed 20 characters")
    private String billingPostalCode;

    // Shipping Address
    @Size(max = 200, message = "Shipping address line 1 must not exceed 200 characters")
    private String shippingAddressLine1;

    @Size(max = 200, message = "Shipping address line 2 must not exceed 200 characters")
    private String shippingAddressLine2;

    @Size(max = 100, message = "Shipping city must not exceed 100 characters")
    private String shippingCity;

    @Size(max = 100, message = "Shipping state must not exceed 100 characters")
    private String shippingState;

    @Size(max = 100, message = "Shipping country must not exceed 100 characters")
    private String shippingCountry;

    @Size(max = 20, message = "Shipping postal code must not exceed 20 characters")
    private String shippingPostalCode;

    // Sales Representative
    private Long assignedSalesRepId;

    @Size(max = 100, message = "Region must not exceed 100 characters")
    private String region;

    @Size(max = 20, message = "Route code must not exceed 20 characters")
    private String routeCode;

    private Boolean isActive;
}
