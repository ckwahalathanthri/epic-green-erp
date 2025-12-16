# Purchase Module - Epic Green ERP

This directory contains **entities and DTOs** for purchase order and goods receipt management in the Epic Green ERP system.

## 📦 Contents

### Entities (purchase/entity) - 4 Files
1. **PurchaseOrder.java** - Purchase order headers
2. **PurchaseOrderItem.java** - PO line items
3. **GoodsReceiptNote.java** - Goods receipt notes (GRN)
4. **GrnItem.java** - GRN line items

### DTOs (purchase/dto) - 3 Files
1. **PurchaseOrderDTO.java** - Purchase order data transfer object
2. **CreatePurchaseOrderRequest.java** - Create purchase order request
3. **GoodsReceiptDTO.java** - GRN data transfer object

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────┐
│ Supplier │
└────┬─────┘
     │
     │ N:1
     ▼
┌────────────────┐         ┌─────────┐
│ PurchaseOrder  │ ◄───N:1─┤ Product │
└────────┬───────┘         └─────────┘
         │ 1:N
         ▼
┌────────────────────┐
│ PurchaseOrderItem  │
└────────┬───────────┘
         │ 1:N
         ▼
┌───────────────────┐      ┌───────────┐
│ GoodsReceiptNote  │ ◄N:1─┤ Warehouse │
└────────┬──────────┘      └───────────┘
         │ 1:N
         ▼
┌────────────┐      ┌──────────────────┐
│  GrnItem   │ ◄N:1─┤ WarehouseLocation│
└────────────┘      └──────────────────┘
```

---

## 🛒 1. PurchaseOrder Entity

**Purpose:** Purchase orders for raw materials and goods

### Key Fields

```java
// Identification
- poNumber (unique, e.g., "PO-2024-001")
- poDate
- supplier (FK)

// Delivery
- expectedDeliveryDate
- deliveryAddress
- deliveryContactPerson
- deliveryContactNumber

// Classification
- poType (RAW_MATERIAL, PACKAGING, SERVICES, OTHERS)

// Currency
- currency (LKR, USD, etc.)
- exchangeRate (if foreign currency)

// Financial
- subtotal (sum of items)
- discountAmount
- discountPercentage
- taxAmount
- taxPercentage
- shippingCost
- otherCharges
- totalAmount (subtotal - discount + tax + shipping + other)

// Payment terms
- paymentTerms (e.g., "Net 30", "50% advance")
- paymentDays

// Workflow
- status (DRAFT, PENDING_APPROVAL, APPROVED, SENT, ACKNOWLEDGED, PARTIAL, COMPLETED, CANCELLED, CLOSED)
- preparedBy
- approvedBy
- approvalDate
- sentDate
- acknowledgedDate

// Additional
- termsAndConditions
- notes (visible to supplier)
- internalNotes (internal only)
- totalItems

// Relationships
- items (Set<PurchaseOrderItem>)
- goodsReceiptNotes (Set<GoodsReceiptNote>)
```

### Helper Methods

```java
// Calculate all totals
void calculateTotals();
// Recalculates subtotal, discount, tax, total based on items

// Status checks
boolean isPendingApproval();
boolean isApproved();
boolean canEdit(); // Only DRAFT
boolean canApprove(); // Only PENDING_APPROVAL
boolean canReceiveGoods(); // SENT, ACKNOWLEDGED, PARTIAL
```

### Table Structure

```sql
CREATE TABLE purchase_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    po_number VARCHAR(50) NOT NULL UNIQUE,
    po_date DATE NOT NULL,
    supplier_id BIGINT NOT NULL,
    
    expected_delivery_date DATE,
    delivery_address TEXT,
    delivery_contact_person VARCHAR(100),
    delivery_contact_number VARCHAR(20),
    
    po_type VARCHAR(30),
    currency VARCHAR(10) NOT NULL DEFAULT 'LKR',
    exchange_rate DECIMAL(15,6) DEFAULT 1.0,
    
    subtotal DECIMAL(15,2) NOT NULL,
    discount_amount DECIMAL(15,2),
    discount_percentage DECIMAL(5,2),
    tax_amount DECIMAL(15,2),
    tax_percentage DECIMAL(5,2),
    shipping_cost DECIMAL(15,2),
    other_charges DECIMAL(15,2),
    total_amount DECIMAL(15,2) NOT NULL,
    
    payment_terms VARCHAR(255),
    payment_days INT,
    
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    prepared_by VARCHAR(50),
    approved_by VARCHAR(50),
    approval_date DATE,
    sent_date DATE,
    acknowledged_date DATE,
    
    terms_and_conditions TEXT,
    notes TEXT,
    internal_notes TEXT,
    total_items INT DEFAULT 0,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    INDEX idx_po_number (po_number),
    INDEX idx_po_date (po_date),
    INDEX idx_po_supplier (supplier_id),
    INDEX idx_po_status (status)
);
```

### PO Status Flow

```
DRAFT
  ↓ (submit for approval)
PENDING_APPROVAL
  ↓ (approve)
APPROVED
  ↓ (send to supplier)
SENT
  ↓ (supplier acknowledges)
ACKNOWLEDGED
  ↓ (receive some items)
PARTIAL
  ↓ (receive all items)
COMPLETED
  ↓ (close)
CLOSED
```

---

## 📋 2. PurchaseOrderItem Entity

**Purpose:** Line items in purchase orders

### Key Fields

```java
// References
- purchaseOrder (FK header)
- product (FK)

// Description
- itemDescription (can override product description)

// Quantities
- orderedQuantity
- receivedQuantity (from GRNs)
- pendingQuantity (ordered - received)

- unit

// Pricing
- unitPrice
- discountPercentage
- discountAmount
- taxPercentage
- taxAmount
- lineTotal (quantity * price - discount + tax)

// Delivery
- expectedDeliveryDate (item specific)

- notes
```

### Helper Methods

```java
// Calculate line total
void calculateLineTotal();
// Recalculates lineTotal with discount and tax

// Get net amount (before tax)
BigDecimal getNetAmount();

// Status checks
boolean isFullyReceived(); // receivedQuantity >= orderedQuantity
boolean isPartiallyReceived(); // receivedQuantity > 0 && < orderedQuantity

// Get received percentage
BigDecimal getReceivedPercentage(); // (received / ordered) * 100
```

### Table Structure

```sql
CREATE TABLE purchase_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    item_description VARCHAR(500),
    
    ordered_quantity DECIMAL(15,3) NOT NULL,
    received_quantity DECIMAL(15,3) DEFAULT 0,
    pending_quantity DECIMAL(15,3),
    unit VARCHAR(10) NOT NULL,
    
    unit_price DECIMAL(15,2) NOT NULL,
    discount_percentage DECIMAL(5,2),
    discount_amount DECIMAL(15,2),
    tax_percentage DECIMAL(5,2),
    tax_amount DECIMAL(15,2),
    line_total DECIMAL(15,2) NOT NULL,
    
    expected_delivery_date DATE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_po_item_purchase_order 
        FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_po_item_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_po_item_po (purchase_order_id),
    INDEX idx_po_item_product (product_id)
);
```

---

## 📥 3. GoodsReceiptNote Entity

**Purpose:** Goods receipt when items are received from suppliers

### Key Fields

```java
// Identification
- grnNumber (unique, e.g., "GRN-2024-001")
- grnDate
- grnTimestamp

// References
- purchaseOrder (FK)
- supplier (FK)
- warehouse (FK where goods are received)

// Supplier documents
- supplierInvoiceNumber
- supplierInvoiceDate
- deliveryNoteNumber

// Delivery details
- vehicleNumber
- driverName
- driverContact

// Personnel
- receivedBy (person who received)
- inspectedBy (quality inspector)
- inspectionDate

// Inspection
- inspectionStatus (PENDING, PASSED, FAILED, CONDITIONAL)
- inspectionRemarks

// Quantities
- totalItems
- totalReceivedQuantity
- totalAcceptedQuantity (after inspection)
- totalRejectedQuantity

// Status
- status (DRAFT, RECEIVED, INSPECTED, POSTED, CANCELLED)
- isPosted (posted to inventory)
- postedDate
- postedBy

- notes

// Relationships
- items (Set<GrnItem>)
```

### Helper Methods

```java
// Calculate totals
void calculateTotals();
// Sums received, accepted, rejected quantities

// Inspection checks
boolean isPendingInspection();
boolean isInspectionPassed();
boolean isInspectionFailed();

// Status checks
boolean canPost(); // INSPECTED && !isPosted && inspection PASSED
boolean canEdit(); // DRAFT or RECEIVED
```

### Table Structure

```sql
CREATE TABLE goods_receipt_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    grn_number VARCHAR(50) NOT NULL UNIQUE,
    grn_date DATE NOT NULL,
    grn_timestamp DATETIME NOT NULL,
    
    purchase_order_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    
    supplier_invoice_number VARCHAR(50),
    supplier_invoice_date DATE,
    delivery_note_number VARCHAR(50),
    
    vehicle_number VARCHAR(30),
    driver_name VARCHAR(100),
    driver_contact VARCHAR(20),
    
    received_by VARCHAR(50) NOT NULL,
    inspected_by VARCHAR(50),
    inspection_date DATE,
    inspection_status VARCHAR(20) DEFAULT 'PENDING',
    inspection_remarks TEXT,
    
    total_items INT DEFAULT 0,
    total_received_quantity DECIMAL(15,3),
    total_accepted_quantity DECIMAL(15,3),
    total_rejected_quantity DECIMAL(15,3),
    
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    is_posted BOOLEAN DEFAULT FALSE,
    posted_date DATE,
    posted_by VARCHAR(50),
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_grn_purchase_order 
        FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_grn_supplier 
        FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_grn_warehouse 
        FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    INDEX idx_grn_number (grn_number),
    INDEX idx_grn_date (grn_date),
    INDEX idx_grn_po (purchase_order_id),
    INDEX idx_grn_supplier (supplier_id),
    INDEX idx_grn_status (status)
);
```

### GRN Status Flow

```
DRAFT
  ↓ (record receipt)
RECEIVED
  ↓ (perform inspection)
INSPECTED (inspection PASSED/FAILED)
  ↓ (post to inventory - only if PASSED)
POSTED
```

---

## 📦 4. GrnItem Entity

**Purpose:** Line items in goods receipt notes

### Key Fields

```java
// References
- goodsReceiptNote (FK header)
- poItem (FK original PO line item)
- product (FK)
- location (FK warehouse location)

// Batch/Serial
- batchNumber (assigned)
- serialNumber

// Quantities
- orderedQuantity (from PO)
- receivedQuantity (actual received)
- acceptedQuantity (after inspection)
- rejectedQuantity
- damagedQuantity

- unit
- unitPrice (from PO)

// Quality control
- manufacturingDate
- expiryDate
- qualityStatus (GOOD, ACCEPTABLE, POOR, REJECTED)
- qualityRemarks
- moistureContent (% - for spices)
- purity (% - for spices)
- sampleWeight

- rejectionReason
- notes
```

### Helper Methods

```java
// Value calculations
BigDecimal getTotalValue(); // receivedQuantity * unitPrice
BigDecimal getAcceptedValue(); // acceptedQuantity * unitPrice

// Variance
BigDecimal getVarianceQuantity(); // received - ordered
BigDecimal getVariancePercentage(); // variance / ordered * 100

// Status checks
boolean isFullyReceived(); // receivedQuantity >= orderedQuantity
boolean hasRejection(); // rejectedQuantity > 0
boolean isQualityAcceptable(); // GOOD or ACCEPTABLE
```

### Table Structure

```sql
CREATE TABLE grn_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_receipt_note_id BIGINT NOT NULL,
    po_item_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    location_id BIGINT,
    
    batch_number VARCHAR(50),
    serial_number VARCHAR(50),
    
    ordered_quantity DECIMAL(15,3) NOT NULL,
    received_quantity DECIMAL(15,3) NOT NULL,
    accepted_quantity DECIMAL(15,3),
    rejected_quantity DECIMAL(15,3) DEFAULT 0,
    damaged_quantity DECIMAL(15,3) DEFAULT 0,
    unit VARCHAR(10) NOT NULL,
    
    unit_price DECIMAL(15,2),
    
    manufacturing_date DATE,
    expiry_date DATE,
    quality_status VARCHAR(20) DEFAULT 'GOOD',
    quality_remarks VARCHAR(500),
    moisture_content DECIMAL(5,2),
    purity DECIMAL(5,2),
    sample_weight DECIMAL(10,3),
    
    rejection_reason VARCHAR(500),
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_grn_item_grn 
        FOREIGN KEY (goods_receipt_note_id) REFERENCES goods_receipt_notes(id),
    CONSTRAINT fk_grn_item_po_item 
        FOREIGN KEY (po_item_id) REFERENCES purchase_order_items(id),
    CONSTRAINT fk_grn_item_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_grn_item_location 
        FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    INDEX idx_grn_item_grn (goods_receipt_note_id),
    INDEX idx_grn_item_po_item (po_item_id),
    INDEX idx_grn_item_product (product_id)
);
```

---

## 💡 Usage Examples

### Example 1: Create Purchase Order

```java
@Service
public class PurchaseService {
    
    @Transactional
    public PurchaseOrderDTO createPurchaseOrder(CreatePurchaseOrderRequest request) {
        // Get supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new NotFoundException("Supplier not found"));
        
        // Create PO header
        PurchaseOrder po = PurchaseOrder.builder()
            .poNumber(generatePoNumber())
            .poDate(request.getPoDate())
            .supplier(supplier)
            .expectedDeliveryDate(request.getExpectedDeliveryDate())
            .currency(request.getCurrency())
            .exchangeRate(request.getExchangeRate())
            .discountPercentage(request.getDiscountPercentage())
            .taxPercentage(request.getTaxPercentage())
            .shippingCost(request.getShippingCost())
            .paymentTerms(request.getPaymentTerms())
            .status("DRAFT")
            .preparedBy(getCurrentUsername())
            .build();
        
        // Add items
        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
            
            PurchaseOrderItem item = PurchaseOrderItem.builder()
                .product(product)
                .orderedQuantity(itemRequest.getOrderedQuantity())
                .unit(itemRequest.getUnit())
                .unitPrice(itemRequest.getUnitPrice())
                .discountPercentage(itemRequest.getDiscountPercentage())
                .taxPercentage(itemRequest.getTaxPercentage())
                .build();
            
            // Calculate line total
            item.calculateLineTotal();
            po.addItem(item);
        }
        
        // Calculate PO totals
        po.calculateTotals();
        
        po = purchaseOrderRepository.save(po);
        return mapper.toDTO(po);
    }
}
```

### Example 2: Approve Purchase Order

```java
@Transactional
public void approvePurchaseOrder(Long poId) {
    PurchaseOrder po = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new NotFoundException("PO not found"));
    
    if (!po.canApprove()) {
        throw new BusinessException("PO cannot be approved in current status");
    }
    
    po.setStatus("APPROVED");
    po.setApprovedBy(getCurrentUsername());
    po.setApprovalDate(LocalDate.now());
    
    purchaseOrderRepository.save(po);
    
    // Record in supplier ledger
    SupplierLedger ledger = SupplierLedger.builder()
        .supplier(po.getSupplier())
        .transactionDate(LocalDate.now())
        .transactionType("PURCHASE")
        .referenceNumber(po.getPoNumber())
        .referenceType("PURCHASE_ORDER")
        .debitAmount(po.getTotalAmount())
        .balance(calculateNewBalance(po.getSupplier(), po.getTotalAmount()))
        .dueDate(LocalDate.now().plusDays(po.getPaymentDays()))
        .paymentStatus("PENDING")
        .build();
    
    supplierLedgerRepository.save(ledger);
}
```

### Example 3: Create Goods Receipt Note

```java
@Transactional
public GoodsReceiptDTO createGoodsReceipt(CreateGoodsReceiptRequest request) {
    // Get PO
    PurchaseOrder po = purchaseOrderRepository.findById(request.getPurchaseOrderId())
        .orElseThrow(() -> new NotFoundException("PO not found"));
    
    if (!po.canReceiveGoods()) {
        throw new BusinessException("PO cannot receive goods in current status");
    }
    
    // Get warehouse
    Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
        .orElseThrow(() -> new NotFoundException("Warehouse not found"));
    
    // Create GRN header
    GoodsReceiptNote grn = GoodsReceiptNote.builder()
        .grnNumber(generateGrnNumber())
        .grnDate(LocalDate.now())
        .grnTimestamp(LocalDateTime.now())
        .purchaseOrder(po)
        .supplier(po.getSupplier())
        .warehouse(warehouse)
        .supplierInvoiceNumber(request.getSupplierInvoiceNumber())
        .vehicleNumber(request.getVehicleNumber())
        .receivedBy(getCurrentUsername())
        .status("RECEIVED")
        .build();
    
    // Add items
    for (var itemRequest : request.getItems()) {
        PurchaseOrderItem poItem = purchaseOrderItemRepository
            .findById(itemRequest.getPoItemId())
            .orElseThrow(() -> new NotFoundException("PO item not found"));
        
        GrnItem grnItem = GrnItem.builder()
            .poItem(poItem)
            .product(poItem.getProduct())
            .orderedQuantity(poItem.getOrderedQuantity())
            .receivedQuantity(itemRequest.getReceivedQuantity())
            .acceptedQuantity(itemRequest.getReceivedQuantity()) // Initially same
            .unit(poItem.getUnit())
            .unitPrice(poItem.getUnitPrice())
            .batchNumber(itemRequest.getBatchNumber())
            .manufacturingDate(itemRequest.getManufacturingDate())
            .expiryDate(itemRequest.getExpiryDate())
            .build();
        
        grn.addItem(grnItem);
        
        // Update PO item received quantity
        poItem.setReceivedQuantity(
            poItem.getReceivedQuantity().add(itemRequest.getReceivedQuantity())
        );
    }
    
    // Calculate totals
    grn.calculateTotals();
    
    grn = goodsReceiptNoteRepository.save(grn);
    
    // Update PO status
    updatePurchaseOrderStatus(po);
    
    return mapper.toDTO(grn);
}
```

### Example 4: Perform Quality Inspection

```java
@Transactional
public void performInspection(Long grnId, InspectionRequest request) {
    GoodsReceiptNote grn = goodsReceiptNoteRepository.findById(grnId)
        .orElseThrow(() -> new NotFoundException("GRN not found"));
    
    if (!"RECEIVED".equals(grn.getStatus())) {
        throw new BusinessException("GRN must be in RECEIVED status for inspection");
    }
    
    // Update inspection details
    grn.setInspectedBy(getCurrentUsername());
    grn.setInspectionDate(LocalDate.now());
    grn.setInspectionStatus(request.getInspectionStatus());
    grn.setInspectionRemarks(request.getRemarks());
    grn.setStatus("INSPECTED");
    
    // Update items with inspection results
    for (var itemResult : request.getItemResults()) {
        GrnItem item = grn.getItems().stream()
            .filter(i -> i.getId().equals(itemResult.getGrnItemId()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("GRN item not found"));
        
        item.setAcceptedQuantity(itemResult.getAcceptedQuantity());
        item.setRejectedQuantity(itemResult.getRejectedQuantity());
        item.setQualityStatus(itemResult.getQualityStatus());
        item.setQualityRemarks(itemResult.getQualityRemarks());
        item.setMoistureContent(itemResult.getMoistureContent());
        item.setPurity(itemResult.getPurity());
    }
    
    // Recalculate totals
    grn.calculateTotals();
    
    goodsReceiptNoteRepository.save(grn);
}
```

### Example 5: Post GRN to Inventory

```java
@Transactional
public void postGrnToInventory(Long grnId) {
    GoodsReceiptNote grn = goodsReceiptNoteRepository.findById(grnId)
        .orElseThrow(() -> new NotFoundException("GRN not found"));
    
    if (!grn.canPost()) {
        throw new BusinessException("GRN cannot be posted");
    }
    
    for (GrnItem grnItem : grn.getItems()) {
        if (!grnItem.isQualityAcceptable()) {
            continue; // Skip rejected items
        }
        
        // Find or create inventory
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouseAndBatch(
                grnItem.getProduct().getId(),
                grn.getWarehouse().getId(),
                grnItem.getBatchNumber()
            )
            .orElse(new Inventory());
        
        if (inventory.getId() == null) {
            inventory.setProduct(grnItem.getProduct());
            inventory.setWarehouse(grn.getWarehouse());
            inventory.setLocation(grnItem.getLocation());
            inventory.setBatchNumber(grnItem.getBatchNumber());
            inventory.setManufacturingDate(grnItem.getManufacturingDate());
            inventory.setExpiryDate(grnItem.getExpiryDate());
            inventory.setCostPerUnit(grnItem.getUnitPrice());
            inventory.setAvailableQuantity(BigDecimal.ZERO);
        }
        
        // Add accepted quantity to inventory
        inventory.setAvailableQuantity(
            inventory.getAvailableQuantity().add(grnItem.getAcceptedQuantity())
        );
        
        inventoryRepository.save(inventory);
        
        // Create stock movement
        StockMovement movement = StockMovement.builder()
            .movementDate(grn.getGrnDate())
            .movementType("IN")
            .product(grnItem.getProduct())
            .toWarehouse(grn.getWarehouse())
            .toLocation(grnItem.getLocation())
            .batchNumber(grnItem.getBatchNumber())
            .quantity(grnItem.getAcceptedQuantity())
            .costPerUnit(grnItem.getUnitPrice())
            .referenceNumber(grn.getGrnNumber())
            .referenceType("GRN")
            .status("COMPLETED")
            .build();
        
        stockMovementRepository.save(movement);
    }
    
    // Mark GRN as posted
    grn.setIsPosted(true);
    grn.setPostedDate(LocalDate.now());
    grn.setPostedBy(getCurrentUsername());
    grn.setStatus("POSTED");
    
    goodsReceiptNoteRepository.save(grn);
}
```

---

## 📁 Directory Structure

```
purchase/
├── entity/
│   ├── PurchaseOrder.java
│   ├── PurchaseOrderItem.java
│   ├── GoodsReceiptNote.java
│   └── GrnItem.java
├── dto/
│   ├── PurchaseOrderDTO.java
│   ├── CreatePurchaseOrderRequest.java
│   └── GoodsReceiptDTO.java
└── README.md
```

---

## ✅ Summary

✅ **4 Entity classes** - Complete purchase management  
✅ **3 DTO classes** - Request/response objects  
✅ **Purchase order workflow** - DRAFT → PENDING → APPROVED → SENT → ACKNOWLEDGED → PARTIAL → COMPLETED  
✅ **Goods receipt process** - DRAFT → RECEIVED → INSPECTED → POSTED  
✅ **Quality inspection** - Status, remarks, moisture, purity, sample weight  
✅ **Partial receipts** - Support multiple GRNs per PO  
✅ **Financial tracking** - Discount, tax, shipping, other charges  
✅ **Multi-currency** - Support foreign currency with exchange rates  
✅ **Batch tracking** - Assign batch numbers during receipt  
✅ **Quality control** - Accept/reject items during inspection  
✅ **Inventory integration** - Auto-post accepted quantities to inventory  
✅ **Stock movements** - Auto-create stock movements for GRN  
✅ **Supplier ledger integration** - Record purchases in supplier ledger  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade purchase management  

**Everything you need for complete purchase order and goods receipt management in a spice production factory!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.purchase
