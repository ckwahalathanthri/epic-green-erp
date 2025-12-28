package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ChartOfAccounts entity
 * Represents the chart of accounts (COA) for the accounting system
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "chart_of_accounts", indexes = {
    @Index(name = "idx_account_code", columnList = "account_code"),
    @Index(name = "idx_account_type", columnList = "account_type"),
    @Index(name = "idx_parent_account_id", columnList = "parent_account_id")
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
    @NotBlank(message = "Account code is required")
    @Size(max = 20)
    @Column(name = "account_code", nullable = false, unique = true, length = 20)
    private String accountCode;
    
    /**
     * Account name
     */
    @NotBlank(message = "Account name is required")
    @Size(max = 200)
    @Column(name = "account_name", nullable = false, length = 200)
    private String accountName;
    
    /**
     * Account type (ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE)
     */
    @NotBlank(message = "Account type is required")
    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;
    
    /**
     * Account category
     * ASSET: CURRENT_ASSET, FIXED_ASSET
     * LIABILITY: CURRENT_LIABILITY, LONG_TERM_LIABILITY
     * EQUITY: CAPITAL
     * REVENUE: DIRECT_INCOME, INDIRECT_INCOME
     * EXPENSE: DIRECT_EXPENSE, INDIRECT_EXPENSE
     * OTHER
     */
    @NotBlank(message = "Account category is required")
    @Column(name = "account_category", nullable = false, length = 30)
    private String accountCategory;
    
    /**
     * Parent account (for hierarchical COA)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", foreignKey = @ForeignKey(name = "fk_coa_parent_account"))
    private ChartOfAccounts parentAccount;
    
    /**
     * Child accounts
     */
    @OneToMany(mappedBy = "parentAccount")
    @Builder.Default
    private List<ChartOfAccounts> childAccounts = new ArrayList<>();
    
    /**
     * Is group account (has child accounts)
     */
    @Column(name = "is_group_account")
    private Boolean isGroupAccount;
    
    /**
     * Is control account (linked to subsidiary ledgers)
     */
    @Column(name = "is_control_account")
    private Boolean isControlAccount;
    
    /**
     * Opening balance
     */
    @Column(name = "opening_balance", precision = 15, scale = 2)
    private BigDecimal openingBalance;
    
    /**
     * Opening balance type (DEBIT, CREDIT)
     */
    @Column(name = "opening_balance_type", length = 10)
    private String openingBalanceType;
    
    /**
     * Current balance
     */
    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Account type checks
     */
    @Transient
    public boolean isAsset() {
        return "ASSET".equals(accountType);
    }
    
    @Transient
    public boolean isLiability() {
        return "LIABILITY".equals(accountType);
    }
    
    @Transient
    public boolean isEquity() {
        return "EQUITY".equals(accountType);
    }
    
    @Transient
    public boolean isRevenue() {
        return "REVENUE".equals(accountType);
    }
    
    @Transient
    public boolean isExpense() {
        return "EXPENSE".equals(accountType);
    }
    
    /**
     * Category checks
     */
    @Transient
    public boolean isCurrentAsset() {
        return "CURRENT_ASSET".equals(accountCategory);
    }
    
    @Transient
    public boolean isFixedAsset() {
        return "FIXED_ASSET".equals(accountCategory);
    }
    
    @Transient
    public boolean isCurrentLiability() {
        return "CURRENT_LIABILITY".equals(accountCategory);
    }
    
    @Transient
    public boolean isLongTermLiability() {
        return "LONG_TERM_LIABILITY".equals(accountCategory);
    }
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if group account
     */
    @Transient
    public boolean isGroupAccount() {
        return Boolean.TRUE.equals(isGroupAccount);
    }
    
    /**
     * Check if control account
     */
    @Transient
    public boolean isControlAccount() {
        return Boolean.TRUE.equals(isControlAccount);
    }
    
    /**
     * Check if has parent
     */
    @Transient
    public boolean hasParent() {
        return parentAccount != null;
    }
    
    /**
     * Check if has children
     */
    @Transient
    public boolean hasChildren() {
        return childAccounts != null && !childAccounts.isEmpty();
    }
    
    /**
     * Get normal balance side (DEBIT or CREDIT)
     */
    @Transient
    public String getNormalBalanceSide() {
        if (isAsset() || isExpense()) {
            return "DEBIT";
        } else if (isLiability() || isEquity() || isRevenue()) {
            return "CREDIT";
        }
        return "DEBIT";
    }
    
    /**
     * Check if debit increases balance
     */
    @Transient
    public boolean isDebitIncrease() {
        return "DEBIT".equals(getNormalBalanceSide());
    }
    
    /**
     * Check if credit increases balance
     */
    @Transient
    public boolean isCreditIncrease() {
        return "CREDIT".equals(getNormalBalanceSide());
    }
    
    /**
     * Get full account path (for hierarchical display)
     */
    @Transient
    public String getFullAccountPath() {
        if (parentAccount == null) {
            return accountName;
        }
        return parentAccount.getFullAccountPath() + " > " + accountName;
    }
    
    /**
     * Get account level in hierarchy
     */
    @Transient
    public int getAccountLevel() {
        if (parentAccount == null) {
            return 0;
        }
        return parentAccount.getAccountLevel() + 1;
    }
    
    /**
     * Update current balance
     */
    public void updateBalance(BigDecimal debitAmount, BigDecimal creditAmount) {
        BigDecimal current = currentBalance != null ? currentBalance : BigDecimal.ZERO;
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        
        if (isDebitIncrease()) {
            // Debit increases, Credit decreases
            currentBalance = current.add(debit).subtract(credit);
        } else {
            // Credit increases, Debit decreases
            currentBalance = current.add(credit).subtract(debit);
        }
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (isGroupAccount == null) {
            isGroupAccount = false;
        }
        if (isControlAccount == null) {
            isControlAccount = false;
        }
        if (openingBalance == null) {
            openingBalance = BigDecimal.ZERO;
        }
        if (openingBalanceType == null) {
            openingBalanceType = "DEBIT";
        }
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
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
