# Warehouse Module - Epic Green ERP

This directory contains **entities and DTOs** for warehouse and inventory management in the Epic Green ERP system.

## 📦 Contents

### Entities (warehouse/entity) - 6 Files
1. **Warehouse.java** - Physical warehouse facilities
2. **WarehouseLocation.java** - Storage locations within warehouses
3. **Inventory.java** - Product stock at locations
4. **StockMovement.java** - All stock movements
5. **StockAdjustment.java** - Stock adjustment headers
6. **StockAdjustmentItem.java** - Stock adjustment details

### DTOs (warehouse/dto) - 5 Files
1. **WarehouseDTO.java** - Warehouse data transfer object
2. **InventoryDTO.java** - Inventory data transfer object
3. **StockMovementDTO.java** - Stock movement data transfer object
4. **StockAdjustmentDTO.java** - Stock adjustment data transfer object
5. **StockAdjustmentRequest.java** - Create stock adjustment request

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌────────────┐
│  Warehouse │ (Physical facilities)
└──────┬─────┘
       │ 1:N
       ├─────────────────┬──────────────────┐
       │                 │                  │
       ▼                 ▼                  ▼
┌──────────────────┐  ┌───────────┐  ┌──────────────┐
│ WarehouseLocation│  │ Inventory │  │StockMovement │
└────────┬─────────┘  └─────┬─────┘  └──────────────┘
         │                   │
         │ self-ref          │ N:1
         │ parent/children   │
         │                   ▼
         │              ┌─────────┐
         └──────────────│ Product │
                        └─────────┘

┌──────────────────┐
│ StockAdjustment  │ (Header)
└────────┬─────────┘
         │ 1:N
         ▼
┌───────────────────────┐
│ StockAdjustmentItem   │ (Detail)
└───────────────────────┘
```

---

## 🏢 1. Warehouse Entity

**Purpose:** Physical warehouse/storage facilities

### Key Fields

```java
// Identification
- warehouseCode (unique, e.g., "WH-MAIN")
- warehouseName
- warehouseType (MAIN, BRANCH, FACTORY, DISTRIBUTION, COLD_STORAGE)
- description

// Management
- managerName
- contactPerson

// Contact
- email, phoneNumber, mobileNumber

// Address
- addressLine1, addressLine2
- city, state, postalCode, country

// Flags
- isMainWarehouse (primary warehouse)
- isActive

// Capacity
- capacity (numeric)
- capacityUnit (SQM, CBM)
- operatingHours

// Facilities (JSON or text)
- facilities

// Relationships
- locations (Set<WarehouseLocation>)
- inventoryItems (Set<Inventory>)
```

### Table Structure

```sql
CREATE TABLE warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_code VARCHAR(20) NOT NULL UNIQUE,
    warehouse_name VARCHAR(100) NOT NULL,
    warehouse_type VARCHAR(30) NOT NULL,
    description TEXT,
    
    manager_name VARCHAR(100),
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    
    is_main_warehouse BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    operating_hours VARCHAR(255),
    capacity INT,
    capacity_unit VARCHAR(10),
    facilities TEXT,
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_warehouse_code (warehouse_code),
    INDEX idx_warehouse_name (warehouse_name),
    INDEX idx_warehouse_type (warehouse_type)
);
```

---

## 📍 2. WarehouseLocation Entity

**Purpose:** Specific storage locations within warehouses

### Key Fields

```java
// Identification
- warehouse (FK)
- locationCode (unique within warehouse)
- locationName
- locationType (ZONE, AISLE, RACK, SHELF, BIN, PALLET)

// Hierarchy
- parent (self-reference)
- children (Set<WarehouseLocation>)

// Location components
- zone (e.g., A, B, C)
- aisle (e.g., 1, 2, 3)
- rack (e.g., R1, R2)
- shelf (e.g., S1, S2)
- bin (e.g., B001)

// Capacity
- capacity
- capacityUnit (PALLETS, BOXES, KG, CBM)

// Status
- isActive
- isAvailable (not blocked/reserved)

// Storage conditions
- storageConditions (COOL, DRY, REFRIGERATED)
- temperatureRange (e.g., "15-25°C")
- humidityRange (e.g., "40-60%")

// Relationships
- inventoryItems (Set<Inventory>)
```

### Helper Methods

```java
// Get full location path
String path = location.getFullLocationPath();
// "Zone A > Aisle 5 > Rack R12 (Zone:A Aisle:5 Rack:R12 Shelf:3 Bin:B005)"

// Get simple location code
String code = location.getSimpleLocationCode();
// "A-A5-R12-S3-B005"

// Check if leaf location (can store inventory)
boolean isLeaf = location.isLeafLocation();
```

### Table Structure

```sql
CREATE TABLE warehouse_locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    location_code VARCHAR(50) NOT NULL,
    location_name VARCHAR(100) NOT NULL,
    location_type VARCHAR(20) NOT NULL,
    parent_id BIGINT,
    
    zone VARCHAR(20),
    aisle VARCHAR(20),
    rack VARCHAR(20),
    shelf VARCHAR(20),
    bin VARCHAR(20),
    
    capacity INT,
    capacity_unit VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_available BOOLEAN DEFAULT TRUE,
    
    storage_conditions VARCHAR(255),
    temperature_range VARCHAR(50),
    humidity_range VARCHAR(50),
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT uk_warehouse_location 
        UNIQUE (warehouse_id, location_code),
    CONSTRAINT fk_warehouse_location_warehouse 
        FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_warehouse_location_parent 
        FOREIGN KEY (parent_id) REFERENCES warehouse_locations(id),
    INDEX idx_warehouse_location_warehouse (warehouse_id),
    INDEX idx_warehouse_location_code (location_code),
    INDEX idx_warehouse_location_type (location_type)
);
```

### Location Hierarchy Example

```
Main Warehouse
├── Zone A (Cold Storage)
│   ├── Aisle 1
│   │   ├── Rack R1
│   │   │   ├── Shelf S1
│   │   │   │   ├── Bin B001
│   │   │   │   └── Bin B002
│   │   │   └── Shelf S2
│   │   └── Rack R2
│   └── Aisle 2
└── Zone B (Dry Storage)
    ├── Aisle 1
    └── Aisle 2
```

---

## 📦 3. Inventory Entity

**Purpose:** Product stock at specific warehouse locations

### Key Fields

```java
// References
- product (FK)
- warehouse (FK)
- location (FK)

// Batch/Serial tracking
- batchNumber
- serialNumber

// Quantities
- availableQuantity (on hand)
- allocatedQuantity (reserved for orders)
- inTransitQuantity (being moved)
- damagedQuantity

- unit

// Dates
- manufacturingDate
- expiryDate
- lastStockCountDate

// Costing
- costPerUnit (for this batch)
- currency

// Quality
- qualityStatus (GOOD, DAMAGED, QUARANTINE, EXPIRED)

- notes
```

### Helper Methods

```java
// Get total quantity
BigDecimal total = inventory.getTotalQuantity();
// available + allocated + inTransit

// Get unallocated quantity
BigDecimal unallocated = inventory.getUnallocatedQuantity();
// available - allocated

// Get stock value
BigDecimal value = inventory.getStockValue();
// totalQuantity * costPerUnit

// Check expiry
boolean expired = inventory.isExpired();
boolean nearExpiry = inventory.isNearExpiry(); // within 30 days
Long daysLeft = inventory.getDaysUntilExpiry();

// Check availability
boolean available = inventory.isAvailable();
// availableQuantity > 0 && qualityStatus == GOOD && !expired
```

### Table Structure

```sql
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT,
    batch_number VARCHAR(50),
    serial_number VARCHAR(50),
    
    available_quantity DECIMAL(15,3) NOT NULL DEFAULT 0,
    allocated_quantity DECIMAL(15,3) DEFAULT 0,
    in_transit_quantity DECIMAL(15,3) DEFAULT 0,
    damaged_quantity DECIMAL(15,3) DEFAULT 0,
    unit VARCHAR(10),
    
    manufacturing_date DATE,
    expiry_date DATE,
    last_stock_count_date DATE,
    
    cost_per_unit DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    quality_status VARCHAR(20) DEFAULT 'GOOD',
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT uk_inventory_product_warehouse_location 
        UNIQUE (product_id, warehouse_id, location_id, batch_number),
    CONSTRAINT fk_inventory_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inventory_warehouse 
        FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_inventory_location 
        FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    INDEX idx_inventory_product (product_id),
    INDEX idx_inventory_warehouse (warehouse_id),
    INDEX idx_inventory_location (location_id),
    INDEX idx_inventory_batch (batch_number)
);
```

---

## 📊 4. StockMovement Entity

**Purpose:** Track all stock movements (in, out, transfer, adjustment)

### Key Fields

```java
- movementNumber (unique)
- movementDate
- movementTimestamp
- movementType (IN, OUT, TRANSFER, ADJUSTMENT, PRODUCTION, DAMAGE, RETURN)

// Product
- product (FK)

// From/To locations
- fromWarehouse (FK)
- fromLocation (FK)
- toWarehouse (FK)
- toLocation (FK)
- warehouse (FK for single warehouse ops)

// Batch/Serial
- batchNumber
- serialNumber

// Quantity and value
- quantity
- unit
- costPerUnit
- totalValue
- currency

// Reference
- referenceNumber (PO, SO, GRN, etc.)
- referenceId
- referenceType

// Additional info
- reason
- performedBy
- approvedBy
- status (PENDING, APPROVED, COMPLETED, CANCELLED)
- notes
```

### Helper Methods

```java
// Get movement direction
String direction = movement.getMovementDirection(); // "Stock In", "Stock Out", "Transfer"

// Check movement type
boolean inbound = movement.isInbound(); // IN, PRODUCTION, RETURN
boolean outbound = movement.isOutbound(); // OUT, DAMAGE
boolean transfer = movement.isTransfer(); // TRANSFER
```

### Table Structure

```sql
CREATE TABLE stock_movements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movement_number VARCHAR(50) UNIQUE,
    movement_date DATE NOT NULL,
    movement_timestamp DATETIME NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    
    product_id BIGINT NOT NULL,
    from_warehouse_id BIGINT,
    from_location_id BIGINT,
    to_warehouse_id BIGINT,
    to_location_id BIGINT,
    warehouse_id BIGINT,
    
    batch_number VARCHAR(50),
    serial_number VARCHAR(50),
    quantity DECIMAL(15,3) NOT NULL,
    unit VARCHAR(10),
    
    cost_per_unit DECIMAL(15,2),
    total_value DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    reference_number VARCHAR(50),
    reference_id BIGINT,
    reference_type VARCHAR(30),
    
    reason VARCHAR(255),
    performed_by VARCHAR(50),
    approved_by VARCHAR(50),
    status VARCHAR(20) DEFAULT 'COMPLETED',
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_stock_movement_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_stock_movement_product (product_id),
    INDEX idx_stock_movement_warehouse (warehouse_id),
    INDEX idx_stock_movement_date (movement_date),
    INDEX idx_stock_movement_type (movement_type),
    INDEX idx_stock_movement_reference (reference_number)
);
```

### Movement Types

```java
IN           - Stock inbound (purchase, production)
OUT          - Stock outbound (sales, consumption)
TRANSFER     - Transfer between warehouses/locations
ADJUSTMENT   - Manual adjustment
PRODUCTION   - Production output
DAMAGE       - Damaged stock write-off
RETURN       - Customer/supplier return
```

---

## 🔄 5. StockAdjustment Entity

**Purpose:** Stock adjustment headers (physical counts, corrections)

### Key Fields

```java
- adjustmentNumber (unique)
- adjustmentDate
- adjustmentType (PHYSICAL_COUNT, DAMAGE, LOSS, FOUND, CORRECTION, WRITE_OFF)
- warehouse (FK)
- reason
- description

// Workflow
- status (DRAFT, PENDING_APPROVAL, APPROVED, COMPLETED, CANCELLED)
- preparedBy
- approvedBy
- approvalDate
- completedBy
- completionDate

- totalItems
- notes

// Relationships
- items (Set<StockAdjustmentItem>)
```

### Helper Methods

```java
// Check status
boolean pending = adjustment.isPendingApproval();
boolean approved = adjustment.isApproved();
boolean completed = adjustment.isCompleted();

// Check permissions
boolean canEdit = adjustment.canEdit(); // DRAFT only
boolean canApprove = adjustment.canApprove(); // PENDING_APPROVAL only
```

---

## 📝 6. StockAdjustmentItem Entity

**Purpose:** Detail line items for stock adjustments

### Key Fields

```java
- stockAdjustment (FK header)
- product (FK)
- location (FK)
- batchNumber
- serialNumber

// Quantities
- systemQuantity (in system)
- physicalQuantity (actual counted)
- adjustmentQuantity (physical - system)

- unit
- costPerUnit
- totalValue
- currency

- reason
- notes
```

### Helper Methods

```java
// Get variance
BigDecimal variance = item.getVariancePercentage();
// (adjustmentQuantity / systemQuantity) * 100

// Check discrepancy
boolean hasDiscrepancy = item.hasDiscrepancy(); // adjustmentQuantity != 0
boolean surplus = item.isSurplus(); // adjustmentQuantity > 0
boolean shortage = item.isShortage(); // adjustmentQuantity < 0
```

---

## 💡 Usage Examples

### Example 1: Create Warehouse with Locations

```java
@Service
public class WarehouseService {
    
    @Transactional
    public WarehouseDTO createWarehouseWithLocations() {
        // Create warehouse
        Warehouse warehouse = Warehouse.builder()
            .warehouseCode("WH-MAIN")
            .warehouseName("Main Warehouse")
            .warehouseType("MAIN")
            .city("Colombo")
            .isMainWarehouse(true)
            .isActive(true)
            .build();
        
        // Create zone
        WarehouseLocation zoneA = WarehouseLocation.builder()
            .locationCode("ZONE-A")
            .locationName("Zone A - Cold Storage")
            .locationType("ZONE")
            .zone("A")
            .storageConditions("REFRIGERATED")
            .temperatureRange("2-8°C")
            .build();
        warehouse.addLocation(zoneA);
        
        // Create aisle under zone
        WarehouseLocation aisle1 = WarehouseLocation.builder()
            .locationCode("ZONE-A-AISLE-1")
            .locationName("Aisle 1")
            .locationType("AISLE")
            .aisle("1")
            .build();
        zoneA.addChild(aisle1);
        
        // Create rack under aisle
        WarehouseLocation rack1 = WarehouseLocation.builder()
            .locationCode("ZONE-A-AISLE-1-RACK-1")
            .locationName("Rack 1")
            .locationType("RACK")
            .rack("R1")
            .build();
        aisle1.addChild(rack1);
        
        return warehouseRepository.save(warehouse);
    }
}
```

### Example 2: Record Stock In (Purchase)

```java
@Transactional
public void recordStockIn(Long productId, Long warehouseId, Long locationId, 
                          BigDecimal quantity, String batchNumber) {
    
    Product product = productRepository.findById(productId).orElseThrow(...);
    Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(...);
    WarehouseLocation location = locationRepository.findById(locationId).orElseThrow(...);
    
    // Update or create inventory
    Inventory inventory = inventoryRepository
        .findByProductAndWarehouseAndLocationAndBatch(productId, warehouseId, locationId, batchNumber)
        .orElse(new Inventory());
    
    if (inventory.getId() == null) {
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setLocation(location);
        inventory.setBatchNumber(batchNumber);
        inventory.setAvailableQuantity(BigDecimal.ZERO);
    }
    
    // Add quantity
    inventory.setAvailableQuantity(inventory.getAvailableQuantity().add(quantity));
    inventoryRepository.save(inventory);
    
    // Record stock movement
    StockMovement movement = StockMovement.builder()
        .movementDate(LocalDate.now())
        .movementType("IN")
        .product(product)
        .toWarehouse(warehouse)
        .toLocation(location)
        .batchNumber(batchNumber)
        .quantity(quantity)
        .referenceType("PURCHASE_ORDER")
        .status("COMPLETED")
        .build();
    
    stockMovementRepository.save(movement);
}
```

### Example 3: Create Stock Adjustment

```java
@Transactional
public StockAdjustmentDTO createStockAdjustment(StockAdjustmentRequest request) {
    // Create adjustment header
    StockAdjustment adjustment = StockAdjustment.builder()
        .adjustmentNumber(generateAdjustmentNumber())
        .adjustmentDate(request.getAdjustmentDate())
        .adjustmentType(request.getAdjustmentType())
        .warehouse(warehouseRepository.findById(request.getWarehouseId()).orElseThrow(...))
        .reason(request.getReason())
        .status("DRAFT")
        .preparedBy(getCurrentUsername())
        .build();
    
    // Add adjustment items
    for (var itemRequest : request.getItems()) {
        Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(...);
        
        StockAdjustmentItem item = StockAdjustmentItem.builder()
            .product(product)
            .systemQuantity(itemRequest.getSystemQuantity())
            .physicalQuantity(itemRequest.getPhysicalQuantity())
            .costPerUnit(itemRequest.getCostPerUnit())
            .reason(itemRequest.getReason())
            .build();
        
        adjustment.addItem(item);
    }
    
    adjustment = stockAdjustmentRepository.save(adjustment);
    return mapper.toDTO(adjustment);
}
```

### Example 4: Approve and Apply Stock Adjustment

```java
@Transactional
public void approveAndApplyAdjustment(Long adjustmentId) {
    StockAdjustment adjustment = stockAdjustmentRepository
        .findById(adjustmentId).orElseThrow(...);
    
    if (!adjustment.canApprove()) {
        throw new BusinessException("Adjustment cannot be approved");
    }
    
    // Update status
    adjustment.setStatus("APPROVED");
    adjustment.setApprovedBy(getCurrentUsername());
    adjustment.setApprovalDate(LocalDate.now());
    
    // Apply adjustments to inventory
    for (StockAdjustmentItem item : adjustment.getItems()) {
        if (!item.hasDiscrepancy()) {
            continue;
        }
        
        // Find inventory
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouseAndLocation(
                item.getProduct().getId(),
                adjustment.getWarehouse().getId(),
                item.getLocation().getId()
            ).orElseThrow(...);
        
        // Adjust quantity
        BigDecimal newQty = inventory.getAvailableQuantity()
            .add(item.getAdjustmentQuantity());
        inventory.setAvailableQuantity(newQty);
        inventoryRepository.save(inventory);
        
        // Record stock movement
        StockMovement movement = StockMovement.builder()
            .movementDate(adjustment.getAdjustmentDate())
            .movementType("ADJUSTMENT")
            .product(item.getProduct())
            .warehouse(adjustment.getWarehouse())
            .quantity(item.getAdjustmentQuantity().abs())
            .referenceNumber(adjustment.getAdjustmentNumber())
            .referenceType("STOCK_ADJUSTMENT")
            .reason(item.getReason())
            .status("COMPLETED")
            .build();
        
        stockMovementRepository.save(movement);
    }
    
    // Mark as completed
    adjustment.setStatus("COMPLETED");
    adjustment.setCompletedBy(getCurrentUsername());
    adjustment.setCompletionDate(LocalDate.now());
    
    stockAdjustmentRepository.save(adjustment);
}
```

### Example 5: Transfer Stock Between Warehouses

```java
@Transactional
public void transferStock(Long productId, Long fromWarehouseId, Long toWarehouseId, 
                         BigDecimal quantity, String batchNumber) {
    
    // Deduct from source
    Inventory fromInventory = inventoryRepository
        .findByProductAndWarehouseAndBatch(productId, fromWarehouseId, batchNumber)
        .orElseThrow(...);
    
    if (fromInventory.getAvailableQuantity().compareTo(quantity) < 0) {
        throw new BusinessException("Insufficient stock");
    }
    
    fromInventory.setAvailableQuantity(
        fromInventory.getAvailableQuantity().subtract(quantity)
    );
    
    // Add to destination
    Inventory toInventory = inventoryRepository
        .findByProductAndWarehouseAndBatch(productId, toWarehouseId, batchNumber)
        .orElse(createNewInventory(productId, toWarehouseId, batchNumber));
    
    toInventory.setAvailableQuantity(
        toInventory.getAvailableQuantity().add(quantity)
    );
    
    inventoryRepository.saveAll(List.of(fromInventory, toInventory));
    
    // Record transfer movement
    StockMovement movement = StockMovement.builder()
        .movementDate(LocalDate.now())
        .movementType("TRANSFER")
        .product(fromInventory.getProduct())
        .fromWarehouse(fromInventory.getWarehouse())
        .toWarehouse(toInventory.getWarehouse())
        .quantity(quantity)
        .batchNumber(batchNumber)
        .status("COMPLETED")
        .build();
    
    stockMovementRepository.save(movement);
}
```

---

## 📁 Directory Structure

```
warehouse/
├── entity/
│   ├── Warehouse.java
│   ├── WarehouseLocation.java
│   ├── Inventory.java
│   ├── StockMovement.java
│   ├── StockAdjustment.java
│   └── StockAdjustmentItem.java
├── dto/
│   ├── WarehouseDTO.java
│   ├── InventoryDTO.java
│   ├── StockMovementDTO.java
│   ├── StockAdjustmentDTO.java
│   └── StockAdjustmentRequest.java
└── README.md
```

---

## ✅ Summary

✅ **6 Entity classes** - Complete warehouse management  
✅ **5 DTO classes** - Request/response objects  
✅ **Hierarchical locations** - Unlimited levels (Zone > Aisle > Rack > Shelf > Bin)  
✅ **Complete inventory tracking** - Available, allocated, in-transit, damaged  
✅ **Batch/serial tracking** - Full traceability  
✅ **Expiry management** - Track expiry dates, near-expiry detection  
✅ **Stock movements** - IN, OUT, TRANSFER, ADJUSTMENT  
✅ **Physical stock counts** - Stock adjustment with approval workflow  
✅ **Multi-warehouse** - Support multiple warehouses  
✅ **Quality tracking** - GOOD, DAMAGED, QUARANTINE, EXPIRED  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade warehouse management  

**Everything you need for complete warehouse and inventory management in a spice production factory!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.warehouse
