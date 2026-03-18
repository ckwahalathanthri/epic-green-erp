package lk.epicgreen.erp.product.dto.response;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryTreeResponse {
    private Long id;
    private String categoryName;
    private String categoryCode;
    private String description;
    private Long parentCategoryId;
    private Integer displayOrder;
    private Boolean isActive;
    private Integer productCount;
    private List<CategoryTreeResponse> children = new ArrayList<>();
}
