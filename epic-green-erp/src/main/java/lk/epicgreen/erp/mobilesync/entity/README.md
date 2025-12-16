# Mobile Sync Module - Epic Green ERP

This directory contains **entities and DTOs** for mobile synchronization management in the Epic Green ERP system.

## 📦 Contents

### Entities (mobilesync/entity) - 3 Files
1. **SyncLog.java** - Track synchronization activities
2. **SyncQueue.java** - Queue of records waiting to be synced
3. **SyncConflict.java** - Track conflicts during sync

### DTOs (mobilesync/dto) - 2 Files
1. **SyncRequest.java** - Sync request from mobile devices
2. **SyncResponse.java** - Sync response to mobile devices

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────┐
│   SyncLog    │ (Sync activity tracking)
└──────────────┘

┌──────────────┐
│  SyncQueue   │ (Pending sync operations)
└──────┬───────┘
       │
       │ 1:1
       ▼
┌──────────────┐
│ SyncConflict │ (Conflict resolution)
└──────────────┘
```

### Mobile Sync Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      MOBILE DEVICE                          │
│  ┌────────────────────────────────────────────────────┐    │
│  │           Local SQLite Database                    │    │
│  │  - Offline-first storage                           │    │
│  │  - Changes tracked locally                         │    │
│  │  - Pending operations queued                       │    │
│  └────────────────────────────────────────────────────┘    │
│                          │                                  │
│                          ▼                                  │
│  ┌────────────────────────────────────────────────────┐    │
│  │           Sync Manager                             │    │
│  │  - Detect online/offline                           │    │
│  │  - Queue operations                                │    │
│  │  - Handle conflicts                                │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      │ HTTPS
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                      SERVER                                 │
│  ┌────────────────────────────────────────────────────┐    │
│  │           Sync Service                             │    │
│  │  - Process sync requests                           │    │
│  │  - Detect conflicts                                │    │
│  │  - Resolve conflicts                               │    │
│  │  - Track sync logs                                 │    │
│  └────────────────────────────────────────────────────┘    │
│                          │                                  │
│                          ▼                                  │
│  ┌────────────────────────────────────────────────────┐    │
│  │           MySQL Database                           │    │
│  │  - sync_logs                                       │    │
│  │  - sync_queue                                      │    │
│  │  - sync_conflicts                                  │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 1. SyncLog Entity

**Purpose:** Track all synchronization activities

### Key Fields

```java
// Device information
- deviceId (unique device identifier)
- deviceName
- deviceType (ANDROID, IOS, WEB)

// User information
- userId
- username

// Sync timing
- syncTimestamp (when sync started)
- syncCompletedTimestamp

// Sync details
- syncType (FULL, INCREMENTAL, UPLOAD, DOWNLOAD)
- syncDirection (UPLOAD, DOWNLOAD, BIDIRECTIONAL)
- syncStatus (IN_PROGRESS, COMPLETED, FAILED, PARTIAL)
- entityType (e.g., CUSTOMER, SALES_ORDER, PAYMENT)

// Statistics
- totalRecords
- syncedRecords
- failedRecords
- conflictsDetected
- dataSizeBytes
- durationMillis

// Sync tokens (for incremental sync)
- lastSyncToken

// Version tracking
- clientVersion
- serverVersion

// Network
- networkType (WIFI, MOBILE_DATA, OFFLINE)

// Error handling
- errorMessage
- errorDetails

// Additional data
- syncDetails (JSON)
- notes
```

### Helper Methods

```java
Long getDurationSeconds(); // Convert milliseconds to seconds
Double getSuccessPercentage(); // (syncedRecords / totalRecords) * 100
Double getFailurePercentage(); // (failedRecords / totalRecords) * 100
boolean isInProgress(); // syncStatus == IN_PROGRESS
boolean isCompleted(); // syncStatus == COMPLETED
boolean isFailed(); // syncStatus == FAILED
boolean hasConflicts(); // conflictsDetected > 0
```

### Sync Types

- **FULL:** Complete data sync (initial sync or after long offline period)
- **INCREMENTAL:** Only changed data since last sync
- **UPLOAD:** Only upload changes from device to server
- **DOWNLOAD:** Only download changes from server to device

---

## 🔧 2. SyncQueue Entity

**Purpose:** Queue of records waiting to be synchronized

### Key Fields

```java
// Device and user
- deviceId
- userId

// Entity identification
- entityType (e.g., CUSTOMER, SALES_ORDER, PAYMENT)
- entityId

// Operation details
- operationType (CREATE, UPDATE, DELETE)
- syncDirection (UPLOAD, DOWNLOAD)
- syncStatus (PENDING, IN_PROGRESS, COMPLETED, FAILED, CONFLICT)

// Priority and scheduling
- priority (1 = highest, 10 = lowest)
- scheduledTime (when to sync)

// Retry logic
- retryCount
- maxRetryAttempts
- lastSyncAttemptTime

// Timing
- completedTime

// Data
- entityData (JSON)
- entityVersion (for optimistic locking)
- serverVersion

// Sync token
- syncToken

// Error handling
- errorMessage
- errorDetails
- conflictId (if conflict detected)

// Dependencies
- dependencies (comma-separated queue IDs that must sync first)

- notes
```

### Helper Methods

```java
boolean isPending(); // syncStatus == PENDING
boolean isInProgress(); // syncStatus == IN_PROGRESS
boolean isCompleted(); // syncStatus == COMPLETED
boolean isFailed(); // syncStatus == FAILED
boolean hasConflict(); // syncStatus == CONFLICT
boolean canRetry(); // isFailed && retryCount < maxRetryAttempts
boolean shouldSyncNow(); // isPending && scheduledTime <= now
Long getMinutesUntilScheduled(); // Time until scheduled sync
```

### Status Flow

```
PENDING (waiting to sync)
  ↓ (start sync)
IN_PROGRESS
  ↓ (success)
COMPLETED
  OR
  ↓ (failure)
FAILED (can retry)
  OR
  ↓ (version mismatch)
CONFLICT (requires resolution)
```

---

## 💳 3. SyncConflict Entity

**Purpose:** Track conflicts detected during synchronization

### Key Fields

```java
// Device and user
- deviceId
- userId
- username

// Entity identification
- entityType
- entityId

// Conflict details
- conflictType (VERSION_MISMATCH, CONCURRENT_UPDATE, DELETE_CONFLICT, DUPLICATE)
- detectedAt

// Version tracking
- clientVersion
- serverVersion

// Data comparison
- clientData (JSON - what device has)
- serverData (JSON - what server has)
- conflictDetails (description)

// Resolution
- resolutionStatus (UNRESOLVED, RESOLVED, AUTO_RESOLVED, CLIENT_WINS, SERVER_WINS, MERGED)
- resolutionStrategy (CLIENT_WINS, SERVER_WINS, LAST_WRITE_WINS, MERGE, MANUAL)
- resolvedData (JSON - final data after resolution)
- resolvedBy
- resolvedAt
- resolutionNotes

// Auto resolution
- autoResolveEnabled

// References
- syncLogId
- syncQueueId

- notes
```

### Helper Methods

```java
boolean isUnresolved(); // resolutionStatus == UNRESOLVED
boolean isResolved(); // Any resolved status
boolean isAutoResolved(); // resolutionStatus == AUTO_RESOLVED
boolean requiresManualResolution(); // isUnresolved && strategy != MANUAL
Long getResolutionTimeMinutes(); // Time taken to resolve
Long getMinutesSinceDetection(); // Time since conflict detected
```

### Conflict Types

- **VERSION_MISMATCH:** Client and server have different versions of same record
- **CONCURRENT_UPDATE:** Both client and server updated the same record
- **DELETE_CONFLICT:** One side deleted, other side updated
- **DUPLICATE:** Record already exists (for CREATE operations)

### Resolution Strategies

- **CLIENT_WINS:** Use client data, overwrite server
- **SERVER_WINS:** Use server data, overwrite client
- **LAST_WRITE_WINS:** Use most recently modified data
- **MERGE:** Merge both changes (field-level merge)
- **MANUAL:** Requires manual intervention

---

## 💡 Usage Examples

### Example 1: Full Sync (Initial Sync)

```java
@Transactional
public SyncResponse performFullSync(SyncRequest request) {
    // Create sync log
    SyncLog syncLog = SyncLog.builder()
        .deviceId(request.getDeviceId())
        .deviceName(request.getDeviceName())
        .deviceType(request.getDeviceType())
        .userId(request.getUserId())
        .syncTimestamp(LocalDateTime.now())
        .syncType("FULL")
        .syncDirection("BIDIRECTIONAL")
        .syncStatus("IN_PROGRESS")
        .clientVersion(request.getClientVersion())
        .serverVersion(applicationVersion)
        .networkType(request.getNetworkType())
        .build();
    
    syncLog = syncLogRepository.save(syncLog);
    
    try {
        // Upload client data
        List<SyncResponse.SyncResult> uploadResults = 
            processUploadData(request.getDataToUpload(), syncLog);
        
        // Download server data
        List<SyncResponse.SyncDataItem> downloadData = 
            getFullDataForDevice(request.getUserId(), request.getEntityTypes());
        
        // Generate new sync token
        String newSyncToken = generateSyncToken();
        
        // Update sync log
        syncLog.setSyncStatus("COMPLETED");
        syncLog.setSyncCompletedTimestamp(LocalDateTime.now());
        syncLog.setLastSyncToken(newSyncToken);
        syncLog.setTotalRecords(uploadResults.size() + downloadData.size());
        syncLog.setSyncedRecords(uploadResults.size() + downloadData.size());
        syncLogRepository.save(syncLog);
        
        // Build response
        return SyncResponse.builder()
            .syncLogId(syncLog.getId())
            .syncStatus("COMPLETED")
            .syncTimestamp(syncLog.getSyncTimestamp())
            .newSyncToken(newSyncToken)
            .serverTimestamp(LocalDateTime.now())
            .uploadResults(uploadResults)
            .downloadData(downloadData)
            .summary(buildSummary(syncLog))
            .message("Full sync completed successfully")
            .build();
            
    } catch (Exception e) {
        syncLog.setSyncStatus("FAILED");
        syncLog.setErrorMessage(e.getMessage());
        syncLogRepository.save(syncLog);
        throw e;
    }
}
```

### Example 2: Incremental Sync (Only Changes)

```java
@Transactional
public SyncResponse performIncrementalSync(SyncRequest request) {
    // Validate last sync token
    SyncLog lastSync = syncLogRepository
        .findByDeviceIdAndLastSyncToken(request.getDeviceId(), request.getLastSyncToken())
        .orElseThrow(() -> new InvalidSyncTokenException("Invalid sync token"));
    
    // Create new sync log
    SyncLog syncLog = SyncLog.builder()
        .deviceId(request.getDeviceId())
        .userId(request.getUserId())
        .syncTimestamp(LocalDateTime.now())
        .syncType("INCREMENTAL")
        .syncStatus("IN_PROGRESS")
        .build();
    
    syncLog = syncLogRepository.save(syncLog);
    
    try {
        // Get changes since last sync
        LocalDateTime lastSyncTime = lastSync.getSyncCompletedTimestamp();
        
        // Upload client changes
        List<SyncResponse.SyncResult> uploadResults = 
            processUploadData(request.getDataToUpload(), syncLog);
        
        // Download only changed records
        List<SyncResponse.SyncDataItem> downloadData = 
            getChangedDataSince(lastSyncTime, request.getUserId(), request.getEntityTypes());
        
        // Generate new sync token
        String newSyncToken = generateSyncToken();
        
        // Update sync log
        syncLog.setSyncStatus("COMPLETED");
        syncLog.setSyncCompletedTimestamp(LocalDateTime.now());
        syncLog.setLastSyncToken(newSyncToken);
        syncLogRepository.save(syncLog);
        
        return SyncResponse.builder()
            .syncLogId(syncLog.getId())
            .syncStatus("COMPLETED")
            .newSyncToken(newSyncToken)
            .uploadResults(uploadResults)
            .downloadData(downloadData)
            .message("Incremental sync completed")
            .build();
            
    } catch (Exception e) {
        syncLog.setSyncStatus("FAILED");
        syncLog.setErrorMessage(e.getMessage());
        syncLogRepository.save(syncLog);
        throw e;
    }
}
```

### Example 3: Process Upload Data with Conflict Detection

```java
@Transactional
public List<SyncResponse.SyncResult> processUploadData(
    List<SyncRequest.SyncDataItem> dataItems,
    SyncLog syncLog
) {
    List<SyncResponse.SyncResult> results = new ArrayList<>();
    
    for (SyncRequest.SyncDataItem item : dataItems) {
        try {
            SyncResponse.SyncResult result;
            
            switch (item.getOperationType()) {
                case "CREATE":
                    result = handleCreate(item, syncLog);
                    break;
                case "UPDATE":
                    result = handleUpdate(item, syncLog);
                    break;
                case "DELETE":
                    result = handleDelete(item, syncLog);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation type");
            }
            
            results.add(result);
            
        } catch (SyncConflictException e) {
            // Conflict detected - create conflict record
            SyncConflict conflict = createConflictRecord(item, e, syncLog);
            
            results.add(SyncResponse.SyncResult.builder()
                .entityType(item.getEntityType())
                .entityId(item.getEntityId())
                .operationType(item.getOperationType())
                .status("CONFLICT")
                .conflictId(conflict.getId())
                .errorMessage("Conflict detected: " + e.getMessage())
                .build());
                
            syncLog.setConflictsDetected(
                (syncLog.getConflictsDetected() != null ? syncLog.getConflictsDetected() : 0) + 1
            );
        }
    }
    
    return results;
}

private SyncResponse.SyncResult handleUpdate(
    SyncRequest.SyncDataItem item,
    SyncLog syncLog
) throws SyncConflictException {
    
    // Get current server version
    BaseEntity entity = entityRepository.findById(item.getEntityType(), item.getEntityId());
    
    // Check for version mismatch
    if (entity.getVersion() > item.getEntityVersion()) {
        throw new SyncConflictException(
            "VERSION_MISMATCH",
            "Server has newer version: " + entity.getVersion() + 
            ", client version: " + item.getEntityVersion()
        );
    }
    
    // Update entity
    updateEntityFromJson(entity, item.getEntityData());
    entity = entityRepository.save(entity);
    
    return SyncResponse.SyncResult.builder()
        .entityType(item.getEntityType())
        .entityId(item.getEntityId())
        .operationType("UPDATE")
        .status("SUCCESS")
        .newVersion(entity.getVersion())
        .lastModifiedAt(entity.getUpdatedAt())
        .build();
}
```

### Example 4: Queue Operations for Offline Support

```java
// On mobile device - queue operation when offline
public void createCustomerOffline(Customer customer) {
    // Save to local database
    localDatabase.save(customer);
    
    // Add to sync queue
    SyncQueue queueItem = SyncQueue.builder()
        .deviceId(deviceId)
        .userId(currentUserId)
        .entityType("CUSTOMER")
        .entityId(customer.getId())
        .operationType("CREATE")
        .syncDirection("UPLOAD")
        .syncStatus("PENDING")
        .priority(1)
        .entityData(toJson(customer))
        .scheduledTime(LocalDateTime.now())
        .build();
    
    syncQueueRepository.save(queueItem);
}

// Process sync queue when online
@Scheduled(fixedDelay = 60000) // Every minute
public void processSyncQueue() {
    if (!isOnline()) {
        return;
    }
    
    List<SyncQueue> pendingItems = syncQueueRepository
        .findByDeviceIdAndStatusAndScheduledTimeBefore(
            deviceId,
            "PENDING",
            LocalDateTime.now()
        );
    
    for (SyncQueue item : pendingItems) {
        if (item.shouldSyncNow()) {
            processQueueItem(item);
        }
    }
}

private void processQueueItem(SyncQueue item) {
    item.setSyncStatus("IN_PROGRESS");
    item.setLastSyncAttemptTime(LocalDateTime.now());
    syncQueueRepository.save(item);
    
    try {
        // Create sync request
        SyncRequest request = buildSyncRequest(List.of(item));
        
        // Call server
        SyncResponse response = syncService.sync(request);
        
        // Check result
        SyncResponse.SyncResult result = response.getUploadResults().get(0);
        
        if ("SUCCESS".equals(result.getStatus())) {
            item.setSyncStatus("COMPLETED");
            item.setCompletedTime(LocalDateTime.now());
        } else if ("CONFLICT".equals(result.getStatus())) {
            item.setSyncStatus("CONFLICT");
            item.setConflictId(result.getConflictId());
        } else {
            item.setSyncStatus("FAILED");
            item.setErrorMessage(result.getErrorMessage());
        }
        
        syncQueueRepository.save(item);
        
    } catch (Exception e) {
        item.setSyncStatus("FAILED");
        item.setErrorMessage(e.getMessage());
        item.setRetryCount(item.getRetryCount() + 1);
        
        if (item.canRetry()) {
            // Schedule retry with exponential backoff
            long delayMinutes = (long) Math.pow(2, item.getRetryCount()) * 5;
            item.setScheduledTime(LocalDateTime.now().plusMinutes(delayMinutes));
            item.setSyncStatus("PENDING");
        }
        
        syncQueueRepository.save(item);
    }
}
```

### Example 5: Auto-Resolve Conflicts

```java
@Transactional
public void autoResolveConflict(Long conflictId) {
    SyncConflict conflict = syncConflictRepository.findById(conflictId)
        .orElseThrow(() -> new NotFoundException("Conflict not found"));
    
    if (!conflict.isUnresolved()) {
        throw new BusinessException("Conflict already resolved");
    }
    
    // Determine resolution strategy based on conflict type
    String strategy;
    String resolvedData;
    
    switch (conflict.getConflictType()) {
        case "VERSION_MISMATCH":
            // Use LAST_WRITE_WINS strategy
            strategy = "LAST_WRITE_WINS";
            
            // Parse timestamps from client and server data
            LocalDateTime clientTime = extractLastModified(conflict.getClientData());
            LocalDateTime serverTime = extractLastModified(conflict.getServerData());
            
            if (clientTime.isAfter(serverTime)) {
                resolvedData = conflict.getClientData();
                conflict.setResolutionStatus("CLIENT_WINS");
            } else {
                resolvedData = conflict.getServerData();
                conflict.setResolutionStatus("SERVER_WINS");
            }
            break;
            
        case "DELETE_CONFLICT":
            // Always use DELETE (server wins)
            strategy = "SERVER_WINS";
            resolvedData = null;
            conflict.setResolutionStatus("SERVER_WINS");
            break;
            
        default:
            // Requires manual resolution
            strategy = "MANUAL";
            resolvedData = null;
            conflict.setResolutionNotes("Requires manual resolution");
            return;
    }
    
    // Apply resolution
    applyResolution(conflict, resolvedData);
    
    // Update conflict
    conflict.setResolutionStrategy(strategy);
    conflict.setResolvedData(resolvedData);
    conflict.setResolvedBy("SYSTEM");
    conflict.setResolvedAt(LocalDateTime.now());
    conflict.setResolutionStatus("AUTO_RESOLVED");
    
    syncConflictRepository.save(conflict);
    
    // Update sync queue item if exists
    if (conflict.getSyncQueueId() != null) {
        SyncQueue queueItem = syncQueueRepository.findById(conflict.getSyncQueueId())
            .orElse(null);
        if (queueItem != null) {
            queueItem.setSyncStatus("PENDING"); // Retry sync
            queueItem.setScheduledTime(LocalDateTime.now());
            syncQueueRepository.save(queueItem);
        }
    }
}
```

### Example 6: Manual Conflict Resolution

```java
@Transactional
public void resolveConflictManually(
    Long conflictId,
    String resolutionChoice, // CLIENT_WINS, SERVER_WINS, or custom JSON
    String resolvedBy
) {
    SyncConflict conflict = syncConflictRepository.findById(conflictId)
        .orElseThrow(() -> new NotFoundException("Conflict not found"));
    
    if (!conflict.isUnresolved()) {
        throw new BusinessException("Conflict already resolved");
    }
    
    String resolvedData;
    String resolutionStatus;
    
    switch (resolutionChoice) {
        case "CLIENT_WINS":
            resolvedData = conflict.getClientData();
            resolutionStatus = "CLIENT_WINS";
            break;
            
        case "SERVER_WINS":
            resolvedData = conflict.getServerData();
            resolutionStatus = "SERVER_WINS";
            break;
            
        default:
            // Custom resolution (merged data provided)
            resolvedData = resolutionChoice;
            resolutionStatus = "MERGED";
            break;
    }
    
    // Apply resolution
    applyResolution(conflict, resolvedData);
    
    // Update conflict
    conflict.setResolutionStrategy("MANUAL");
    conflict.setResolvedData(resolvedData);
    conflict.setResolutionStatus(resolutionStatus);
    conflict.setResolvedBy(resolvedBy);
    conflict.setResolvedAt(LocalDateTime.now());
    
    syncConflictRepository.save(conflict);
}
```

### Example 7: Get Sync Status for Device

```java
public DeviceSyncStatus getSyncStatus(String deviceId) {
    // Get last sync
    SyncLog lastSync = syncLogRepository
        .findTopByDeviceIdOrderBySyncTimestampDesc(deviceId)
        .orElse(null);
    
    // Get pending queue items
    List<SyncQueue> pendingItems = syncQueueRepository
        .findByDeviceIdAndStatus(deviceId, "PENDING");
    
    // Get unresolved conflicts
    List<SyncConflict> unresolvedConflicts = syncConflictRepository
        .findByDeviceIdAndResolutionStatus(deviceId, "UNRESOLVED");
    
    return DeviceSyncStatus.builder()
        .deviceId(deviceId)
        .lastSyncTime(lastSync != null ? lastSync.getSyncTimestamp() : null)
        .lastSyncStatus(lastSync != null ? lastSync.getSyncStatus() : null)
        .lastSyncToken(lastSync != null ? lastSync.getLastSyncToken() : null)
        .pendingUploads(pendingItems.size())
        .unresolvedConflicts(unresolvedConflicts.size())
        .needsFullSync(lastSync == null || "FAILED".equals(lastSync.getSyncStatus()))
        .build();
}
```

---

## 📁 Directory Structure

```
mobilesync/
├── entity/
│   ├── SyncLog.java
│   ├── SyncQueue.java
│   ├── SyncConflict.java
│   └── README.md
└── dto/
    ├── SyncRequest.java
    └── SyncResponse.java
```

---

## ✅ Summary

✅ **3 Entity classes** - Complete mobile sync management  
✅ **2 DTO classes** - Request/response objects  
✅ **Offline-first architecture** - Work offline, sync when online  
✅ **Sync types** - FULL, INCREMENTAL, UPLOAD, DOWNLOAD  
✅ **Sync directions** - UPLOAD, DOWNLOAD, BIDIRECTIONAL  
✅ **Sync queue** - Queue operations when offline  
✅ **Priority-based sync** - High priority items sync first  
✅ **Retry logic** - Automatic retry with exponential backoff  
✅ **Conflict detection** - Detect version mismatches and concurrent updates  
✅ **4 Conflict types** - VERSION_MISMATCH, CONCURRENT_UPDATE, DELETE_CONFLICT, DUPLICATE  
✅ **5 Resolution strategies** - CLIENT_WINS, SERVER_WINS, LAST_WRITE_WINS, MERGE, MANUAL  
✅ **Auto-resolution** - Automatic conflict resolution based on rules  
✅ **Manual resolution** - Manual intervention for complex conflicts  
✅ **Sync tokens** - Incremental sync using tokens  
✅ **Version tracking** - Optimistic locking with version numbers  
✅ **Network awareness** - Track network type (WIFI, MOBILE_DATA)  
✅ **Performance tracking** - Duration, data size, success rate  
✅ **Error handling** - Comprehensive error tracking  
✅ **Audit tracking** - Complete sync history  
✅ **Production-ready** - Enterprise-grade mobile sync  

**Everything you need for complete mobile synchronization, offline-first support, conflict detection and resolution, and seamless data sync between mobile devices and server in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.mobilesync
