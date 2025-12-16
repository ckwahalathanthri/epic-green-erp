# Common Constants - Epic Green ERP

This directory contains **common constants** used across all modules in the Epic Green ERP system.

## 📦 Contents (6 Constant Classes)

1. **AppConstants.java** - Application-wide constants
2. **ErrorMessages.java** - All error messages
3. **SuccessMessages.java** - All success messages
4. **RolesAndPermissions.java** - Role and permission definitions
5. **ApiEndpoints.java** - All API endpoint paths
6. **ValidationMessages.java** - All validation messages

---

## 1. AppConstants.java

**Purpose:** Central location for all application-wide configuration constants

**Categories:**

### Application Info
```java
AppConstants.APP_NAME                 // "Epic Green ERP"
AppConstants.APP_VERSION              // "1.0.0"
AppConstants.COMPANY_NAME             // "Epic Green (Pvt) Ltd"
```

### Pagination
```java
AppConstants.DEFAULT_PAGE_NUMBER      // 0
AppConstants.DEFAULT_PAGE_SIZE        // 20
AppConstants.MAX_PAGE_SIZE            // 100
```

### Date/Time Formats
```java
AppConstants.DATE_FORMAT              // "yyyy-MM-dd"
AppConstants.DATETIME_FORMAT          // "yyyy-MM-dd HH:mm:ss"
AppConstants.DEFAULT_TIMEZONE         // "Asia/Colombo"
```

### Currency
```java
AppConstants.DEFAULT_CURRENCY         // "LKR"
AppConstants.CURRENCY_SYMBOL          // "Rs."
AppConstants.CURRENCY_DECIMAL_PLACES  // 2
```

### File Upload
```java
AppConstants.MAX_FILE_SIZE            // 10MB
AppConstants.MAX_IMAGE_SIZE           // 5MB
AppConstants.UPLOAD_DIR               // "uploads/"
AppConstants.ALLOWED_IMAGE_EXTENSIONS // [".jpg", ".jpeg", ".png", ...]
```

### Cache Names
```java
AppConstants.CACHE_PRODUCTS           // "products"
AppConstants.CACHE_SUPPLIERS          // "suppliers"
AppConstants.CACHE_TTL_MEDIUM         // 1800 seconds (30 min)
```

### Queue Names
```java
AppConstants.QUEUE_EMAIL              // "email-queue"
AppConstants.QUEUE_NOTIFICATION       // "notification-queue"
AppConstants.QUEUE_SYNC               // "sync-queue"
```

### Validation Limits
```java
AppConstants.MIN_NAME_LENGTH          // 2
AppConstants.MAX_NAME_LENGTH          // 100
AppConstants.MAX_DESCRIPTION_LENGTH   // 500
AppConstants.MIN_PASSWORD_LENGTH      // 8
AppConstants.MAX_LOGIN_ATTEMPTS       // 5
```

### Regex Patterns
```java
AppConstants.EMAIL_PATTERN            // Email validation regex
AppConstants.PHONE_PATTERN            // Phone validation regex
AppConstants.CODE_PATTERN             // "^[A-Z0-9-]+$"
```

### Status Values
```java
AppConstants.STATUS_ACTIVE            // "ACTIVE"
AppConstants.STATUS_INACTIVE          // "INACTIVE"
AppConstants.STATUS_PENDING           // "PENDING"
AppConstants.STATUS_APPROVED          // "APPROVED"
```

**Usage Example:**
```java
@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
@Pattern(regexp = AppConstants.EMAIL_PATTERN)
@Size(max = AppConstants.MAX_NAME_LENGTH)
```

---

## 2. ErrorMessages.java

**Purpose:** Centralized error messages for consistent error handling

**Categories:**

### Generic Errors
```java
ErrorMessages.ERROR_GENERIC           // "An error occurred..."
ErrorMessages.ERROR_INTERNAL_SERVER   // "Internal server error..."
ErrorMessages.ERROR_BAD_REQUEST       // "Bad request"
```

### Not Found Errors
```java
ErrorMessages.ERROR_NOT_FOUND         // "Resource not found"
ErrorMessages.ERROR_PRODUCT_NOT_FOUND // "Product not found with ID: %s"
ErrorMessages.ERROR_CUSTOMER_NOT_FOUND
ErrorMessages.ERROR_RECORD_NOT_FOUND  // Generic: "%s not found with ID: %s"
```

### Validation Errors
```java
ErrorMessages.ERROR_VALIDATION_FAILED
ErrorMessages.ERROR_REQUIRED_FIELD    // "%s is required"
ErrorMessages.ERROR_INVALID_EMAIL
ErrorMessages.ERROR_INVALID_PHONE
ErrorMessages.ERROR_MIN_LENGTH        // "%s must be at least %d characters"
```

### Duplicate Errors
```java
ErrorMessages.ERROR_DUPLICATE_CODE    // "%s code '%s' already exists"
ErrorMessages.ERROR_DUPLICATE_EMAIL
ErrorMessages.ERROR_ALREADY_EXISTS
```

### Business Logic Errors
```java
ErrorMessages.ERROR_INSUFFICIENT_STOCK
ErrorMessages.ERROR_NEGATIVE_QUANTITY
ErrorMessages.ERROR_INVALID_PRICE
ErrorMessages.ERROR_CANNOT_DELETE     // "Cannot delete %s: %s"
ErrorMessages.ERROR_REFERENCE_EXISTS
```

### Authentication Errors
```java
ErrorMessages.ERROR_UNAUTHORIZED
ErrorMessages.ERROR_INVALID_CREDENTIALS
ErrorMessages.ERROR_ACCOUNT_LOCKED
ErrorMessages.ERROR_TOKEN_EXPIRED
ErrorMessages.ERROR_SESSION_EXPIRED
```

### File Upload Errors
```java
ErrorMessages.ERROR_FILE_TOO_LARGE    // "File size exceeds maximum..."
ErrorMessages.ERROR_FILE_TYPE_NOT_ALLOWED
ErrorMessages.ERROR_INVALID_FILE_FORMAT
```

**Usage Example:**
```java
throw new ResourceNotFoundException(
    String.format(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, id)
);

throw new BusinessException(ErrorMessages.ERROR_INSUFFICIENT_STOCK);
```

---

## 3. SuccessMessages.java

**Purpose:** Centralized success messages for consistent user feedback

**Categories:**

### Generic Success
```java
SuccessMessages.SUCCESS_CREATED       // "Record created successfully"
SuccessMessages.SUCCESS_UPDATED       // "Record updated successfully"
SuccessMessages.SUCCESS_DELETED       // "Record deleted successfully"
```

### CRUD Operations
```java
SuccessMessages.SUCCESS_PRODUCT_CREATED
SuccessMessages.SUCCESS_SUPPLIER_UPDATED
SuccessMessages.SUCCESS_CUSTOMER_DELETED
```

### Status Changes
```java
SuccessMessages.SUCCESS_ACTIVATED     // "%s activated successfully"
SuccessMessages.SUCCESS_APPROVED      // "%s approved successfully"
SuccessMessages.SUCCESS_STATUS_UPDATED
```

### Bulk Operations
```java
SuccessMessages.SUCCESS_BULK_CREATED  // "%d records created successfully"
SuccessMessages.SUCCESS_BULK_DELETED
SuccessMessages.SUCCESS_BULK_OPERATION // "Bulk operation completed: %d succeeded..."
```

### Authentication
```java
SuccessMessages.SUCCESS_LOGIN
SuccessMessages.SUCCESS_PASSWORD_CHANGED
SuccessMessages.SUCCESS_PASSWORD_RESET_EMAIL
```

### Import/Export
```java
SuccessMessages.SUCCESS_IMPORT        // "Import completed: %d records imported"
SuccessMessages.SUCCESS_EXPORT_GENERATED
```

**Usage Example:**
```java
return ApiResponse.success(productDTO, SuccessMessages.SUCCESS_PRODUCT_CREATED);

return ApiResponse.success(
    String.format(SuccessMessages.SUCCESS_BULK_CREATED, count)
);
```

---

## 4. RolesAndPermissions.java

**Purpose:** Role-Based Access Control (RBAC) definitions

**Categories:**

### System Roles
```java
RolesAndPermissions.ROLE_SUPER_ADMIN
RolesAndPermissions.ROLE_ADMIN
RolesAndPermissions.ROLE_MANAGER
RolesAndPermissions.ROLE_USER
```

### Department Roles
```java
RolesAndPermissions.ROLE_WAREHOUSE_MANAGER
RolesAndPermissions.ROLE_SALES_REP
RolesAndPermissions.ROLE_ACCOUNTANT
RolesAndPermissions.ROLE_PRODUCTION_STAFF
```

### Product Permissions
```java
RolesAndPermissions.PERM_VIEW_PRODUCTS
RolesAndPermissions.PERM_CREATE_PRODUCTS
RolesAndPermissions.PERM_EDIT_PRODUCTS
RolesAndPermissions.PERM_DELETE_PRODUCTS
RolesAndPermissions.PERM_APPROVE_PRODUCTS
```

### Supplier Permissions
```java
RolesAndPermissions.PERM_VIEW_SUPPLIERS
RolesAndPermissions.PERM_CREATE_SUPPLIERS
RolesAndPermissions.PERM_BLOCK_SUPPLIERS
```

### Customer Permissions
```java
RolesAndPermissions.PERM_VIEW_CUSTOMERS
RolesAndPermissions.PERM_VIEW_CUSTOMER_LEDGER
RolesAndPermissions.PERM_EDIT_CREDIT_LIMIT
```

### Warehouse Permissions
```java
RolesAndPermissions.PERM_VIEW_INVENTORY
RolesAndPermissions.PERM_ADJUST_INVENTORY
RolesAndPermissions.PERM_TRANSFER_STOCK
```

### Sales Permissions
```java
RolesAndPermissions.PERM_CREATE_SALES_ORDERS
RolesAndPermissions.PERM_APPROVE_SALES_ORDERS
RolesAndPermissions.PERM_DISPATCH_ORDERS
```

### Payment Permissions
```java
RolesAndPermissions.PERM_VIEW_PAYMENTS
RolesAndPermissions.PERM_APPROVE_PAYMENTS
RolesAndPermissions.PERM_PROCESS_CHEQUES
```

### Accounting Permissions
```java
RolesAndPermissions.PERM_VIEW_LEDGER
RolesAndPermissions.PERM_POST_JOURNAL
RolesAndPermissions.PERM_CLOSE_PERIOD
```

### User Management Permissions
```java
RolesAndPermissions.PERM_CREATE_USERS
RolesAndPermissions.PERM_ASSIGN_ROLES
RolesAndPermissions.PERM_RESET_PASSWORD
```

**Usage Example:**
```java
@PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_CREATE_PRODUCTS + "')")
public ResponseEntity<ApiResponse<ProductDTO>> createProduct(...) {
    // ...
}

@PreAuthorize("hasRole('" + RolesAndPermissions.ROLE_ADMIN + "')")
public void adminOnlyMethod() {
    // ...
}
```

---

## 5. ApiEndpoints.java

**Purpose:** Central definition of all API endpoint paths

**Categories:**

### Authentication
```java
ApiEndpoints.AUTH_LOGIN               // "/api/v1/auth/login"
ApiEndpoints.AUTH_LOGOUT              // "/api/v1/auth/logout"
ApiEndpoints.AUTH_REFRESH             // "/api/v1/auth/refresh"
ApiEndpoints.AUTH_FORGOT_PASSWORD
```

### User Management
```java
ApiEndpoints.USERS_BASE               // "/api/v1/users"
ApiEndpoints.USERS_BY_ID              // "/api/v1/users/{id}"
ApiEndpoints.USERS_SEARCH             // "/api/v1/users/search"
ApiEndpoints.USERS_LOCK               // "/api/v1/users/{id}/lock"
```

### Product Management
```java
ApiEndpoints.PRODUCTS_BASE            // "/api/v1/products"
ApiEndpoints.PRODUCTS_BY_ID           // "/api/v1/products/{id}"
ApiEndpoints.PRODUCTS_SEARCH          // "/api/v1/products/search"
ApiEndpoints.PRODUCTS_LOW_STOCK       // "/api/v1/products/low-stock"
ApiEndpoints.PRODUCTS_IMPORT          // "/api/v1/products/import"
```

### Supplier Management
```java
ApiEndpoints.SUPPLIERS_BASE           // "/api/v1/suppliers"
ApiEndpoints.SUPPLIERS_LEDGER         // "/api/v1/suppliers/{id}/ledger"
ApiEndpoints.SUPPLIERS_CONTACTS       // "/api/v1/suppliers/{id}/contacts"
```

### Customer Management
```java
ApiEndpoints.CUSTOMERS_BASE           // "/api/v1/customers"
ApiEndpoints.CUSTOMERS_LEDGER         // "/api/v1/customers/{id}/ledger"
ApiEndpoints.CUSTOMERS_CREDIT_LIMIT   // "/api/v1/customers/{id}/credit-limit"
```

### Warehouse & Inventory
```java
ApiEndpoints.WAREHOUSES_BASE          // "/api/v1/warehouses"
ApiEndpoints.INVENTORY_ADJUSTMENTS    // "/api/v1/inventory/adjustments"
ApiEndpoints.INVENTORY_TRANSFERS      // "/api/v1/inventory/transfers"
```

### Purchase Management
```java
ApiEndpoints.PURCHASE_ORDERS_BASE     // "/api/v1/purchase-orders"
ApiEndpoints.GRN_BASE                 // "/api/v1/grn"
```

### Sales Management
```java
ApiEndpoints.SALES_ORDERS_BASE        // "/api/v1/sales-orders"
ApiEndpoints.INVOICES_BASE            // "/api/v1/invoices"
```

### Reports
```java
ApiEndpoints.REPORTS_SALES            // "/api/v1/reports/sales"
ApiEndpoints.REPORTS_INVENTORY        // "/api/v1/reports/inventory"
ApiEndpoints.REPORTS_FINANCIAL        // "/api/v1/reports/financial"
```

**Usage Example:**
```java
@RequestMapping(ApiEndpoints.PRODUCTS_BASE)
public class ProductController {
    
    @GetMapping(ApiEndpoints.PRODUCTS_LOW_STOCK)
    public ResponseEntity<...> getLowStock() {
        // ...
    }
}
```

---

## 6. ValidationMessages.java

**Purpose:** Validation messages for @Valid annotations

**Categories:**

### Required Field Messages
```java
ValidationMessages.PRODUCT_CODE_REQUIRED  // "Product code is required"
ValidationMessages.EMAIL_REQUIRED
ValidationMessages.QUANTITY_REQUIRED
```

### String Length Messages
```java
ValidationMessages.CODE_MIN_LENGTH        // "Code must be at least 2 characters"
ValidationMessages.NAME_MAX_LENGTH        // "Name must not exceed 100 characters"
ValidationMessages.PASSWORD_MIN_LENGTH
```

### Pattern/Format Messages
```java
ValidationMessages.EMAIL_INVALID          // "Invalid email address format"
ValidationMessages.PHONE_INVALID
ValidationMessages.CODE_INVALID           // "Code must contain only uppercase..."
```

### Numeric Value Messages
```java
ValidationMessages.QUANTITY_POSITIVE      // "Quantity must be greater than zero"
ValidationMessages.PRICE_POSITIVE
ValidationMessages.DISCOUNT_RANGE         // "Discount must be between 0 and 100"
```

### Date/Time Messages
```java
ValidationMessages.DATE_PAST              // "Date cannot be in the past"
ValidationMessages.DATE_FUTURE
ValidationMessages.DATE_RANGE_INVALID     // "End date must be after start date"
```

**Usage Example:**
```java
public class CreateProductRequest {
    
    @NotBlank(message = ValidationMessages.PRODUCT_CODE_REQUIRED)
    @Size(min = 2, max = 30, message = ValidationMessages.CODE_MIN_LENGTH)
    @Pattern(regexp = AppConstants.CODE_PATTERN, message = ValidationMessages.CODE_INVALID)
    private String productCode;
    
    @NotBlank(message = ValidationMessages.PRODUCT_NAME_REQUIRED)
    @Size(max = 100, message = ValidationMessages.NAME_MAX_LENGTH)
    private String productName;
    
    @Min(value = 1, message = ValidationMessages.QUANTITY_POSITIVE)
    private Integer quantity;
}
```

---

## 🎯 Usage Patterns

### 1. In Controllers
```java
@RestController
@RequestMapping(ApiEndpoints.PRODUCTS_BASE)
public class ProductController {
    
    @PostMapping
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_CREATE_PRODUCTS + "')")
    public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody CreateProductRequest request) {
        ProductDTO product = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(product, SuccessMessages.SUCCESS_PRODUCT_CREATED));
    }
}
```

### 2. In Exception Handling
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponse<ErrorResponse>> handleNotFound(ResourceNotFoundException ex) {
    ErrorResponse error = ErrorResponse.of(
        404,
        "Not Found",
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error(error));
}
```

### 3. In DTOs
```java
@Data
@Builder
public class CreateProductRequest {
    
    @NotBlank(message = ValidationMessages.PRODUCT_CODE_REQUIRED)
    @Size(max = AppConstants.MAX_CODE_LENGTH)
    @Pattern(regexp = AppConstants.CODE_PATTERN)
    private String productCode;
}
```

### 4. In Services
```java
public ProductDTO createProduct(CreateProductRequest request) {
    if (repository.existsByCode(request.getProductCode())) {
        throw new BusinessException(
            String.format(ErrorMessages.ERROR_DUPLICATE_CODE, "Product", request.getProductCode())
        );
    }
    // ...
}
```

---

## 📁 Directory Structure

```
common/constants/
├── AppConstants.java
├── ErrorMessages.java
├── SuccessMessages.java
├── RolesAndPermissions.java
├── ApiEndpoints.java
├── ValidationMessages.java
└── README.md
```

---

## 🔧 Where to Place These Files

```
src/main/java/lk/epicgreen/erp/
└── common/
    └── constants/
        ├── AppConstants.java
        ├── ErrorMessages.java
        ├── SuccessMessages.java
        ├── RolesAndPermissions.java
        ├── ApiEndpoints.java
        └── ValidationMessages.java
```

---

## ✅ Benefits

1. **Centralization** - All constants in one place
2. **Consistency** - Same messages/values everywhere
3. **Maintainability** - Change once, affect all
4. **Type Safety** - Compile-time checking
5. **Refactoring** - Easy to find usages
6. **Documentation** - Self-documenting code
7. **i18n Ready** - Easy to add internationalization

---

## 💡 Best Practices

1. **Use Constants Everywhere** - Never use magic strings/numbers
2. **String.format() for Dynamic Messages** - Use placeholders (%s, %d)
3. **Meaningful Names** - Constants should be self-explanatory
4. **Group Related Constants** - Use comments to separate groups
5. **Final Classes** - Prevent instantiation and extension
6. **Static Imports** - For better readability
   ```java
   import static lk.epicgreen.erp.common.constants.AppConstants.*;
   ```

---

## 🎓 Usage Examples

### Example 1: API Endpoint
```java
@GetMapping(ApiEndpoints.PRODUCTS_SEARCH)
@PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_VIEW_PRODUCTS + "')")
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> search(
        @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    // Implementation
}
```

### Example 2: Validation
```java
@NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
@Email(message = ValidationMessages.EMAIL_INVALID)
@Size(max = AppConstants.MAX_EMAIL_LENGTH)
private String email;
```

### Example 3: Error Handling
```java
if (stock < quantity) {
    throw new BusinessException(
        String.format(ErrorMessages.ERROR_INSUFFICIENT_STOCK, productName)
    );
}
```

### Example 4: Success Response
```java
return ApiResponse.success(
    dto,
    String.format(SuccessMessages.SUCCESS_BULK_CREATED, count)
);
```

---

## 🎯 Summary

**6 comprehensive constant classes** providing:
- ✅ Application configuration constants
- ✅ Consistent error messages
- ✅ Success messages
- ✅ Role & permission definitions
- ✅ API endpoint paths
- ✅ Validation messages

**Total:** ~2,000+ constants covering all aspects of the application!

**Use these constants everywhere for consistent, maintainable code!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.constants
