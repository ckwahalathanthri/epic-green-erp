# Common Exceptions - Epic Green ERP

This directory contains **custom exception classes** and **global exception handling** for the Epic Green ERP system.

## 📦 Contents (8 Files)

1. **ResourceNotFoundException.java** - 404 Not Found
2. **BusinessException.java** - 400 Business rule violations
3. **ValidationException.java** - 400 Validation errors
4. **UnauthorizedException.java** - 401 Authentication errors
5. **ForbiddenException.java** - 403 Authorization errors
6. **DuplicateResourceException.java** - 409 Duplicate resources
7. **FileStorageException.java** - 500 File handling errors
8. **GlobalExceptionHandler.java** - Central exception handling

---

## Exception Classes Overview

### 1. ResourceNotFoundException (404)

**Purpose:** Thrown when a requested resource is not found

**HTTP Status:** 404 Not Found

**Usage Examples:**

```java
// Simple message
throw new ResourceNotFoundException("Product not found");

// With resource details
throw new ResourceNotFoundException("Product", "id", productId);
// Result: "Product not found with id: '123'"

// With cause
throw new ResourceNotFoundException("User not found", cause);
```

**When to Use:**
- Entity not found by ID
- Record doesn't exist in database
- Resource deleted or never existed

---

### 2. BusinessException (400)

**Purpose:** Thrown when business rules are violated

**HTTP Status:** 400 Bad Request

**Usage Examples:**

```java
// Simple message
throw new BusinessException("Insufficient stock for this product");

// With error code
throw new BusinessException(
    "Cannot cancel completed order",
    "ERR_ORDER_001"
);

// With parameters
throw new BusinessException(
    "Credit limit exceeded",
    "ERR_CREDIT_001",
    customerId,
    creditLimit
);
```

**When to Use:**
- Insufficient stock
- Invalid business state transitions
- Credit limit exceeded
- Date/time conflicts
- Invalid discount codes
- Any domain-specific rule violation

---

### 3. ValidationException (400)

**Purpose:** Thrown when manual validation fails

**HTTP Status:** 400 Bad Request

**Usage Examples:**

```java
// Single field error
throw new ValidationException(
    "Validation failed",
    "email",
    "Email format is invalid"
);

// Multiple field errors
Map<String, String> errors = new HashMap<>();
errors.put("price", "Price must be greater than zero");
errors.put("quantity", "Quantity is required");
throw new ValidationException("Validation failed", errors);

// Building errors dynamically
ValidationException exception = new ValidationException("Validation failed");
exception.addFieldError("startDate", "Start date cannot be in the past");
exception.addFieldError("endDate", "End date must be after start date");
throw exception;
```

**When to Use:**
- Manual validation in service layer
- Complex cross-field validation
- Business-specific validation rules
- Complementing @Valid annotation

**Note:** For simple DTO validation, use @Valid with @NotBlank, @Size, etc.

---

### 4. UnauthorizedException (401)

**Purpose:** Thrown when authentication fails

**HTTP Status:** 401 Unauthorized

**Usage Examples:**

```java
// Simple message
throw new UnauthorizedException("Invalid or expired token");

// With error code
throw new UnauthorizedException(
    "Session has expired",
    "ERR_SESSION_001"
);

// With cause
throw new UnauthorizedException("Authentication failed", cause);
```

**When to Use:**
- Invalid JWT token
- Expired session
- Invalid credentials
- Missing authentication
- Token signature verification fails

---

### 5. ForbiddenException (403)

**Purpose:** Thrown when user lacks required permissions

**HTTP Status:** 403 Forbidden

**Usage Examples:**

```java
// Simple message
throw new ForbiddenException("You don't have permission to delete products");

// With required permission
throw new ForbiddenException(
    "Access denied",
    "DELETE_PRODUCTS"
);

// With role requirement
ForbiddenException ex = new ForbiddenException("Admin access required");
ex.setRequiredRole("ROLE_ADMIN");
throw ex;
```

**When to Use:**
- User authenticated but lacks permission
- Role-based access control failures
- Resource ownership violations
- Tenant-based access restrictions

---

### 6. DuplicateResourceException (409)

**Purpose:** Thrown when attempting to create a duplicate

**HTTP Status:** 409 Conflict

**Usage Examples:**

```java
// Simple message
throw new DuplicateResourceException("Product code already exists");

// With resource details
throw new DuplicateResourceException("Product", "code", "PRD-001");
// Result: "Product already exists with code: 'PRD-001'"

// With cause
throw new DuplicateResourceException("Duplicate email", cause);
```

**When to Use:**
- Unique constraint violations
- Duplicate codes/names
- Duplicate email/phone
- Duplicate username
- Any uniqueness violation

---

### 7. FileStorageException (500)

**Purpose:** Thrown when file operations fail

**HTTP Status:** 500 Internal Server Error

**Usage Examples:**

```java
// Simple message
throw new FileStorageException("Failed to upload file");

// With file name
throw new FileStorageException("Could not store file", "document.pdf");

// With cause
throw new FileStorageException(
    "Failed to delete file",
    "image.jpg",
    cause
);
```

**When to Use:**
- File upload failures
- File delete failures
- Invalid file paths
- Disk space issues
- I/O errors

---

## 8. GlobalExceptionHandler

**Purpose:** Central exception handling for entire application

**Features:**
- Catches all exceptions in one place
- Returns standardized error responses
- Logs all errors appropriately
- Converts exceptions to proper HTTP responses

### Handled Exception Types

#### Custom Exceptions
- ResourceNotFoundException → 404
- BusinessException → 400
- ValidationException → 400
- UnauthorizedException → 401
- ForbiddenException → 403
- DuplicateResourceException → 409
- FileStorageException → 500

#### Spring Validation Exceptions
- MethodArgumentNotValidException → 400 (from @Valid)
- ConstraintViolationException → 400 (from @Validated)

#### Spring Security Exceptions
- BadCredentialsException → 401
- LockedException → 401
- AuthenticationException → 401
- AccessDeniedException → 403

#### Database Exceptions
- DataIntegrityViolationException → 409

#### HTTP Exceptions
- HttpMessageNotReadableException → 400
- HttpRequestMethodNotSupportedException → 405
- NoHandlerFoundException → 404
- MissingServletRequestParameterException → 400
- MethodArgumentTypeMismatchException → 400
- MaxUploadSizeExceededException → 413

#### Generic Exception
- Exception → 500 (catch-all)

---

## Error Response Structure

All exceptions return a standardized error response:

```json
{
  "success": false,
  "message": "Product not found with id: '123'",
  "data": {
    "timestamp": "2024-12-10 10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Product not found with id: '123'",
    "path": "/api/v1/products/123"
  }
}
```

### With Field Errors (Validation)

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "timestamp": "2024-12-10 10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/api/v1/products",
    "fieldErrors": {
      "productCode": "Product code is required",
      "price": "Price must be greater than zero"
    }
  }
}
```

---

## Usage Patterns

### 1. In Service Layer

```java
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    @Override
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toDTO(product);
    }
    
    @Override
    @Transactional
    public ProductDTO create(CreateProductRequest request) {
        // Check for duplicates
        if (productRepository.existsByCode(request.getProductCode())) {
            throw new DuplicateResourceException(
                "Product", 
                "code", 
                request.getProductCode()
            );
        }
        
        // Business rule validation
        if (request.getMinStock() >= request.getMaxStock()) {
            throw new BusinessException(
                "Minimum stock must be less than maximum stock"
            );
        }
        
        // Create product
        Product product = productMapper.toEntity(request);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        // Check if product is referenced
        if (orderRepository.existsByProductId(id)) {
            throw new BusinessException(
                "Cannot delete product: referenced in orders"
            );
        }
        
        productRepository.delete(product);
    }
}
```

### 2. In Controller Layer

```java
@RestController
@RequestMapping(ApiEndpoints.PRODUCTS_BASE)
public class ProductController {
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + PERM_VIEW_PRODUCTS + "')")
    public ResponseEntity<ApiResponse<ProductDTO>> getById(@PathVariable Long id) {
        // Exception handling is done by GlobalExceptionHandler
        ProductDTO product = productService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('" + PERM_CREATE_PRODUCTS + "')")
    public ResponseEntity<ApiResponse<ProductDTO>> create(
            @Valid @RequestBody CreateProductRequest request) {
        // @Valid throws MethodArgumentNotValidException if validation fails
        // GlobalExceptionHandler catches it and returns proper error response
        
        ProductDTO product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(product, SUCCESS_PRODUCT_CREATED));
    }
}
```

### 3. In Security Layer

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(...) {
        try {
            String token = extractToken(request);
            
            if (token == null) {
                throw new UnauthorizedException("Missing authentication token");
            }
            
            if (!jwtService.isTokenValid(token)) {
                throw new UnauthorizedException("Invalid or expired token");
            }
            
            // Set authentication
            // ...
            
        } catch (Exception e) {
            throw new UnauthorizedException("Authentication failed", e);
        }
    }
}
```

### 4. Manual Validation

```java
public class OrderService {
    
    public OrderDTO createOrder(CreateOrderRequest request) {
        ValidationException validationException = new ValidationException("Order validation failed");
        
        // Validate order items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            validationException.addFieldError("items", "Order must contain at least one item");
        }
        
        // Validate stock for each item
        for (OrderItemRequest item : request.getItems()) {
            Integer availableStock = inventoryService.getAvailableStock(item.getProductId());
            if (availableStock < item.getQuantity()) {
                validationException.addFieldError(
                    "items[" + item.getProductId() + "].quantity",
                    "Insufficient stock. Available: " + availableStock
                );
            }
        }
        
        // Throw if any errors
        if (validationException.hasFieldErrors()) {
            throw validationException;
        }
        
        // Create order
        // ...
    }
}
```

---

## Best Practices

### 1. Choose the Right Exception

```java
// ❌ Wrong - Using generic exception
if (!found) {
    throw new RuntimeException("Not found");
}

// ✅ Correct - Using specific exception
if (!found) {
    throw new ResourceNotFoundException("Product", "id", productId);
}
```

### 2. Provide Meaningful Messages

```java
// ❌ Wrong - Vague message
throw new BusinessException("Invalid");

// ✅ Correct - Clear, specific message
throw new BusinessException(
    "Cannot cancel order: order has already been shipped"
);
```

### 3. Don't Catch and Rethrow Generic Exceptions

```java
// ❌ Wrong
try {
    // ...
} catch (Exception e) {
    throw new RuntimeException(e);
}

// ✅ Correct - Let GlobalExceptionHandler handle it
try {
    // ...
} catch (SpecificException e) {
    throw new BusinessException("Failed to process order", e);
}
// Or just let it propagate
```

### 4. Log at Appropriate Levels

```java
// In GlobalExceptionHandler:

// Client errors (400s) - log at ERROR level
log.error("Business exception: {}", ex.getMessage());

// Server errors (500s) - log with stack trace
log.error("Internal server error: ", ex);
```

### 5. Don't Expose Sensitive Information

```java
// ❌ Wrong
throw new BusinessException("Database connection failed: " + dbUrl);

// ✅ Correct
throw new BusinessException("Database connection failed");
// Log full details server-side only
```

---

## Exception Flow

```
Request
   ↓
Controller (@Valid validation)
   ↓
Service Layer (Business logic)
   ↓
Repository (Database access)
   ↓
Exception thrown
   ↓
GlobalExceptionHandler catches
   ↓
Maps to HTTP status
   ↓
Creates ErrorResponse
   ↓
Wraps in ApiResponse
   ↓
Returns to client
```

---

## Testing Exceptions

### Unit Test

```java
@Test
void shouldThrowResourceNotFoundException() {
    // Given
    Long productId = 999L;
    when(productRepository.findById(productId)).thenReturn(Optional.empty());
    
    // When & Then
    assertThrows(
        ResourceNotFoundException.class,
        () -> productService.getById(productId)
    );
}
```

### Integration Test

```java
@Test
void shouldReturn404WhenProductNotFound() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/v1/products/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data.status").value(404))
        .andExpect(jsonPath("$.data.message").exists());
}
```

---

## Directory Structure

```
common/exception/
├── ResourceNotFoundException.java
├── BusinessException.java
├── ValidationException.java
├── UnauthorizedException.java
├── ForbiddenException.java
├── DuplicateResourceException.java
├── FileStorageException.java
├── GlobalExceptionHandler.java
└── README.md
```

---

## Integration with Other Components

### Works With:
- **ErrorMessages** constants - For consistent messages
- **ErrorResponse** DTO - For standardized error format
- **ApiResponse** DTO - For wrapping errors
- **Spring Security** - For auth/authz exceptions
- **Spring Validation** - For @Valid exceptions

### Example:

```java
// Constants
throw new ResourceNotFoundException(
    String.format(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, id)
);

// Wrapped in ApiResponse
return ApiResponse.error(
    ErrorMessages.ERROR_INSUFFICIENT_STOCK,
    errorResponse
);
```

---

## Summary

**7 custom exception classes** covering:
- ✅ Resource not found (404)
- ✅ Business rule violations (400)
- ✅ Validation errors (400)
- ✅ Authentication failures (401)
- ✅ Authorization failures (403)
- ✅ Duplicate resources (409)
- ✅ File storage errors (500)

**1 global exception handler** providing:
- ✅ Centralized error handling
- ✅ Standardized error responses
- ✅ Proper HTTP status codes
- ✅ Field-level validation errors
- ✅ Comprehensive logging
- ✅ Security exception handling
- ✅ Database exception handling

**Use these exceptions consistently throughout the application for professional error handling!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.exception
