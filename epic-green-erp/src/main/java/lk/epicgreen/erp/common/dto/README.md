# Common DTOs - Epic Green ERP

This directory contains **common Data Transfer Objects (DTOs)** used across all modules in the Epic Green ERP system.

## 📦 Contents (8 DTO Classes)

1. **ApiResponse.java** - Generic API response wrapper
2. **PageResponse.java** - Pagination wrapper
3. **ErrorResponse.java** - Standardized error responses
4. **BaseDTO.java** - Base class with audit fields
5. **SearchRequest.java** - Advanced search with filters
6. **IdNameDTO.java** - Simple dropdown DTO
7. **KeyValueDTO.java** - Generic key-value pairs
8. **ImportResultDTO.java** - File import results
9. **BulkOperationResultDTO.java** - Bulk operation results

---

## 1. ApiResponse<T>

**Purpose:** Standardized wrapper for all API responses

**Features:**
- Success/error indication
- Consistent structure
- Timestamp
- HTTP status and path
- Metadata support

**Usage Examples:**

```java
// Success response with data
return ResponseEntity.ok(ApiResponse.success(productDTO, "Product created successfully"));

// Success response without data
return ResponseEntity.ok(ApiResponse.success("Operation completed"));

// Error response
return ResponseEntity.badRequest()
    .body(ApiResponse.error("Product not found"));

// Error with details
return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    .body(ApiResponse.error("Validation failed", validationErrors));
```

**Response Structure:**
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": { /* product data */ },
  "timestamp": "2024-12-10 10:30:00",
  "status": 200,
  "path": "/api/v1/products"
}
```

---

## 2. PageResponse<T>

**Purpose:** Pagination wrapper for list endpoints

**Features:**
- Content list
- Page metadata
- Helper methods
- Display text generation

**Usage Examples:**

```java
// From Spring Data Page
Page<Product> page = productRepository.findAll(pageable);
PageResponse<ProductDTO> response = PageResponse.from(page);

// Manual pagination
PageResponse<ProductDTO> response = PageResponse.of(
    products,      // List<ProductDTO>
    0,            // page number
    20,           // page size
    100           // total elements
);

// Empty response
PageResponse<ProductDTO> empty = PageResponse.empty(0, 20);
```

**Response Structure:**
```json
{
  "content": [ /* list of items */ ],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true,
  "empty": false,
  "numberOfElements": 20
}
```

**Helper Methods:**
```java
pageResponse.hasNext();                    // true if more pages
pageResponse.hasPrevious();                // true if not first page
pageResponse.getDisplayText("products");   // "Showing 1-20 of 100 products"
```

---

## 3. ErrorResponse

**Purpose:** Standardized error response structure

**Features:**
- HTTP status information
- Error message and details
- Field validation errors
- Stack trace (dev only)
- Sub-errors support

**Usage Examples:**

```java
// Basic error
ErrorResponse error = ErrorResponse.of(
    404,
    "Not Found",
    "Product not found",
    "/api/v1/products/123"
);

// Validation errors
ErrorResponse error = ErrorResponse.ofValidation(
    400,
    "Bad Request",
    "Validation failed",
    "/api/v1/products",
    fieldErrors  // Map<String, String>
);

// With error code
ErrorResponse error = ErrorResponse.withCode(
    500,
    "Internal Server Error",
    "Database connection failed",
    "/api/v1/products",
    "ERR_DB_001"
);
```

**Response Structure:**
```json
{
  "timestamp": "2024-12-10 10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/products",
  "fieldErrors": {
    "productCode": "Product code is required",
    "price": "Price must be greater than 0"
  }
}
```

---

## 4. BaseDTO

**Purpose:** Base class with common audit fields for all DTOs

**Features:**
- ID field
- Audit fields (created/updated)
- Soft delete support
- Version for optimistic locking
- Helper methods

**Usage Example:**

```java
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProductDTO extends BaseDTO {
    private String productCode;
    private String productName;
    private BigDecimal price;
    // ... other fields
}
```

**Inherited Fields:**
```java
private Long id;
private LocalDateTime createdAt;
private String createdBy;
private LocalDateTime updatedAt;
private String updatedBy;
private LocalDateTime deletedAt;
private String deletedBy;
private Integer version;
```

**Helper Methods:**
```java
productDTO.isDeleted();                  // Check if soft deleted
productDTO.isNew();                      // Check if new record
productDTO.isModified();                 // Check if modified
productDTO.getAuditDisplayText();        // "Created by John Doe on 2024-12-10"
```

---

## 5. SearchRequest

**Purpose:** Advanced search with pagination, sorting, and filtering

**Features:**
- Search query
- Pagination
- Sorting
- Multiple filters
- Date range
- Soft delete option

**Usage Example:**

```java
// In Controller
@PostMapping("/search")
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> search(
        @RequestBody SearchRequest searchRequest) {
    
    // Convert to Pageable
    Pageable pageable = searchRequest.toPageable();
    
    // Use filters
    if (searchRequest.hasFilters()) {
        // Apply filters
    }
    
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

**Request Structure:**
```json
{
  "query": "search term",
  "page": 0,
  "size": 20,
  "sortBy": "productName",
  "sortDirection": "ASC",
  "filters": [
    {
      "field": "price",
      "operation": "GREATER_THAN",
      "value": 100
    }
  ],
  "dateFrom": "2024-01-01",
  "dateTo": "2024-12-31",
  "includeDeleted": false
}
```

**Filter Operations:**
- EQUALS, NOT_EQUALS
- GREATER_THAN, LESS_THAN
- GREATER_OR_EQUAL, LESS_OR_EQUAL
- LIKE, STARTS_WITH, ENDS_WITH
- IN, NOT_IN
- IS_NULL, IS_NOT_NULL
- BETWEEN

---

## 6. IdNameDTO

**Purpose:** Simple DTO for dropdown lists and autocomplete

**Features:**
- Minimal fields (id, name)
- Optional code and description
- Active status
- Display text generation

**Usage Examples:**

```java
// Simple
IdNameDTO dto = IdNameDTO.of(1L, "Product Name");

// With code
IdNameDTO dto = IdNameDTO.of(1L, "Product Name", "PRD-001");

// Full
IdNameDTO dto = IdNameDTO.of(
    1L, 
    "Product Name", 
    "PRD-001", 
    "Description",
    true  // active
);
```

**Use Cases:**
- Dropdown lists
- Autocomplete fields
- Quick lookups
- Related entity references

**Response Structure:**
```json
{
  "id": 1,
  "name": "Product Name",
  "code": "PRD-001",
  "description": "Product description",
  "active": true
}
```

---

## 7. KeyValueDTO<K, V>

**Purpose:** Generic key-value pair for configuration, metadata, etc.

**Usage Examples:**

```java
// Simple
KeyValueDTO<String, Integer> dto = KeyValueDTO.of("maxStock", 1000);

// With label
KeyValueDTO<String, String> dto = KeyValueDTO.of(
    "currency", 
    "LKR", 
    "Default Currency"
);

// In lists (for settings)
List<KeyValueDTO<String, Object>> settings = Arrays.asList(
    KeyValueDTO.of("companyName", "Epic Green"),
    KeyValueDTO.of("taxRate", 15.0),
    KeyValueDTO.of("enableNotifications", true)
);
```

**Use Cases:**
- Configuration settings
- System parameters
- Metadata
- Report parameters

---

## 8. ImportResultDTO

**Purpose:** Result of file import operations (Excel, CSV)

**Features:**
- Success/failure counts
- Error details per row
- Warnings
- Duration tracking
- Created record IDs

**Usage Example:**

```java
ImportResultDTO result = ImportResultDTO.builder()
    .fileName("products.xlsx")
    .totalRows(100)
    .startTime(LocalDateTime.now())
    .build();

// Process import
for (int i = 0; i < rows.size(); i++) {
    try {
        // Import row
        result.incrementSuccess();
    } catch (Exception e) {
        result.addError(i + 1, "productCode", e.getMessage());
    }
}

result.setEndTime(LocalDateTime.now());
return ResponseEntity.ok(ApiResponse.success(result));
```

**Response Structure:**
```json
{
  "success": true,
  "message": "Import completed",
  "fileName": "products.xlsx",
  "totalRows": 100,
  "successCount": 95,
  "failureCount": 5,
  "skippedCount": 0,
  "errors": [
    {
      "rowNumber": 10,
      "field": "price",
      "message": "Price must be greater than 0",
      "value": "-10"
    }
  ],
  "warnings": ["Duplicate product codes found"],
  "durationMs": 2500
}
```

---

## 9. BulkOperationResultDTO

**Purpose:** Result of bulk operations (delete, update, activate)

**Features:**
- Operation type
- Success/failure counts
- Failed item details
- Duration tracking

**Usage Example:**

```java
// Bulk delete
List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L);
List<Long> successIds = new ArrayList<>();
List<BulkOperationResultDTO.FailedItem> failedItems = new ArrayList<>();

for (Long id : ids) {
    try {
        productService.delete(id);
        successIds.add(id);
    } catch (Exception e) {
        failedItems.add(
            BulkOperationResultDTO.FailedItem.builder()
                .id(id)
                .reason(e.getMessage())
                .build()
        );
    }
}

BulkOperationResultDTO result;
if (failedItems.isEmpty()) {
    result = BulkOperationResultDTO.success("DELETE", successIds);
} else {
    result = BulkOperationResultDTO.partial("DELETE", successIds, failedItems);
}

return ResponseEntity.ok(ApiResponse.success(result));
```

**Response Structure:**
```json
{
  "operation": "DELETE",
  "success": false,
  "message": "Processed 95 of 100 items successfully",
  "totalItems": 100,
  "successCount": 95,
  "failureCount": 5,
  "successIds": [1, 2, 3, ...],
  "failedItems": [
    {
      "id": 10,
      "reason": "Product is referenced in orders",
      "errorCode": "FK_CONSTRAINT"
    }
  ]
}
```

---

## 🎯 Usage Patterns

### 1. Standard CRUD Endpoint

```java
@PostMapping
public ResponseEntity<ApiResponse<ProductDTO>> create(
        @Valid @RequestBody CreateProductRequest request) {
    ProductDTO product = productService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(product, "Product created successfully"));
}
```

### 2. Paginated List Endpoint

```java
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    
    Page<Product> products = productService.getAll(PageRequest.of(page, size));
    PageResponse<ProductDTO> pageResponse = PageResponse.from(products);
    
    return ResponseEntity.ok(ApiResponse.success(pageResponse));
}
```

### 3. Search Endpoint

```java
@PostMapping("/search")
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> search(
        @RequestBody SearchRequest searchRequest) {
    
    PageResponse<ProductDTO> result = productService.search(searchRequest);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

### 4. Dropdown/Autocomplete Endpoint

```java
@GetMapping("/dropdown")
public ResponseEntity<ApiResponse<List<IdNameDTO>>> getDropdown() {
    List<IdNameDTO> categories = categoryService.getAllForDropdown();
    return ResponseEntity.ok(ApiResponse.success(categories));
}
```

### 5. Import Endpoint

```java
@PostMapping("/import")
public ResponseEntity<ApiResponse<ImportResultDTO>> importFile(
        @RequestParam("file") MultipartFile file) {
    
    ImportResultDTO result = productService.importFromExcel(file);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

### 6. Bulk Operation Endpoint

```java
@DeleteMapping("/bulk")
public ResponseEntity<ApiResponse<BulkOperationResultDTO>> bulkDelete(
        @RequestBody List<Long> ids) {
    
    BulkOperationResultDTO result = productService.bulkDelete(ids);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

---

## 📦 Package Structure

```
common/dto/
├── ApiResponse.java              ← Generic response wrapper
├── PageResponse.java             ← Pagination wrapper
├── ErrorResponse.java            ← Error response
├── BaseDTO.java                  ← Base class for all DTOs
├── SearchRequest.java            ← Advanced search
├── IdNameDTO.java                ← Simple dropdown DTO
├── KeyValueDTO.java              ← Key-value pairs
├── ImportResultDTO.java          ← Import results
├── BulkOperationResultDTO.java   ← Bulk operation results
└── README.md                     ← This file
```

---

## 🔧 Where to Place These Files

```
src/main/java/lk/epicgreen/erp/
└── common/
    └── dto/
        ├── ApiResponse.java
        ├── PageResponse.java
        ├── ErrorResponse.java
        ├── BaseDTO.java
        ├── SearchRequest.java
        ├── IdNameDTO.java
        ├── KeyValueDTO.java
        ├── ImportResultDTO.java
        └── BulkOperationResultDTO.java
```

---

## ✅ Benefits

1. **Consistency** - Same response structure across all endpoints
2. **Type Safety** - Generic types ensure compile-time checking
3. **Maintainability** - Changes in one place affect all endpoints
4. **Documentation** - Clear structure for frontend developers
5. **Error Handling** - Standardized error responses
6. **Pagination** - Built-in pagination support
7. **Reusability** - Use across all modules

---

## 🚀 Quick Start

1. Copy all 9 files to `src/main/java/lk/epicgreen/erp/common/dto/`
2. Update your controllers to use `ApiResponse` wrapper
3. Update list endpoints to use `PageResponse`
4. Use `BaseDTO` for all your DTOs
5. Implement search using `SearchRequest`
6. Use `IdNameDTO` for dropdowns
7. Implement import/export with result DTOs

---

## 📊 Usage Statistics

| DTO | Primary Use | Frequency |
|-----|-------------|-----------|
| ApiResponse | All endpoints | 100% |
| PageResponse | List endpoints | 60% |
| ErrorResponse | Exception handling | 100% |
| BaseDTO | All entity DTOs | 90% |
| SearchRequest | Search endpoints | 30% |
| IdNameDTO | Dropdown/autocomplete | 80% |
| KeyValueDTO | Settings/metadata | 20% |
| ImportResultDTO | Import operations | 10% |
| BulkOperationResultDTO | Bulk operations | 15% |

---

## 💡 Pro Tips

1. **Always wrap responses** - Use `ApiResponse` for consistency
2. **Extend BaseDTO** - For audit trail on all entities
3. **Use PageResponse** - For any list that could grow large
4. **IdNameDTO for dropdowns** - Minimal data transfer
5. **SearchRequest for flexibility** - Powerful search without custom code
6. **Handle imports properly** - Detailed error reporting with ImportResultDTO
7. **Track bulk operations** - Show users exactly what succeeded/failed

---

## 📚 Related Files

- **GlobalExceptionHandler** - Uses ErrorResponse
- **ProductDTO** - Extends BaseDTO
- **ProductController** - Uses ApiResponse, PageResponse
- **SecurityConfig** - Exception handling configuration

---

## 🎯 Summary

These **9 common DTOs** provide:
- ✅ Consistent API responses
- ✅ Standardized error handling
- ✅ Built-in pagination
- ✅ Audit trail support
- ✅ Advanced search capabilities
- ✅ Dropdown/autocomplete support
- ✅ Import/export tracking
- ✅ Bulk operation results

**Use them in every module for consistent, professional APIs!**

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.dto
