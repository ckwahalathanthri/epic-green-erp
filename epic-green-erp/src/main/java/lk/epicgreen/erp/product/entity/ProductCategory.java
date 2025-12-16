package lk.epicgreen.erp.product.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * ProductCategory entity
 * Represents product categories for organizing finished goods
 * (e.g., Ground Spices, Whole Spices, Blended Spices, Spice Mixes)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "product_categories", indexes = {
    @Index(name = "idx_category_code", columnList = "category_code"),
    @Index(name = "idx_category_name", columnList = "category_name"),
    @Index(name = "idx_category_parent", columnList = "parent_id")
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
    @Column(name = "category_code", nullable = false, unique = true, length = 20)
    private String categoryCode;
    
    /**
     * Category name
     */
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;
    
    /**
     * Category description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Parent category (for hierarchical categories)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    private ProductCategory parent;
    
    /**
     * Child categories
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ProductCategory> children = new HashSet<>();
    
    /**
     * Products in this category
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Product> products = new HashSet<>();
    
    /**
     * Category level (0 = root, 1 = child, 2 = grandchild, etc.)
     */
    @Column(name = "category_level")
    private Integer categoryLevel;
    
    /**
     * Display order
     */
    @Column(name = "display_order")
    private Integer displayOrder;
    
    /**
     * Image URL
     */
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    /**
     * Is active flag
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    /**
     * Parent category ID (denormalized for performance)
     */
    @Column(name = "parent_category_id_direct")
    private Long parentCategoryId;
    
    /**
     * Is leaf category (has no children)
     */
    @Column(name = "is_leaf")
    private Boolean isLeaf;
    
    /**
     * Level in hierarchy (0 = root, 1 = first level, etc.)
     * Alias for categoryLevel
     */
    @Transient
    private Integer level;
    
    /**
     * Category path (e.g., "/1/5/12" for hierarchy navigation)
     */
    @Column(name = "category_path", length = 500)
    private String categoryPath;
    
    /**
     * Status (for consistency with other entities)
     */
    @Transient
    private String status;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets category ID (alias for getId() for consistency with DTOs)
     */
    @Transient
    public Long getCategoryId() {
        return this.getId();
    }
    
    /**
     * Gets parent category ID
     */
    @Transient
    public Long getParentCategoryId() {
        return parent != null ? parent.getId() : parentCategoryId;
    }
    
    /**
     * Sets parent category ID (for denormalized access)
     */
    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
    
    /**
     * Gets level (alias for categoryLevel)
     */
    public Integer getLevel() {
        return this.categoryLevel;
    }
    
    /**
     * Sets level (alias for categoryLevel)
     */
    public void setLevel(Integer level) {
        this.categoryLevel = level;
        this.level = level;
    }
    
    /**
     * Gets full category path (e.g., "Spices > Ground Spices > Cinnamon")
     */
    @Transient
    public String getFullPath() {
        if (parent == null) {
            return categoryName;
        }
        return parent.getFullPath() + " > " + categoryName;
    }
    
    /**
     * Checks if this is a root category
     */
    @Transient
    public boolean isRootCategory() {
        return parent == null;
    }
    
    /**
     * Checks if this is a leaf category (has no children)
     */
    @Transient
    public boolean isLeafCategory() {
        return children == null || children.isEmpty();
    }
    
    /**
     * Adds a child category
     */
    public void addChild(ProductCategory child) {
        child.setParent(this);
        child.setCategoryLevel(this.categoryLevel != null ? this.categoryLevel + 1 : 1);
        children.add(child);
    }
    
    /**
     * Removes a child category
     */
    public void removeChild(ProductCategory child) {
        children.remove(child);
        child.setParent(null);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (categoryLevel == null) {
            categoryLevel = parent != null ? parent.getCategoryLevel() + 1 : 0;
        }
        if (isLeaf == null) {
            isLeaf = true; // Assume leaf unless children added
        }
        if (level == null) {
            level = categoryLevel;
        }
        // Sync parent category ID
        if (parent != null && parentCategoryId == null) {
            parentCategoryId = parent.getId();
        }
        // Build category path if not set
        if (categoryPath == null) {
            if (parent != null && parent.getCategoryPath() != null) {
                categoryPath = parent.getCategoryPath() + "/" + getId();
            } else if (getId() != null) {
                categoryPath = "/" + getId();
            }
        }
        // Sync status from isActive
        status = isActive ? "ACTIVE" : "INACTIVE";
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
