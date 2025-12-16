# Audit Module - Epic Green ERP

This directory contains **entities and AOP aspect** for comprehensive audit, activity, and error logging in the Epic Green ERP system.

## 📦 Contents

### Entities (audit/entity) - 3 Files
1. **AuditLog.java** - General audit log for tracking system activities
2. **ActivityLog.java** - User activity tracking
3. **ErrorLog.java** - System error tracking

### AOP (audit/dto) - 1 File
1. **AuditAspect.java** - AOP aspect for automatic audit logging

---

## 📊 Database Schema

### Audit Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    AUDIT SYSTEM                         │
│                                                         │
│  ┌────────────────────────────────────────────────┐   │
│  │         AOP Aspect                             │   │
│  │  - Intercept method calls                      │   │
│  │  - Capture parameters                          │   │
│  │  - Capture results                             │   │
│  │  - Capture errors                              │   │
│  └────────────┬─────────────┬─────────────────────┘   │
│               │             │                           │
│               ▼             ▼                           │
│  ┌─────────────────┐   ┌──────────────────┐           │
│  │   Audit Log     │   │   Error Log      │           │
│  │  - Data changes │   │  - Exceptions    │           │
│  │  - User actions │   │  - Stack traces  │           │
│  │  - API calls    │   │  - Error status  │           │
│  └─────────────────┘   └──────────────────┘           │
│                                                         │
│  ┌────────────────────────────────────────────────┐   │
│  │         Activity Log                           │   │
│  │  - User login/logout                           │   │
│  │  - Page views                                  │   │
│  │  - Feature usage                               │   │
│  └────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 1. AuditLog Entity

**Purpose:** Track all system activities, data changes, and user actions

### Key Fields

```java
// Module and entity
- moduleName (ADMIN, SUPPLIER, PRODUCT, WAREHOUSE, etc.)
- entityType (USER, CUSTOMER, PRODUCT, INVOICE, etc.)
- entityId
- entityName

// Action details
- actionType (CREATE, UPDATE, DELETE, VIEW, APPROVE, REJECT, POST, etc.)
- actionDescription
- actionTimestamp

// User information
- userId
- username
- userRole

// Data changes
- oldValues (JSON - before change)
- newValues (JSON - after change)
- changedFields (comma-separated)

// Request details
- ipAddress
- userAgent
- requestMethod (GET, POST, PUT, DELETE)
- requestUrl
- requestParameters (JSON)
- responseStatus
- executionTimeMillis

// Session and device
- sessionId
- deviceType (DESKTOP, MOBILE, TABLET)
- deviceId
- location (city, country)

// Status
- isSuccessful
- errorMessage (if not successful)

// Categorization
- tags
- notes
```

### Helper Methods

```java
Double getExecutionTimeSeconds(); // Convert millis to seconds
boolean isCreateAction(); // actionType == CREATE
boolean isUpdateAction(); // actionType == UPDATE
boolean isDeleteAction(); // actionType == DELETE
boolean isViewAction(); // actionType == VIEW
Long getHoursSinceAction(); // Time since action
```

### Action Types

- **CREATE** - Record created
- **UPDATE** - Record updated
- **DELETE** - Record deleted
- **VIEW** - Record viewed
- **APPROVE** - Record approved
- **REJECT** - Record rejected
- **POST** - Record posted
- **CANCEL** - Record cancelled
- **EXECUTE** - Other actions

---

## 🔧 2. ActivityLog Entity

**Purpose:** Track user activities (login, logout, page views, feature usage)

### Key Fields

```java
// User information
- userId
- username
- userRole

// Activity details
- activityType (LOGIN, LOGOUT, PAGE_VIEW, FEATURE_USED, DOWNLOAD, UPLOAD, etc.)
- activityDescription
- activityTimestamp

// Module and feature
- moduleName
- featureName

// Page details
- pageUrl
- pageTitle
- referrerUrl

// Session and device
- sessionId
- ipAddress
- userAgent
- deviceType (DESKTOP, MOBILE, TABLET)
- deviceId
- operatingSystem
- browser
- location

// Duration tracking
- durationMillis
- startTime
- endTime

// Status
- isSuccessful
- errorMessage

// Additional data
- metadata (JSON)
- tags
- notes
```

### Helper Methods

```java
Double getDurationSeconds(); // Convert millis to seconds
Double getDurationMinutes(); // Convert millis to minutes
boolean isLoginActivity(); // activityType == LOGIN
boolean isLogoutActivity(); // activityType == LOGOUT
boolean isPageView(); // activityType == PAGE_VIEW
boolean isMobileDevice(); // deviceType == MOBILE
Long getHoursSinceActivity(); // Time since activity
```

### Activity Types

- **LOGIN** - User login
- **LOGOUT** - User logout
- **PAGE_VIEW** - Page viewed
- **FEATURE_USED** - Feature used
- **DOWNLOAD** - File downloaded
- **UPLOAD** - File uploaded
- **SEARCH** - Search performed
- **EXPORT** - Data exported
- **PRINT** - Document printed
- **REPORT_GENERATED** - Report generated

---

## 💳 3. ErrorLog Entity

**Purpose:** Track system errors, exceptions, and failures

### Key Fields

```java
// Error details
- errorTimestamp
- severityLevel (CRITICAL, HIGH, MEDIUM, LOW, INFO)
- errorType (EXCEPTION, VALIDATION, BUSINESS, DATABASE, NETWORK, SECURITY, etc.)
- errorCode
- errorMessage

// Exception details
- exceptionClass
- stackTrace
- rootCause

// Location
- moduleName
- className
- methodName
- lineNumber

// User information
- userId
- username

// Request details
- requestUrl
- requestMethod
- requestParameters (JSON)
- requestBody (JSON)
- responseStatus

// Session and device
- ipAddress
- userAgent
- sessionId
- deviceType

// Environment
- serverName
- applicationVersion
- environment (PRODUCTION, STAGING, DEVELOPMENT)

// Status and resolution
- status (NEW, ACKNOWLEDGED, IN_PROGRESS, RESOLVED, CLOSED)
- assignedTo
- assignedToUsername
- resolvedAt
- resolvedBy
- resolvedByUsername
- resolutionNotes

// Occurrence tracking
- occurrenceCount
- firstOccurrence
- lastOccurrence

// Notification
- isNotified
- notificationSentAt

// Categorization
- tags
- notes
```

### Helper Methods

```java
boolean isCritical(); // severityLevel == CRITICAL
boolean isHighSeverity(); // severityLevel == HIGH
boolean isNew(); // status == NEW
boolean isResolved(); // status == RESOLVED or CLOSED
Long getResolutionTimeHours(); // Time to resolve
Long getHoursSinceError(); // Time since error
boolean shouldNotify(); // Should send notification
```

### Severity Levels

- **CRITICAL** - System-wide failure, immediate attention required
- **HIGH** - Major functionality affected, needs urgent fix
- **MEDIUM** - Moderate impact, can be addressed in next cycle
- **LOW** - Minor issue, low priority
- **INFO** - Informational, not an error

### Error Types

- **EXCEPTION** - General exception
- **VALIDATION** - Validation error
- **BUSINESS** - Business logic error
- **DATABASE** - Database error
- **NETWORK** - Network error
- **SECURITY** - Security violation
- **CONFIGURATION** - Configuration error

---

## 🎯 4. AuditAspect (AOP)

**Purpose:** Automatic audit logging using Aspect-Oriented Programming

### Annotations

#### @Audited
Mark methods that should be audited:

```java
@Audited(
    module = "SALES",
    action = "CREATE",
    description = "Create sales order",
    logParameters = true,
    logResult = true
)
public SalesOrder createOrder(SalesOrderRequest request) {
    // Method implementation
}
```

**Parameters:**
- `module` - Module name (ADMIN, SALES, PURCHASE, etc.)
- `action` - Action type (CREATE, UPDATE, DELETE, etc.)
- `description` - Human-readable description
- `logParameters` - Log method parameters
- `logResult` - Log method result

#### @AuditedEntity
Mark entity classes that should be audited:

```java
@AuditedEntity(
    entityType = "SALES_ORDER",
    moduleName = "SALES"
)
@Entity
public class SalesOrder {
    // Entity fields
}
```

### Features

✅ **Automatic logging** - No manual logging code needed  
✅ **Method interception** - Intercepts all @Audited methods  
✅ **Parameter logging** - Logs method parameters (optional)  
✅ **Result logging** - Logs method results (optional)  
✅ **Error logging** - Automatically logs errors  
✅ **Execution time** - Tracks method execution time  
✅ **User tracking** - Captures current user details  
✅ **Request tracking** - Captures HTTP request details  
✅ **IP tracking** - Captures client IP address  

### How It Works

1. **Method call** → AOP intercepts
2. **Before execution** → Capture parameters, user, request details
3. **Execute method** → Execute original method
4. **After execution** → Capture result, execution time
5. **Save audit log** → Save to database asynchronously
6. **On error** → Log error to error log table

---

## 💡 Usage Examples

### Example 1: Basic Audit Logging

```java
@Service
public class SalesOrderService {
    
    @Audited(
        module = "SALES",
        action = "CREATE",
        description = "Create sales order"
    )
    public SalesOrder createOrder(SalesOrderRequest request) {
        // Create order logic
        SalesOrder order = new SalesOrder();
        // ... set fields
        return salesOrderRepository.save(order);
    }
    
    @Audited(
        module = "SALES",
        action = "UPDATE",
        description = "Update sales order"
    )
    public SalesOrder updateOrder(Long id, SalesOrderRequest request) {
        // Update order logic
        SalesOrder order = findById(id);
        // ... update fields
        return salesOrderRepository.save(order);
    }
    
    @Audited(
        module = "SALES",
        action = "DELETE",
        description = "Delete sales order"
    )
    public void deleteOrder(Long id) {
        // Delete order logic
        salesOrderRepository.deleteById(id);
    }
}
```

### Example 2: Audit with Parameter and Result Logging

```java
@Service
public class PaymentService {
    
    @Audited(
        module = "PAYMENT",
        action = "CREATE",
        description = "Process payment",
        logParameters = true,  // Log payment details
        logResult = true       // Log payment result
    )
    public Payment processPayment(PaymentRequest request) {
        // Process payment
        Payment payment = new Payment();
        // ... process payment
        return paymentRepository.save(payment);
    }
}
```

### Example 3: Manual Audit Logging

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final AuditLogRepository auditLogRepository;
    
    public void updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = findById(productId);
        BigDecimal oldPrice = product.getUnitPrice();
        
        // Update price
        product.setUnitPrice(newPrice);
        productRepository.save(product);
        
        // Manual audit log
        AuditLog auditLog = AuditLog.builder()
            .moduleName("PRODUCT")
            .entityType("PRODUCT")
            .entityId(productId)
            .entityName(product.getProductName())
            .actionType("UPDATE")
            .actionDescription("Update product price")
            .actionTimestamp(LocalDateTime.now())
            .oldValues(String.format("{\"unitPrice\": %s}", oldPrice))
            .newValues(String.format("{\"unitPrice\": %s}", newPrice))
            .changedFields("unitPrice")
            .userId(getCurrentUserId())
            .username(getCurrentUsername())
            .build();
        
        auditLogRepository.save(auditLog);
    }
}
```

### Example 4: Activity Logging (Login/Logout)

```java
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final ActivityLogRepository activityLogRepository;
    
    public void logLogin(User user, HttpServletRequest request) {
        ActivityLog activityLog = ActivityLog.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .userRole(user.getRole())
            .activityType("LOGIN")
            .activityDescription("User logged in")
            .activityTimestamp(LocalDateTime.now())
            .ipAddress(getClientIp(request))
            .userAgent(request.getHeader("User-Agent"))
            .deviceType(detectDeviceType(request))
            .sessionId(request.getSession().getId())
            .isSuccessful(true)
            .build();
        
        activityLogRepository.save(activityLog);
    }
    
    public void logLogout(User user) {
        ActivityLog activityLog = ActivityLog.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .activityType("LOGOUT")
            .activityDescription("User logged out")
            .activityTimestamp(LocalDateTime.now())
            .isSuccessful(true)
            .build();
        
        activityLogRepository.save(activityLog);
    }
}
```

### Example 5: Page View Tracking

```java
@Component
@RequiredArgsConstructor
public class PageViewInterceptor implements HandlerInterceptor {
    
    private final ActivityLogRepository activityLogRepository;
    
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        User currentUser = getCurrentUser();
        
        ActivityLog activityLog = ActivityLog.builder()
            .userId(currentUser.getId())
            .username(currentUser.getUsername())
            .activityType("PAGE_VIEW")
            .activityDescription("Page viewed")
            .activityTimestamp(LocalDateTime.now())
            .pageUrl(request.getRequestURI())
            .referrerUrl(request.getHeader("Referer"))
            .ipAddress(getClientIp(request))
            .userAgent(request.getHeader("User-Agent"))
            .sessionId(request.getSession().getId())
            .startTime(LocalDateTime.now())
            .build();
        
        // Store in request attribute for post-processing
        request.setAttribute("activityLog", activityLog);
        
        return true;
    }
    
    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex
    ) {
        ActivityLog activityLog = (ActivityLog) request.getAttribute("activityLog");
        
        if (activityLog != null) {
            activityLog.setEndTime(LocalDateTime.now());
            activityLogRepository.save(activityLog);
        }
    }
}
```

### Example 6: Error Logging with Exception Handler

```java
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    
    private final ErrorLogRepository errorLogRepository;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
        Exception ex,
        HttpServletRequest request
    ) {
        // Log error
        ErrorLog errorLog = ErrorLog.builder()
            .errorTimestamp(LocalDateTime.now())
            .severityLevel(determineSeverity(ex))
            .errorType(determineType(ex))
            .errorMessage(ex.getMessage())
            .exceptionClass(ex.getClass().getName())
            .stackTrace(Arrays.toString(ex.getStackTrace()))
            .moduleName(extractModule(request))
            .requestUrl(request.getRequestURI())
            .requestMethod(request.getMethod())
            .ipAddress(getClientIp(request))
            .status("NEW")
            .build();
        
        errorLogRepository.save(errorLog);
        
        // Return error response
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

### Example 7: Query Audit Logs

```java
@Service
@RequiredArgsConstructor
public class AuditReportService {
    
    private final AuditLogRepository auditLogRepository;
    
    // Get all actions by user
    public List<AuditLog> getUserActions(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        return auditLogRepository.findByUserIdAndActionTimestampBetween(
            userId,
            startOfDay,
            endOfDay
        );
    }
    
    // Get all changes to an entity
    public List<AuditLog> getEntityHistory(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByActionTimestampDesc(
            entityType,
            entityId
        );
    }
    
    // Get all failed actions
    public List<AuditLog> getFailedActions(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        return auditLogRepository.findByIsSuccessfulAndActionTimestampBetween(
            false,
            startOfDay,
            endOfDay
        );
    }
}
```

### Example 8: Monitor Critical Errors

```java
@Service
@RequiredArgsConstructor
public class ErrorMonitorService {
    
    private final ErrorLogRepository errorLogRepository;
    private final NotificationService notificationService;
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void monitorCriticalErrors() {
        // Find unnotified critical errors
        List<ErrorLog> criticalErrors = errorLogRepository
            .findBySeverityLevelAndIsNotified("CRITICAL", false);
        
        for (ErrorLog error : criticalErrors) {
            // Send notification to admin
            notificationService.sendErrorAlert(error);
            
            // Mark as notified
            error.setIsNotified(true);
            error.setNotificationSentAt(LocalDateTime.now());
            errorLogRepository.save(error);
        }
    }
    
    // Get error statistics
    public Map<String, Long> getErrorStatistics(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        List<ErrorLog> errors = errorLogRepository
            .findByErrorTimestampBetween(startOfDay, endOfDay);
        
        return errors.stream()
            .collect(Collectors.groupingBy(
                ErrorLog::getSeverityLevel,
                Collectors.counting()
            ));
    }
}
```

---

## 📁 Directory Structure

```
audit/
├── entity/
│   ├── AuditLog.java
│   ├── ActivityLog.java
│   ├── ErrorLog.java
│   └── README.md
└── dto/
    └── AuditAspect.java
```

---

## ✅ Summary

✅ **3 Entity classes** - Complete audit system  
✅ **1 AOP Aspect** - Automatic audit logging  
✅ **Audit logging** - Track all data changes and actions  
✅ **Activity logging** - Track user activities (login, page views, etc.)  
✅ **Error logging** - Track all system errors and exceptions  
✅ **Automatic logging** - @Audited annotation for methods  
✅ **Parameter logging** - Log method parameters  
✅ **Result logging** - Log method results  
✅ **Error tracking** - Automatic error logging via AOP  
✅ **User tracking** - Capture user details automatically  
✅ **Request tracking** - Capture HTTP request details  
✅ **IP tracking** - Capture client IP address  
✅ **Session tracking** - Track user sessions  
✅ **Device tracking** - Track device type and ID  
✅ **Execution time** - Track method execution time  
✅ **Data comparison** - Track old vs new values  
✅ **Changed fields** - Track which fields changed  
✅ **Severity levels** - CRITICAL, HIGH, MEDIUM, LOW, INFO  
✅ **Error types** - EXCEPTION, VALIDATION, BUSINESS, DATABASE, etc.  
✅ **Error status** - NEW, ACKNOWLEDGED, IN_PROGRESS, RESOLVED, CLOSED  
✅ **Error assignment** - Assign errors to users  
✅ **Error notification** - Auto-notify admins of critical errors  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade audit system  

**Everything you need for complete audit, activity, and error logging with automatic AOP-based logging, comprehensive tracking of all system activities, user actions, data changes, errors, and exceptions in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.audit
