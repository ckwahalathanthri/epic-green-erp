package lk.epicgreen.erp.supplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating supplier contact
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContactRequest {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Contact name is required")
    @Size(max = 100, message = "Contact name must not exceed 100 characters")
    private String contactName;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid phone format")
    private String phone;

    @Size(max = 20, message = "Mobile must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid mobile format")
    private String mobile;

    private Boolean isPrimary;
}
