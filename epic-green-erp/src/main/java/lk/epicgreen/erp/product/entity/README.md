# Product Module - Epic Green ERP

This directory contains **entities and DTOs** for product and category management in the Epic Green ERP system.

## 📦 Contents

### Entities (product/entity) - 2 Files
1. **Product.java** - Finished spice products
2. **ProductCategory.java** - Product categories (hierarchical)

### DTOs (product/dto) - 4 Files
1. **ProductDTO.java** - Product data transfer object
2. **ProductCategoryDTO.java** - Category data transfer object
3. **CreateProductRequest.java** - Create product request
4. **UpdateProductRequest.java** - Update product request

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌───────────────────┐
│ ProductCategory   │ (Hierarchical categories)
└────────┬──────────┘
         │ self-referencing
         │ parent/children
         │
         │ 1:N
         ▼
    ┌─────────┐
    │ Product │ (Finished goods)
    └─────────┘
```

### Relationships

- **ProductCategory** → **ProductCategory**: Self-referencing (parent/children)
  - Hierarchical structure (e.g., Spices > Ground Spices > Cinnamon)
  - Supports unlimited levels

- **ProductCategory** → **Product**: One-to-Many
  - Each product belongs to one category
  - Each category can have many products

---

## 📂 1. ProductCategory Entity

**Purpose:** Organize products into hierarchical categories

### Key Fields

```java
// Identification
- categoryCode (unique, e.g., "CAT-GROUND-SPICES")
- categoryName (e.g., "Ground Spices")
- description

// Hierarchy
- parent (self-reference to parent category)
- children (Set<ProductCategory>)
- categoryLevel (0=root, 1=child, 2=grandchild)

// Display
- displayOrder (for sorting)
- imageUrl

// Status
- isActive

// Related
- products (Set<Product>)
```

### Helper Methods

```java
// Get full category path
String path = category.getFullPath();
// "Spices > Ground Spices > Cinnamon Powder"

// Check if root category
boolean isRoot = category.isRootCategory(); // parent == null

// Check if leaf category
boolean isLeaf = category.isLeafCategory(); // no children

// Manage hierarchy
category.addChild(childCategory);
category.removeChild(childCategory);
```

### Table Structure

```sql
CREATE TABLE product_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_code VARCHAR(20) NOT NULL UNIQUE,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    category_level INT,
    display_order INT,
    image_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_category_parent 
        FOREIGN KEY (parent_id) REFERENCES product_categories(id),
    INDEX idx_category_code (category_code),
    INDEX idx_category_name (category_name),
    INDEX idx_category_parent (parent_id)
);
```

### Category Hierarchy Example

```
Spices (Level 0)
├── Whole Spices (Level 1)
│   ├── Cinnamon Sticks (Level 2)
│   ├── Cardamom Pods (Level 2)
│   └── Cloves (Level 2)
├── Ground Spices (Level 1)
│   ├── Cinnamon Powder (Level 2)
│   ├── Cardamom Powder (Level 2)
│   └── Chili Powder (Level 2)
├── Blended Spices (Level 1)
│   ├── Curry Powder (Level 2)
│   ├── Garam Masala (Level 2)
│   └── Mixed Spice (Level 2)
└── Specialty Products (Level 1)
    ├── Organic Range (Level 2)
    └── Premium Range (Level 2)
```

---

## 🌶️ 2. Product Entity

**Purpose:** Finished spice products ready for sale

### Key Fields (70+ fields)

```java
// Identification
- productCode (unique, e.g., "PROD-CIN-100G")
- productName (e.g., "Cinnamon Powder")
- description
- category (FK to ProductCategory)

// Identification Codes
- sku (Stock Keeping Unit)
- barcode (EAN-13, UPC)
- hsnCode (for taxation)

// Classification
- productType (FINISHED_GOOD, SEMI_FINISHED, RAW_MATERIAL)
- brand
- manufacturer
- countryOfOrigin

// Packaging
- baseUnit (KG, G, L, ML, PCS)
- packageSize (100 for 100g)
- packageUnit (G, KG, ML, L)
- packageType (POUCH, BOTTLE, JAR, BOX, BAG)
- unitsPerCase (24 units per carton)

// Storage & Shelf Life
- shelfLifeDays (365 days)
- storageConditions ("Store in cool, dry place")

// Pricing
- costPrice (production cost)
- sellingPrice (retail price)
- mrp (Maximum Retail Price)
- wholesalePrice
- currency (LKR, USD)
- taxRate (percentage)
- isTaxable

// Inventory
- currentStock (available quantity)
- minimumStock (reorder point)
- maximumStock
- reorderQuantity
- stockLocation

// Order Quantities
- minimumOrderQuantity
- maximumOrderQuantity

// Quality Specifications
- qualitySpecs (text/JSON)
- moistureContent (%)
- purity (%)

// Physical Specifications
- weight (net weight)
- weightUnit (KG, G)
- dimensions (L x W x H in cm)

// Media
- imageUrl (primary image)
- images (multiple images)

// Status & Flags
- isFeatured (featured product)
- isActiveForSale
- isActiveForPurchase
- status (ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK)
- discontinuedDate
- launchDate

// Tracking
- batchTrackingEnabled
- serialTrackingEnabled

// SEO (for online catalog)
- metaKeywords
- metaDescription
```

### Helper Methods

```java
// Get profit margin percentage
BigDecimal margin = product.getProfitMargin();
// (sellingPrice - costPrice) / costPrice * 100

// Check stock levels
boolean lowStock = product.isLowStock(); // currentStock <= minimumStock
boolean outOfStock = product.isOutOfStock(); // currentStock <= 0

// Check if active
boolean active = product.isActive(); // status == ACTIVE && isActiveForSale

// Get full product name with package
String fullName = product.getFullProductName();
// "Cinnamon Powder - 100G"

// Get stock value
BigDecimal value = product.getStockValue(); // currentStock * costPrice
```

### Table Structure

```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Identification
    product_code VARCHAR(20) NOT NULL UNIQUE,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    
    -- Codes
    sku VARCHAR(50) UNIQUE,
    barcode VARCHAR(50) UNIQUE,
    hsn_code VARCHAR(20),
    
    -- Classification
    product_type VARCHAR(30) NOT NULL DEFAULT 'FINISHED_GOOD',
    brand VARCHAR(100),
    manufacturer VARCHAR(100),
    country_of_origin VARCHAR(100),
    
    -- Packaging
    base_unit VARCHAR(10) NOT NULL,
    package_size DECIMAL(10,3),
    package_unit VARCHAR(10),
    package_type VARCHAR(50),
    units_per_case INT,
    
    -- Storage
    shelf_life_days INT,
    storage_conditions VARCHAR(500),
    
    -- Pricing
    cost_price DECIMAL(15,2),
    selling_price DECIMAL(15,2) NOT NULL,
    mrp DECIMAL(15,2),
    wholesale_price DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    tax_rate DECIMAL(5,2),
    is_taxable BOOLEAN DEFAULT TRUE,
    
    -- Inventory
    current_stock DECIMAL(15,3) DEFAULT 0,
    minimum_stock DECIMAL(15,3),
    maximum_stock DECIMAL(15,3),
    reorder_quantity DECIMAL(15,3),
    stock_location VARCHAR(100),
    
    -- Order Quantities
    minimum_order_quantity DECIMAL(15,3),
    maximum_order_quantity DECIMAL(15,3),
    
    -- Quality
    quality_specs TEXT,
    moisture_content DECIMAL(5,2),
    purity DECIMAL(5,2),
    
    -- Physical
    weight DECIMAL(10,3),
    weight_unit VARCHAR(10),
    dimensions VARCHAR(50),
    
    -- Media
    image_url VARCHAR(255),
    images TEXT,
    
    -- Status
    is_featured BOOLEAN DEFAULT FALSE,
    is_active_for_sale BOOLEAN DEFAULT TRUE,
    is_active_for_purchase BOOLEAN DEFAULT TRUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    discontinued_date DATE,
    launch_date DATE,
    
    -- Tracking
    batch_tracking_enabled BOOLEAN DEFAULT FALSE,
    serial_tracking_enabled BOOLEAN DEFAULT FALSE,
    
    -- SEO
    meta_keywords VARCHAR(500),
    meta_description VARCHAR(500),
    
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_product_category 
        FOREIGN KEY (category_id) REFERENCES product_categories(id),
    INDEX idx_product_code (product_code),
    INDEX idx_product_name (product_name),
    INDEX idx_product_category (category_id),
    INDEX idx_product_status (status),
    INDEX idx_product_barcode (barcode),
    INDEX idx_product_sku (sku)
);
```

### Product Types

```java
FINISHED_GOOD    - Ready-to-sell finished products
SEMI_FINISHED    - Intermediate products in production
RAW_MATERIAL     - Raw materials for production
```

### Product Status

```java
ACTIVE          - Active and available for sale
INACTIVE        - Temporarily inactive
DISCONTINUED    - No longer produced
OUT_OF_STOCK    - Currently out of stock
```

### Package Types

```java
POUCH      - Flexible pouch packaging
BOTTLE     - Glass or plastic bottles
JAR        - Glass jars
BOX        - Cardboard boxes
BAG        - Paper or plastic bags
SACHET     - Small sachets
BULK       - Bulk packaging
```

---

## 📋 DTOs

### ProductDTO

**Purpose:** Transfer complete product data

```json
{
  "id": 1,
  "productCode": "PROD-CIN-100G",
  "productName": "Cinnamon Powder",
  "description": "Premium quality Ceylon cinnamon powder",
  "categoryId": 5,
  "categoryCode": "CAT-GROUND",
  "categoryName": "Ground Spices",
  "categoryPath": "Spices > Ground Spices",
  "sku": "SKU-CIN-100G",
  "barcode": "9876543210123",
  "hsnCode": "09061100",
  "productType": "FINISHED_GOOD",
  "brand": "Epic Green",
  "manufacturer": "Epic Green Spices",
  "countryOfOrigin": "Sri Lanka",
  "baseUnit": "G",
  "packageSize": 100.000,
  "packageUnit": "G",
  "packageType": "POUCH",
  "unitsPerCase": 24,
  "shelfLifeDays": 365,
  "storageConditions": "Store in cool, dry place away from direct sunlight",
  "costPrice": 150.00,
  "sellingPrice": 250.00,
  "mrp": 280.00,
  "wholesalePrice": 220.00,
  "currency": "LKR",
  "taxRate": 8.00,
  "isTaxable": true,
  "currentStock": 1500.000,
  "minimumStock": 200.000,
  "maximumStock": 3000.000,
  "reorderQuantity": 500.000,
  "stockLocation": "Warehouse-A, Rack-5, Bin-12",
  "minimumOrderQuantity": 10.000,
  "moistureContent": 10.50,
  "purity": 99.00,
  "weight": 100.000,
  "weightUnit": "G",
  "dimensions": "15 x 10 x 3 cm",
  "imageUrl": "/products/cinnamon-powder-100g.jpg",
  "isFeatured": true,
  "isActiveForSale": true,
  "isActiveForPurchase": true,
  "status": "ACTIVE",
  "launchDate": "2024-01-15",
  "batchTrackingEnabled": true,
  "fullProductName": "Cinnamon Powder - 100G",
  "profitMargin": 66.67,
  "stockValue": 225000.00,
  "isLowStock": false,
  "isOutOfStock": false,
  "isActive": true,
  "totalSalesQuantity": 5000.000,
  "totalSalesAmount": 1250000.00,
  "orderCount": 250,
  "createdAt": "2024-01-15T10:00:00",
  "createdBy": "admin"
}
```

### CreateProductRequest

**Purpose:** Create new product

```json
{
  "productCode": "PROD-CARD-50G",
  "productName": "Cardamom Powder",
  "description": "Aromatic cardamom powder ground from premium quality green cardamom",
  "categoryId": 5,
  "sku": "SKU-CARD-50G",
  "barcode": "9876543210456",
  "hsnCode": "09083100",
  "productType": "FINISHED_GOOD",
  "brand": "Epic Green",
  "manufacturer": "Epic Green Spices",
  "countryOfOrigin": "Sri Lanka",
  "baseUnit": "G",
  "packageSize": 50.000,
  "packageUnit": "G",
  "packageType": "POUCH",
  "unitsPerCase": 48,
  "shelfLifeDays": 365,
  "storageConditions": "Store in airtight container in cool place",
  "costPrice": 450.00,
  "sellingPrice": 750.00,
  "mrp": 800.00,
  "wholesalePrice": 680.00,
  "currency": "LKR",
  "taxRate": 8.00,
  "isTaxable": true,
  "currentStock": 500.000,
  "minimumStock": 100.000,
  "maximumStock": 1000.000,
  "reorderQuantity": 300.000,
  "minimumOrderQuantity": 5.000,
  "moistureContent": 8.00,
  "purity": 98.50,
  "weight": 50.000,
  "weightUnit": "G",
  "dimensions": "12 x 8 x 2 cm",
  "imageUrl": "/products/cardamom-powder-50g.jpg",
  "isFeatured": false,
  "isActiveForSale": true,
  "isActiveForPurchase": true,
  "status": "ACTIVE",
  "batchTrackingEnabled": true,
  "metaKeywords": "cardamom, cardamom powder, elaichi powder, green cardamom",
  "metaDescription": "Premium quality cardamom powder made from finest green cardamom"
}
```

**Validation Rules:**
- productCode: Required, 2-20 chars, alphanumeric + hyphen
- productName: Required, 2-200 chars
- categoryId: Required
- productType: Required
- baseUnit: Required
- sellingPrice: Required, > 0
- Stock values: Non-negative decimals
- Tax rate: 0-100
- Moisture/purity: 0-100
- MRP >= selling price
- Maximum stock >= minimum stock
- Maximum order quantity >= minimum order quantity

---

## 💡 Usage Examples

### Example 1: Create Product

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    
    @Transactional
    public ProductDTO createProduct(CreateProductRequest request) {
        // Validate unique product code
        if (productRepository.existsByProductCode(request.getProductCode())) {
            throw new DuplicateResourceException("Product code already exists");
        }
        
        // Validate category exists
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        
        // Create product
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        
        // Generate SKU if not provided
        if (product.getSku() == null) {
            product.setSku(generateSKU(product));
        }
        
        // Save
        product = productRepository.save(product);
        
        return productMapper.toDTO(product);
    }
    
    private String generateSKU(Product product) {
        // Generate SKU based on category and product code
        String categoryPrefix = product.getCategory().getCategoryCode().substring(0, 3);
        return String.format("SKU-%s-%s", categoryPrefix, product.getProductCode());
    }
}
```

### Example 2: Update Stock

```java
@Transactional
public void updateStock(Long productId, BigDecimal quantity, String operation) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    
    BigDecimal currentStock = product.getCurrentStock();
    
    if ("ADD".equals(operation)) {
        // Add to stock (production, purchase)
        product.setCurrentStock(currentStock.add(quantity));
    } else if ("DEDUCT".equals(operation)) {
        // Deduct from stock (sales, consumption)
        BigDecimal newStock = currentStock.subtract(quantity);
        
        if (newStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Insufficient stock");
        }
        
        product.setCurrentStock(newStock);
        
        // Update status if out of stock
        if (newStock.compareTo(BigDecimal.ZERO) == 0) {
            product.setStatus("OUT_OF_STOCK");
        }
    }
    
    productRepository.save(product);
    
    // Check if reorder needed
    if (product.isLowStock()) {
        // Trigger reorder notification
        notificationService.sendLowStockAlert(product);
    }
}
```

### Example 3: Calculate Profit Margin

```java
@Transactional(readOnly = true)
public ProductProfitReport getProductProfitReport(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    
    BigDecimal profitMargin = product.getProfitMargin();
    BigDecimal profitPerUnit = product.getSellingPrice().subtract(product.getCostPrice());
    BigDecimal totalProfit = profitPerUnit.multiply(product.getCurrentStock());
    
    return ProductProfitReport.builder()
        .productCode(product.getProductCode())
        .productName(product.getProductName())
        .costPrice(product.getCostPrice())
        .sellingPrice(product.getSellingPrice())
        .profitPerUnit(profitPerUnit)
        .profitMargin(profitMargin)
        .currentStock(product.getCurrentStock())
        .totalProfit(totalProfit)
        .build();
}
```

### Example 4: Get Low Stock Products

```java
@Transactional(readOnly = true)
public List<ProductDTO> getLowStockProducts() {
    List<Product> products = productRepository.findLowStockProducts();
    
    return products.stream()
        .map(productMapper::toDTO)
        .toList();
}

// In ProductRepository
@Query("SELECT p FROM Product p WHERE p.currentStock <= p.minimumStock " +
       "AND p.status = 'ACTIVE' ORDER BY p.currentStock ASC")
List<Product> findLowStockProducts();
```

### Example 5: Get Products by Category with Hierarchy

```java
@Transactional(readOnly = true)
public List<ProductDTO> getProductsByCategoryHierarchy(Long categoryId) {
    ProductCategory category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    
    // Get all descendant categories (recursive)
    Set<Long> categoryIds = getAllDescendantCategoryIds(category);
    categoryIds.add(categoryId);
    
    List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
    
    return products.stream()
        .map(productMapper::toDTO)
        .toList();
}

private Set<Long> getAllDescendantCategoryIds(ProductCategory category) {
    Set<Long> ids = new HashSet<>();
    for (ProductCategory child : category.getChildren()) {
        ids.add(child.getId());
        ids.addAll(getAllDescendantCategoryIds(child));
    }
    return ids;
}
```

### Example 6: Create Category Hierarchy

```java
@Transactional
public ProductCategoryDTO createCategoryHierarchy() {
    // Create root category
    ProductCategory spices = ProductCategory.builder()
        .categoryCode("CAT-SPICES")
        .categoryName("Spices")
        .description("All spice products")
        .isActive(true)
        .build();
    spices = categoryRepository.save(spices);
    
    // Create child category
    ProductCategory groundSpices = ProductCategory.builder()
        .categoryCode("CAT-GROUND")
        .categoryName("Ground Spices")
        .description("Finely ground spice powders")
        .isActive(true)
        .build();
    spices.addChild(groundSpices);
    
    // Create grandchild category
    ProductCategory cinnamon = ProductCategory.builder()
        .categoryCode("CAT-CINNAMON")
        .categoryName("Cinnamon Products")
        .description("All cinnamon based products")
        .isActive(true)
        .build();
    groundSpices.addChild(cinnamon);
    
    categoryRepository.save(spices);
    
    return categoryMapper.toDTO(spices);
}
```

---

## 📁 Directory Structure

```
product/
├── entity/
│   ├── Product.java
│   └── ProductCategory.java
├── dto/
│   ├── ProductDTO.java
│   ├── ProductCategoryDTO.java
│   ├── CreateProductRequest.java
│   └── UpdateProductRequest.java
└── README.md
```

---

## 🎯 Key Features

### Product Management
✅ **Comprehensive product data** - 70+ fields covering all aspects  
✅ **Multiple product types** - Finished goods, semi-finished, raw materials  
✅ **Product codes** - Code, SKU, barcode, HSN support  
✅ **Brand & manufacturer** - Track brand and manufacturer info  
✅ **Multi-unit support** - Base unit + package unit  

### Category Management
✅ **Hierarchical categories** - Unlimited levels  
✅ **Category path** - Full path display (e.g., "Spices > Ground > Cinnamon")  
✅ **Category images** - Visual category representation  
✅ **Active management** - Enable/disable categories  

### Pricing & Costing
✅ **Multiple price points** - Cost, selling, MRP, wholesale  
✅ **Profit calculation** - Auto-calculate profit margin  
✅ **Tax support** - Configurable tax rates  
✅ **Multi-currency** - Support different currencies  

### Inventory Management
✅ **Stock tracking** - Current stock, min/max levels  
✅ **Reorder points** - Automatic low stock detection  
✅ **Stock value** - Calculate total stock value  
✅ **Stock location** - Track physical location  
✅ **Low stock alerts** - isLowStock() helper method  

### Quality Control
✅ **Quality specs** - Store quality specifications  
✅ **Moisture content** - Track moisture percentage  
✅ **Purity** - Track purity percentage  
✅ **Batch tracking** - Enable batch tracking  
✅ **Serial tracking** - Enable serial number tracking  

### Packaging Details
✅ **Package info** - Size, unit, type  
✅ **Case packing** - Units per case/carton  
✅ **Physical specs** - Weight, dimensions  
✅ **Shelf life** - Track shelf life in days  
✅ **Storage conditions** - Store storage requirements  

### E-commerce Ready
✅ **Product images** - Primary + multiple images  
✅ **SEO fields** - Meta keywords and description  
✅ **Featured products** - Mark featured products  
✅ **Full product name** - Auto-generate with package size  

### Business Intelligence
✅ **Profit margin** - Calculate profit percentage  
✅ **Stock value** - Calculate total stock worth  
✅ **Sales statistics** - Track sales quantity and amount  
✅ **Order count** - Track number of orders  

---

## 📊 Common Queries

### Get Active Products
```java
List<Product> products = productRepository
    .findByStatusAndIsActiveForSaleTrue("ACTIVE");
```

### Get Products by Category
```java
List<Product> products = productRepository
    .findByCategoryId(categoryId);
```

### Get Featured Products
```java
List<Product> featured = productRepository
    .findByIsFeaturedTrueAndStatus("ACTIVE");
```

### Get Low Stock Products
```java
@Query("SELECT p FROM Product p WHERE p.currentStock <= p.minimumStock")
List<Product> findLowStockProducts();
```

### Search Products
```java
@Query("SELECT p FROM Product p WHERE " +
       "LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
       "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
       "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))")
List<Product> searchProducts(@Param("search") String search);
```

### Get Category Tree
```java
@Query("SELECT c FROM ProductCategory c WHERE c.parent IS NULL")
List<ProductCategory> findRootCategories();
```

---

## ✅ Summary

✅ **2 Entity classes** - Product, ProductCategory  
✅ **4 DTO classes** - Complete request/response objects  
✅ **70+ product fields** - Comprehensive product data model  
✅ **Hierarchical categories** - Unlimited category levels  
✅ **Inventory management** - Stock tracking, reorder points  
✅ **Quality control** - Moisture, purity, quality specs  
✅ **Pricing support** - Multiple price points, profit calculation  
✅ **Packaging details** - Complete packaging specifications  
✅ **Batch/serial tracking** - Enable tracking per product  
✅ **E-commerce ready** - Images, SEO, featured products  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade product management  

**Everything you need for complete product and category management in a spice production factory!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.product
