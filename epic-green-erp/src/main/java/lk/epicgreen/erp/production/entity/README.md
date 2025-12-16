# Production Module - Epic Green ERP

This directory contains **entities and DTOs** for production planning and execution management in the Epic Green ERP system.

## 📦 Contents

### Entities (production/entity) - 6 Files
1. **BillOfMaterials.java** - Production recipes/formulas (BOM)
2. **BomItem.java** - Ingredients in BOMs
3. **WorkOrder.java** - Production orders/jobs
4. **WorkOrderItem.java** - Products to produce in work orders
5. **MaterialConsumption.java** - Raw materials consumed
6. **ProductionOutput.java** - Finished goods produced

### DTOs (production/dto) - 3 Files
1. **BomDTO.java** - BOM data transfer object
2. **WorkOrderDTO.java** - Work order data transfer object
3. **CreateWorkOrderRequest.java** - Create work order request

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────────┐
│ BillOfMaterials  │ (Recipe/Formula)
│   (BOM)          │
└────────┬─────────┘
         │ 1:N
         ▼
┌────────────┐      ┌─────────┐
│  BomItem   │ ◄N:1─┤ Product │ (Raw Material)
└────────────┘      └─────────┘
                         ▲
                         │ N:1
┌────────────┐           │
│ WorkOrder  │ ──────────┘ (Finished Good)
└──────┬─────┘
       │ 1:N
       ├─────────────────────┬──────────────────────┐
       ▼                     ▼                      ▼
┌────────────────┐  ┌─────────────────┐  ┌──────────────────┐
│ WorkOrderItem  │  │MaterialConsump. │  │ProductionOutput  │
└────────────────┘  └─────────────────┘  └──────────────────┘
```

---

## 📋 1. BillOfMaterials (BOM) Entity

**Purpose:** Recipe/formula for producing finished goods

### Key Fields

```java
// Identification
- bomNumber (unique, e.g., "BOM-CINN-100G")
- bomName
- version (e.g., "1.0", "2.0")

// Product
- product (FK - what is being produced)

// Output
- outputQuantity (this BOM produces this much)
- outputUnit

// Type
- bomType (STANDARD, ALTERNATE, PROTOTYPE)
- description
- productionInstructions

// Time and costs
- processingTimeMinutes
- laborCost (per output quantity)
- overheadCost (per output quantity)
- totalMaterialCost (calculated from items)
- totalCost (material + labor + overhead)
- currency

// Validity
- validFrom (BOM effective from date)
- validTo (BOM expires on date)

// Status
- status (DRAFT, ACTIVE, INACTIVE, OBSOLETE)
- isDefault (default BOM for product)
- approvedBy
- approvalDate

- notes
- totalItems

// Relationships
- items (Set<BomItem>) - ingredients/components
```

### Helper Methods

```java
// Calculate total material cost from all items
void calculateTotalMaterialCost();

// Get cost per unit of output
BigDecimal getCostPerUnit(); // totalCost / outputQuantity

// Check if BOM is currently active
boolean isActive(); // ACTIVE status + within valid dates

// Check if BOM is valid on specific date
boolean isValidOn(LocalDate date);

// Check if BOM can be edited
boolean canEdit(); // Only DRAFT
```

### Table Structure

```sql
CREATE TABLE bill_of_materials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bom_number VARCHAR(50) NOT NULL UNIQUE,
    bom_name VARCHAR(200) NOT NULL,
    version VARCHAR(20),
    product_id BIGINT NOT NULL,
    
    output_quantity DECIMAL(15,3) NOT NULL,
    output_unit VARCHAR(10) NOT NULL,
    bom_type VARCHAR(20),
    description TEXT,
    production_instructions TEXT,
    
    processing_time_minutes INT,
    labor_cost DECIMAL(15,2),
    overhead_cost DECIMAL(15,2),
    total_material_cost DECIMAL(15,2),
    total_cost DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    valid_from DATE,
    valid_to DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    is_default BOOLEAN DEFAULT FALSE,
    approved_by VARCHAR(50),
    approval_date DATE,
    notes TEXT,
    total_items INT DEFAULT 0,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_bom_product FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_bom_number (bom_number),
    INDEX idx_bom_product (product_id),
    INDEX idx_bom_status (status)
);
```

### BOM Example

**BOM for: Cinnamon Powder 100g**
- Output: 100g
- Labor Cost: LKR 50
- Overhead: LKR 30
- Ingredients:
  1. Cinnamon Sticks: 110g (10% wastage)
  2. Packaging Pouch: 1 piece
  3. Label: 1 piece

---

## 🔧 2. BomItem Entity

**Purpose:** Individual ingredients/components in a BOM

### Key Fields

```java
// References
- billOfMaterials (FK header)
- product (FK - raw material/component)

// Description
- itemDescription (optional override)

// Quantity
- quantity (required per output quantity)
- unit
- wastagePercentage (scrap/loss %)
- actualQuantity (quantity + wastage)

// Cost
- unitCost (cost per unit of raw material)
- totalCost (actualQuantity * unitCost)
- currency

// Ordering and type
- sequenceNumber (step order in production)
- itemType (RAW_MATERIAL, PACKAGING, CONSUMABLE)
- isOptional

// Instructions
- preparationInstructions
- notes
```

### Helper Methods

```java
// Calculate actual quantity with wastage
void calculateActualQuantity();

// Calculate total cost
void calculateTotalCost();

// Get quantity needed for specific output
BigDecimal getQuantityForOutput(BigDecimal outputQuantity);
```

### Table Structure

```sql
CREATE TABLE bom_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bill_of_materials_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    item_description VARCHAR(500),
    
    quantity DECIMAL(15,3) NOT NULL,
    unit VARCHAR(10) NOT NULL,
    wastage_percentage DECIMAL(5,2),
    actual_quantity DECIMAL(15,3),
    
    unit_cost DECIMAL(15,2),
    total_cost DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    sequence_number INT,
    item_type VARCHAR(20) DEFAULT 'RAW_MATERIAL',
    is_optional BOOLEAN DEFAULT FALSE,
    preparation_instructions VARCHAR(500),
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_bom_item_bom 
        FOREIGN KEY (bill_of_materials_id) REFERENCES bill_of_materials(id),
    CONSTRAINT fk_bom_item_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_bom_item_bom (bill_of_materials_id),
    INDEX idx_bom_item_product (product_id)
);
```

---

## 🏭 3. WorkOrder Entity

**Purpose:** Production orders/jobs for manufacturing

### Key Fields

```java
// Identification
- woNumber (unique, e.g., "WO-2024-001")
- woDate

// Recipe
- billOfMaterials (FK - BOM to follow)

// Warehouses
- outputWarehouse (FK - where finished goods go)
- materialWarehouse (FK - where raw materials come from)

// Quantities
- plannedQuantity
- producedQuantity
- unit

// Schedule
- plannedStartDate
- plannedEndDate
- actualStartDate
- actualEndDate

// Assignment
- priority (LOW, MEDIUM, HIGH, URGENT)
- status (DRAFT, PLANNED, RELEASED, IN_PROGRESS, COMPLETED, CLOSED, CANCELLED, ON_HOLD)
- supervisor
- shift (MORNING, AFTERNOON, NIGHT)
- batchNumber

// Costs (Planned vs Actual)
- plannedMaterialCost
- actualMaterialCost
- plannedLaborCost
- actualLaborCost
- plannedOverheadCost
- actualOverheadCost
- totalPlannedCost
- totalActualCost
- currency

// Quality
- qualityCheckRequired
- qualityCheckStatus (PENDING, PASSED, FAILED)
- qualityRemarks

// Instructions
- productionInstructions
- notes

// Relationships
- items (Set<WorkOrderItem>)
- materialConsumptions (Set<MaterialConsumption>)
- productionOutputs (Set<ProductionOutput>)
```

### Helper Methods

```java
// Calculate planned costs based on BOM
void calculatePlannedCosts();

// Calculate actual costs from consumptions
void calculateActualCosts();

// Get completion percentage
BigDecimal getCompletionPercentage(); // (produced / planned) * 100

// Status checks
boolean isCompleted();
boolean canStart(); // RELEASED status
boolean canEdit(); // DRAFT or PLANNED
```

### Table Structure

```sql
CREATE TABLE work_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_number VARCHAR(50) NOT NULL UNIQUE,
    wo_date DATE NOT NULL,
    bom_id BIGINT NOT NULL,
    
    output_warehouse_id BIGINT,
    material_warehouse_id BIGINT,
    
    planned_quantity DECIMAL(15,3) NOT NULL,
    produced_quantity DECIMAL(15,3) DEFAULT 0,
    unit VARCHAR(10) NOT NULL,
    
    planned_start_date DATE,
    planned_end_date DATE,
    actual_start_date DATE,
    actual_end_date DATE,
    
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    supervisor VARCHAR(50),
    shift VARCHAR(20),
    batch_number VARCHAR(50),
    
    planned_material_cost DECIMAL(15,2),
    actual_material_cost DECIMAL(15,2),
    planned_labor_cost DECIMAL(15,2),
    actual_labor_cost DECIMAL(15,2),
    planned_overhead_cost DECIMAL(15,2),
    actual_overhead_cost DECIMAL(15,2),
    total_planned_cost DECIMAL(15,2),
    total_actual_cost DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    quality_check_required BOOLEAN DEFAULT TRUE,
    quality_check_status VARCHAR(20) DEFAULT 'PENDING',
    quality_remarks TEXT,
    production_instructions TEXT,
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_wo_bom FOREIGN KEY (bom_id) REFERENCES bill_of_materials(id),
    CONSTRAINT fk_wo_output_warehouse FOREIGN KEY (output_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_wo_material_warehouse FOREIGN KEY (material_warehouse_id) REFERENCES warehouses(id),
    INDEX idx_wo_number (wo_number),
    INDEX idx_wo_date (wo_date),
    INDEX idx_wo_bom (bom_id),
    INDEX idx_wo_status (status),
    INDEX idx_wo_start_date (planned_start_date)
);
```

### Work Order Status Flow

```
DRAFT
  ↓ (plan)
PLANNED
  ↓ (release to production floor)
RELEASED
  ↓ (start production)
IN_PROGRESS
  ↓ (complete production)
COMPLETED
  ↓ (close after review)
CLOSED
```

---

## 📦 4. WorkOrderItem Entity

**Purpose:** Products to produce in a work order

### Key Fields

```java
// References
- workOrder (FK header)
- product (FK - finished good to produce)

// Description
- itemDescription

// Quantities
- plannedQuantity
- producedQuantity
- goodQuantity (passed quality)
- rejectedQuantity (failed quality)
- unit

// Cost
- unitCost
- totalCost
- currency

- notes
```

### Helper Methods

```java
void calculateTotalCost();
BigDecimal getCompletionPercentage();
BigDecimal getQualityPassPercentage();
BigDecimal getPendingQuantity();
boolean isFullyProduced();
```

---

## ⚡ 5. MaterialConsumption Entity

**Purpose:** Track raw materials consumed during production

### Key Fields

```java
// References
- workOrder (FK)
- product (FK - raw material consumed)
- warehouse (FK - source warehouse)
- location (FK - source location)

// Date
- consumptionDate
- consumptionTimestamp

// Batch
- batchNumber
- serialNumber

// Quantities
- plannedQuantity (from BOM)
- consumedQuantity (actual used)
- wastageQuantity
- unit

// Cost
- costPerUnit
- totalCost
- currency

// Personnel
- consumedBy (operator)

// Posting
- isPosted (posted to inventory)
- notes
```

### Helper Methods

```java
void calculateTotalCost();
BigDecimal getVarianceQuantity(); // consumed - planned
BigDecimal getVariancePercentage();
boolean isOverConsumption();
boolean isUnderConsumption();
```

### Table Structure

```sql
CREATE TABLE material_consumptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    location_id BIGINT,
    
    consumption_date DATE NOT NULL,
    consumption_timestamp DATETIME,
    batch_number VARCHAR(50),
    serial_number VARCHAR(50),
    
    planned_quantity DECIMAL(15,3),
    consumed_quantity DECIMAL(15,3) NOT NULL,
    wastage_quantity DECIMAL(15,3) DEFAULT 0,
    unit VARCHAR(10) NOT NULL,
    
    cost_per_unit DECIMAL(15,2),
    total_cost DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    consumed_by VARCHAR(50),
    is_posted BOOLEAN DEFAULT FALSE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_mat_cons_work_order 
        FOREIGN KEY (work_order_id) REFERENCES work_orders(id),
    CONSTRAINT fk_mat_cons_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_mat_cons_wo (work_order_id),
    INDEX idx_mat_cons_product (product_id),
    INDEX idx_mat_cons_date (consumption_date)
);
```

---

## 📤 6. ProductionOutput Entity

**Purpose:** Finished goods produced from work orders

### Key Fields

```java
// References
- workOrder (FK)
- workOrderItem (FK)
- product (FK - finished good produced)
- warehouse (FK - destination warehouse)
- location (FK - storage location)

// Date
- outputDate
- outputTimestamp

// Batch
- batchNumber (assigned to output)
- serialNumber

// Quantities
- quantityProduced
- goodQuantity (passed quality)
- rejectedQuantity (failed quality)
- reworkQuantity (needs rework)
- unit

// Dates
- manufacturingDate
- expiryDate

// Cost
- unitCost (cost to produce)
- totalCost
- currency

// Quality
- qualityStatus (PENDING, GOOD, ACCEPTABLE, POOR, REJECTED)
- qualityRemarks
- qualityInspector
- qualityCheckDate
- moistureContent (%)
- purity (%)

// Personnel
- producedBy (operator)

// Posting
- isPosted (posted to inventory)
- postedDate
- notes
```

### Helper Methods

```java
void calculateTotalCost();
BigDecimal getQualityPassPercentage();
BigDecimal getRejectionPercentage();
boolean isQualityAcceptable();
boolean hasRejection();
```

### Table Structure

```sql
CREATE TABLE production_outputs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_order_id BIGINT NOT NULL,
    work_order_item_id BIGINT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    location_id BIGINT,
    
    output_date DATE NOT NULL,
    output_timestamp DATETIME,
    batch_number VARCHAR(50),
    serial_number VARCHAR(50),
    
    quantity_produced DECIMAL(15,3) NOT NULL,
    good_quantity DECIMAL(15,3),
    rejected_quantity DECIMAL(15,3) DEFAULT 0,
    rework_quantity DECIMAL(15,3) DEFAULT 0,
    unit VARCHAR(10) NOT NULL,
    
    manufacturing_date DATE,
    expiry_date DATE,
    
    unit_cost DECIMAL(15,2),
    total_cost DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    quality_status VARCHAR(20) DEFAULT 'PENDING',
    quality_remarks TEXT,
    quality_inspector VARCHAR(50),
    quality_check_date DATE,
    moisture_content DECIMAL(5,2),
    purity DECIMAL(5,2),
    
    produced_by VARCHAR(50),
    is_posted BOOLEAN DEFAULT FALSE,
    posted_date DATE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_prod_output_work_order 
        FOREIGN KEY (work_order_id) REFERENCES work_orders(id),
    INDEX idx_prod_output_wo (work_order_id),
    INDEX idx_prod_output_product (product_id),
    INDEX idx_prod_output_date (output_date),
    INDEX idx_prod_output_batch (batch_number)
);
```

---

## 💡 Usage Examples

### Example 1: Create BOM

```java
@Transactional
public BomDTO createBom(CreateBomRequest request) {
    // Get product
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new NotFoundException("Product not found"));
    
    // Create BOM
    BillOfMaterials bom = BillOfMaterials.builder()
        .bomNumber(generateBomNumber())
        .bomName(request.getBomName())
        .product(product)
        .outputQuantity(request.getOutputQuantity())
        .outputUnit(request.getOutputUnit())
        .bomType(request.getBomType())
        .processingTimeMinutes(request.getProcessingTimeMinutes())
        .laborCost(request.getLaborCost())
        .overheadCost(request.getOverheadCost())
        .status("DRAFT")
        .build();
    
    // Add items
    for (var itemRequest : request.getItems()) {
        Product rawMaterial = productRepository.findById(itemRequest.getProductId())
            .orElseThrow(() -> new NotFoundException("Raw material not found"));
        
        BomItem item = BomItem.builder()
            .product(rawMaterial)
            .quantity(itemRequest.getQuantity())
            .unit(itemRequest.getUnit())
            .wastagePercentage(itemRequest.getWastagePercentage())
            .unitCost(itemRequest.getUnitCost())
            .sequenceNumber(itemRequest.getSequenceNumber())
            .itemType(itemRequest.getItemType())
            .build();
        
        // Calculate actual quantity and cost
        item.calculateActualQuantity();
        item.calculateTotalCost();
        
        bom.addItem(item);
    }
    
    // Calculate total material cost
    bom.calculateTotalMaterialCost();
    
    bom = bomRepository.save(bom);
    return mapper.toDTO(bom);
}
```

### Example 2: Create Work Order from BOM

```java
@Transactional
public WorkOrderDTO createWorkOrder(CreateWorkOrderRequest request) {
    // Get BOM
    BillOfMaterials bom = bomRepository.findById(request.getBomId())
        .orElseThrow(() -> new NotFoundException("BOM not found"));
    
    if (!bom.isActive()) {
        throw new BusinessException("BOM is not active");
    }
    
    // Create work order
    WorkOrder wo = WorkOrder.builder()
        .woNumber(generateWoNumber())
        .woDate(request.getWoDate())
        .billOfMaterials(bom)
        .plannedQuantity(request.getPlannedQuantity())
        .unit(request.getUnit())
        .plannedStartDate(request.getPlannedStartDate())
        .plannedEndDate(request.getPlannedEndDate())
        .priority(request.getPriority())
        .supervisor(request.getSupervisor())
        .shift(request.getShift())
        .batchNumber(generateBatchNumber())
        .status("DRAFT")
        .build();
    
    // Calculate planned costs based on BOM
    wo.calculatePlannedCosts();
    
    // Create work order item for the product
    WorkOrderItem woItem = WorkOrderItem.builder()
        .product(bom.getProduct())
        .plannedQuantity(request.getPlannedQuantity())
        .unit(request.getUnit())
        .build();
    
    wo.addItem(woItem);
    
    wo = workOrderRepository.save(wo);
    return mapper.toDTO(wo);
}
```

### Example 3: Release Work Order to Production

```java
@Transactional
public void releaseWorkOrder(Long woId) {
    WorkOrder wo = workOrderRepository.findById(woId)
        .orElseThrow(() -> new NotFoundException("Work order not found"));
    
    if (!"PLANNED".equals(wo.getStatus())) {
        throw new BusinessException("Work order must be PLANNED to release");
    }
    
    // Check material availability
    checkMaterialAvailability(wo);
    
    wo.setStatus("RELEASED");
    workOrderRepository.save(wo);
}

private void checkMaterialAvailability(WorkOrder wo) {
    BillOfMaterials bom = wo.getBillOfMaterials();
    BigDecimal ratio = wo.getPlannedQuantity()
        .divide(bom.getOutputQuantity(), 6, BigDecimal.ROUND_HALF_UP);
    
    for (BomItem bomItem : bom.getItems()) {
        BigDecimal requiredQty = bomItem.getActualQuantity().multiply(ratio);
        
        // Check inventory
        BigDecimal availableQty = inventoryRepository
            .getTotalAvailableQuantity(bomItem.getProduct().getId());
        
        if (availableQty.compareTo(requiredQty) < 0) {
            throw new BusinessException(
                "Insufficient material: " + bomItem.getProduct().getProductName() +
                ". Required: " + requiredQty + ", Available: " + availableQty
            );
        }
    }
}
```

### Example 4: Record Material Consumption

```java
@Transactional
public void consumeMaterial(Long woId, MaterialConsumptionRequest request) {
    WorkOrder wo = workOrderRepository.findById(woId)
        .orElseThrow(() -> new NotFoundException("Work order not found"));
    
    if (!"IN_PROGRESS".equals(wo.getStatus())) {
        throw new BusinessException("Work order must be IN_PROGRESS");
    }
    
    Product rawMaterial = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new NotFoundException("Product not found"));
    
    // Get inventory
    Inventory inventory = inventoryRepository
        .findByProductAndWarehouseAndBatch(
            request.getProductId(),
            request.getWarehouseId(),
            request.getBatchNumber()
        )
        .orElseThrow(() -> new NotFoundException("Inventory not found"));
    
    // Check availability
    if (inventory.getAvailableQuantity().compareTo(request.getConsumedQuantity()) < 0) {
        throw new BusinessException("Insufficient inventory");
    }
    
    // Create material consumption record
    MaterialConsumption consumption = MaterialConsumption.builder()
        .workOrder(wo)
        .product(rawMaterial)
        .warehouse(inventory.getWarehouse())
        .location(inventory.getLocation())
        .consumptionDate(LocalDate.now())
        .batchNumber(request.getBatchNumber())
        .plannedQuantity(request.getPlannedQuantity())
        .consumedQuantity(request.getConsumedQuantity())
        .costPerUnit(inventory.getCostPerUnit())
        .consumedBy(getCurrentUsername())
        .build();
    
    consumption.calculateTotalCost();
    materialConsumptionRepository.save(consumption);
    
    // Deduct from inventory
    inventory.setAvailableQuantity(
        inventory.getAvailableQuantity().subtract(request.getConsumedQuantity())
    );
    inventoryRepository.save(inventory);
    
    // Create stock movement
    StockMovement movement = StockMovement.builder()
        .movementDate(LocalDate.now())
        .movementType("PRODUCTION")
        .product(rawMaterial)
        .fromWarehouse(inventory.getWarehouse())
        .fromLocation(inventory.getLocation())
        .quantity(request.getConsumedQuantity())
        .referenceNumber(wo.getWoNumber())
        .referenceType("WORK_ORDER")
        .status("COMPLETED")
        .build();
    
    stockMovementRepository.save(movement);
}
```

### Example 5: Record Production Output

```java
@Transactional
public void recordProduction(Long woId, ProductionOutputRequest request) {
    WorkOrder wo = workOrderRepository.findById(woId)
        .orElseThrow(() -> new NotFoundException("Work order not found"));
    
    WorkOrderItem woItem = wo.getItems().stream()
        .filter(i -> i.getProduct().getId().equals(request.getProductId()))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("Work order item not found"));
    
    // Create production output
    ProductionOutput output = ProductionOutput.builder()
        .workOrder(wo)
        .workOrderItem(woItem)
        .product(woItem.getProduct())
        .warehouse(wo.getOutputWarehouse())
        .outputDate(LocalDate.now())
        .batchNumber(wo.getBatchNumber())
        .quantityProduced(request.getQuantityProduced())
        .goodQuantity(request.getGoodQuantity())
        .rejectedQuantity(request.getRejectedQuantity())
        .unit(woItem.getUnit())
        .manufacturingDate(LocalDate.now())
        .expiryDate(calculateExpiryDate(woItem.getProduct()))
        .qualityStatus(request.getQualityStatus())
        .producedBy(getCurrentUsername())
        .build();
    
    output.calculateTotalCost();
    productionOutputRepository.save(output);
    
    // Update work order item produced quantity
    woItem.setProducedQuantity(
        woItem.getProducedQuantity().add(request.getQuantityProduced())
    );
    woItem.setGoodQuantity(
        woItem.getGoodQuantity().add(request.getGoodQuantity())
    );
    
    // Update work order produced quantity
    wo.setProducedQuantity(
        wo.getProducedQuantity().add(request.getQuantityProduced())
    );
    
    // Check if completed
    if (wo.getProducedQuantity().compareTo(wo.getPlannedQuantity()) >= 0) {
        wo.setStatus("COMPLETED");
        wo.setActualEndDate(LocalDate.now());
    }
    
    workOrderRepository.save(wo);
}
```

### Example 6: Post Production Output to Inventory

```java
@Transactional
public void postProductionToInventory(Long outputId) {
    ProductionOutput output = productionOutputRepository.findById(outputId)
        .orElseThrow(() -> new NotFoundException("Production output not found"));
    
    if (output.getIsPosted()) {
        throw new BusinessException("Already posted");
    }
    
    if (!output.isQualityAcceptable()) {
        throw new BusinessException("Quality not acceptable");
    }
    
    // Find or create inventory
    Inventory inventory = inventoryRepository
        .findByProductAndWarehouseAndBatch(
            output.getProduct().getId(),
            output.getWarehouse().getId(),
            output.getBatchNumber()
        )
        .orElse(new Inventory());
    
    if (inventory.getId() == null) {
        inventory.setProduct(output.getProduct());
        inventory.setWarehouse(output.getWarehouse());
        inventory.setLocation(output.getLocation());
        inventory.setBatchNumber(output.getBatchNumber());
        inventory.setManufacturingDate(output.getManufacturingDate());
        inventory.setExpiryDate(output.getExpiryDate());
        inventory.setCostPerUnit(output.getUnitCost());
        inventory.setAvailableQuantity(BigDecimal.ZERO);
    }
    
    // Add good quantity to inventory
    inventory.setAvailableQuantity(
        inventory.getAvailableQuantity().add(output.getGoodQuantity())
    );
    
    inventoryRepository.save(inventory);
    
    // Create stock movement
    StockMovement movement = StockMovement.builder()
        .movementDate(output.getOutputDate())
        .movementType("IN")
        .product(output.getProduct())
        .toWarehouse(output.getWarehouse())
        .toLocation(output.getLocation())
        .batchNumber(output.getBatchNumber())
        .quantity(output.getGoodQuantity())
        .referenceNumber(output.getWorkOrder().getWoNumber())
        .referenceType("PRODUCTION")
        .status("COMPLETED")
        .build();
    
    stockMovementRepository.save(movement);
    
    // Mark as posted
    output.setIsPosted(true);
    output.setPostedDate(LocalDate.now());
    productionOutputRepository.save(output);
}
```

---

## 📁 Directory Structure

```
production/
├── entity/
│   ├── BillOfMaterials.java
│   ├── BomItem.java
│   ├── WorkOrder.java
│   ├── WorkOrderItem.java
│   ├── MaterialConsumption.java
│   └── ProductionOutput.java
├── dto/
│   ├── BomDTO.java
│   ├── WorkOrderDTO.java
│   └── CreateWorkOrderRequest.java
└── README.md
```

---

## ✅ Summary

✅ **6 Entity classes** - Complete production management  
✅ **3 DTO classes** - Request/response objects  
✅ **BOM management** - Recipe/formula for production  
✅ **Multi-level BOMs** - Support for alternate BOMs and versions  
✅ **Cost tracking** - Material, labor, overhead costs  
✅ **Work order workflow** - DRAFT → PLANNED → RELEASED → IN_PROGRESS → COMPLETED → CLOSED  
✅ **Material consumption tracking** - Track what was actually used  
✅ **Production output tracking** - Track what was produced  
✅ **Quality control** - Quality status, moisture, purity tracking  
✅ **Batch tracking** - Assign batch numbers to production  
✅ **Variance analysis** - Compare planned vs actual  
✅ **Wastage tracking** - Account for scrap/loss  
✅ **Inventory integration** - Auto-deduct materials, auto-add outputs  
✅ **Stock movement** - Complete audit trail  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade production management  

**Everything you need for complete production planning and execution in a spice production factory!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.production
