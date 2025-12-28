package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * BankAccount entity
 * Represents company bank accounts
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bank_accounts", indexes = {
    @Index(name = "idx_account_number", columnList = "account_number"),
    @Index(name = "idx_bank_name", columnList = "bank_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Account number
     */
    @NotBlank(message = "Account number is required")
    @Size(max = 50)
    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;
    
    /**
     * Account name
     */
    @NotBlank(message = "Account name is required")
    @Size(max = 200)
    @Column(name = "account_name", nullable = false, length = 200)
    private String accountName;
    
    /**
     * Bank name
     */
    @NotBlank(message = "Bank name is required")
    @Size(max = 100)
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;
    
    /**
     * Bank branch
     */
    @Size(max = 100)
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Account type (CURRENT, SAVINGS, OVERDRAFT, CASH)
     */
    @NotBlank(message = "Account type is required")
    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;
    
    /**
     * Currency code (e.g., LKR, USD)
     */
    @Size(max = 3)
    @Column(name = "currency_code", length = 3)
    private String currencyCode;
    
    /**
     * General ledger account (link to COA)
     */
    @NotNull(message = "GL account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bank_account_gl_account"))
    private ChartOfAccounts glAccount;
    
    /**
     * Opening balance
     */
    @Column(name = "opening_balance", precision = 15, scale = 2)
    private BigDecimal openingBalance;
    
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
    public boolean isCurrent() {
        return "CURRENT".equals(accountType);
    }
    
    @Transient
    public boolean isSavings() {
        return "SAVINGS".equals(accountType);
    }
    
    @Transient
    public boolean isOverdraft() {
        return "OVERDRAFT".equals(accountType);
    }
    
    @Transient
    public boolean isCash() {
        return "CASH".equals(accountType);
    }
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Get full bank details
     */
    @Transient
    public String getFullBankDetails() {
        StringBuilder details = new StringBuilder();
        details.append(bankName);
        if (bankBranch != null) {
            details.append(" - ").append(bankBranch);
        }
        details.append(" (").append(accountNumber).append(")");
        return details.toString();
    }
    
    /**
     * Update balance
     */
    public void updateBalance(BigDecimal amount) {
        BigDecimal current = currentBalance != null ? currentBalance : BigDecimal.ZERO;
        currentBalance = current.add(amount);
    }
    
    /**
     * Debit account (decrease balance)
     */
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        updateBalance(amount.negate());
    }
    
    /**
     * Credit account (increase balance)
     */
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        updateBalance(amount);
    }
    
    /**
     * Check if overdrawn
     */
    @Transient
    public boolean isOverdrawn() {
        BigDecimal balance = currentBalance != null ? currentBalance : BigDecimal.ZERO;
        return balance.compareTo(BigDecimal.ZERO) < 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (currencyCode == null) {
            currencyCode = "LKR";
        }
        if (openingBalance == null) {
            openingBalance = BigDecimal.ZERO;
        }
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
