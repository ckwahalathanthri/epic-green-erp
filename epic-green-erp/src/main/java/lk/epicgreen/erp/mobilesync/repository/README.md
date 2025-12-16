# Mobile Sync Module - Repository, Service & Controller

This directory contains **repository, service, and controller layers** for mobile synchronization in Epic Green ERP.

## 📦 Contents

### Repository (mobilesync/repository) - 2 Files
1. **SyncLogRepository.java** - Sync log data access
2. **SyncQueueRepository.java** - Sync queue data access

### Service (mobilesync/service) - 2 Files
1. **SyncService.java** - Sync service interface
2. **SyncServiceImpl.java** - Sync service implementation

### Controller (mobilesync/controller) - 1 File
1. **MobileSyncController.java** - REST controller for sync endpoints

---

## 📊 1. Repositories

### SyncLogRepository

**Purpose:** Data access for sync logs - track all synchronization operations

**Key Methods (70+):**
- **Find by fields:** Type, direction, status, entity type, user, device, session
- **Find by status:** Successful, failed, pending, in progress
- **Find by conflicts:** Has conflicts, unresolved conflicts
- **Find by date range:** Between dates, by timestamp
- **Session tracking:** By session ID, session history
- **Last sync:** Last successful sync by user/entity, last sync by device
- **Retry:** Requiring retry, slow syncs
- **Statistics:** Count by type/status, type distribution, direction distribution
- **Activity:** Hourly/daily sync count, most active users/devices
- **Metrics:** Average duration, average records synced, total records, success rate
- **Search:** Full-text search
- **Cleanup:** Delete old sync logs

**Total:** 70+ query methods

### SyncQueueRepository

**Purpose:** Data access for sync queue - manage pending synchronization operations

**Key Methods (85+):**
- **Find by fields:** Type, direction, status, priority, entity type, user, device
- **Find by entity:** By entity type and ID, by entity reference
- **Find by status:** Pending, processing, completed, failed, cancelled
- **Queue processing:** Ready to process, high priority, stuck queues
- **Retry:** Failed queues for retry
- **User/Device:** User pending, device pending, recent by user/device
- **Update operations:** Update status, update retry info, mark as processing/completed/failed
- **Cancel operations:** Cancel queue, cancel user/device pending queues
- **Statistics:** Count by type/status/priority, type/status/priority distribution
- **Activity:** Hourly/daily queue count, most active users/devices
- **Metrics:** Average processing time
- **Search:** Full-text search
- **Cleanup:** Delete completed/cancelled queues

**Total:** 85+ query methods

---

## 🔧 2. Service Layer

### SyncService Interface

**Purpose:** Define mobile sync service contract

**Method Categories (110+ methods):**
1. **Sync Operations** (6 methods) - synchronize, push, pull, full sync, incremental, force
2. **Entity-Specific Sync** (7 methods) - customers, products, sales orders, payments, inventory, price lists, sales reps
3. **Sync Queue Operations** (13 methods) - add, process, retry, get, cancel
4. **Sync Log Operations** (15 methods) - create, update, get, search
5. **Conflict Resolution** (6 methods) - detect, resolve (server/client/manual), unresolved
6. **Sync Status & Monitoring** (7 methods) - status, progress, last sync time, in progress check
7. **Data Validation** (3 methods) - validate data, entity data, integrity check
8. **Bulk Operations** (3 methods) - bulk sync, push, pull
9. **Statistics** (14 methods) - sync stats, distributions, most active, success rate
10. **Cleanup** (4 methods) - delete old logs/queues
11. **Offline Support** (4 methods) - queue offline, get offline, sync offline, clear

**Total:** 110+ methods

### SyncServiceImpl Implementation

**Purpose:** Implement mobile sync service business logic

**Key Features:**
- **Bidirectional sync** - Push and pull operations
- **Full sync** - Complete data synchronization
- **Incremental sync** - Only changes since last sync
- **Force sync** - Server always wins
- **Entity-specific** - 7 entity types supported
- **Queue management** - Priority-based processing (HIGH/MEDIUM/LOW)
- **Async processing** - Non-blocking operations
- **Retry mechanism** - Exponential backoff for failures
- **Conflict detection** - Automatic conflict detection
- **Conflict resolution** - Server wins, client wins, manual merge
- **Offline support** - Queue offline changes for later sync
- **Statistics** - Comprehensive sync analytics
- **Monitoring** - Real-time sync status and progress
- **Validation** - Data validation before sync
- **Cleanup** - Automatic cleanup of old data

**Sync Directions:**
- **PUSH** - Mobile → Server (upload)
- **PULL** - Server → Mobile (download)
- **BIDIRECTIONAL** - Both directions (full sync)

**Sync Types:**
- **FULL** - Complete synchronization
- **INCREMENTAL** - Only changes since last sync
- **OFFLINE_CHANGE** - Offline data to sync

**Queue Priorities:**
- **HIGH** - Process immediately
- **MEDIUM** - Normal processing
- **LOW** - Process when idle

**Status Values:**
- **PENDING** - Waiting to be processed
- **IN_PROGRESS** - Currently processing
- **SUCCESS** - Completed successfully
- **FAILED** - Failed with error
- **CANCELLED** - Cancelled by user/system
- **CONFLICT** - Conflict detected

---

## 🌐 3. Controller Layer

### MobileSyncController

**Purpose:** REST API endpoints for mobile synchronization

**Endpoint Categories (50+ endpoints):**

**Sync Operations:**
- `POST /api/mobile-sync/synchronize` - Bidirectional sync
- `POST /api/mobile-sync/push` - Push data to server
- `POST /api/mobile-sync/pull` - Pull data from server
- `POST /api/mobile-sync/full-sync` - Full synchronization
- `POST /api/mobile-sync/incremental-sync` - Incremental sync
- `POST /api/mobile-sync/force-sync` - Force sync

**Entity-Specific Sync:**
- `POST /api/mobile-sync/sync/customers` - Sync customers
- `POST /api/mobile-sync/sync/products` - Sync products
- `POST /api/mobile-sync/sync/sales-orders` - Sync sales orders
- `POST /api/mobile-sync/sync/payments` - Sync payments
- `POST /api/mobile-sync/sync/inventory` - Sync inventory
- `POST /api/mobile-sync/sync/price-lists` - Sync price lists

**Sync Queue:**
- `POST /api/mobile-sync/queue` - Add to queue
- `POST /api/mobile-sync/queue/process` - Process queue
- `POST /api/mobile-sync/queue/process-pending` - Process pending
- `POST /api/mobile-sync/queue/process-high-priority` - Process high priority
- `POST /api/mobile-sync/queue/retry-failed` - Retry failed
- `GET /api/mobile-sync/queue` - Get all queues
- `GET /api/mobile-sync/queue/{id}` - Get by ID
- `GET /api/mobile-sync/queue/pending` - Get pending
- `GET /api/mobile-sync/queue/user/{userId}` - Get user queues
- `GET /api/mobile-sync/queue/device/{deviceId}` - Get device queues
- `DELETE /api/mobile-sync/queue/{id}` - Cancel queue
- `DELETE /api/mobile-sync/queue/user/{userId}/pending` - Cancel user pending

**Sync Logs:**
- `GET /api/mobile-sync/logs` - Get all logs
- `GET /api/mobile-sync/logs/{id}` - Get by ID
- `GET /api/mobile-sync/logs/user/{userId}` - Get user logs
- `GET /api/mobile-sync/logs/device/{deviceId}` - Get device logs
- `GET /api/mobile-sync/logs/status/{status}` - Get by status
- `GET /api/mobile-sync/logs/successful` - Get successful
- `GET /api/mobile-sync/logs/failed` - Get failed
- `GET /api/mobile-sync/logs/conflicts` - Get with conflicts
- `GET /api/mobile-sync/logs/search` - Search logs

**Conflict Resolution:**
- `POST /api/mobile-sync/conflicts/{logId}/resolve/server-wins` - Server wins
- `POST /api/mobile-sync/conflicts/{logId}/resolve/client-wins` - Client wins
- `POST /api/mobile-sync/conflicts/{logId}/resolve/manual` - Manual merge
- `GET /api/mobile-sync/conflicts/unresolved` - Get unresolved
- `GET /api/mobile-sync/conflicts/user/{userId}/unresolved` - User unresolved

**Status & Monitoring:**
- `GET /api/mobile-sync/status/user/{userId}` - User sync status
- `GET /api/mobile-sync/status/device/{deviceId}` - Device sync status
- `GET /api/mobile-sync/progress/{sessionId}` - Sync progress
- `GET /api/mobile-sync/last-sync-time` - Last sync time

**Statistics:**
- `GET /api/mobile-sync/statistics` - Get statistics
- `GET /api/mobile-sync/statistics/user/{userId}` - User statistics
- `GET /api/mobile-sync/statistics/device/{deviceId}` - Device statistics
- `GET /api/mobile-sync/statistics/dashboard` - Dashboard stats

**Offline Support:**
- `POST /api/mobile-sync/offline/queue` - Queue offline change
- `GET /api/mobile-sync/offline/changes` - Get offline changes
- `POST /api/mobile-sync/offline/sync` - Sync offline changes
- `DELETE /api/mobile-sync/offline/clear` - Clear offline changes

**Bulk Operations:**
- `POST /api/mobile-sync/bulk/synchronize` - Bulk synchronize

**Total:** 50+ REST endpoints

---

## 💡 Usage Examples

### Example 1: Full Synchronization

```java
@RestController
@RequiredArgsConstructor
public class MobileController {
    
    private final SyncService syncService;
    
    @PostMapping("/mobile/sync-all")
    public ResponseEntity<?> syncAll(
        @RequestParam Long userId,
        @RequestParam String deviceId
    ) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .syncType("FULL")
            .syncDirection("BIDIRECTIONAL")
            .build();
        
        SyncResponse response = syncService.fullSync(request);
        return ResponseEntity.ok(response);
    }
}
```

### Example 2: Push Data to Server

```java
SyncRequest request = SyncRequest.builder()
    .userId(userId)
    .deviceId(deviceId)
    .entityType("SALES_ORDER")
    .syncType("INCREMENTAL")
    .syncDirection("PUSH")
    .data(salesOrderData)
    .build();

SyncResponse response = syncService.pushData(request);
```

### Example 3: Pull Data from Server

```java
LocalDateTime lastSyncTime = syncService.getLastSyncTime(userId, "CUSTOMER");

SyncRequest request = SyncRequest.builder()
    .userId(userId)
    .deviceId(deviceId)
    .entityType("CUSTOMER")
    .syncType("INCREMENTAL")
    .syncDirection("PULL")
    .lastSyncTime(lastSyncTime)
    .build();

SyncResponse response = syncService.pullData(request);
```

### Example 4: Entity-Specific Sync

```java
// Sync customers
SyncResponse customersResponse = syncService.syncCustomers(userId, deviceId, lastSyncTime);

// Sync products
SyncResponse productsResponse = syncService.syncProducts(userId, deviceId, lastSyncTime);

// Sync sales orders
SyncResponse ordersResponse = syncService.syncSalesOrders(userId, deviceId, lastSyncTime);
```

### Example 5: Queue Offline Changes

```java
// Queue offline sales order
Map<String, Object> orderData = new HashMap<>();
orderData.put("customerId", 123L);
orderData.put("totalAmount", 5000.00);

SyncQueue queue = syncService.queueOfflineChange(
    "SALES_ORDER",
    null, // New order, no ID yet
    "CREATE",
    orderData,
    userId,
    deviceId
);
```

### Example 6: Sync Offline Changes

```java
// Sync all offline changes when online
SyncResponse response = syncService.syncOfflineChanges(userId, deviceId);

System.out.println("Synced " + response.getRecordsSynced() + " offline changes");
```

### Example 7: Check Sync Status

```java
Map<String, Object> status = syncService.getSyncStatus(userId);

boolean syncInProgress = (Boolean) status.get("syncInProgress");
int pendingQueues = (Integer) status.get("pendingQueues");
int failedSyncs = (Integer) status.get("failedSyncs");
int unresolvedConflicts = (Integer) status.get("unresolvedConflicts");
```

### Example 8: Monitor Sync Progress

```java
String sessionId = "SESSION123";

Map<String, Object> progress = syncService.getSyncProgress(sessionId);

long total = (Long) progress.get("total");
long completed = (Long) progress.get("completed");
double percentage = (Double) progress.get("percentage");

System.out.println("Progress: " + percentage + "%");
```

### Example 9: Resolve Conflicts

```java
// Get unresolved conflicts
List<SyncLog> conflicts = syncService.getUserUnresolvedConflicts(userId);

// Resolve conflict - server wins
SyncResponse response = syncService.resolveConflictServerWins(conflictLogId);

// OR resolve conflict - client wins
SyncResponse response = syncService.resolveConflictClientWins(conflictLogId);

// OR resolve conflict - manual merge
Map<String, Object> mergedData = new HashMap<>();
mergedData.put("field1", "value1");
mergedData.put("field2", "value2");

SyncResponse response = syncService.resolveConflictManualMerge(conflictLogId, mergedData);
```

### Example 10: Process Sync Queue

```java
@Component
@RequiredArgsConstructor
public class SyncQueueProcessor {
    
    private final SyncService syncService;
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void processPendingQueues() {
        // Process high priority first
        int highPriority = syncService.processHighPriorityQueues();
        log.info("Processed {} high priority items", highPriority);
        
        // Process pending queues
        int pending = syncService.processPendingSyncQueues(100);
        log.info("Processed {} pending items", pending);
        
        // Retry failed queues
        int retried = syncService.retryFailedSyncQueues();
        log.info("Retried {} failed items", retried);
    }
}
```

---

## 📋 Entity Types Supported

1. **CUSTOMER** - Customer data
2. **PRODUCT** - Product catalog
3. **SALES_ORDER** - Sales orders
4. **PAYMENT** - Payment records
5. **INVENTORY** - Stock levels
6. **PRICE_LIST** - Pricing information
7. **SALES_REP** - Sales representative data

---

## 📋 Sync Directions

- **PUSH** - Mobile → Server (upload local changes)
- **PULL** - Server → Mobile (download server changes)
- **BIDIRECTIONAL** - Both directions (full synchronization)

---

## 📋 Sync Types

- **FULL** - Complete synchronization (all records)
- **INCREMENTAL** - Only changes since last sync
- **OFFLINE_CHANGE** - Queue offline changes for later sync

---

## 📋 Queue Priorities

- **HIGH** - Process immediately (e.g., critical transactions)
- **MEDIUM** - Normal processing
- **LOW** - Process when idle

---

## 📋 Status Values

- **PENDING** - Waiting to be processed
- **IN_PROGRESS** - Currently synchronizing
- **SUCCESS** - Completed successfully
- **FAILED** - Failed with error
- **CANCELLED** - Cancelled by user/system
- **CONFLICT** - Conflict detected

---

## 📋 Conflict Resolution Strategies

1. **SERVER_WINS** - Server data takes precedence
2. **CLIENT_WINS** - Client data takes precedence
3. **MANUAL_MERGE** - User manually merges conflicting data

---

## 🔒 Security

**Role-based Access Control:**

- **ADMIN** - Full access to all sync operations
- **MANAGER** - Manage sync queues and resolve conflicts
- **SALES_REP** - Sync assigned data
- **MOBILE_USER** - Basic sync operations

**Endpoint Security:**
- All endpoints require authentication
- `@PreAuthorize` annotations on all endpoints
- Role-based authorization
- Device-specific access control

---

## 📁 Directory Structure

```
mobilesync/
├── repository/
│   ├── SyncLogRepository.java (70+ methods)
│   ├── SyncQueueRepository.java (85+ methods)
│   └── README.md
├── service/
│   ├── SyncService.java (110+ methods)
│   ├── SyncServiceImpl.java (900+ lines)
│   └── README.md
└── controller/
    ├── MobileSyncController.java (50+ endpoints)
    └── README.md
```

---

## ✅ Summary

✅ **2 Repositories** - 155+ query methods  
✅ **2 Service files** - 110+ business logic methods  
✅ **1 Controller** - 50+ REST endpoints  
✅ **Bidirectional sync** - Push, pull, full sync  
✅ **7 Entity types** - Comprehensive entity support  
✅ **3 Sync types** - Full, incremental, offline  
✅ **3 Sync directions** - Push, pull, bidirectional  
✅ **3 Priorities** - High, medium, low  
✅ **Queue management** - Priority-based processing  
✅ **Retry mechanism** - Exponential backoff  
✅ **Conflict resolution** - 3 strategies (server/client/manual)  
✅ **Offline support** - Queue and sync offline changes  
✅ **Real-time monitoring** - Status and progress tracking  
✅ **Statistics** - Comprehensive sync analytics  
✅ **Async processing** - Non-blocking operations  
✅ **Data validation** - Pre-sync validation  
✅ **Bulk operations** - Bulk synchronization  
✅ **Session tracking** - Track sync sessions  
✅ **Cleanup** - Automatic cleanup of old data  
✅ **Search** - Full-text search  
✅ **Pagination** - Page-based results  
✅ **Role-based security** - Secure access control  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete mobile synchronization with bidirectional sync (push/pull), 7 entity types (customers, products, sales orders, payments, inventory, price lists, sales reps), offline support with queue management, priority-based processing (HIGH/MEDIUM/LOW), retry mechanism with exponential backoff, conflict detection and resolution (server wins, client wins, manual merge), real-time monitoring with status and progress tracking, comprehensive statistics and analytics, async operations, data validation, bulk sync, and secure role-based access control in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.mobilesync
