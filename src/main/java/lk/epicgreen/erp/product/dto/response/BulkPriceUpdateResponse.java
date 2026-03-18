package lk.epicgreen.erp.product.dto.response;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;
import java.util.List;

@Data
public class BulkPriceUpdateResponse {
    @NotNull
    private List<Long> productIds;
    
    private String priceType;
    
    @NotNull
    private String updateType; // PERCENTAGE, FIXED_AMOUNT, SET_PRICE
    
    @NotNull
    private BigDecimal value;
    
    private String reason;
}
