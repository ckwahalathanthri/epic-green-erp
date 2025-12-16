# Accounting Module - Epic Green ERP

This directory contains **entities and DTOs** for financial accounting and general ledger management in the Epic Green ERP system.

## 📦 Contents

### Entities (accounting/entity) - 5 Files
1. **ChartOfAccounts.java** - Chart of accounts structure
2. **FinancialPeriod.java** - Accounting periods
3. **JournalEntry.java** - Journal entry headers
4. **JournalEntryLine.java** - Journal entry line items
5. **GeneralLedger.java** - General ledger entries

### DTOs (accounting/dto) - 3 Files
1. **AccountDTO.java** - Chart of accounts DTO
2. **JournalEntryDTO.java** - Journal entry DTO
3. **GeneralLedgerDTO.java** - General ledger DTO

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────────────┐
│  ChartOfAccounts     │ (Account structure)
└──────────┬───────────┘
           │ N:1 (parent)
           ▼
┌──────────────────────┐
│  FinancialPeriod     │ (Accounting periods)
└──────────┬───────────┘
           │ N:1
           ▼
┌──────────────────────┐
│   JournalEntry       │ (Journal headers)
└──────────┬───────────┘
           │ 1:N
           ▼
┌──────────────────────┐      ┌──────────────────────┐
│ JournalEntryLine     │ ─N:1→│  ChartOfAccounts     │
└──────────┬───────────┘      └──────────────────────┘
           │
           │ 1:1
           ▼
┌──────────────────────┐
│   GeneralLedger      │ (Posted entries with running balance)
└──────────────────────┘
```

### Accounting Process Flow

```
1. SETUP CHART OF ACCOUNTS
   ↓ (Create account structure)
   Define account types, categories, hierarchy
   ↓
   
2. CREATE FINANCIAL PERIODS
   ↓ (Define accounting periods)
   Monthly, Quarterly, Yearly periods
   ↓
   
3. CREATE JOURNAL ENTRIES
   ↓ (Record transactions)
   DRAFT → PENDING_APPROVAL → APPROVED
   - Must be balanced (debit = credit)
   ↓
   
4. POST TO GENERAL LEDGER
   ↓ (Post approved entries)
   - Create general ledger entries
   - Update account balances
   - Calculate running balance
   ↓
   POSTED
   ↓
   
5. CLOSE PERIOD
   ↓ (Period end closing)
   OPEN → CLOSED → LOCKED
```

---

## 📋 1. ChartOfAccounts Entity

**Purpose:** Chart of accounts structure

### Key Fields

```java
// Identification
- accountCode (unique, e.g., "1000", "1100", "1110")
- accountName (e.g., "Assets", "Current Assets", "Cash")

// Classification
- accountType (ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE)
- accountCategory (CURRENT_ASSET, FIXED_ASSET, CURRENT_LIABILITY, LONG_TERM_LIABILITY, etc.)
- accountSubcategory

// Hierarchy
- parentAccount (FK for hierarchical structure)
- childAccounts (Set<ChartOfAccounts>)
- accountLevel (1 = top level, 2 = sub-account, etc.)

// Properties
- isHeader (has child accounts, cannot post transactions)
- isActive
- isSystem (cannot be deleted)
- allowManualEntries (can accept manual journal entries)

// Currency
- currency

// Balances
- openingBalanceDebit
- openingBalanceCredit
- currentBalanceDebit
- currentBalanceCredit

// Description
- description
- notes
```

### Helper Methods

```java
String getFullPath(); // Get account hierarchy path
BigDecimal getBalance(); // Get net balance (debit - credit for ASSET/EXPENSE)
BigDecimal getOpeningBalance(); // Get opening net balance
String getNormalBalanceSide(); // DEBIT for ASSET/EXPENSE, CREDIT for others
boolean isDebitAccount(); // ASSET or EXPENSE
boolean isCreditAccount(); // LIABILITY, EQUITY, or REVENUE
boolean canAcceptEntries(); // !isHeader && allowManualEntries
```

### Account Types and Normal Balance

| Account Type | Normal Balance | Increases With | Decreases With |
|--------------|----------------|----------------|----------------|
| ASSET        | DEBIT          | DEBIT          | CREDIT         |
| EXPENSE      | DEBIT          | DEBIT          | CREDIT         |
| LIABILITY    | CREDIT         | CREDIT         | DEBIT          |
| EQUITY       | CREDIT         | CREDIT         | DEBIT          |
| REVENUE      | CREDIT         | CREDIT         | DEBIT          |

---

## 🔧 2. FinancialPeriod Entity

**Purpose:** Accounting periods (monthly, quarterly, yearly)

### Key Fields

```java
// Identification
- periodCode (unique, e.g., "2024-01", "2024-Q1", "FY2024")
- periodName (e.g., "January 2024", "Q1 2024", "Fiscal Year 2024")

// Period details
- periodType (MONTHLY, QUARTERLY, YEARLY)
- fiscalYear (e.g., 2024)
- periodNumber (1-12 for monthly, 1-4 for quarterly, 1 for yearly)

// Dates
- startDate
- endDate

// Status
- status (OPEN, CLOSED, LOCKED)
- isCurrent (currently active period)

// Closing
- closedBy
- closedDate
- lockedBy
- lockedDate

// Description
- description
- notes
```

### Helper Methods

```java
boolean isOpen(); // status == OPEN
boolean isClosed(); // status == CLOSED or LOCKED
boolean isLocked(); // status == LOCKED
boolean canPostEntries(); // status == OPEN
boolean containsDate(LocalDate date); // Date within period
long getDurationDays(); // Number of days in period
boolean isCurrentPeriod(); // Today within period
```

### Status Flow

```
OPEN (can post entries)
  ↓ (close period)
CLOSED (cannot post, can reopen)
  ↓ (lock period)
LOCKED (cannot post, cannot reopen)
```

---

## 💳 3. JournalEntry Entity

**Purpose:** Journal entry headers for double-entry bookkeeping

### Key Fields

```java
// Identification
- entryNumber (unique, e.g., "JE-2024-001")
- entryDate
- financialPeriod (FK)

// Entry details
- entryType (MANUAL, SALES, PURCHASE, PAYMENT, RECEIPT, ADJUSTMENT, CLOSING, OPENING, OTHER)
- referenceNumber (e.g., invoice number, payment number)
- referenceId (reference to source document)
- referenceType (e.g., SALES_INVOICE, PAYMENT, PURCHASE)

// Currency
- currency
- exchangeRate

// Totals (auto-calculated from lines)
- totalDebit
- totalCredit
- difference (should be zero for valid entry)

// Status
- status (DRAFT, PENDING_APPROVAL, APPROVED, POSTED, REJECTED, CANCELLED)

// Posting
- isPosted (posted to general ledger)
- postedDate
- postedBy

// Approval
- approvedBy
- approvalDate

// Description
- description
- notes
- totalLines

// Relationships
- lines (Set<JournalEntryLine>)
```

### Helper Methods

```java
void calculateTotals(); // Calculate total debit, credit, difference from lines
boolean isBalanced(); // difference == 0
boolean canPost(); // APPROVED && !isPosted && isBalanced
boolean canEdit(); // DRAFT only
boolean requiresApproval(); // entryType == MANUAL
```

### Status Flow

```
DRAFT (can edit)
  ↓ (submit for approval - if MANUAL)
PENDING_APPROVAL
  ↓ (approve)
APPROVED
  ↓ (post to general ledger)
POSTED (cannot edit)
```

---

## 📝 4. JournalEntryLine Entity

**Purpose:** Line items in journal entries (debits and credits)

### Key Fields

```java
// References
- journalEntry (FK header)
- lineNumber (sequence)
- account (FK)

// Amounts
- debitAmount (increases ASSET/EXPENSE, decreases LIABILITY/EQUITY/REVENUE)
- creditAmount (decreases ASSET/EXPENSE, increases LIABILITY/EQUITY/REVENUE)

// Currency
- currency
- exchangeRate
- baseDebitAmount (converted to base currency)
- baseCreditAmount (converted to base currency)

// Description
- description
- notes
```

### Helper Methods

```java
BigDecimal getNetAmount(); // debitAmount - creditAmount
boolean isDebitEntry(); // debitAmount > creditAmount
boolean isCreditEntry(); // creditAmount > debitAmount
void calculateBaseAmounts(); // Convert to base currency
```

### Validation

```java
@PrePersist/@PreUpdate:
- Either debit or credit must be non-zero, but NOT both
- At least one must be non-zero
```

---

## 📖 5. GeneralLedger Entity

**Purpose:** General ledger entries with running balance

### Key Fields

```java
// References
- account (FK)
- financialPeriod (FK)
- journalEntry (FK)
- journalEntryLine (FK)

// Transaction details
- transactionDate
- transactionType (OPENING, SALES, PURCHASE, PAYMENT, RECEIPT, ADJUSTMENT, CLOSING, OTHER)
- referenceNumber
- referenceId
- referenceType

// Amounts
- debitAmount
- creditAmount
- balance (running balance after this transaction)

// Currency
- currency
- exchangeRate
- baseDebitAmount
- baseCreditAmount
- baseBalance

// Description
- description
- notes

// Reconciliation
- isReconciled
- reconciliationDate
```

### Helper Methods

```java
BigDecimal getNetAmount(); // debitAmount - creditAmount
boolean isDebitEntry(); // debitAmount > creditAmount
boolean isCreditEntry(); // creditAmount > debitAmount
void calculateBaseAmounts(); // Convert to base currency
```

---

## 💡 Usage Examples

### Example 1: Create Chart of Accounts

```java
// Create top-level account
ChartOfAccounts assets = ChartOfAccounts.builder()
    .accountCode("1000")
    .accountName("Assets")
    .accountType("ASSET")
    .accountCategory("TOTAL_ASSETS")
    .isHeader(true)
    .allowManualEntries(false)
    .build();

assets = accountRepository.save(assets);

// Create sub-account
ChartOfAccounts currentAssets = ChartOfAccounts.builder()
    .accountCode("1100")
    .accountName("Current Assets")
    .accountType("ASSET")
    .accountCategory("CURRENT_ASSET")
    .parentAccount(assets)
    .isHeader(true)
    .allowManualEntries(false)
    .build();

currentAssets = accountRepository.save(currentAssets);

// Create leaf account (can accept entries)
ChartOfAccounts cash = ChartOfAccounts.builder()
    .accountCode("1110")
    .accountName("Cash in Hand")
    .accountType("ASSET")
    .accountCategory("CURRENT_ASSET")
    .accountSubcategory("CASH")
    .parentAccount(currentAssets)
    .isHeader(false)
    .allowManualEntries(true)
    .openingBalanceDebit(new BigDecimal("100000.00"))
    .build();

cash = accountRepository.save(cash);

// Full path: "1000 - Assets > 1100 - Current Assets > 1110 - Cash in Hand"
System.out.println(cash.getFullPath());
```

### Example 2: Create Financial Period

```java
// Create monthly period
FinancialPeriod period = FinancialPeriod.builder()
    .periodCode("2024-01")
    .periodName("January 2024")
    .periodType("MONTHLY")
    .fiscalYear(2024)
    .periodNumber(1)
    .startDate(LocalDate.of(2024, 1, 1))
    .endDate(LocalDate.of(2024, 1, 31))
    .status("OPEN")
    .isCurrent(true)
    .description("First month of fiscal year 2024")
    .build();

period = periodRepository.save(period);
```

### Example 3: Create Manual Journal Entry

```java
@Transactional
public JournalEntryDTO createManualJournalEntry(CreateJournalEntryRequest request) {
    // Get financial period
    FinancialPeriod period = periodRepository
        .findByContainingDate(request.getEntryDate())
        .orElseThrow(() -> new NotFoundException("No open period found for date"));
    
    if (!period.canPostEntries()) {
        throw new BusinessException("Period is closed or locked");
    }
    
    // Create journal entry
    JournalEntry entry = JournalEntry.builder()
        .entryNumber(generateEntryNumber())
        .entryDate(request.getEntryDate())
        .financialPeriod(period)
        .entryType("MANUAL")
        .currency(request.getCurrency())
        .exchangeRate(request.getExchangeRate())
        .description(request.getDescription())
        .notes(request.getNotes())
        .status("DRAFT")
        .build();
    
    // Add lines
    int lineNumber = 1;
    for (var lineReq : request.getLines()) {
        ChartOfAccounts account = accountRepository.findById(lineReq.getAccountId())
            .orElseThrow(() -> new NotFoundException("Account not found"));
        
        if (!account.canAcceptEntries()) {
            throw new BusinessException(
                "Account " + account.getAccountCode() + " cannot accept manual entries"
            );
        }
        
        JournalEntryLine line = JournalEntryLine.builder()
            .lineNumber(lineNumber++)
            .account(account)
            .debitAmount(lineReq.getDebitAmount())
            .creditAmount(lineReq.getCreditAmount())
            .currency(request.getCurrency())
            .exchangeRate(request.getExchangeRate())
            .description(lineReq.getDescription())
            .notes(lineReq.getNotes())
            .build();
        
        entry.addLine(line);
    }
    
    // Calculate totals
    entry.calculateTotals();
    
    // Validate balanced
    if (!entry.isBalanced()) {
        throw new BusinessException(
            "Journal entry is not balanced. Difference: " + entry.getDifference()
        );
    }
    
    entry = journalEntryRepository.save(entry);
    return mapper.toDTO(entry);
}
```

### Example 4: Create Journal Entry from Sales Invoice

```java
@Transactional
public void createJournalEntryFromInvoice(Long invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new NotFoundException("Invoice not found"));
    
    // Get accounts
    ChartOfAccounts accountsReceivable = accountRepository.findByAccountCode("1200");
    ChartOfAccounts salesRevenue = accountRepository.findByAccountCode("4100");
    ChartOfAccounts salesTax = accountRepository.findByAccountCode("2300");
    
    // Get financial period
    FinancialPeriod period = periodRepository
        .findByContainingDate(invoice.getInvoiceDate())
        .orElseThrow(() -> new NotFoundException("No open period found"));
    
    // Create journal entry
    JournalEntry entry = JournalEntry.builder()
        .entryNumber(generateEntryNumber())
        .entryDate(invoice.getInvoiceDate())
        .financialPeriod(period)
        .entryType("SALES")
        .referenceNumber(invoice.getInvoiceNumber())
        .referenceId(invoice.getId())
        .referenceType("SALES_INVOICE")
        .currency(invoice.getCurrency())
        .exchangeRate(invoice.getExchangeRate())
        .description("Sales Invoice - " + invoice.getInvoiceNumber())
        .status("APPROVED") // Auto-approved for system entries
        .build();
    
    // Line 1: Debit Accounts Receivable (increases asset)
    JournalEntryLine line1 = JournalEntryLine.builder()
        .lineNumber(1)
        .account(accountsReceivable)
        .debitAmount(invoice.getTotalAmount())
        .creditAmount(BigDecimal.ZERO)
        .description("Accounts Receivable - " + invoice.getCustomer().getCustomerName())
        .build();
    entry.addLine(line1);
    
    // Line 2: Credit Sales Revenue (increases revenue)
    BigDecimal netAmount = invoice.getTotalAmount().subtract(invoice.getTaxAmount());
    JournalEntryLine line2 = JournalEntryLine.builder()
        .lineNumber(2)
        .account(salesRevenue)
        .debitAmount(BigDecimal.ZERO)
        .creditAmount(netAmount)
        .description("Sales Revenue")
        .build();
    entry.addLine(line2);
    
    // Line 3: Credit Sales Tax (increases liability)
    if (invoice.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
        JournalEntryLine line3 = JournalEntryLine.builder()
            .lineNumber(3)
            .account(salesTax)
            .debitAmount(BigDecimal.ZERO)
            .creditAmount(invoice.getTaxAmount())
            .description("Sales Tax Payable")
            .build();
        entry.addLine(line3);
    }
    
    // Calculate totals and save
    entry.calculateTotals();
    entry = journalEntryRepository.save(entry);
    
    // Auto-post to general ledger
    postJournalEntry(entry.getId());
}
```

### Example 5: Post Journal Entry to General Ledger

```java
@Transactional
public void postJournalEntry(Long entryId) {
    JournalEntry entry = journalEntryRepository.findById(entryId)
        .orElseThrow(() -> new NotFoundException("Journal entry not found"));
    
    if (!entry.canPost()) {
        throw new BusinessException("Entry cannot be posted. Must be approved, not posted, and balanced");
    }
    
    // Check period is open
    if (!entry.getFinancialPeriod().canPostEntries()) {
        throw new BusinessException("Financial period is closed or locked");
    }
    
    // Post each line to general ledger
    for (JournalEntryLine line : entry.getLines()) {
        // Get current balance for account
        BigDecimal currentBalance = generalLedgerRepository
            .getAccountBalance(line.getAccount().getId())
            .orElse(line.getAccount().getOpeningBalance());
        
        // Calculate new balance based on account type
        BigDecimal newBalance;
        if (line.getAccount().isDebitAccount()) {
            // For ASSET/EXPENSE: balance = previous + debit - credit
            newBalance = currentBalance
                .add(line.getDebitAmount())
                .subtract(line.getCreditAmount());
        } else {
            // For LIABILITY/EQUITY/REVENUE: balance = previous + credit - debit
            newBalance = currentBalance
                .add(line.getCreditAmount())
                .subtract(line.getDebitAmount());
        }
        
        // Create general ledger entry
        GeneralLedger glEntry = GeneralLedger.builder()
            .account(line.getAccount())
            .financialPeriod(entry.getFinancialPeriod())
            .journalEntry(entry)
            .journalEntryLine(line)
            .transactionDate(entry.getEntryDate())
            .transactionType(entry.getEntryType())
            .referenceNumber(entry.getReferenceNumber())
            .referenceId(entry.getReferenceId())
            .referenceType(entry.getReferenceType())
            .debitAmount(line.getDebitAmount())
            .creditAmount(line.getCreditAmount())
            .balance(newBalance)
            .currency(line.getCurrency())
            .exchangeRate(line.getExchangeRate())
            .description(line.getDescription())
            .notes(line.getNotes())
            .build();
        
        glEntry.calculateBaseAmounts();
        generalLedgerRepository.save(glEntry);
        
        // Update account current balance
        ChartOfAccounts account = line.getAccount();
        account.setCurrentBalanceDebit(
            account.getCurrentBalanceDebit().add(line.getDebitAmount())
        );
        account.setCurrentBalanceCredit(
            account.getCurrentBalanceCredit().add(line.getCreditAmount())
        );
        accountRepository.save(account);
    }
    
    // Mark entry as posted
    entry.setIsPosted(true);
    entry.setPostedDate(LocalDate.now());
    entry.setPostedBy(getCurrentUsername());
    journalEntryRepository.save(entry);
}
```

### Example 6: Get Account Ledger (Trial Balance)

```java
public List<GeneralLedgerDTO> getAccountLedger(Long accountId, LocalDate startDate, LocalDate endDate) {
    ChartOfAccounts account = accountRepository.findById(accountId)
        .orElseThrow(() -> new NotFoundException("Account not found"));
    
    List<GeneralLedger> entries = generalLedgerRepository
        .findByAccountAndDateRange(account, startDate, endDate);
    
    return entries.stream()
        .map(mapper::toDTO)
        .collect(Collectors.toList());
}
```

### Example 7: Close Financial Period

```java
@Transactional
public void closeFinancialPeriod(Long periodId) {
    FinancialPeriod period = periodRepository.findById(periodId)
        .orElseThrow(() -> new NotFoundException("Period not found"));
    
    if (!period.isOpen()) {
        throw new BusinessException("Period is already closed or locked");
    }
    
    // Verify all entries are posted
    long unpostedCount = journalEntryRepository
        .countUnpostedByPeriod(period);
    
    if (unpostedCount > 0) {
        throw new BusinessException(
            "Cannot close period. " + unpostedCount + " unposted entries exist"
        );
    }
    
    // Close period
    period.setStatus("CLOSED");
    period.setClosedBy(getCurrentUsername());
    period.setClosedDate(LocalDate.now());
    
    // Mark next period as current if exists
    periodRepository.findByFiscalYearAndPeriodNumber(
        period.getFiscalYear(), 
        period.getPeriodNumber() + 1
    ).ifPresent(nextPeriod -> {
        nextPeriod.setIsCurrent(true);
        periodRepository.save(nextPeriod);
    });
    
    period.setIsCurrent(false);
    periodRepository.save(period);
}
```

---

## 📁 Directory Structure

```
accounting/
├── entity/
│   ├── ChartOfAccounts.java
│   ├── FinancialPeriod.java
│   ├── JournalEntry.java
│   ├── JournalEntryLine.java
│   ├── GeneralLedger.java
│   └── README.md
└── dto/
    ├── AccountDTO.java
    ├── JournalEntryDTO.java
    └── GeneralLedgerDTO.java
```

---

## ✅ Summary

✅ **5 Entity classes** - Complete accounting and general ledger management  
✅ **3 DTO classes** - Request/response objects  
✅ **Chart of accounts** - Hierarchical account structure  
✅ **Account types** - ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE  
✅ **Account hierarchy** - Parent-child relationships with unlimited levels  
✅ **Normal balance sides** - DEBIT for ASSET/EXPENSE, CREDIT for LIABILITY/EQUITY/REVENUE  
✅ **Financial periods** - Monthly, quarterly, yearly periods  
✅ **Period status** - OPEN, CLOSED, LOCKED  
✅ **Double-entry bookkeeping** - Every debit has a corresponding credit  
✅ **Journal entries** - Complete transaction recording  
✅ **Entry types** - MANUAL, SALES, PURCHASE, PAYMENT, RECEIPT, ADJUSTMENT, CLOSING, OPENING  
✅ **Entry workflow** - DRAFT → PENDING_APPROVAL → APPROVED → POSTED  
✅ **Balance validation** - Ensure debit = credit  
✅ **General ledger** - Posted entries with running balance  
✅ **Running balance** - Calculate balance after each transaction  
✅ **Multi-currency** - Foreign currency support with exchange rates  
✅ **Base currency** - Convert all amounts to base currency  
✅ **Reconciliation** - Mark entries as reconciled  
✅ **Period closing** - Close and lock periods  
✅ **Approval workflow** - Manual entries require approval  
✅ **System integration** - Auto-create entries from sales, purchases, payments  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade financial accounting  

**Everything you need for complete financial accounting, double-entry bookkeeping, chart of accounts management, journal entries, and general ledger in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.accounting
