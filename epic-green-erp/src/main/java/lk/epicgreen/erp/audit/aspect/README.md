# Audit Module - Aspect, Repository & Service

This directory contains **aspect, repository, and service layers** for the audit system in Epic Green ERP.

## 📦 Contents

### Aspect (audit/aspect) - 1 File
1. **AuditAspect.java** - AOP aspect for automatic audit logging

### Repository (audit/repository) - 3 Files
1. **AuditLogRepository.java** - Audit log data access
2. **ActivityLogRepository.java** - Activity log data access
3. **ErrorLogRepository.java** - Error log data access

### Service (audit/service) - 2 Files
1. **AuditService.java** - Audit service interface
2. **AuditServiceImpl.java** - Audit service implementation

---

## 🎯 1. AuditAspect (AOP)

**Purpose:** Automatic audit logging using Aspect-Oriented Programming

### Features

✅ **@Audited annotation** - Mark methods for audit logging  
✅ **@AuditedEntity annotation** - Mark entities for audit  
✅ **Automatic interception** - Intercepts method calls  
✅ **Parameter logging** - Log method parameters (optional)  
✅ **Result logging** - Log method results (optional)  
✅ **Error logging** - Automatic error logging  
✅ **Execution time tracking** - Track method execution time  
✅ **User extraction** - Auto-extract current user  
✅ **Request extraction** - Auto-extract HTTP request details  
✅ **IP extraction** - Auto-extract client IP (proxy support)  
✅ **Device detection** - Detect device type (DESKTOP, MOBILE, TABLET)  
✅ **Async saving** - Non-blocking save operations  

### Annotations

#### @Audited
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
- `logParameters` - Log method parameters (default: false)
- `logResult` - Log method result (default: false)

#### @AuditedEntity
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

### How It Works

1. **Method call** → AOP intercepts
2. **Before execution** → Capture parameters, user, request
3. **Execute method** → Run original method
4. **After execution** → Capture result, execution time
5. **Save audit log** → Save asynchronously
6. **On error** → Log to error log table

---

## 📊 2. Repositories

### AuditLogRepository

**Purpose:** Data access for audit logs

**Key Methods:**
- **Find by user:** `findByUserId()`, `findByUsername()`
- **Find by module:** `findByModuleName()`
- **Find by action:** `findByActionType()`
- **Find by entity:** `findByEntityTypeAndEntityId()`
- **Find by date range:** `findByActionTimestampBetween()`
- **Entity history:** `findByEntityTypeAndEntityIdOrderByActionTimestampDesc()`
- **Failed actions:** `findByIsSuccessful(false)`
- **Slow queries:** `findSlowQueries(thresholdMillis)`
- **Search:** `searchAuditLogs(keyword)`
- **Statistics:** `getMostActiveUsers()`, `getMostUsedModules()`, etc.
- **Activity:** `getHourlyActivity()`, `getDailyActivity()`

**Total:** 50+ query methods

### ActivityLogRepository

**Purpose:** Data access for activity logs

**Key Methods:**
- **Find by user:** `findByUserId()`, `findByUsername()`
- **Find by type:** `findByActivityType()`
- **Find by session:** `findBySessionId()`
- **Login activities:** `findLoginActivitiesByUserId()`
- **Last login:** `findLastLoginByUserId()`
- **Failed logins:** `findFailedLoginAttempts()`
- **Page views:** `findPageViewsByUserId()`, `findMostViewedPages()`
- **Current sessions:** `findCurrentUserSessions()`
- **Statistics:** `getMostActiveUsers()`, `getActivityTypeDistribution()`, etc.
- **Device stats:** `getDeviceTypeDistribution()`
- **Activity:** `getHourlyActivity()`, `getDailyActivity()`

**Total:** 40+ query methods

### ErrorLogRepository

**Purpose:** Data access for error logs

**Key Methods:**
- **Find by severity:** `findBySeverityLevel()`
- **Find by type:** `findByErrorType()`
- **Find by status:** `findByStatus()`
- **Find by module:** `findByModuleName()`
- **Critical errors:** `findBySeverityLevel("CRITICAL")`
- **Unnotified:** `findUnnotifiedCriticalErrors()`
- **Unresolved:** `findByStatus("NEW")`
- **Frequent errors:** `getFrequentErrors(threshold)`
- **Statistics:** `getErrorSeverityDistribution()`, `getErrorTypeDistribution()`, etc.
- **Error tracking:** `getTopExceptions()`, `getMostErrorProneModules()`
- **Resolution:** `getAverageResolutionTimeMinutes()`

**Total:** 35+ query methods

---

## 🔧 3. Service Layer

### AuditService Interface

**Purpose:** Define audit service contract

**Method Categories:**
1. **Audit Log Operations** (20 methods)
2. **Activity Log Operations** (15 methods)
3. **Statistics and Reports** (20 methods)
4. **Error Log Operations** (12 methods)

**Total:** 67 methods

### AuditServiceImpl Implementation

**Purpose:** Implement audit service business logic

**Key Features:**
- ✅ **Complete CRUD** - Create, read, update, delete operations
- ✅ **Search & filter** - Comprehensive search capabilities
- ✅ **Statistics** - Detailed statistics and reports
- ✅ **Date ranges** - Query by date ranges
- ✅ **User activities** - Track user activities
- ✅ **Error management** - Manage errors and resolutions
- ✅ **Async operations** - Non-blocking operations
- ✅ **Transaction management** - Proper @Transactional usage

---

## 💡 Usage Examples

### Example 1: Audit Logging with @Audited

```java
@Service
public class SalesOrderService {
    
    @Audited(
        module = "SALES",
        action = "CREATE",
        description = "Create sales order",
        logParameters = true,
        logResult = true
    )
    public SalesOrder createOrder(SalesOrderRequest request) {
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
        salesOrderRepository.deleteById(id);
    }
}
```

### Example 2: Get Entity History

```java
@Service
@RequiredArgsConstructor
public class AuditReportService {
    
    private final AuditService auditService;
    
    public List<AuditLog> getProductHistory(Long productId) {
        return auditService.getEntityHistory("PRODUCT", productId);
    }
    
    public List<AuditLog> getCustomerHistory(Long customerId) {
        return auditService.getEntityHistory("CUSTOMER", customerId);
    }
}
```

### Example 3: Get User Activity Summary

```java
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    
    private final AuditService auditService;
    
    @GetMapping("/users/{userId}/activity")
    public ResponseEntity<Map<String, Object>> getUserActivity(@PathVariable Long userId) {
        Map<String, Object> summary = auditService.getUserActivitySummary(userId);
        return ResponseEntity.ok(summary);
    }
}
```

### Example 4: Get Audit Statistics

```java
@GetMapping("/statistics")
public ResponseEntity<Map<String, Object>> getAuditStatistics(
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
) {
    Map<String, Object> stats;
    
    if (startDate != null && endDate != null) {
        stats = auditService.getAuditStatistics(startDate, endDate);
    } else {
        stats = auditService.getAuditStatistics();
    }
    
    return ResponseEntity.ok(stats);
}
```

### Example 5: Get Most Active Users

```java
@GetMapping("/most-active-users")
public ResponseEntity<List<Map<String, Object>>> getMostActiveUsers() {
    List<Map<String, Object>> users = auditService.getMostActiveUsers();
    return ResponseEntity.ok(users);
}
```

### Example 6: Get Failed Login Attempts

```java
@GetMapping("/failed-logins")
public ResponseEntity<Page<ActivityLog>> getFailedLogins(Pageable pageable) {
    Page<ActivityLog> failedLogins = auditService.getFailedLoginAttempts(pageable);
    return ResponseEntity.ok(failedLogins);
}
```

### Example 7: Manage Errors

```java
@Service
@RequiredArgsConstructor
public class ErrorManagementService {
    
    private final AuditService auditService;
    
    public List<ErrorLog> getCriticalErrors() {
        return auditService.getCriticalErrors();
    }
    
    public ErrorLog assignError(Long errorId, Long userId) {
        return auditService.assignError(errorId, userId);
    }
    
    public ErrorLog resolveError(Long errorId, String resolutionNotes) {
        return auditService.resolveError(errorId, resolutionNotes);
    }
}
```

### Example 8: Scheduled Cleanup

```java
@Component
@RequiredArgsConstructor
public class AuditCleanupScheduler {
    
    private final AuditService auditService;
    
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    public void cleanupOldLogs() {
        // Keep audit logs for 90 days
        auditService.deleteOldAuditLogs(90);
        
        // Keep activity logs for 180 days
        auditService.deleteOldActivityLogs(180);
        
        log.info("Audit log cleanup completed");
    }
}
```

### Example 9: Monitor Critical Errors

```java
@Component
@RequiredArgsConstructor
public class ErrorMonitoringService {
    
    private final ErrorLogRepository errorLogRepository;
    private final NotificationService notificationService;
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void monitorCriticalErrors() {
        List<ErrorLog> criticalErrors = errorLogRepository.findUnnotifiedCriticalErrors();
        
        for (ErrorLog error : criticalErrors) {
            // Send notification
            notificationService.sendCriticalErrorAlert(error);
            
            // Mark as notified
            error.setIsNotified(true);
            error.setNotificationSentAt(LocalDateTime.now());
            errorLogRepository.save(error);
        }
    }
}
```

### Example 10: Get Dashboard Statistics

```java
@GetMapping("/dashboard")
public ResponseEntity<Map<String, Object>> getDashboardStats() {
    Map<String, Object> dashboard = new HashMap<>();
    
    // Audit statistics
    dashboard.put("auditStats", auditService.getAuditStatistics());
    
    // Activity statistics
    dashboard.put("activityStats", auditService.getActivityStatistics());
    
    // Most active users (top 10)
    dashboard.put("mostActiveUsers", 
        auditService.getMostActiveUsers().stream().limit(10).collect(Collectors.toList()));
    
    // Most used modules
    dashboard.put("mostUsedModules", auditService.getMostUsedModules());
    
    // Error statistics
    dashboard.put("criticalErrors", auditService.getCriticalErrors().size());
    dashboard.put("unresolvedErrors", auditService.getUnresolvedErrors().size());
    
    // Today's activity
    dashboard.put("todayActivity", auditService.getTodayAuditLogs().size());
    
    return ResponseEntity.ok(dashboard);
}
```

---

## 📁 Directory Structure

```
audit/
├── aspect/
│   └── AuditAspect.java
├── repository/
│   ├── AuditLogRepository.java
│   ├── ActivityLogRepository.java
│   ├── ErrorLogRepository.java
│   └── README.md
└── service/
    ├── AuditService.java
    ├── AuditServiceImpl.java
    └── README.md
```

---

## ✅ Summary

✅ **1 AOP Aspect** - Automatic audit logging  
✅ **3 Repositories** - Comprehensive data access (125+ query methods)  
✅ **2 Service files** - Business logic layer (67 methods)  
✅ **@Audited annotation** - Simple method annotation  
✅ **@AuditedEntity annotation** - Entity-level configuration  
✅ **Automatic logging** - No manual code needed  
✅ **Parameter & result logging** - Optional detailed logging  
✅ **Error interception** - Auto-log all service errors  
✅ **User tracking** - Auto-extract current user  
✅ **Request tracking** - Auto-extract HTTP request  
✅ **IP tracking** - Auto-extract client IP (proxy support)  
✅ **Device detection** - Auto-detect device type  
✅ **Execution time** - Track method execution  
✅ **Entity history** - Complete audit trail  
✅ **User activities** - Login, logout, page views  
✅ **Failed logins** - Security monitoring  
✅ **Error management** - Assign, resolve, track  
✅ **Statistics** - Comprehensive reports  
✅ **Search & filter** - Advanced search  
✅ **Date ranges** - Flexible date filtering  
✅ **Pagination** - Page-based results  
✅ **Async operations** - Non-blocking saves  
✅ **Transaction management** - Proper @Transactional  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete audit logging with automatic AOP-based logging (@Audited annotation), comprehensive data access (125+ query methods), business logic layer (67 service methods), complete error management, detailed statistics, and user activity tracking in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.audit
