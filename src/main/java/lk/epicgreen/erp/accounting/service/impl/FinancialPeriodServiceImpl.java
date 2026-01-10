package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.FinancialPeriodRequest;
import lk.epicgreen.erp.accounting.dto.response.FinancialPeriodResponse;
import lk.epicgreen.erp.accounting.entity.FinancialPeriod;
import lk.epicgreen.erp.accounting.mapper.FinancialPeriodMapper;
import lk.epicgreen.erp.accounting.repository.FinancialPeriodRepository;
import lk.epicgreen.erp.accounting.service.FinancialPeriodService;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FinancialPeriodServiceImpl implements FinancialPeriodService {

    private final FinancialPeriodRepository periodRepository;
    private final FinancialPeriodMapper periodMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FinancialPeriodResponse createPeriod(FinancialPeriodRequest request) {
        log.info("Creating new Financial Period: {}", request.getPeriodCode());

        validateUniquePeriodCode(request.getPeriodCode(), null);
        validateDateRange(request.getStartDate(), request.getEndDate());

        FinancialPeriod period = periodMapper.toEntity(request);
        FinancialPeriod savedPeriod = periodRepository.save(period);
        log.info("Financial Period created successfully: {}", savedPeriod.getPeriodCode());

        return periodMapper.toResponse(savedPeriod);
    }

    @Override
    @Transactional
    public FinancialPeriodResponse updatePeriod(Long id, FinancialPeriodRequest request) {
        log.info("Updating Financial Period: {}", id);

        FinancialPeriod period = findPeriodById(id);

        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Financial Period. Period is closed.");
        }

        if (!period.getPeriodCode().equals(request.getPeriodCode())) {
            validateUniquePeriodCode(request.getPeriodCode(), id);
        }

        validateDateRange(request.getStartDate(), request.getEndDate());

        periodMapper.updateEntityFromRequest(request, period);
        FinancialPeriod updatedPeriod = periodRepository.save(period);
        log.info("Financial Period updated successfully: {}", updatedPeriod.getPeriodCode());

        return periodMapper.toResponse(updatedPeriod);
    }

    @Override
    @Transactional
    public void closePeriod(Long id, Long closedBy) {
        log.info("Closing Financial Period: {} by user: {}", id, closedBy);

        FinancialPeriod period = findPeriodById(id);

        if (period.getIsClosed()) {
            throw new InvalidOperationException("Financial Period is already closed.");
        }

        period.setIsClosed(true);
        period.setClosedBy(userRepository.findById(closedBy).get());
        period.setClosedAt(LocalDateTime.now());
        periodRepository.save(period);

        log.info("Financial Period closed successfully: {}", id);
    }

    @Override
    @Transactional
    public void deletePeriod(Long id) {
        log.info("Deleting Financial Period: {}", id);

        if (!canDelete(id)) {
            FinancialPeriod period = findPeriodById(id);
            throw new InvalidOperationException(
                "Cannot delete Financial Period. Period is closed or has journal entries.");
        }

        periodRepository.deleteById(id);
        log.info("Financial Period deleted successfully: {}", id);
    }

    @Override
    public FinancialPeriodResponse getPeriodById(Long id) {
        FinancialPeriod period = findPeriodById(id);
        return periodMapper.toResponse(period);
    }

    @Override
    public FinancialPeriodResponse getPeriodByCode(String periodCode) {
        FinancialPeriod period = periodRepository.findByPeriodCode(periodCode)
            .orElseThrow(() -> new ResourceNotFoundException("Financial Period not found: " + periodCode));
        return periodMapper.toResponse(period);
    }

    @Override
    public PageResponse<FinancialPeriodResponse> getAllPeriods(Pageable pageable) {
        Page<FinancialPeriod> periodPage = periodRepository.findAll(pageable);
        return createPageResponse(periodPage);
    }

    @Override
    public List<FinancialPeriodResponse> getPeriodsByFiscalYear(Integer fiscalYear) {
        List<FinancialPeriod> periods = periodRepository.findByFiscalYear(fiscalYear);
        return periods.stream()
            .map(periodMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FinancialPeriodResponse> getPeriodsByType(String periodType) {
        List<FinancialPeriod> periods = periodRepository.findByPeriodType(periodType);
        return periods.stream()
            .map(periodMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FinancialPeriodResponse> getOpenPeriods() {
        List<FinancialPeriod> periods = periodRepository.findByIsClosedFalse();
        return periods.stream()
            .map(periodMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * @param date
     * @return
     */
    @Override
    public FinancialPeriodResponse getCurrentPeriod(LocalDate date) {
        return null;
    }

//    @Override
//    public FinancialPeriodResponse getCurrentPeriod(LocalDate date) {
//        FinancialPeriod period = periodRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date)
//            .orElseThrow(() -> new ResourceNotFoundException("No period found for date: " + date));
//        return periodMapper.toResponse(period);
//    }

    @Override
    public boolean canDelete(Long id) {
        FinancialPeriod period = findPeriodById(id);
        return !period.getIsClosed();
    }

    @Override
    public boolean canUpdate(Long id) {
        FinancialPeriod period = findPeriodById(id);
        return !period.getIsClosed();
    }

    private void validateUniquePeriodCode(String periodCode, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = periodRepository.existsByPeriodCodeAndIdNot(periodCode, excludeId);
        } else {
            exists = periodRepository.existsByPeriodCode(periodCode);
        }

        if (exists) {
            throw new DuplicateResourceException("Financial Period with code '" + periodCode + "' already exists");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new InvalidOperationException("End date must be after start date.");
        }
    }

    private FinancialPeriod findPeriodById(Long id) {
        return periodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Financial Period not found: " + id));
    }

    private PageResponse<FinancialPeriodResponse> createPageResponse(Page<FinancialPeriod> periodPage) {
        List<FinancialPeriodResponse> content = periodPage.getContent().stream()
            .map(periodMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<FinancialPeriodResponse>builder()
            .content(content)
            .pageNumber(periodPage.getNumber())
            .pageSize(periodPage.getSize())
            .totalElements(periodPage.getTotalElements())
            .totalPages(periodPage.getTotalPages())
            .last(periodPage.isLast())
            .first(periodPage.isFirst())
            .empty(periodPage.isEmpty())
            .build();
    }
}
