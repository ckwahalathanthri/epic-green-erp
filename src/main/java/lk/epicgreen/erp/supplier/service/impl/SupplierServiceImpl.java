package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.mapper.SupplierMapper;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.service.SupplierService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating new supplier: {}", request.getSupplierCode());

        // Validate unique constraint
        validateUniqueSupplierCode(request.getSupplierCode(), null);

        // Create supplier entity
        Supplier supplier = supplierMapper.toEntity(request);

        Supplier savedSupplier = supplierRepository.save(supplier);
        log.info("Supplier created successfully: {}", savedSupplier.getSupplierCode());

        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier: {}", id);

        Supplier supplier = findSupplierById(id);

        // Validate unique constraint
        validateUniqueSupplierCode(request.getSupplierCode(), id);

        // Update fields
        supplierMapper.updateEntityFromRequest(request, supplier);

        Supplier updatedSupplier = supplierRepository.save(supplier);
        log.info("Supplier updated successfully: {}", updatedSupplier.getSupplierCode());

        return supplierMapper.toResponse(updatedSupplier);
    }

    @Override
    @Transactional
    public Supplier activateSupplier(Long id) {
        log.info("Activating supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsActive(true);
        supplierRepository.save(supplier);

        log.info("Supplier activated successfully: {}", id);
        return supplier;
    }

    @Override
    @Transactional
    public Supplier deactivateSupplier(Long id) {
        log.info("Deactivating supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsActive(false);
        supplierRepository.save(supplier);

        log.info("Supplier deactivated successfully: {}", id);
        return supplier;
    }

    @Transactional
    public Supplier getSupplierByEmail(String email){
        return supplierRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with email: " + email));
    }

    @Transactional
    public Supplier approveSupplier(Long id, Long approvedByUserId, String approvalNotes){
        log.info("Approving supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsApproved(true);
        supplier.setApprovedBy(approvedByUserId);
        supplier.setApprovalNotes(approvalNotes);
        supplier.setApprovedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        log.info("Supplier approved successfully: {}", id);
        return supplier;
    }
    @Transactional
    public Supplier rejectSupplier(Long id, String rejectionReason){
        log.info("Rejecting supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsApproved(false);
        supplier.setRejectionReason(rejectionReason);
        supplier.setApprovedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        log.info("Supplier rejected successfully: {}", id);
        return supplier;
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier (soft delete): {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setDeletedAt(LocalDateTime.now());
        supplier.setIsActive(false);
        supplierRepository.save(supplier);

        log.info("Supplier deleted successfully: {}", id);
    }

    @Transactional
    public Supplier blockSupplier(Long id, String blockReason){
        log.info("Blocking supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsBlocked(true);
        supplier.setBlockReason(blockReason);
        supplierRepository.save(supplier);

        log.info("Supplier blocked successfully: {}", id);
        return supplier;
    }

    @Transactional
    public  Supplier unblockSupplier(Long id){
        log.info("Unblocking supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setIsBlocked(false);
        supplier.setBlockReason(null);
        supplierRepository.save(supplier);

        log.info("Supplier unblocked successfully: {}", id);
        return supplier;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Supplier> getActiveSuppliers(Pageable pageable) {
        return null;
    }

    @Transactional
    public List<Supplier> getActiveSuppliers(){
        return supplierRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }

    @Transactional
    public  List<Supplier> getInactiveSuppliers(){
        return supplierRepository.findByIsActiveFalse();
    }

    @Transactional
    public List<Supplier> getApprovedSuppliers(){
        return supplierRepository.findByIsApprovedTrueAndDeletedAtIsNull();
    }

    @Transactional
    public List<Supplier> getPendingApprovalSuppliers(){
        return supplierRepository.findByIsApprovedFalseAndDeletedAtIsNull();
    }

    @Transactional
    public  List<Supplier> getRejectedSuppliers(){
        return supplierRepository.findByIsApprovedFalseAndRejectionReasonIsNotNullAndDeletedAtIsNull();
    }

    @Transactional
    public List<Supplier>getSuppliersByStatus(String status){
        return supplierRepository.findByStatus(status);
    }

    @Transactional
    public List<Supplier> getSuppliersByCity(String city){
        return supplierRepository.findByCity(city);
    }

    @Transactional
    public List<Supplier> getSuppliersByCountry(String country){
        return supplierRepository.findByCountry(country);
    }

    @Transactional
    public List<Supplier> getLocalSuppliers(String country){
        return supplierRepository.findByCountryAndIsActiveTrueAndDeletedAtIsNull(country);
    }

    @Transactional
    public List<Supplier> getInternationalSuppliers(String country){
        return supplierRepository.findByCountryNotAndIsActiveTrueAndDeletedAtIsNull(country);
    }

    @Transactional
    public List<Supplier> getSuppliersWithCredit(){
        return supplierRepository.findByCreditDaysGreaterThanAndIsActiveTrueAndDeletedAtIsNull(0);
    }

//    @Transactional
//    public List<Supplier> getSuppliersByBalanceRange(Double minBalance, Double maxBalance){
//        return supplierRepository.findByCurrentBalanceBetweenAndDeletedAtIsNull(BigDecimal.valueOf(minBalance), BigDecimal.valueOf(maxBalance));
//    }

    @Transactional
    public List<Supplier> getHighRatedSuppliers(Double minRating){
        return supplierRepository.findByRatingIsLargerThan(minRating);
    }

    @Transactional
    public List<Supplier> getLowRatedSuppliers(Double maxRating){
        return supplierRepository.findByRatingLessThanEqualAndDeletedAtIsNull(BigDecimal.valueOf(maxRating));
    }

    @Transactional
    public     List<Supplier> getSuppliersByRegion(String region){
        return supplierRepository.findByRegionAndDeletedAtIsNull(region);
    }

    @Transactional
    public List<Supplier> getRecentlyUpdatedSuppliers(Pageable limit){
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNullOrderByUpdatedAtDesc(limit);
        return supplierPage.getContent();
    }

    @Transactional
    public List<Supplier> getTopRatedSuppliers(Pageable limit){
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNullOrderByRatingDesc(limit);
        return supplierPage.getContent();
    }

    @Transactional
    public List<Supplier> getSuppliersWithHighestBalance(Pageable limit){
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNullOrderByCurrentBalanceDesc(limit);
        return supplierPage.getContent();
    }

    @Transactional
    public void updateCreditLimit(Long id, Double newCreditLimit){
        log.info("Updating credit limit for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setCreditLimit(BigDecimal.valueOf(newCreditLimit));
        supplierRepository.save(supplier);

        log.info("Credit limit updated successfully for supplier: {}", id);
    }

    @Transactional
    public List<Supplier> getSuppliersWithHighestCreditLimit(Pageable limit){
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNullOrderByCreditLimitDesc(limit);
        return supplierPage.getContent();
    }
    @Transactional
    public Supplier updateCreditSettings(Long id, Double creditLimit, Integer creditDays){
        log.info("Updating credit settings for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setCreditLimit(BigDecimal.valueOf(creditLimit));
        supplier.setCreditDays(creditDays);
        supplierRepository.save(supplier);

        log.info("Credit settings updated successfully for supplier: {}", id);
        return supplier;
    }

    @Transactional
    public void enableCredit(Long id, Double creditLimit, Integer creditDays){
        log.info("Enabling credit for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setCreditLimit(BigDecimal.valueOf(creditLimit));
        supplier.setCreditDays(creditDays);
        supplierRepository.save(supplier);

        log.info("Credit enabled successfully for supplier: {}", id);
    }

    @Transactional
    public void disableCredit(Long id){
        log.info("Disabling credit for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setCreditLimit(BigDecimal.ZERO);
        supplier.setCreditDays(0);
        supplierRepository.save(supplier);

        log.info("Credit disabled successfully for supplier: {}", id);
    }

    /**
     * @param id
     * @param newBalance
     */
    @Override
    public void updateCurrentBalance(Long id, Double newBalance) {

    }

    @Transactional
    public void updateCurrentBalance(Long id, BigDecimal newBalance){
        log.info("Updating current balance for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        log.info("Current balance updated successfully for supplier: {}", id);
    }

    /**
     * @param id
     * @param amount
     */
    @Override
    public void increaseBalance(Long id, Double amount) {

    }

    /**
     * @param id
     * @param amount
     */
    @Override
    public void decreaseBalance(Long id, Double amount) {

    }

//    @Transactional
//    public void increaseBalance(Long id, Double amount){
//        log.info("Increasing balance for supplier: {}", id);
//
//        Supplier supplier = findSupplierById(id);
//        BigDecimal currentBalance = BigDecimal.valueOf(supplier.getCurrentBalance());
//        supplier.setCurrentBalance(currentBalance.add(BigDecimal.valueOf(amount)));
//        supplierRepository.save(supplier);
//
//        log.info("Balance increased successfully for supplier: {}", id);
//    }

//    @Transactional
//    public  void decreaseBalance(Long id, Double amount){
//        log.info("Decreasing balance for supplier: {}", id);
//
//        Supplier supplier = findSupplierById(id);
//        BigDecimal currentBalance = BigDecimal.valueOf(supplier.getCurrentBalance());
//        supplier.setCurrentBalance(currentBalance.subtract(BigDecimal.valueOf(amount)));
//        supplierRepository.save(supplier);
//
//        log.info("Balance decreased successfully for supplier: {}", id);
//    }
    @Transactional
    public Double getAvailableCredit(Long id){
        Supplier supplier = findSupplierById(id);
        BigDecimal creditLimit = supplier.getCreditLimit() != null ? supplier.getCreditLimit() : BigDecimal.ZERO;
        BigDecimal currentBalance = supplier.getCurrentBalance();
        return creditLimit.subtract(currentBalance).doubleValue();
    }


    @Transactional
    public List<Supplier> getRecentSuppliers(Pageable limit){
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(limit);
        return supplierPage.getContent();
    }

    @Transactional
    public boolean canExtendCredit(Long id, Double amount){
        Supplier supplier = findSupplierById(id);
        BigDecimal creditLimit = supplier.getCreditLimit() != null ? supplier.getCreditLimit() : BigDecimal.ZERO;
        BigDecimal currentBalance = supplier.getCurrentBalance();
        BigDecimal availableCredit = creditLimit.subtract(currentBalance);
        return availableCredit.compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    @Transactional
    public boolean isCreditAvailable(Long id, Double amount){
        Supplier supplier = findSupplierById(id);
        BigDecimal creditLimit = supplier.getCreditLimit() != null ? supplier.getCreditLimit() : BigDecimal.ZERO;
        return creditLimit.compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    @Transactional
    public void updateRating(Long id, Double rating){
        log.info("Updating rating for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setRating(BigDecimal.valueOf(rating));
        supplierRepository.save(supplier);

        log.info("Rating updated successfully for supplier: {}", id);
    }

    @Transactional
    public void updateRatingAndReviews(Long id, Double rating, String reviewComments){
        log.info("Updating rating and reviews for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setRating(BigDecimal.valueOf(rating));
        supplier.setReviewComment(reviewComments);
        supplierRepository.save(supplier);

        log.info("Rating and reviews updated successfully for supplier: {}", id);
    }

    @Transactional
    public boolean isSupplierCodeAvailable(String supplierCode){
        return !supplierRepository.existsBySupplierCode(supplierCode);
    }

    @Transactional
    public boolean isEmailAvailable(String email){
        return !supplierRepository.existsByEmail(email);
    }

//    @Transactional
//    public boolean canDeleteSupplier(Long id){
//        Supplier supplier = findSupplierById(id);
//        return supplier.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0;
//    }

    @Transactional
    public boolean canApproveSupplier(Long id){
        Supplier supplier = findSupplierById(id);
        return !supplier.getIsApproved();
    }

    @Transactional
    public Map<String, Object> getSupplierStatistics(){
        return (Map<String, Object>) supplierRepository.getSupplierStatistics();
    }

    /**
     * @return
     */
    @Override
    public List<Map<String, Object>> getSupplierTypeDistribution() {
        return null;
    }

    @Transactional
    public Double getTotalOutstandingBalance(){
        Double totalBalance = supplierRepository.getTotalOutstandingBalance();
        return totalBalance != null ? totalBalance : 0.0;
    }

    @Transactional
    public Double getTotalCreditLimit(){
        Double totalCreditLimit = supplierRepository.getTotalCreditLimit();
        return totalCreditLimit != null ? totalCreditLimit : 0.0;
    }

    @Transactional
    public Double getAverageCreditLimit(){
        Double averageCreditLimit = supplierRepository.getAverageCreditLimit();
        return averageCreditLimit != null ? averageCreditLimit : 0.0;
    }

    @Transactional
    public Double getAverageRating(){
        Double averageRating = supplierRepository.getAverageRating();
        return averageRating != null ? averageRating : 0.0;
    }

    @Transactional
    public Double getAverageCurrentBalance(){
        Double averageCurrentBalance = supplierRepository.getAverageCurrentBalance();
        return averageCurrentBalance != null ? averageCurrentBalance : 0.0;
    }

    @Transactional
    public Long countActiveSuppliers(){
        return supplierRepository.countByIsActiveTrueAndDeletedAtIsNull();
    }

    @Transactional
    public Long countSuppliersByStatus(String status){
        return supplierRepository.countSuppliersByStatus(status);
    }

    @Transactional
    public Map<String, Object> getDashboardStatistics(){
        return (Map<String, Object>) supplierRepository.getDashboardStatistics();
    }
//    @Transactional
//    public List<Map<String, Object>> getTopSuppliersByOrders(Pageable limit){
//        return supplierRepository.getTopSuppliersByOrders(limit);
//    }

    @Transactional
    public Map<String, Object> getSupplierSummary(Long id){
        return (Map<String, Object>) supplierRepository.getSupplierSummary(id);
    }

    /**
     * @param limit
     * @return
     */
    @Override
    public List<Map<String, Object>> getTopSuppliersByOrders(Pageable limit) {
        return null;
    }

//    @Transactional
//    public List<Map<String, Object>> getStatusDistribution(){
//        return supplierRepository.getStatusDistribution();
//    }

//    @Transactional
//    public List<Map<String, Object>> getSupplierTypeDistribution(){
//        return supplierRepository.getSupplierTypeDistribution();
//    }

//    @Transactional
//    public List<Map<String, Object>> getPaymentTermsDistribution(){
//        return supplierRepository.getPaymentTermsDistribution();
//    }
//
    @Transactional
    public boolean canBlockSupplier(Long id){
        Supplier supplier = findSupplierById(id);
        return !supplier.getIsBlocked();
    }
    @Transactional
    public boolean isTaxNumberAvailable(String taxNumber){
        return !supplierRepository.existsByTaxNumber(taxNumber);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean canDeleteSupplier(Long id) {
        return false;
    }

    @Transactional
    public  void incrementTotalOrders(Long id){
        log.info("Incrementing total orders for supplier: {}", id);

        Supplier supplier = findSupplierById(id);
        Integer totalOrders = supplier.getTotalOrders() != null ? supplier.getTotalOrders() : 0;
        supplier.setTotalOrders(totalOrders + 1);
        supplierRepository.save(supplier);

        log.info("Total orders incremented successfully for supplier: {}", id);
    }

    @Transactional
    public List<Supplier> getSuppliersRequiringAttention(double lowRatingThreshold){
        return getLowRatedSuppliers(lowRatingThreshold);
    }
    @Transactional
    public List<Supplier> getBlockedSuppliers(){
        return supplierRepository.findByIsBlockedTrueAndDeletedAtIsNull();
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = findSupplierById(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse getSupplierByCode(String supplierCode) {
        Supplier supplier = (Supplier) supplierRepository.findBySupplierCodeAndDeletedAtIsNull(supplierCode)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + supplierCode));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public PageResponse<SupplierResponse> getAllSuppliers(Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getAllActiveSuppliers() {
        List<Supplier> suppliers = supplierRepository.findByIsActiveTrueAndDeletedAtIsNull();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SupplierResponse> getSuppliersByType(String supplierType, Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.findBySupplierTypeAndDeletedAtIsNull(supplierType, pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getRawMaterialSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("RAW_MATERIAL");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getPackagingSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("PACKAGING");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getServiceSuppliers() {
        List<Supplier> suppliers = supplierRepository.findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull("SERVICES");
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SupplierResponse> searchSuppliers(String keyword, Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.searchSuppliers(keyword,null,null,null,null, pageable);
        return createPageResponse(supplierPage);
    }

    @Override
    public List<SupplierResponse> getSuppliersWithOutstandingBalance() {
        List<Supplier> suppliers = supplierRepository.findSuppliersWithOutstandingBalance();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponse> getSuppliersExceedingCreditLimit() {
        List<Supplier> suppliers = supplierRepository.findSuppliersExceedingCreditLimit();
        return suppliers.stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSupplierRating(Long id, BigDecimal rating) {
        log.info("Updating supplier rating: {}", id);

        Supplier supplier = findSupplierById(id);
        supplier.setRating(rating);
        supplierRepository.save(supplier);

        log.info("Supplier rating updated successfully: {}", id);
    }

    @Override
    public BigDecimal getSupplierBalance(Long id) {
        Supplier supplier = findSupplierById(id);
        return supplier.getCurrentBalance();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findByIdAndDeletedAtIsNull(id);
    }

    private void validateUniqueSupplierCode(String supplierCode, Long excludeId) {
        if (excludeId == null) {
            if (supplierRepository.existsBySupplierCode(supplierCode)) {
                throw new DuplicateResourceException("Supplier code already exists: " + supplierCode);
            }
        } else {
            if (supplierRepository.existsBySupplierCodeAndIdNot(supplierCode, excludeId)) {
                throw new DuplicateResourceException("Supplier code already exists: " + supplierCode);
            }
        }
    }

    private PageResponse<SupplierResponse> createPageResponse(Page<Supplier> supplierPage) {
        List<SupplierResponse> content = supplierPage.getContent().stream()
            .map(supplierMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SupplierResponse>builder()
            .content(content)
            .pageNumber(supplierPage.getNumber())
            .pageSize(supplierPage.getSize())
            .totalElements(supplierPage.getTotalElements())
            .totalPages(supplierPage.getTotalPages())
            .last(supplierPage.isLast())
            .first(supplierPage.isFirst())
            .empty(supplierPage.isEmpty())
            .build();
    }
}
