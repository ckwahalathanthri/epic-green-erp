# Common Audit - Epic Green ERP

This directory contains **audit functionality** for tracking all changes and operations in the system.

## 📦 Contents (7 Files)

1. **AuditEntity.java** - Base entity with audit fields
2. **AuditorAwareImpl.java** - Provides current user for auditing
3. **AuditLog.java** - Entity for detailed audit logs
4. **AuditLogRepository.java** - Repository for audit logs
5. **AuditLogService.java** - Service for audit operations
6. **JpaAuditingConfig.java** - Configuration for JPA auditing
7. **Auditable.java** - Annotation for detailed audit logging

---

## Overview

The audit system provides **two levels of auditing**:

### 1. Basic Auditing (All Entities)
- Automatic via `AuditEntity` base class
- Tracks: createdAt, createdBy, updatedAt, updatedBy
- Supports soft delete: deletedAt, deletedBy
- Optimistic locking: version field

### 2. Detailed Auditing (Specific Entities)
- Manual via `AuditLogService`
- Stores: old values, new values, changed fields
- Includes: IP address, user agent, request URL
- Supports: success/failure tracking

---

## 1. AuditEntity.java

**Purpose:** Base class that all entities should extend

### Features
- **@CreatedDate** - Auto-populated on first save
- **@CreatedBy** - Auto-populated with current user
- **@LastModifiedDate** - Auto-updated on every save
- **@LastModifiedBy** - Auto-updated with current user
- **deletedAt / deletedBy** - For soft delete
- **@Version** - For optimistic locking

### Usage

```java
@Entity
@Table(name = "products")
public class Product extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String productCode;
    private String productName;
    private BigDecimal price;
    
    // Audit fields inherited from AuditEntity:
    // - createdAt
    // - createdBy
    // - updatedAt
    // - updatedBy
    // - deletedAt
    // - deletedBy
    // - version
}
```

### Helper Methods

```java
// Check if deleted
boolean deleted = product.isDeleted();

// Check if new
boolean isNew = product.isNew();

// Soft delete
product.markAsDeleted("admin");

// Restore
product.restore();

// Get audit info
String info = product.getAuditInfo();
// "Created: 2024-12-10 10:00:00 by admin | Updated: 2024-12-10 11:00:00 by admin"
```

---

## 2. AuditorAwareImpl.java

**Purpose:** Provides current user for JPA auditing

### How It Works

```java
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        // Gets username from Spring Security context
        // Returns "SYSTEM" if no user authenticated
        // Returns "ANONYMOUS" if anonymous user
    }
}
```

**Automatic:** No need to call manually - Spring Data JPA uses it automatically

---

## 3. AuditLog.java

**Purpose:** Entity for storing detailed audit logs

### Fields

```java
- id                  // Primary key
- entityName          // "Product", "Customer", etc.
- entityId            // ID of the entity
- action              // "CREATE", "UPDATE", "DELETE"
- performedBy         // Username
- timestamp           // When action occurred
- oldValues           // Old values (JSON)
- newValues           // New values (JSON)
- changedFields       // List of changed fields
- ipAddress           // User's IP
- userAgent           // Browser/device info
- requestUrl          // API endpoint
- httpMethod          // GET, POST, PUT, DELETE
- status              // SUCCESS, FAILURE
- errorMessage        // If failed
- details             // Additional info (JSON)
- durationMs          // Operation duration
- module              // PRODUCT, SALES, etc.
```

### Database Table

```sql
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(50) NOT NULL,
    action VARCHAR(20) NOT NULL,
    performed_by VARCHAR(50) NOT NULL,
    timestamp DATETIME NOT NULL,
    old_values TEXT,
    new_values TEXT,
    changed_fields VARCHAR(500),
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    request_url VARCHAR(500),
    http_method VARCHAR(10),
    status VARCHAR(20),
    error_message TEXT,
    details TEXT,
    duration_ms BIGINT,
    module VARCHAR(50),
    
    INDEX idx_audit_entity (entity_name, entity_id),
    INDEX idx_audit_user (performed_by),
    INDEX idx_audit_action (action),
    INDEX idx_audit_timestamp (timestamp)
);
```

---

## 4. AuditLogRepository.java

**Purpose:** Repository for querying audit logs

### Key Methods

```java
// Find by entity
Page<AuditLog> findByEntityNameAndEntityIdOrderByTimestampDesc(
    String entityName, String entityId, Pageable pageable
);

// Find by user
Page<AuditLog> findByPerformedByOrderByTimestampDesc(
    String performedBy, Pageable pageable
);

// Find by date range
Page<AuditLog> findByTimestampBetweenOrderByTimestampDesc(
    LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
);

// Search with criteria
Page<AuditLog> search(
    String entityName, String action, String performedBy,
    LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
);

// Get recent logs
List<AuditLog> findRecentByEntity(
    String entityName, String entityId, int limit
);
```

---

## 5. AuditLogService.java

**Purpose:** Service for creating and querying audit logs

### Creating Audit Logs

```java
// Simple audit log
auditLogService.log("Product", productId, "CREATE");

// With old and new values
auditLogService.log("Product", productId, "UPDATE", oldProduct, newProduct);

// With additional details
Map<String, Object> details = Map.of("reason", "Price update");
auditLogService.log("Product", productId, "UPDATE", oldProduct, newProduct, details);

// Log failure
auditLogService.logFailure("Product", productId, "DELETE", "Product in use");
```

### Querying Audit Logs

```java
// Get audit logs for an entity
Page<AuditLog> logs = auditLogService.getAuditLogs("Product", "123", pageable);

// Get recent logs
List<AuditLog> recent = auditLogService.getRecentAuditLogs("Product", "123", 10);

// Get user activity
Page<AuditLog> userLogs = auditLogService.getAuditLogsByUser("admin", pageable);

// Search with criteria
Page<AuditLog> results = auditLogService.searchAuditLogs(
    "Product",           // entity name
    "UPDATE",           // action
    "admin",            // user
    startDate,          // from date
    endDate,            // to date
    pageable
);

// Get failed operations
Page<AuditLog> failures = auditLogService.getFailedAuditLogs(pageable);
```

### Statistics

```java
// Count audit logs
long count = auditLogService.countAuditLogs("Product", "123");

// Count user activity
long activity = auditLogService.countUserActivity("admin", startDate, endDate);
```

### Cleanup

```java
// Delete logs older than 90 days
long deleted = auditLogService.deleteOldAuditLogs(90);
```

---

## 6. JpaAuditingConfig.java

**Purpose:** Enables JPA auditing

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
```

**Automatic:** Just include in your application - no manual configuration needed

---

## 7. Auditable.java

**Purpose:** Annotation for marking entities that need detailed audit logging

### Usage

```java
@Entity
@Table(name = "products")
@Auditable(module = "PRODUCT")
public class Product extends AuditEntity {
    // ...
}
```

### Attributes

```java
@Auditable(
    module = "PRODUCT",           // Module name for categorization
    logViews = false,             // Whether to log view operations
    storeOldValues = true,        // Store old values
    storeNewValues = true         // Store new values
)
```

---

## Complete Usage Example

### 1. Entity Setup

```java
@Entity
@Table(name = "products")
@Auditable(module = "PRODUCT")
public class Product extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String productCode;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    // Audit fields inherited from AuditEntity
}
```

### 2. Service with Audit Logging

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;
    private final ProductMapper productMapper;
    
    @Transactional
    public ProductDTO create(CreateProductRequest request) {
        // Create product
        Product product = productMapper.toEntity(request);
        product = productRepository.save(product);
        
        // Log audit
        auditLogService.log("Product", product.getId(), "CREATE", null, product);
        
        return productMapper.toDTO(product);
    }
    
    @Transactional
    public ProductDTO update(Long id, UpdateProductRequest request) {
        // Get existing product
        Product oldProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        // Keep old values for audit
        Product oldValues = oldProduct.clone(); // or use mapper
        
        // Update product
        productMapper.updateEntity(request, oldProduct);
        Product updatedProduct = productRepository.save(oldProduct);
        
        // Log audit with old and new values
        auditLogService.log("Product", id, "UPDATE", oldValues, updatedProduct);
        
        return productMapper.toDTO(updatedProduct);
    }
    
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        try {
            // Soft delete
            product.markAsDeleted(getCurrentUsername());
            productRepository.save(product);
            
            // Log success
            auditLogService.log("Product", id, "DELETE", product, null);
            
        } catch (Exception e) {
            // Log failure
            auditLogService.logFailure("Product", id, "DELETE", e.getMessage());
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public List<AuditLog> getProductHistory(Long productId) {
        return auditLogService.getRecentAuditLogs("Product", String.valueOf(productId), 50);
    }
}
```

### 3. Controller with Audit Endpoint

```java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final AuditLogService auditLogService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> create(
            @Valid @RequestBody CreateProductRequest request) {
        ProductDTO product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(product, "Product created"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductDTO product = productService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(product, "Product updated"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted"));
    }
    
    // Audit history endpoint
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getHistory(@PathVariable Long id) {
        List<AuditLog> history = productService.getProductHistory(id);
        return ResponseEntity.ok(ApiResponse.success(history));
    }
    
    // Get all audit logs
    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLog>>> getAuditLogs(
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Pageable pageable) {
        
        Page<AuditLog> page = auditLogService.searchAuditLogs(
            entityName, action, performedBy, startDate, endDate, pageable
        );
        
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }
}
```

---

## What Gets Audited

### Automatic (via AuditEntity)
✅ **Create** - createdAt, createdBy populated  
✅ **Update** - updatedAt, updatedBy populated  
✅ **Soft Delete** - deletedAt, deletedBy populated  
✅ **Version** - Incremented on each update  

### Manual (via AuditLogService)
✅ **Action** - CREATE, UPDATE, DELETE, VIEW, etc.  
✅ **Old/New Values** - Complete state before and after  
✅ **Changed Fields** - List of what changed  
✅ **Request Info** - IP, user agent, URL, HTTP method  
✅ **Status** - SUCCESS or FAILURE  
✅ **Duration** - How long operation took  

---

## Benefits

1. **Compliance** - Meet regulatory requirements
2. **Security** - Track who changed what and when
3. **Debugging** - Trace issues to specific changes
4. **Accountability** - Users accountable for actions
5. **Recovery** - Restore previous values if needed
6. **Analytics** - Understand user behavior
7. **Forensics** - Investigate security incidents

---

## Best Practices

### 1. Always Extend AuditEntity

```java
// ✅ Good
@Entity
public class Product extends AuditEntity { }

// ❌ Bad
@Entity
public class Product {
    @CreatedDate
    private LocalDateTime createdAt;
    // ... manual audit fields
}
```

### 2. Log Important Operations

```java
// ✅ Good - Log business-critical operations
auditLogService.log("Order", orderId, "APPROVE", oldOrder, newOrder);

// ⚠️ Not necessary for every read
// Don't log simple GET operations unless required
```

### 3. Use Async Logging

```java
// Already async - don't worry about performance
auditLogService.log(...); // Non-blocking
```

### 4. Include Context in Details

```java
Map<String, Object> details = Map.of(
    "reason", "Price adjustment",
    "approvedBy", "manager",
    "previousPrice", oldPrice,
    "newPrice", newPrice
);
auditLogService.log("Product", id, "UPDATE", oldProduct, newProduct, details);
```

### 5. Regular Cleanup

```java
// Schedule cleanup job
@Scheduled(cron = "0 0 2 * * *") // 2 AM daily
public void cleanupOldAuditLogs() {
    long deleted = auditLogService.deleteOldAuditLogs(90); // Keep 90 days
    log.info("Deleted {} old audit logs", deleted);
}
```

---

## Query Examples

### Get Product History
```java
List<AuditLog> history = auditLogService.getRecentAuditLogs("Product", "123", 20);
```

### Get User Activity
```java
Page<AuditLog> activity = auditLogService.getAuditLogsByUser(
    "admin",
    PageRequest.of(0, 20)
);
```

### Get Today's Changes
```java
LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

Page<AuditLog> todayLogs = auditLogService.getAuditLogsByDateRange(
    startOfDay, endOfDay, pageable
);
```

### Search Specific Operations
```java
Page<AuditLog> deletions = auditLogService.searchAuditLogs(
    "Product",      // entity
    "DELETE",       // action
    null,           // any user
    startDate,
    endDate,
    pageable
);
```

---

## Directory Structure

```
common/audit/
├── AuditEntity.java
├── AuditorAwareImpl.java
├── AuditLog.java
├── AuditLogRepository.java
├── AuditLogService.java
├── JpaAuditingConfig.java
├── Auditable.java
└── README.md
```

---

## Configuration Required

### 1. Enable Async Support

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("audit-");
        executor.initialize();
        return executor;
    }
}
```

### 2. Jackson ObjectMapper Bean

```java
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
}
```

---

## Summary

✅ **Comprehensive auditing** at two levels  
✅ **Automatic field tracking** via AuditEntity  
✅ **Detailed operation logging** via AuditLogService  
✅ **Complete history** of all changes  
✅ **User accountability** - who did what  
✅ **Timestamp tracking** - when it happened  
✅ **Change tracking** - what changed  
✅ **Request tracking** - where it came from  
✅ **Failure tracking** - errors logged  
✅ **Async logging** - no performance impact  
✅ **Flexible querying** - find anything  
✅ **Easy cleanup** - automated maintenance  

**Everything you need for professional, compliant audit tracking!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.audit

