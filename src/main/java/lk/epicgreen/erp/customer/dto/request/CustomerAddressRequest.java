package lk.epicgreen.erp.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating customer address
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Address type is required")
    @Pattern(regexp = "^(BILLING|SHIPPING|BOTH)$", 
             message = "Address type must be one of: BILLING, SHIPPING, BOTH")
    private String addressType;

    @Size(max = 100, message = "Address name must not exceed 100 characters")
    private String addressName;

    @NotBlank(message = "Address line 1 is required")
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

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid phone format")
    private String contactPhone;

    private Boolean isDefault;
}
