package lk.epicgreen.erp.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductCategory entity
 * Represents hierarchical product categories
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "product_categories", indexes = {
    @Index(name = "idx_category_code", columnList = "category_code"),
    @Index(name = "idx_parent_category_id", columnList = "parent_category_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Category code (unique identifier)
     */
    @NotBlank(message = "Category code is required")
    @Size(max = 20)
    @Column(name = "category_code", nullable = false, unique = true, length = 20)
    private String categoryCode;
    
    /**
     * Category name
     */
    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;
    
    /**
     * Parent category (for hierarchical structure)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", foreignKey = @ForeignKey(name = "fk_product_category_parent"))
    private ProductCategory parentCategory;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * Transient field for children categories (not persisted)
     */
    @Transient
    private List<ProductCategory> children;
    
    /**
     * Child categories
     */
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductCategory> childCategories = new ArrayList<>();
    
    /**
     * Products in this category
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> products = new ArrayList<>();
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if this is a root category (no parent)
     */
    @Transient
    public boolean isRootCategory() {
        return parentCategory == null;
    }
    
    /**
     * Check if this is a leaf category (no children)
     */
    @Transient
    public boolean isLeafCategory() {
        return childCategories == null || childCategories.isEmpty();
    }
    
    /**
     * Check if category has products
     */
    @Transient
    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }
    
    /**
     * Check if category can be deleted
     */
    @Transient
    public boolean canDelete() {
        return !hasProducts() && isLeafCategory();
    }
    
    /**
     * Get full category path (e.g., "Spices > Dried > Chili")
     */
    @Transient
    public String getFullPath() {
        if (parentCategory == null) {
            return categoryName;
        }
        return parentCategory.getFullPath() + " > " + categoryName;
    }
    
    /**
     * Get category level (0 for root, 1 for first level, etc.)
     */
    @Transient
    public int getLevel() {
        if (parentCategory == null) {
            return 0;
        }
        return parentCategory.getLevel() + 1;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCategory)) return false;
        ProductCategory that = (ProductCategory) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
