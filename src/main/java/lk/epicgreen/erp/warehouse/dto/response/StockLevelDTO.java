package lk.epicgreen.erp.warehouse.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLevelDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
}
