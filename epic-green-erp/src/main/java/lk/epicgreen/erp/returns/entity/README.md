# Returns Module - Epic Green ERP

This directory contains **entities and DTOs** for sales returns and credit note management in the Epic Green ERP system.

## 📦 Contents

### Entities (returns/entity) - 3 Files
1. **SalesReturn.java** - Sales return header
2. **SalesReturnItem.java** - Sales return line items
3. **CreditNote.java** - Credit notes issued for returns

### DTOs (returns/dto) - 2 Files
1. **SalesReturnDTO.java** - Sales return data transfer object
2. **CreditNoteDTO.java** - Credit note DTO

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────┐
│  Customer    │
└──────┬───────┘
       │ N:1
       ▼
┌─────────────────┐      ┌─────────────┐
│  SalesReturn    │ ◄N:1─┤   Invoice   │
└────────┬────────┘      └─────────────┘
         │ 1:N
         ▼
┌─────────────────┐
│SalesReturnItem  │
└────────┬────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐
│   CreditNote    │
└─────────────────┘
```

### Returns Process Flow

```
1. RECEIVE RETURN
   ↓ (Customer returns goods)
   DRAFT
   ↓
   
2. APPROVE RETURN
   ↓ (Review return request)
   PENDING_APPROVAL → APPROVED
   ↓
   
3. RECEIVE GOODS
   ↓ (Physically receive returned goods)
   RECEIVED
   ↓
   
4. QUALITY INSPECTION
   ↓ (Inspect returned goods)
   INSPECTED (PASSED/FAILED/PARTIAL)
   - acceptedQuantity (return to stock)
   - rejectedQuantity (write-off)
   ↓
   
5. POST TO INVENTORY
   ↓ (Add accepted goods to inventory)
   - Create stock movement (IN)
   - Update inventory quantities
   ↓
   COMPLETED
   ↓
   
6. GENERATE CREDIT NOTE
   ↓ (Issue credit to customer)
   Create credit note
   ↓
   
7. POST CREDIT NOTE TO LEDGER
   ↓ (Update customer balance)
   - Create CREDIT ledger entry
   - Reduce customer outstanding balance
```

---

## 📋 1. SalesReturn Entity

**Purpose:** Customer returns of goods

### Key Fields

```java
// Identification
- returnNumber (unique, e.g., "RET-2024-001")
- returnDate
- customer (FK)
- invoice (FK optional - original invoice)
- warehouse (FK where goods returned to)

// Return details
- returnType (FULL, PARTIAL)
- returnReason (DAMAGED, DEFECTIVE, WRONG_ITEM, EXPIRED, CUSTOMER_REQUEST, QUALITY_ISSUE, OTHER)
- returnReasonDescription
- customerReference

// Financial
- currency
- exchangeRate (for foreign currency)
- subtotal (sum of line totals)
- discountAmount / discountPercentage
- taxAmount / taxPercentage
- totalAmount (subtotal - discount + tax)

// Status
- status (DRAFT, PENDING_APPROVAL, APPROVED, RECEIVED, INSPECTED, COMPLETED, REJECTED, CANCELLED)

// Quality inspection
- qualityInspectionRequired
- qualityInspectionStatus (PENDING, PASSED, FAILED, PARTIAL)
- qualityInspector
- qualityInspectionDate
- qualityRemarks

// Received details
- receivedBy
- receivedDate

// Approval
- approvedBy
- approvalDate

// Posting to inventory
- isPosted (goods returned to stock)
- postedDate
- postedBy

// Credit note
- creditNoteGenerated

// Notes
- notes (visible)
- internalNotes (internal only)
- totalItems

// Relationships
- items (Set<SalesReturnItem>)
- creditNotes (Set<CreditNote>)
```

### Helper Methods

```java
void calculateTotals(); // Calculate subtotal, discount, tax, total from items
BigDecimal getTotalAcceptedQuantity(); // Sum of accepted quantities
BigDecimal getTotalRejectedQuantity(); // Sum of rejected quantities
boolean isQualityInspectionComplete(); // Inspection done
boolean canPost(); // INSPECTED && !isPosted && inspection complete
boolean canEdit(); // DRAFT only
boolean requiresApproval(); // totalAmount > 0
```

### Status Flow

```
DRAFT (can edit)
  ↓ (submit for approval)
PENDING_APPROVAL
  ↓ (approve)
APPROVED
  ↓ (receive goods)
RECEIVED
  ↓ (quality inspection)
INSPECTED
  ↓ (post to inventory)
COMPLETED
```

---

## 🔧 2. SalesReturnItem Entity

**Purpose:** Line items in sales returns

### Key Fields

```java
// References
- salesReturn (FK header)
- invoiceItem (FK optional)
- product (FK)
- location (FK where goods stored)

// Description
- itemDescription (optional override)

// Batch/Serial
- batchNumber
- serialNumber

// Quantities
- returnQuantity
- acceptedQuantity (passed inspection, return to stock)
- rejectedQuantity (failed inspection, write-off)
- unit

// Pricing (at time of original sale)
- unitPrice
- discountPercentage / discountAmount
- taxPercentage / taxAmount
- lineTotal (quantity * price - discount + tax)

// Return details
- returnReason (DAMAGED, DEFECTIVE, WRONG_ITEM, EXPIRED, QUALITY_ISSUE, OTHER)
- returnAction (RETURN_TO_STOCK, WRITE_OFF, REWORK, SEND_TO_SUPPLIER)

// Quality
- qualityStatus (PENDING, GOOD, ACCEPTABLE, POOR, REJECTED)
- qualityRemarks

- notes
```

### Helper Methods

```java
void calculateLineTotal(); // With discount and tax
BigDecimal getNetAmount(); // Before tax
boolean passedQualityInspection(); // GOOD or ACCEPTABLE
boolean failedQualityInspection(); // POOR or REJECTED
BigDecimal getAcceptancePercentage(); // (accepted / return) * 100
```

### Validation

```java
@PreUpdate:
- Validates: acceptedQuantity + rejectedQuantity <= returnQuantity
```

---

## 💳 3. CreditNote Entity

**Purpose:** Credit notes issued for sales returns

### Key Fields

```java
// Identification
- creditNoteNumber (unique, e.g., "CN-2024-001")
- creditNoteDate
- customer (FK)
- invoice (FK original invoice)
- salesReturn (FK)

// Credit note details
- creditNoteType (SALES_RETURN, PRICE_ADJUSTMENT, DISCOUNT, DAMAGE_CLAIM, OTHER)
- reason

// Financial
- currency
- exchangeRate
- creditAmount (amount credited to customer)
- appliedAmount (applied to outstanding invoices)
- unappliedAmount (creditAmount - appliedAmount, auto-calculated)

// Status
- status (DRAFT, PENDING_APPROVAL, APPROVED, APPLIED, CANCELLED)

// Approval
- approvedBy
- approvalDate

// Posting to ledger
- isPosted (posted to customer ledger)
- postedDate
- postedBy

// Terms and notes
- termsAndConditions
- notes (visible)
- internalNotes (internal only)
```

### Helper Methods

```java
void calculateUnappliedAmount(); // creditAmount - appliedAmount
boolean isFullyApplied(); // appliedAmount >= creditAmount
boolean hasUnappliedAmount(); // unappliedAmount > 0
boolean canPost(); // APPROVED && !isPosted
boolean canEdit(); // DRAFT only
BigDecimal getApplicationPercentage(); // (applied / creditAmount) * 100
```

### Status Flow

```
DRAFT (can edit)
  ↓ (submit for approval)
PENDING_APPROVAL
  ↓ (approve)
APPROVED
  ↓ (post to ledger)
Posted to Customer Ledger
  ↓ (apply to invoices)
APPLIED (if fully applied)
```

---

## 💡 Usage Examples

### Example 1: Create Sales Return

```java
@Transactional
public SalesReturnDTO createSalesReturn(CreateSalesReturnRequest request) {
    // Get customer and invoice
    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    Invoice invoice = null;
    if (request.getInvoiceId() != null) {
        invoice = invoiceRepository.findById(request.getInvoiceId())
            .orElseThrow(() -> new NotFoundException("Invoice not found"));
        
        // Verify invoice belongs to customer
        if (!invoice.getCustomer().getId().equals(customer.getId())) {
            throw new BusinessException("Invoice does not belong to customer");
        }
    }
    
    // Get warehouse
    Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
        .orElseThrow(() -> new NotFoundException("Warehouse not found"));
    
    // Create sales return
    SalesReturn salesReturn = SalesReturn.builder()
        .returnNumber(generateReturnNumber())
        .returnDate(request.getReturnDate())
        .customer(customer)
        .invoice(invoice)
        .warehouse(warehouse)
        .returnType(request.getReturnType())
        .returnReason(request.getReturnReason())
        .returnReasonDescription(request.getReturnReasonDescription())
        .customerReference(request.getCustomerReference())
        .currency(request.getCurrency())
        .exchangeRate(request.getExchangeRate())
        .qualityInspectionRequired(true)
        .notes(request.getNotes())
        .internalNotes(request.getInternalNotes())
        .status("DRAFT")
        .build();
    
    // Add items
    for (var itemReq : request.getItems()) {
        Product product = productRepository.findById(itemReq.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));
        
        SalesReturnItem item = SalesReturnItem.builder()
            .product(product)
            .itemDescription(itemReq.getItemDescription())
            .batchNumber(itemReq.getBatchNumber())
            .serialNumber(itemReq.getSerialNumber())
            .returnQuantity(itemReq.getReturnQuantity())
            .unit(itemReq.getUnit())
            .unitPrice(itemReq.getUnitPrice())
            .discountPercentage(itemReq.getDiscountPercentage())
            .taxPercentage(itemReq.getTaxPercentage())
            .returnReason(itemReq.getReturnReason())
            .notes(itemReq.getNotes())
            .build();
        
        item.calculateLineTotal();
        salesReturn.addItem(item);
    }
    
    // Calculate totals
    salesReturn.setDiscountPercentage(request.getDiscountPercentage());
    salesReturn.setTaxPercentage(request.getTaxPercentage());
    salesReturn.calculateTotals();
    
    salesReturn = salesReturnRepository.save(salesReturn);
    return mapper.toDTO(salesReturn);
}
```

### Example 2: Approve Sales Return

```java
@Transactional
public void approveSalesReturn(Long returnId) {
    SalesReturn salesReturn = salesReturnRepository.findById(returnId)
        .orElseThrow(() -> new NotFoundException("Sales return not found"));
    
    if (!"PENDING_APPROVAL".equals(salesReturn.getStatus())) {
        throw new BusinessException("Return must be pending approval");
    }
    
    salesReturn.setStatus("APPROVED");
    salesReturn.setApprovedBy(getCurrentUsername());
    salesReturn.setApprovalDate(LocalDate.now());
    
    salesReturnRepository.save(salesReturn);
}
```

### Example 3: Receive Returned Goods

```java
@Transactional
public void receiveReturnedGoods(Long returnId, ReceiveGoodsRequest request) {
    SalesReturn salesReturn = salesReturnRepository.findById(returnId)
        .orElseThrow(() -> new NotFoundException("Sales return not found"));
    
    if (!"APPROVED".equals(salesReturn.getStatus())) {
        throw new BusinessException("Return must be approved");
    }
    
    salesReturn.setStatus("RECEIVED");
    salesReturn.setReceivedBy(request.getReceivedBy());
    salesReturn.setReceivedDate(LocalDate.now());
    
    // Set quality inspection status to pending if required
    if (salesReturn.getQualityInspectionRequired()) {
        salesReturn.setQualityInspectionStatus("PENDING");
    }
    
    salesReturnRepository.save(salesReturn);
}
```

### Example 4: Conduct Quality Inspection

```java
@Transactional
public void conductQualityInspection(Long returnId, QualityInspectionRequest request) {
    SalesReturn salesReturn = salesReturnRepository.findById(returnId)
        .orElseThrow(() -> new NotFoundException("Sales return not found"));
    
    if (!"RECEIVED".equals(salesReturn.getStatus())) {
        throw new BusinessException("Goods must be received first");
    }
    
    BigDecimal totalAccepted = BigDecimal.ZERO;
    BigDecimal totalRejected = BigDecimal.ZERO;
    
    // Process each item inspection
    for (var inspectionItem : request.getItems()) {
        SalesReturnItem item = salesReturn.getItems().stream()
            .filter(i -> i.getId().equals(inspectionItem.getItemId()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Return item not found"));
        
        // Set inspection results
        item.setAcceptedQuantity(inspectionItem.getAcceptedQuantity());
        item.setRejectedQuantity(inspectionItem.getRejectedQuantity());
        item.setQualityStatus(inspectionItem.getQualityStatus());
        item.setQualityRemarks(inspectionItem.getQualityRemarks());
        
        // Determine return action based on quality
        if (item.passedQualityInspection()) {
            item.setReturnAction("RETURN_TO_STOCK");
        } else {
            item.setReturnAction("WRITE_OFF");
        }
        
        totalAccepted = totalAccepted.add(item.getAcceptedQuantity());
        totalRejected = totalRejected.add(item.getRejectedQuantity());
    }
    
    // Update return status
    salesReturn.setStatus("INSPECTED");
    salesReturn.setQualityInspector(request.getInspector());
    salesReturn.setQualityInspectionDate(LocalDate.now());
    salesReturn.setQualityRemarks(request.getRemarks());
    
    // Determine overall quality status
    if (totalRejected.compareTo(BigDecimal.ZERO) == 0) {
        salesReturn.setQualityInspectionStatus("PASSED");
    } else if (totalAccepted.compareTo(BigDecimal.ZERO) == 0) {
        salesReturn.setQualityInspectionStatus("FAILED");
    } else {
        salesReturn.setQualityInspectionStatus("PARTIAL");
    }
    
    salesReturnRepository.save(salesReturn);
}
```

### Example 5: Post Return to Inventory

```java
@Transactional
public void postReturnToInventory(Long returnId) {
    SalesReturn salesReturn = salesReturnRepository.findById(returnId)
        .orElseThrow(() -> new NotFoundException("Sales return not found"));
    
    if (!salesReturn.canPost()) {
        throw new BusinessException("Return cannot be posted. Must be INSPECTED, not yet posted, and inspection complete");
    }
    
    for (SalesReturnItem item : salesReturn.getItems()) {
        if (item.getAcceptedQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            continue; // Skip items with no accepted quantity
        }
        
        // Only return to stock if action is RETURN_TO_STOCK
        if (!"RETURN_TO_STOCK".equals(item.getReturnAction())) {
            continue;
        }
        
        // Find or create inventory
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouseAndBatch(
                item.getProduct().getId(),
                salesReturn.getWarehouse().getId(),
                item.getBatchNumber()
            )
            .orElse(Inventory.builder()
                .product(item.getProduct())
                .warehouse(salesReturn.getWarehouse())
                .location(item.getLocation())
                .batchNumber(item.getBatchNumber())
                .quantityOnHand(BigDecimal.ZERO)
                .availableQuantity(BigDecimal.ZERO)
                .build());
        
        // Add accepted quantity to inventory
        inventory.setQuantityOnHand(
            inventory.getQuantityOnHand().add(item.getAcceptedQuantity())
        );
        inventory.setAvailableQuantity(
            inventory.getAvailableQuantity().add(item.getAcceptedQuantity())
        );
        inventoryRepository.save(inventory);
        
        // Create stock movement (IN)
        StockMovement movement = StockMovement.builder()
            .movementDate(LocalDate.now())
            .movementType("IN")
            .product(item.getProduct())
            .toWarehouse(salesReturn.getWarehouse())
            .toLocation(item.getLocation())
            .batchNumber(item.getBatchNumber())
            .quantity(item.getAcceptedQuantity())
            .referenceNumber(salesReturn.getReturnNumber())
            .referenceType("SALES_RETURN")
            .status("COMPLETED")
            .build();
        
        stockMovementRepository.save(movement);
    }
    
    // Mark as posted
    salesReturn.setIsPosted(true);
    salesReturn.setPostedDate(LocalDate.now());
    salesReturn.setPostedBy(getCurrentUsername());
    salesReturn.setStatus("COMPLETED");
    salesReturnRepository.save(salesReturn);
}
```

### Example 6: Generate Credit Note

```java
@Transactional
public CreditNoteDTO generateCreditNote(Long returnId) {
    SalesReturn salesReturn = salesReturnRepository.findById(returnId)
        .orElseThrow(() -> new NotFoundException("Sales return not found"));
    
    if (!salesReturn.getIsPosted()) {
        throw new BusinessException("Return must be posted to inventory first");
    }
    
    if (salesReturn.getCreditNoteGenerated()) {
        throw new BusinessException("Credit note already generated");
    }
    
    // Calculate credit amount based on accepted quantities
    BigDecimal creditAmount = BigDecimal.ZERO;
    for (SalesReturnItem item : salesReturn.getItems()) {
        if (item.getAcceptedQuantity().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal itemCredit = item.getAcceptedQuantity()
                .multiply(item.getUnitPrice())
                .subtract(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO)
                .add(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO);
            
            creditAmount = creditAmount.add(itemCredit);
        }
    }
    
    // Create credit note
    CreditNote creditNote = CreditNote.builder()
        .creditNoteNumber(generateCreditNoteNumber())
        .creditNoteDate(LocalDate.now())
        .customer(salesReturn.getCustomer())
        .invoice(salesReturn.getInvoice())
        .salesReturn(salesReturn)
        .creditNoteType("SALES_RETURN")
        .reason("Sales Return - " + salesReturn.getReturnReason())
        .currency(salesReturn.getCurrency())
        .exchangeRate(salesReturn.getExchangeRate())
        .creditAmount(creditAmount)
        .status("DRAFT")
        .build();
    
    creditNote = creditNoteRepository.save(creditNote);
    
    // Update return
    salesReturn.setCreditNoteGenerated(true);
    salesReturnRepository.save(salesReturn);
    
    return mapper.toDTO(creditNote);
}
```

### Example 7: Post Credit Note to Customer Ledger

```java
@Transactional
public void postCreditNoteToLedger(Long creditNoteId) {
    CreditNote creditNote = creditNoteRepository.findById(creditNoteId)
        .orElseThrow(() -> new NotFoundException("Credit note not found"));
    
    if (!creditNote.canPost()) {
        throw new BusinessException("Credit note must be approved and not yet posted");
    }
    
    // Create ledger entry
    CustomerLedger ledger = CustomerLedger.builder()
        .customer(creditNote.getCustomer())
        .transactionDate(creditNote.getCreditNoteDate())
        .transactionType("RETURN")
        .referenceNumber(creditNote.getCreditNoteNumber())
        .referenceId(creditNote.getId())
        .referenceType("CREDIT_NOTE")
        .debitAmount(BigDecimal.ZERO)
        .creditAmount(creditNote.getCreditAmount()) // Decreases receivable
        .description("Credit Note - " + creditNote.getCreditNoteNumber())
        .status("CLEARED")
        .build();
    
    // Calculate running balance
    BigDecimal previousBalance = customerLedgerService.getCustomerBalance(creditNote.getCustomer().getId());
    ledger.setBalance(previousBalance.subtract(creditNote.getCreditAmount()));
    
    customerLedgerRepository.save(ledger);
    
    // Update customer outstanding balance
    Customer customer = creditNote.getCustomer();
    customer.setOutstandingBalance(ledger.getBalance());
    customerRepository.save(customer);
    
    // Mark credit note as posted
    creditNote.setIsPosted(true);
    creditNote.setPostedDate(LocalDate.now());
    creditNote.setPostedBy(getCurrentUsername());
    creditNote.setStatus("APPLIED"); // If auto-applied
    creditNoteRepository.save(creditNote);
    
    // Auto-apply to original invoice if exists
    if (creditNote.getInvoice() != null) {
        Invoice invoice = creditNote.getInvoice();
        BigDecimal applyAmount = creditNote.getCreditAmount();
        
        if (applyAmount.compareTo(invoice.getBalanceAmount()) > 0) {
            applyAmount = invoice.getBalanceAmount();
        }
        
        invoice.setPaidAmount(invoice.getPaidAmount().add(applyAmount));
        invoice.calculateBalance();
        invoiceRepository.save(invoice);
        
        creditNote.setAppliedAmount(applyAmount);
        creditNote.calculateUnappliedAmount();
        creditNoteRepository.save(creditNote);
    }
}
```

---

## 📁 Directory Structure

```
returns/
├── entity/
│   ├── SalesReturn.java
│   ├── SalesReturnItem.java
│   ├── CreditNote.java
│   └── README.md
└── dto/
    ├── SalesReturnDTO.java
    └── CreditNoteDTO.java
```

---

## ✅ Summary

✅ **3 Entity classes** - Complete returns and credit note management  
✅ **2 DTO classes** - Request/response objects  
✅ **Sales return workflow** - DRAFT → PENDING_APPROVAL → APPROVED → RECEIVED → INSPECTED → COMPLETED  
✅ **Return types** - FULL, PARTIAL  
✅ **Return reasons** - DAMAGED, DEFECTIVE, WRONG_ITEM, EXPIRED, CUSTOMER_REQUEST, QUALITY_ISSUE, OTHER  
✅ **Quality inspection** - Separate accepted and rejected quantities  
✅ **Quality status** - PENDING, GOOD, ACCEPTABLE, POOR, REJECTED  
✅ **Return actions** - RETURN_TO_STOCK, WRITE_OFF, REWORK, SEND_TO_SUPPLIER  
✅ **Inventory posting** - Add accepted goods back to inventory  
✅ **Stock movements** - Complete audit trail  
✅ **Credit note generation** - Based on accepted quantities  
✅ **Credit note types** - SALES_RETURN, PRICE_ADJUSTMENT, DISCOUNT, DAMAGE_CLAIM, OTHER  
✅ **Credit application** - Apply to outstanding invoices  
✅ **Ledger integration** - Auto-post to customer ledger  
✅ **Balance updates** - Reduce customer outstanding balance  
✅ **Multi-currency** - Foreign currency support  
✅ **Approval workflow** - Returns require approval  
✅ **Batch tracking** - Track batch numbers  
✅ **Location tracking** - Track warehouse location  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade returns management  

**Everything you need for complete sales returns processing, quality inspection, inventory management, credit note generation, and customer ledger integration in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.returns
