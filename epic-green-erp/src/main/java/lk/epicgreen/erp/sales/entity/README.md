# Sales Module - Epic Green ERP

This directory contains **entities and DTOs** for sales order processing and invoicing in the Epic Green ERP system.

## 📦 Contents

### Entities (sales/entity) - 6 Files
1. **SalesOrder.java** - Sales order header
2. **SalesOrderItem.java** - Sales order line items
3. **DispatchNote.java** - Goods dispatch/delivery notes
4. **DispatchItem.java** - Dispatch note items
5. **Invoice.java** - Sales invoices
6. **InvoiceItem.java** - Invoice line items

### DTOs (sales/dto) - 4 Files
1. **SalesOrderDTO.java** - Sales order data transfer object
2. **CreateSalesOrderRequest.java** - Create sales order request
3. **DispatchDTO.java** - Dispatch note data transfer object
4. **InvoiceDTO.java** - Invoice data transfer object

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────┐
│  Customer    │
└──────┬───────┘
       │ N:1
       ▼
┌─────────────────┐
│  SalesOrder     │ (Order from customer)
└────────┬────────┘
         │ 1:N
         ▼
┌─────────────────┐      ┌─────────┐
│SalesOrderItem   │ ◄N:1─┤ Product │
└────────┬────────┘      └─────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐
│  DispatchNote   │ (Goods delivery)
└────────┬────────┘
         │ 1:N
         ▼
┌─────────────────┐
│  DispatchItem   │
└────────┬────────┘
         │
         │ N:1
         ▼
┌─────────────────┐
│    Invoice      │ (Billing document)
└────────┬────────┘
         │ 1:N
         ▼
┌─────────────────┐
│  InvoiceItem    │
└─────────────────┘
```

### Sales Process Flow

```
1. SALES ORDER
   ↓ (Create from customer order)
   DRAFT → PENDING_APPROVAL → APPROVED → CONFIRMED
   ↓
   
2. DISPATCH NOTE
   ↓ (Pick & pack goods, create dispatch)
   DRAFT → PREPARED → DISPATCHED → IN_TRANSIT → DELIVERED
   ↓ (Post to inventory - deduct stock)
   
3. INVOICE
   ↓ (Generate invoice)
   DRAFT → PENDING → APPROVED → SENT
   ↓ (Post to customer ledger)
   
4. PAYMENT
   ↓ (Receive payment)
   UNPAID → PARTIAL → PAID
```

---

## 📋 1. SalesOrder Entity

**Purpose:** Customer orders for products

### Key Fields

```java
// Identification
- orderNumber (unique, e.g., "SO-2024-001")
- orderDate
- customer (FK)
- warehouse (FK for dispatch)

// Order details
- orderType (STANDARD, EXPORT, SAMPLE, RETURN)
- customerReference (customer's PO number)
- expectedDeliveryDate
- deliveryAddressId
- deliveryInstructions

// Financial
- currency
- exchangeRate (for foreign currency)
- subtotal (sum of line totals)
- discountAmount / discountPercentage
- taxAmount / taxPercentage
- shippingCharges
- otherCharges
- totalAmount (subtotal - discount + tax + shipping + other)

// Status
- status (DRAFT, PENDING_APPROVAL, APPROVED, CONFIRMED, PARTIAL, DISPATCHED, INVOICED, COMPLETED, CANCELLED)
- paymentTerms
- paymentMethod

// Sales management
- salesRepresentative
- priority (LOW, MEDIUM, HIGH, URGENT)
- approvedBy
- approvalDate

// Notes
- notes (visible to customer)
- internalNotes (internal only)
- totalItems

// Relationships
- items (Set<SalesOrderItem>)
- dispatchNotes (Set<DispatchNote>)
- invoices (Set<Invoice>)
```

### Helper Methods

```java
void calculateTotals(); // Calculate subtotal, discount, tax, total from items
BigDecimal getTotalDispatchedQuantity();
BigDecimal getTotalPendingQuantity();
boolean isFullyDispatched();
boolean isPendingApproval();
boolean isApproved();
boolean canEdit(); // DRAFT only
boolean canDispatch(); // CONFIRMED or PARTIAL
```

### Status Flow

```
DRAFT (can edit)
  ↓ (submit for approval)
PENDING_APPROVAL
  ↓ (approve)
APPROVED
  ↓ (confirm with customer)
CONFIRMED (ready for dispatch)
  ↓ (create dispatch note)
PARTIAL (partially dispatched)
  ↓ (all items dispatched)
DISPATCHED
  ↓ (invoice created)
INVOICED
  ↓ (payment received)
COMPLETED
```

---

## 🔧 2. SalesOrderItem Entity

**Purpose:** Line items in sales orders

### Key Fields

```java
// References
- salesOrder (FK header)
- product (FK)

// Description
- itemDescription (optional override)

// Quantities
- orderedQuantity
- dispatchedQuantity (from dispatch notes)
- invoicedQuantity (from invoices)
- pendingQuantity (ordered - dispatched, auto-calculated)
- unit

// Pricing
- unitPrice
- discountPercentage / discountAmount
- taxPercentage / taxAmount
- lineTotal (quantity * price - discount + tax)

- notes
```

### Helper Methods

```java
void calculateLineTotal(); // With discount and tax
void calculatePendingQuantity(); // ordered - dispatched
BigDecimal getNetAmount(); // Before tax
boolean isFullyDispatched();
boolean isPartiallyDispatched();
BigDecimal getDispatchPercentage();
```

---

## 🚚 3. DispatchNote Entity

**Purpose:** Goods dispatch/delivery notes

### Key Fields

```java
// Identification
- dispatchNumber (unique, e.g., "DN-2024-001")
- dispatchDate
- dispatchTimestamp

// References
- salesOrder (FK optional)
- customer (FK)
- warehouse (FK from where dispatched)

// Delivery
- deliveryAddressId
- deliveryAddressText (copy at time of dispatch)

// Transport
- vehicleNumber
- driverName
- driverContact
- transporterName
- trackingNumber

// Personnel
- dispatchedBy (person who prepared)

// Status
- status (DRAFT, PREPARED, DISPATCHED, IN_TRANSIT, DELIVERED, CANCELLED)
- deliveryStatus (PENDING, DELIVERED, PARTIALLY_DELIVERED, FAILED, RETURNED)

// Delivery confirmation
- deliveredDate
- receivedBy (person who received)
- receivedDate

// Posting to inventory
- isPosted (inventory deducted)
- postedDate
- postedBy

// Instructions and notes
- deliveryInstructions
- notes
- totalItems

// Relationships
- items (Set<DispatchItem>)
```

### Helper Methods

```java
BigDecimal getTotalDispatchedQuantity();
boolean canPost(); // DISPATCHED && !isPosted
boolean canEdit(); // DRAFT or PREPARED
boolean isDelivered();
```

### Status Flow

```
DRAFT (can edit)
  ↓ (prepare goods)
PREPARED (goods picked & packed)
  ↓ (dispatch goods)
DISPATCHED (goods sent, post to inventory)
  ↓ (goods in transit)
IN_TRANSIT
  ↓ (goods reach customer)
DELIVERED
```

---

## 📦 4. DispatchItem Entity

**Purpose:** Line items in dispatch notes

### Key Fields

```java
// References
- dispatchNote (FK header)
- orderItem (FK optional, links to sales order item)
- product (FK)
- location (FK warehouse location from where picked)

// Batch/Serial
- batchNumber
- serialNumber

// Quantities
- orderedQuantity (from sales order)
- dispatchedQuantity (actual dispatched)
- unit

// Pricing (for reference)
- unitPrice
- totalValue (dispatched quantity * unit price)

- notes
```

### Helper Methods

```java
void calculateTotalValue();
BigDecimal getVarianceQuantity(); // dispatched - ordered
boolean isFullyDispatched(); // compared to order
```

---

## 🧾 5. Invoice Entity

**Purpose:** Sales invoices (billing documents)

### Key Fields

```java
// Identification
- invoiceNumber (unique, e.g., "INV-2024-001")
- invoiceDate

// References
- salesOrder (FK optional)
- dispatchNote (FK optional)
- customer (FK)

// Invoice details
- invoiceType (SALES, PROFORMA, CREDIT_NOTE, DEBIT_NOTE)
- customerReference

// Billing address
- billingAddressId
- billingAddressText (copy at time of invoice)

// Financial
- currency
- exchangeRate
- subtotal
- discountAmount / discountPercentage
- taxAmount / taxPercentage
- shippingCharges
- otherCharges
- totalAmount
- paidAmount
- balanceAmount (total - paid)

// Payment
- paymentTerms
- dueDate

// Status
- status (DRAFT, PENDING, APPROVED, SENT, PAID, PARTIAL, OVERDUE, CANCELLED)
- paymentStatus (UNPAID, PARTIAL, PAID, OVERPAID)

// Posting to ledger
- isPosted (posted to customer ledger)
- postedDate

// Terms and notes
- termsAndConditions
- notes (visible to customer)
- internalNotes
- totalItems

// Relationships
- items (Set<InvoiceItem>)
```

### Helper Methods

```java
void calculateTotals(); // Calculate from items
void calculateBalance(); // total - paid
boolean isOverdue(); // past due date && not paid
Long getDaysOverdue();
boolean isFullyPaid();
boolean canEdit(); // DRAFT only
```

### Payment Status Auto-Update

```java
@PreUpdate:
- paidAmount = 0 → UNPAID
- paidAmount = totalAmount → PAID
- paidAmount < totalAmount → PARTIAL
- paidAmount > totalAmount → OVERPAID
```

---

## 📝 6. InvoiceItem Entity

**Purpose:** Line items in invoices

### Key Fields

```java
// References
- invoice (FK header)
- orderItem (FK optional)
- dispatchItem (FK optional)
- product (FK)

// Description
- itemDescription (optional override)

// Quantity
- quantity
- unit

// Pricing
- unitPrice
- discountPercentage / discountAmount
- taxPercentage / taxAmount
- lineTotal (quantity * price - discount + tax)

- notes
```

### Helper Methods

```java
void calculateLineTotal(); // With discount and tax
BigDecimal getNetAmount(); // Before tax
```

---

## 💡 Usage Examples

### Example 1: Create Sales Order

```java
@Transactional
public SalesOrderDTO createSalesOrder(CreateSalesOrderRequest request) {
    // Get customer
    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Check credit limit
    if (!customer.canPurchase(estimateTotalAmount(request))) {
        throw new BusinessException("Customer credit limit exceeded");
    }
    
    // Create sales order
    SalesOrder order = SalesOrder.builder()
        .orderNumber(generateOrderNumber())
        .orderDate(request.getOrderDate())
        .customer(customer)
        .orderType(request.getOrderType())
        .customerReference(request.getCustomerReference())
        .expectedDeliveryDate(request.getExpectedDeliveryDate())
        .currency(request.getCurrency())
        .exchangeRate(request.getExchangeRate())
        .paymentTerms(request.getPaymentTerms())
        .salesRepresentative(request.getSalesRepresentative())
        .priority(request.getPriority())
        .status("DRAFT")
        .build();
    
    // Add items
    for (var itemReq : request.getItems()) {
        Product product = productRepository.findById(itemReq.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));
        
        // Get customer price
        BigDecimal unitPrice = getCustomerPrice(customer.getId(), product.getId());
        
        SalesOrderItem item = SalesOrderItem.builder()
            .product(product)
            .itemDescription(itemReq.getItemDescription())
            .orderedQuantity(itemReq.getOrderedQuantity())
            .unit(itemReq.getUnit())
            .unitPrice(unitPrice)
            .discountPercentage(itemReq.getDiscountPercentage())
            .taxPercentage(itemReq.getTaxPercentage())
            .build();
        
        item.calculateLineTotal();
        order.addItem(item);
    }
    
    // Calculate totals
    order.setDiscountPercentage(request.getDiscountPercentage());
    order.setTaxPercentage(request.getTaxPercentage());
    order.setShippingCharges(request.getShippingCharges());
    order.calculateTotals();
    
    order = salesOrderRepository.save(order);
    return mapper.toDTO(order);
}
```

### Example 2: Approve Sales Order

```java
@Transactional
public void approveSalesOrder(Long orderId) {
    SalesOrder order = salesOrderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Sales order not found"));
    
    if (!"PENDING_APPROVAL".equals(order.getStatus())) {
        throw new BusinessException("Order must be pending approval");
    }
    
    // Check stock availability
    for (SalesOrderItem item : order.getItems()) {
        BigDecimal available = inventoryRepository
            .getTotalAvailableQuantity(item.getProduct().getId());
        
        if (available.compareTo(item.getOrderedQuantity()) < 0) {
            throw new BusinessException(
                "Insufficient stock for " + item.getProduct().getProductName() +
                ". Available: " + available + ", Required: " + item.getOrderedQuantity()
            );
        }
    }
    
    order.setStatus("APPROVED");
    order.setApprovedBy(getCurrentUsername());
    order.setApprovalDate(LocalDate.now());
    
    salesOrderRepository.save(order);
}
```

### Example 3: Create Dispatch Note

```java
@Transactional
public DispatchDTO createDispatch(Long orderId, CreateDispatchRequest request) {
    SalesOrder order = salesOrderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Sales order not found"));
    
    if (!order.canDispatch()) {
        throw new BusinessException("Order status must be CONFIRMED or PARTIAL");
    }
    
    // Create dispatch note
    DispatchNote dispatch = DispatchNote.builder()
        .dispatchNumber(generateDispatchNumber())
        .dispatchDate(LocalDate.now())
        .salesOrder(order)
        .customer(order.getCustomer())
        .warehouse(order.getWarehouse())
        .deliveryAddressId(order.getDeliveryAddressId())
        .vehicleNumber(request.getVehicleNumber())
        .driverName(request.getDriverName())
        .driverContact(request.getDriverContact())
        .dispatchedBy(getCurrentUsername())
        .status("DRAFT")
        .build();
    
    // Add items from sales order
    for (SalesOrderItem orderItem : order.getItems()) {
        if (orderItem.getPendingQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            continue; // Skip fully dispatched items
        }
        
        // Find inventory with batch
        Inventory inventory = inventoryRepository
            .findAvailableInventory(orderItem.getProduct().getId(), order.getWarehouse().getId())
            .orElseThrow(() -> new BusinessException("No inventory available"));
        
        BigDecimal toDispatch = orderItem.getPendingQuantity();
        if (inventory.getAvailableQuantity().compareTo(toDispatch) < 0) {
            toDispatch = inventory.getAvailableQuantity(); // Partial dispatch
        }
        
        DispatchItem dispatchItem = DispatchItem.builder()
            .orderItem(orderItem)
            .product(orderItem.getProduct())
            .location(inventory.getLocation())
            .batchNumber(inventory.getBatchNumber())
            .orderedQuantity(orderItem.getOrderedQuantity())
            .dispatchedQuantity(toDispatch)
            .unit(orderItem.getUnit())
            .unitPrice(orderItem.getUnitPrice())
            .build();
        
        dispatchItem.calculateTotalValue();
        dispatch.addItem(dispatchItem);
    }
    
    dispatch = dispatchNoteRepository.save(dispatch);
    return mapper.toDTO(dispatch);
}
```

### Example 4: Post Dispatch to Inventory

```java
@Transactional
public void postDispatchToInventory(Long dispatchId) {
    DispatchNote dispatch = dispatchNoteRepository.findById(dispatchId)
        .orElseThrow(() -> new NotFoundException("Dispatch note not found"));
    
    if (!dispatch.canPost()) {
        throw new BusinessException("Dispatch must be DISPATCHED and not yet posted");
    }
    
    for (DispatchItem item : dispatch.getItems()) {
        // Find inventory
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouseAndBatch(
                item.getProduct().getId(),
                dispatch.getWarehouse().getId(),
                item.getBatchNumber()
            )
            .orElseThrow(() -> new NotFoundException("Inventory not found"));
        
        // Check availability
        if (inventory.getAvailableQuantity().compareTo(item.getDispatchedQuantity()) < 0) {
            throw new BusinessException("Insufficient inventory for " + item.getProduct().getProductName());
        }
        
        // Deduct from inventory
        inventory.setAvailableQuantity(
            inventory.getAvailableQuantity().subtract(item.getDispatchedQuantity())
        );
        inventoryRepository.save(inventory);
        
        // Create stock movement
        StockMovement movement = StockMovement.builder()
            .movementDate(dispatch.getDispatchDate())
            .movementType("OUT")
            .product(item.getProduct())
            .fromWarehouse(dispatch.getWarehouse())
            .fromLocation(item.getLocation())
            .batchNumber(item.getBatchNumber())
            .quantity(item.getDispatchedQuantity())
            .referenceNumber(dispatch.getDispatchNumber())
            .referenceType("DISPATCH_NOTE")
            .status("COMPLETED")
            .build();
        
        stockMovementRepository.save(movement);
        
        // Update sales order item dispatched quantity
        if (item.getOrderItem() != null) {
            SalesOrderItem orderItem = item.getOrderItem();
            orderItem.setDispatchedQuantity(
                orderItem.getDispatchedQuantity().add(item.getDispatchedQuantity())
            );
            orderItem.calculatePendingQuantity();
        }
    }
    
    // Mark as posted
    dispatch.setIsPosted(true);
    dispatch.setPostedDate(LocalDate.now());
    dispatch.setPostedBy(getCurrentUsername());
    dispatchNoteRepository.save(dispatch);
    
    // Update sales order status
    SalesOrder order = dispatch.getSalesOrder();
    if (order != null) {
        if (order.isFullyDispatched()) {
            order.setStatus("DISPATCHED");
        } else {
            order.setStatus("PARTIAL");
        }
        salesOrderRepository.save(order);
    }
}
```

### Example 5: Create Invoice from Dispatch

```java
@Transactional
public InvoiceDTO createInvoiceFromDispatch(Long dispatchId) {
    DispatchNote dispatch = dispatchNoteRepository.findById(dispatchId)
        .orElseThrow(() -> new NotFoundException("Dispatch note not found"));
    
    if (!dispatch.getIsPosted()) {
        throw new BusinessException("Dispatch must be posted to inventory first");
    }
    
    // Create invoice
    Invoice invoice = Invoice.builder()
        .invoiceNumber(generateInvoiceNumber())
        .invoiceDate(LocalDate.now())
        .salesOrder(dispatch.getSalesOrder())
        .dispatchNote(dispatch)
        .customer(dispatch.getCustomer())
        .invoiceType("SALES")
        .currency("LKR")
        .status("DRAFT")
        .build();
    
    // Calculate due date
    if (dispatch.getCustomer().getCreditDays() != null) {
        invoice.setDueDate(
            LocalDate.now().plusDays(dispatch.getCustomer().getCreditDays())
        );
    }
    
    // Add items from dispatch
    for (DispatchItem dispatchItem : dispatch.getItems()) {
        InvoiceItem invoiceItem = InvoiceItem.builder()
            .orderItem(dispatchItem.getOrderItem())
            .dispatchItem(dispatchItem)
            .product(dispatchItem.getProduct())
            .itemDescription(dispatchItem.getItemDescription())
            .quantity(dispatchItem.getDispatchedQuantity())
            .unit(dispatchItem.getUnit())
            .unitPrice(dispatchItem.getUnitPrice())
            .build();
        
        // Apply customer discount
        if (dispatch.getCustomer().getTradeDiscount() != null) {
            invoiceItem.setDiscountPercentage(dispatch.getCustomer().getTradeDiscount());
        }
        
        // Apply tax (e.g., 15% VAT)
        invoiceItem.setTaxPercentage(new BigDecimal("15"));
        
        invoiceItem.calculateLineTotal();
        invoice.addItem(invoiceItem);
    }
    
    // Calculate totals
    invoice.calculateTotals();
    
    invoice = invoiceRepository.save(invoice);
    return mapper.toDTO(invoice);
}
```

### Example 6: Post Invoice to Customer Ledger

```java
@Transactional
public void postInvoiceToLedger(Long invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new NotFoundException("Invoice not found"));
    
    if (invoice.getIsPosted()) {
        throw new BusinessException("Invoice already posted");
    }
    
    // Create ledger entry
    CustomerLedger ledger = CustomerLedger.builder()
        .customer(invoice.getCustomer())
        .transactionDate(invoice.getInvoiceDate())
        .transactionType("SALE")
        .referenceNumber(invoice.getInvoiceNumber())
        .referenceId(invoice.getId())
        .referenceType("SALES_INVOICE")
        .debitAmount(invoice.getTotalAmount()) // Increases receivable
        .creditAmount(BigDecimal.ZERO)
        .dueDate(invoice.getDueDate())
        .description("Sales Invoice - " + invoice.getInvoiceNumber())
        .status("PENDING")
        .build();
    
    // Calculate running balance
    BigDecimal previousBalance = customerLedgerService.getCustomerBalance(invoice.getCustomer().getId());
    ledger.setBalance(previousBalance.add(invoice.getTotalAmount()));
    
    customerLedgerRepository.save(ledger);
    
    // Update customer outstanding balance
    Customer customer = invoice.getCustomer();
    customer.setOutstandingBalance(ledger.getBalance());
    customerRepository.save(customer);
    
    // Mark invoice as posted
    invoice.setIsPosted(true);
    invoice.setPostedDate(LocalDate.now());
    invoice.setStatus("SENT");
    invoiceRepository.save(invoice);
    
    // Update sales order status
    if (invoice.getSalesOrder() != null) {
        SalesOrder order = invoice.getSalesOrder();
        order.setStatus("INVOICED");
        salesOrderRepository.save(order);
    }
}
```

---

## 📁 Directory Structure

```
sales/
├── entity/
│   ├── SalesOrder.java
│   ├── SalesOrderItem.java
│   ├── DispatchNote.java
│   ├── DispatchItem.java
│   ├── Invoice.java
│   ├── InvoiceItem.java
│   └── README.md
└── dto/
    ├── SalesOrderDTO.java
    ├── CreateSalesOrderRequest.java
    ├── DispatchDTO.java
    └── InvoiceDTO.java
```

---

## ✅ Summary

✅ **6 Entity classes** - Complete sales order-to-cash cycle  
✅ **4 DTO classes** - Request/response objects  
✅ **Sales order workflow** - DRAFT → PENDING_APPROVAL → APPROVED → CONFIRMED → PARTIAL → DISPATCHED → INVOICED → COMPLETED  
✅ **Order types** - STANDARD, EXPORT, SAMPLE, RETURN  
✅ **Multi-currency** - Foreign currency with exchange rates  
✅ **Flexible pricing** - Line and header level discounts and taxes  
✅ **Dispatch management** - Track goods delivery with vehicle, driver, transporter  
✅ **Batch tracking** - Track which batches dispatched  
✅ **Partial dispatch** - Support multiple dispatches per order  
✅ **Inventory integration** - Auto-deduct stock on dispatch posting  
✅ **Stock movements** - Complete audit trail  
✅ **Invoice generation** - From dispatch or order  
✅ **Payment tracking** - Paid amount, balance, payment status  
✅ **Overdue detection** - Auto-detect overdue invoices  
✅ **Ledger integration** - Auto-post to customer ledger  
✅ **Approval workflow** - Orders require approval  
✅ **Credit limit checks** - Prevent over-limit sales  
✅ **Delivery tracking** - Track delivery status, received by, date  
✅ **Priority levels** - LOW, MEDIUM, HIGH, URGENT  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade sales management  

**Everything you need for complete sales order processing, dispatch management, and invoicing in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.sales
