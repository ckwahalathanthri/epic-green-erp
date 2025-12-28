package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.response.TrialBalanceResponse;
import lk.epicgreen.erp.accounting.entity.TrialBalance;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.entity.FinancialPeriod;
import lk.epicgreen.erp.accounting.mapper.TrialBalanceMapper;
import lk.epicgreen.erp.accounting.repository.TrialBalanceRepository;
import lk.epicgreen.erp.accounting.repository.ChartOfAccountsRepository;
import lk.epicgreen.erp.accounting.repository.FinancialPeriodRepository;
import lk.epicgreen.erp.accounting.repository.GeneralLedgerRepository;
import lk.epicgreen.erp.accounting.service.TrialBalanceService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TrialBalanceServiceImpl implements TrialBalanceService {

    private final TrialBalanceRepository trialBalanceRepository;
    private final ChartOfAccountsRepository accountRepository;
    private final FinancialPeriodRepository periodRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final TrialBalanceMapper trialBalanceMapper;

    @Override
    @Transactional
    public List<TrialBalanceResponse> generateTrialBalance(Long periodId, Long generatedBy) {
        log.info("Generating Trial Balance for period: {}", periodId);

        FinancialPeriod period = periodRepository.findById(periodId)
            .orElseThrow(() -> new ResourceNotFoundException("Financial Period not found: " + periodId));

        // Delete existing trial balance for this period
        trialBalanceRepository.deleteByPeriodId(periodId);

        List<ChartOfAccounts> activeAccounts = accountRepository.findByIsActiveTrue();
        List<TrialBalance> trialBalances = new ArrayList<>();

        for (ChartOfAccounts account : activeAccounts) {
            BigDecimal periodDebit = generalLedgerRepository
                .sumDebitByAccountAndPeriod(account.getId(), periodId)
                .orElse(BigDecimal.ZERO);
            
            BigDecimal periodCredit = generalLedgerRepository
                .sumCreditByAccountAndPeriod(account.getId(), periodId)
                .orElse(BigDecimal.ZERO);

            TrialBalance tb = TrialBalance.builder()
                .period(period)
                .account(account)
                .openingDebit(account.getOpeningBalance() != null && "DEBIT".equals(account.getOpeningBalanceType()) ? 
                    account.getOpeningBalance() : BigDecimal.ZERO)
                .openingCredit(account.getOpeningBalance() != null && "CREDIT".equals(account.getOpeningBalanceType()) ? 
                    account.getOpeningBalance() : BigDecimal.ZERO)
                .periodDebit(periodDebit)
                .periodCredit(periodCredit)
                .closingDebit(BigDecimal.ZERO)
                .closingCredit(BigDecimal.ZERO)
                .generatedAt(LocalDateTime.now())
                .generatedBy(generatedBy)
                .build();

            // Calculate closing balances
            BigDecimal netDebit = tb.getOpeningDebit().add(periodDebit);
            BigDecimal netCredit = tb.getOpeningCredit().add(periodCredit);

            if (netDebit.compareTo(netCredit) > 0) {
                tb.setClosingDebit(netDebit.subtract(netCredit));
                tb.setClosingCredit(BigDecimal.ZERO);
            } else {
                tb.setClosingDebit(BigDecimal.ZERO);
                tb.setClosingCredit(netCredit.subtract(netDebit));
            }

            trialBalances.add(tb);
        }

        List<TrialBalance> savedBalances = trialBalanceRepository.saveAll(trialBalances);
        log.info("Trial Balance generated successfully for period: {}", periodId);

        return savedBalances.stream()
            .map(trialBalanceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<TrialBalanceResponse> getTrialBalanceByPeriod(Long periodId) {
        List<TrialBalance> balances = trialBalanceRepository.findByPeriodId(periodId);
        return balances.stream()
            .map(trialBalanceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public TrialBalanceResponse getTrialBalanceById(Long id) {
        TrialBalance balance = trialBalanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Balance entry not found: " + id));
        return trialBalanceMapper.toResponse(balance);
    }
}
