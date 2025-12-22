package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ChartOfAccounts entity
 * Represents the chart of accounts structure
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "chart_of_accounts", indexes = {
    @Index(name = "idx_account_code", columnList = "account_code"),
    @Index(name = "idx_account_name", columnList = "account_name"),
    @Index(name = "idx_account_type", columnList = "account_type"),
    @Index(name = "idx_parent_account", columnList = "parent_account_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccounts extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Account code (unique identifier)
     */
    @Column(name = "account_code", nullable = false, unique = true, length = 50)
    private String accountCode;
    
    /**
     * Account name
     */
    @Column(name = "account_name", nullable = false, length = 200)
    private String accountName;
    
    /**
     * Account type (ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE)
     */
    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;
    
    /**
     * Account category (CURRENT_ASSET, FIXED_ASSET, CURRENT_LIABILITY, LONG_TERM_LIABILITY, etc.)
     */
    @Column(name = "account_category", length = 50)
    private String accountCategory;
    
    /**
     * Account subcategory
     */
    @Column(name = "account_subcategory", length = 50)
    private String accountSubcategory;
    
    /**
     * Parent account (for hierarchical structure)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", foreignKey = @ForeignKey(name = "fk_account_parent"))
    private ChartOfAccounts parentAccount;
    
    /**
     * Child accounts
     */
    @OneToMany(mappedBy = "parentAccount")
    @Builder.Default
    private Set<ChartOfAccounts> childAccounts = new HashSet<>();
    
    /**
     * Account level (1 = top level, 2 = sub-account, etc.)
     */
    @Column(name = "account_level")
    private Integer accountLevel;
    
    /**
     * Is header account (has child accounts)
     */
    @Column(name = "is_header")
    private Boolean isHeader;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Is system account (cannot be deleted)
     */
    @Column(name = "is_system")
    private Boolean isSystem;
    
    /**
     * Can post transactions to this account
     */
    @Column(name = "can_post")
    private Boolean canPost;
    
    /**
     * Requires reconciliation
     */
    @Column(name = "requires_reconciliation")
    private Boolean requiresReconciliation;
    
    /**
     * Account status (ACTIVE, INACTIVE, CLOSED)
     */
    @Column(name = "status", length = 20)
    private String status;
    
    /**
     * Tags for categorization
     */
    @Column(name = "tags", length = 500)
    private String tags;
    
    /**
     * Allow manual journal entries
     */
    @Column(name = "allow_manual_entries")
    private Boolean allowManualEntries;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Opening balance debit
     */
    @Column(name = "opening_balance_debit", precision = 15, scale = 2)
    private BigDecimal openingBalanceDebit;
    
    /**
     * Opening balance credit
     */
    @Column(name = "opening_balance_credit", precision = 15, scale = 2)
    private BigDecimal openingBalanceCredit;
    
    /**
     * Current balance debit
     */
    @Column(name = "current_balance_debit", precision = 15, scale = 2)
    private BigDecimal currentBalanceDebit;
    
    /**
     * Current balance credit
     */
    @Column(name = "current_balance_credit", precision = 15, scale = 2)
    private BigDecimal currentBalanceCredit;
    
    /**
     * Debit balance (Double for backward compatibility)
     */
    @Column(name = "debit_balance")
    private Double debitBalance;
    
    /**
     * Credit balance (Double for backward compatibility)
     */
    @Column(name = "credit_balance")
    private Double creditBalance;
    
    /**
     * Current balance (Double for backward compatibility)
     */
    @Column(name = "current_balance")
    private Double currentBalance;
    
    /**
     * Last balance update date
     */
    @Column(name = "last_balance_update")
    private java.time.LocalDate lastBalanceUpdate;
    
    /**
     * Last reconciliation date
     */
    @Column(name = "last_reconciliation_date")
    private java.time.LocalDate lastReconciliationDate;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Gets full account path (e.g., "1000 > 1100 > 1110")
     */
    @Transient
    public String getFullPath() {
        if (parentAccount == null) {
            return accountCode + " - " + accountName;
        }
        return parentAccount.getFullPath() + " > " + accountCode + " - " + accountName;
    }
    
    /**
     * Gets parent account ID
     */
    @Transient
    public Long getParentAccountId() {
        return parentAccount != null ? parentAccount.getId() : null;
    }
    
    /**
     * Sets parent account by ID (for service layer)
     */
    public void setParentAccountId(Long parentAccountId) {
        // This is handled by setting the parentAccount object directly
        // This method exists for compatibility with service layer
    }
    
    /**
     * Gets account balance (debit - credit for ASSET/EXPENSE, credit - debit for LIABILITY/EQUITY/REVENUE)
     */
    @Transient
    public java.math.BigDecimal getBalance() {
        if (currentBalanceDebit == null || currentBalanceCredit == null) {
            return java.math.BigDecimal.ZERO;
        }
        
        if ("ASSET".equals(accountType) || "EXPENSE".equals(accountType)) {
            return currentBalanceDebit.subtract(currentBalanceCredit);
        } else {
            return currentBalanceCredit.subtract(currentBalanceDebit);
        }
    }
    
    /**
     * Gets opening balance
     */
    @Transient
    public java.math.BigDecimal getOpeningBalance() {
        if (openingBalanceDebit == null || openingBalanceCredit == null) {
            return java.math.BigDecimal.ZERO;
        }
        
        if ("ASSET".equals(accountType) || "EXPENSE".equals(accountType)) {
            return openingBalanceDebit.subtract(openingBalanceCredit);
        } else {
            return openingBalanceCredit.subtract(openingBalanceDebit);
        }
    }
    
    /**
     * Gets normal balance side (DEBIT for ASSET/EXPENSE, CREDIT for LIABILITY/EQUITY/REVENUE)
     */
    @Transient
    public String getNormalBalanceSide() {
        if ("ASSET".equals(accountType) || "EXPENSE".equals(accountType)) {
            return "DEBIT";
        } else {
            return "CREDIT";
        }
    }
    
    /**
     * Checks if is debit account
     */
    @Transient
    public boolean isDebitAccount() {
        return "ASSET".equals(accountType) || "EXPENSE".equals(accountType);
    }
    
    /**
     * Checks if is credit account
     */
    @Transient
    public boolean isCreditAccount() {
        return "LIABILITY".equals(accountType) || "EQUITY".equals(accountType) || "REVENUE".equals(accountType);
    }
    
    /**
     * Checks if can accept manual entries
     */
    @Transient
    public boolean canAcceptEntries() {
        return !isHeader && allowManualEntries;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isHeader == null) {
            isHeader = false;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isSystem == null) {
            isSystem = false;
        }
        if (canPost == null) {
            canPost = !isHeader; // Headers can't post, leaf accounts can
        }
        if (requiresReconciliation == null) {
            requiresReconciliation = false;
        }
        if (status == null) {
            status = "ACTIVE";
        }
        if (allowManualEntries == null) {
            allowManualEntries = true;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (openingBalanceDebit == null) {
            openingBalanceDebit = java.math.BigDecimal.ZERO;
        }
        if (openingBalanceCredit == null) {
            openingBalanceCredit = java.math.BigDecimal.ZERO;
        }
        if (currentBalanceDebit == null) {
            currentBalanceDebit = openingBalanceDebit;
        }
        if (currentBalanceCredit == null) {
            currentBalanceCredit = openingBalanceCredit;
        }
        if (debitBalance == null) {
            debitBalance = 0.0;
        }
        if (creditBalance == null) {
            creditBalance = 0.0;
        }
        if (currentBalance == null) {
            currentBalance = 0.0;
        }
        
        // Calculate account level
        if (parentAccount == null) {
            accountLevel = 1;
        } else {
            accountLevel = parentAccount.getAccountLevel() + 1;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartOfAccounts)) return false;
        ChartOfAccounts that = (ChartOfAccounts) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
