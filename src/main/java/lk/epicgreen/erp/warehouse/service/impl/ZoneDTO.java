package lk.epicgreen.erp.warehouse.service.impl;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
}
