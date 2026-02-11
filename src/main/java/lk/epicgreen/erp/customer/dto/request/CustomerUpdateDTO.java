package lk.epicgreen.erp.customer.dto.request;

import lk.epicgreen.erp.customer.dto.response.CustomerAddressResponse;
import lk.epicgreen.erp.customer.dto.response.CustomerContactDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer Update Data Transfer Object
 * Used for updating existing customers with validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDTO {

    @NotNull(message = "Customer ID is required")
    private Long id;

    @NotBlank(message = "Customer code is required")
    @Size(max = 50, message = "Customer code must be less than 50 characters")
    private String customerCode;

    @NotBlank(message = "Customer name is required")
    @Size(max = 200, message = "Customer name must be less than 200 characters")
    private String customerName;

    @Size(max = 200, message = "Local name must be less than 200 characters")
    private String customerNameLocal;

    private Long customerTypeId;

    private Long customerCategoryId;

    @Size(max = 50, message = "Tax ID must be less than 50 characters")
    private String taxId;

    @Size(max = 50, message = "Registration number must be less than 50 characters")
    private String registrationNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Size(max = 20, message = "Phone must be less than 20 characters")
    private String phone;

    @Size(max = 20, message = "Mobile must be less than 20 characters")
    private String mobile;

    @Size(max = 20, message = "Fax must be less than 20 characters")
    private String fax;

    @Size(max = 100, message = "Website must be less than 100 characters")
    private String website;

    @DecimalMin(value = "0.0", message = "Credit limit must be positive")
    private BigDecimal creditLimit;

    @Min(value = 0, message = "Payment terms days must be positive")
    private Integer paymentTermsDays;

    @DecimalMin(value = "0.0", message = "Discount percentage must be positive")
    @DecimalMax(value = "100.0", message = "Discount percentage must be less than 100")
    private BigDecimal discountPercentage;

    private Boolean isActive;

    private String notes;

    private List<CustomerContactDTO> contacts = new ArrayList<>();

    private List<CustomerAddressResponse> addresses = new ArrayList<>();
}
