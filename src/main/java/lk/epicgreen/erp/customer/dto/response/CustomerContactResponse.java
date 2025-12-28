package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for customer contact response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContactResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String contactName;
    private String designation;
    private String email;
    private String phone;
    private String mobile;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
